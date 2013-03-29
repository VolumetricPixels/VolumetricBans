package com.volumetricpixels.bans;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Server;
import org.spout.api.chat.ChatArguments;
import org.spout.api.command.Command;
import org.spout.api.command.RootCommand;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.scheduler.Scheduler;
import org.spout.api.scheduler.Task;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.command.VBCommandHelper;
import com.volumetricpixels.bans.command.VBCommands;
import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.connection.BanSynchroniser;
import com.volumetricpixels.bans.event.VolumetricBansDisabledEvent;
import com.volumetricpixels.bans.event.VolumetricBansEnabledEvent;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.exception.InitialisationException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.storage.FileManager;
import com.volumetricpixels.bans.storage.PunishmentStorage;

/**
 * The main class for the VolumetricBans plugin
 */
public final class VolumetricBans extends CommonPlugin {
	/** The PluginLogger prefix (tag) */
	public static final String LOGGER_TAG = "[VolumetricBans] ";

	// Config
	private YamlConfiguration config;
	private String apiKey = "";
	private boolean onlineMode = true;
	private boolean strictGlobal = false;
	// Requests
	private boolean canConnectToServers = true;
	private APIRequestHandler utilityRequestHandler;
	// Managers
	private FileManager fileSystem;
	private PunishmentStorage storageHandler;
	private PunishmentManager punishManager;
	// Helpers
	private VBCommandHelper cmdHelper;
	// Listeners / tasks / runnables
	private VolumetricBansListener listener;
	private BanSynchroniser banSync;
	private Task banSyncTask;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		Engine engine = getEngine();
		Platform platform = engine.getPlatform();
		setTag((PluginLogger) getLogger(), LOGGER_TAG);
		if ((platform == Platform.SERVER) || (platform == Platform.PROXY)) {
			Server server = (Server) engine;
			Scheduler scheduler = server.getScheduler();

			fileSystem = new FileManager(this);
			storageHandler = new PunishmentStorage(this);
			punishManager = new PunishmentManager(this);

			config = new YamlConfiguration(fileSystem.getConfigFile());
			config.setWritesDefaults(true);

			try {
				config.load();
				config.save();
			} catch (ConfigurationException e) {
				new InitialisationException("Could not load configuration!", e).printStackTrace();
			}

			boolean configurationUntouched = false;
			apiKey = config.getNode("api-key").getString("unspecified-api-key");
			if (apiKey.equals("unspecified-api-key")) {
				configurationUntouched = true;
				getLogger().warning("You haven't specified an API key");
				getLogger().warning("You must specify an API key for the plugin to work");
				getLogger().warning("Disabling VolumetricBans because no API key was found");
				server.getPluginManager().disablePlugin(this);
				return;
			}

			onlineMode = config.getNode("online-mode").getBoolean(true);
			if (onlineMode) {
				getLogger().info("Running in online mode!");
				utilityRequestHandler = new APIRequestHandler(this, "data");
				try {
					// Check api key validity
					Map<String, String> postData = new HashMap<String, String>();
					postData.put("action", "checkValidity");
					JSONObject jO = null;
					try {
						jO = utilityRequestHandler.retrieveJSONObject(postData);

						if (!jO.getBoolean("result")) {
							getLogger().severe("Invalid API key");
							getLogger().severe("This means the key was never valid, or your server has been disabled by VolumetricBans staff");
							getLogger().severe("Disabling VolumetricBans because of invalid API key");
							server.getPluginManager().disablePlugin(this);
							return;
						}
					} catch (DataRetrievalException e) {
						new InitialisationException("Could not connect to servers!", e).printStackTrace();
						getLogger().severe("As we could not connect to the servers, the plugin is now running in offline mode");
						getLogger().severe("This means bans made will not be synchronised to the website until the plugin is in online mode");
						getLogger().severe("You should still be able to use the plugin normally");
						onlineMode = false;
					}
				} catch (JSONException e) {
					new InitialisationException("Invalid data received from servers, or error in parsing!", e).printStackTrace();
					getLogger().severe("Could not check API key validity");
					getLogger().severe("Disabling all web-based functionality");
					canConnectToServers = false;
					onlineMode = false;
				}
			} else {
				configurationUntouched = false;
				getLogger().info("Running in offline mode! Website-based functionality is disabled");
			}

			strictGlobal = config.getNode("strict-globals").getBoolean(false);
			if (strictGlobal) {
				getLogger().info("Running in strict global mode! This means that more players will be filtered from the server!");
				configurationUntouched = false;
			}

			if (configurationUntouched) {
				try {
					// Save so default config is written
					config.save();
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
			}

			try {
				storageHandler.loadBans();
				storageHandler.loadMutes();
			} catch (StorageException e) {
				new InitialisationException("Could not load punishments!", e).printStackTrace();
			} catch (DataLoadException e) {
				new InitialisationException("Could not load punishments!", e).printStackTrace();
			}

			// Initialise / register listener
			listener = new VolumetricBansListener(this);
			server.getEventManager().registerEvents(listener, this);

			// Initialise / register commands
			Command vbCmd = server.getRootCommand().addSubCommand(this, "vbans").addAlias("vb", "volumetricbans");

			cmdHelper = new VBCommandHelper(this);
			new VBCommands(this).register();

			if (onlineMode && !canConnectToServers) {
				onlineMode = canConnectToServers;
			}
			if (onlineMode) {
				banSync = new BanSynchroniser(this);
				banSyncTask = scheduler.scheduleAsyncTask(this, banSync);
			}

			server.getEventManager().callDelayedEvent(new VolumetricBansEnabledEvent(this));
		} else if (platform == Platform.CLIENT) {
			Client client = (Client) engine;
			/*
			 * TODO: Client side of the plugin Ideas for the client side so far
			 * include:
			 * 
			 * GUI for admins
			 */
		}
		engine.getEventManager().callDelayedEvent(new VolumetricBansEnabledEvent(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable() {
		Engine engine = getEngine();
		Platform platform = engine.getPlatform();
		if (platform == Platform.SERVER || platform == Platform.PROXY) {
			if (banSyncTask != null) {
				banSyncTask.cancel();
			}

			if ((listener != null) && (listener.getChecker() != null)) {
				listener.getChecker().interrupt();
			}

			try {
				storageHandler.saveBans();
				storageHandler.saveMutes();
			} catch (StorageException e) {
				getLogger().severe("Could not save punishments!");
				e.printStackTrace();
			}
		} else if (platform == Platform.CLIENT) {
			// TODO:
		}
		engine.getEventManager().callDelayedEvent(new VolumetricBansDisabledEvent(this));
	}

	/**
	 * Gets the FileManager
	 * 
	 * @return The FileManager for VolumetricBans
	 */
	public FileManager getFileManager() {
		return fileSystem;
	}

	/**
	 * Gets the PunishmentStorage
	 * 
	 * @return The PunishmentStorage instance
	 */
	public PunishmentStorage getStorageHandler() {
		return storageHandler;
	}

	/**
	 * Gets the VolumetricBans punishment manager
	 * 
	 * @return The PunishmentManager instance
	 */
	public PunishmentManager getPunishmentManager() {
		return punishManager;
	}

	/**
	 * Gets the command helper
	 * 
	 * @return The VBCommandHelper instance
	 */
	public VBCommandHelper getCommandHelper() {
		return cmdHelper;
	}

	/**
	 * Gets the server's API key
	 * 
	 * @return The API key of the server the plugin is running on
	 */
	public String getAPIKey() {
		return apiKey;
	}

	/**
	 * Gets whether the plugin is running in online mode
	 * 
	 * @return Whether the plugin is in online mode
	 */
	public boolean isOnlineMode() {
		return onlineMode;
	}

	/**
	 * Gets whether the plugin is running in strict global mode
	 * 
	 * @return Whether the plugin is in strict global mode
	 */
	public boolean isStrictGlobal() {
		return strictGlobal;
	}

	/**
	 * Sets the plugin to offline mode
	 */
	public void setToOfflineMode() {
		onlineMode = false;
	}

	/**
	 * Sets the plugin to offline mode because of a DataRetrievalException
	 * 
	 * @param cause
	 *            The cause of changing the plugin to offline mode
	 */
	public void setToOfflineMode(DataRetrievalException cause) {
		setToOfflineMode();
		getLogger().warning("Changing to offline mode because of exception:");
		cause.printStackTrace();
	}

	/**
	 * Sets the plugin to online mode
	 */
	public void setToOnlineMode() {
		onlineMode = true;
	}

	/**
	 * Sets the tag of the given PluginLogger to the given String
	 * 
	 * @param logger
	 *            The PluginLogger to set the tag of
	 * @param string
	 *            The tag to set
	 */
	private void setTag(PluginLogger logger, String string) {
		logger.setTag(ChatArguments.fromString(string));
	}
}
