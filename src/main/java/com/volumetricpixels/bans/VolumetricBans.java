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

import com.volumetricpixels.bans.command.CommandHelper;
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

public final class VolumetricBans extends CommonPlugin {
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
	private CommandHelper cmdHelper;
	// Listeners / tasks / runnables
	private VolumetricBansListener listener;
	private BanSynchroniser banSync;
	private Task banSyncTask;

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

			cmdHelper = new CommandHelper(this);
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

	public FileManager getFileSystem() {
		return fileSystem;
	}

	public PunishmentStorage getStorageHandler() {
		return storageHandler;
	}

	public PunishmentManager getPunishmentManager() {
		return punishManager;
	}

	public APIRequestHandler getUtilityHandler() {
		return utilityRequestHandler;
	}

	public CommandHelper getCommandHelper() {
		return cmdHelper;
	}

	public String getAPIKey() {
		return apiKey;
	}

	public boolean isOnlineMode() {
		return onlineMode;
	}

	public boolean isStrictGlobal() {
		return strictGlobal;
	}

	public void setToOfflineMode() {
		onlineMode = false;
	}

	public void setToOfflineMode(DataRetrievalException cause) {
		setToOfflineMode();
		cause.printStackTrace();
	}

	public void setToOnlineMode() {
		onlineMode = true;
	}

	private void setTag(PluginLogger logger, String string) {
		logger.setTag(ChatArguments.fromString(string));
	}
}
