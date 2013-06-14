package com.volumetricpixels.bans;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.logging.Level;

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Server;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.scheduler.Scheduler;
import org.spout.api.scheduler.Task;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.bans.command.VBCommandHelper;
import com.volumetricpixels.bans.command.VBCommands;
import com.volumetricpixels.bans.event.VolumetricBansDisabledEvent;
import com.volumetricpixels.bans.event.VolumetricBansEnabledEvent;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.exception.VolumetricBansInitialisationException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.request.APIRequestHandler;
import com.volumetricpixels.bans.request.BanSynchroniser;
import com.volumetricpixels.bans.storage.PunishmentStorage;
import com.volumetricpixels.bans.storage.file.FileManager;
import com.volumetricpixels.bans.util.Utilities;

/**
 * The main class for the VolumetricBans global banning system plugin for the
 * Spout engine
 */
public final class VolumetricBans extends Plugin {
	/** The PluginLogger prefix (tag) */
	public static final String LOGGER_TAG = "[VolumetricBans] ";
	/** The website URL */
	public static final String WEBSITE_URL = "vbans-dev.herokuapp.com";

	// Config info
	/** The server's API key */
	private String apiKey = "";
	/** Whether the plugin is in online mode */
	private boolean onlineMode = true;
	/** Whether we can connect to the VB servers */
	private boolean canConnectToServers = true;
	/** Whether the server is premium */
	private boolean premium = false;

	// General plugin objects
	/** Keeps track of files used by the plugin */
	private FileManager fileSystem;
	/** The punishment storage handler */
	private PunishmentStorage storageHandler;
	/** The punishment manager */
	private PunishmentManager punishManager;
	/** Helper for command parsing / responses */
	private VBCommandHelper cmdHelper;
	/** Main plugin listener */
	private VolumetricBansListener listener;

	// Tasks
	/** Ban synchronisation task */
	private Task banSyncTask;
	/** Update request task */
	private Task updateReqTask;

	// APIRequestHandlers
	/** APIRequestHandler for general data requests */
	private APIRequestHandler dataReqHandler;

	/** {@inheritDoc} */
	@Override
	public void onEnable() {
		try {
			doEnable();
		} catch (final VolumetricBansInitialisationException e) {
			getLogger().log(Level.SEVERE,
					"Initialisation exception! Disabling plugin!", e);
			getEngine().getPluginManager().disablePlugin(this);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onDisable() {
		final Engine engine = getEngine();
		final Platform platform = engine.getPlatform();
		if (platform == Platform.SERVER || platform == Platform.PROXY) {
			if (banSyncTask != null) {
				banSyncTask.cancel();
			}
			if (updateReqTask != null) {
				updateReqTask.cancel();
			}
			if (listener != null && listener.getThreadPool() != null) {
				listener.getThreadPool().shutdownNow();
			}
			if (storageHandler != null) {
				try {
					storageHandler.saveBans();
					storageHandler.saveMutes();
				} catch (final StorageException e) {
					getLogger().log(Level.SEVERE,
							"Could not save punishments!", e);
				}
			}
		} else if (platform == Platform.CLIENT) {
			// TODO: Disable client shizzle
		}
		engine.getEventManager().callDelayedEvent(
				new VolumetricBansDisabledEvent(this));
	}

	private void doEnable() throws VolumetricBansInitialisationException {
		final Engine engine = getEngine();
		final Platform platform = engine.getPlatform();
		setTag((PluginLogger) getLogger(), LOGGER_TAG);
		if (platform == Platform.SERVER || platform == Platform.PROXY) {
			final Server server = (Server) engine;
			final Scheduler scheduler = server.getScheduler();

			fileSystem = new FileManager(this);
			storageHandler = new PunishmentStorage(this);
			punishManager = new PunishmentManager(this);

			final YamlConfiguration config = new YamlConfiguration(
					fileSystem.getConfigFile());
			config.setWritesDefaults(true);
			config.setPathSeparator(".");
			config.setHeader(Utilities.genConfigHeader(this));

			try {
				config.load();
				config.save();
			} catch (final ConfigurationException e) {
				throw new VolumetricBansInitialisationException(
						"Could not load configuration!", e);
			}

			boolean configurationUntouched = true;
			apiKey = config.getNode("api-key").getString("unspecified-api-key");
			if (apiKey.equals("unspecified-api-key")) {
				getLogger().warning("You haven't specified an API key");
				getLogger().warning(
						"You must specify an API key for the plugin to work");
				getLogger()
						.warning(
								"Disabling VolumetricBans because no API key was found");
				server.getPluginManager().disablePlugin(this);
				return;
			}

			// Initialise request handlers
			dataReqHandler = new APIRequestHandler(this);

			onlineMode = config.getNode("online-mode").getBoolean(true);
			if (onlineMode) {
				try {
					// Check api key validity
					final Map<String, String> postData = new THashMap<String, String>();
					postData.put("action", "checkValidity");
					JSONObject jO;
					try {
						jO = dataReqHandler.submitRequest(postData);
						if (!jO.getBoolean("result")) {
							throw new VolumetricBansInitialisationException(
									"Invalid API key! You entered an invalid key or your server is disabled!");
						}
						getLogger().info("Running in online mode!");
					} catch (final DataRetrievalException e) {
						e.printStackTrace();
						getLogger()
								.severe("As we could not connect to the servers, the plugin is now running in offline mode");
						getLogger()
								.severe("This means bans made will not be synchronised to the website until the plugin is in online mode");
						getLogger()
								.severe("You should still be able to use the plugin normally with offline functionality");
						canConnectToServers = false;
					}
				} catch (final JSONException e) {
					e.printStackTrace();
					getLogger().severe("Could not check API key validity");
					getLogger().severe("Disabling all web-based functionality");
					canConnectToServers = false;
				}
			} else {
				configurationUntouched = false;
				getLogger()
						.info("Running in offline mode! Online-only functionality is disabled for the current session");
			}

			if (configurationUntouched) {
				try {
					// Save so default config is written
					config.save();
				} catch (final ConfigurationException e) {
					e.printStackTrace();
				}
			}

			try {
				storageHandler.loadBans();
				storageHandler.loadMutes();
			} catch (final StorageException e) {
				throw new VolumetricBansInitialisationException(
						"Could not load punishments!", e);
			} catch (final DataLoadException e) {
				throw new VolumetricBansInitialisationException(
						"Could not load punishments!", e);
			}

			// Initialise / register listener
			listener = new VolumetricBansListener(this);
			server.getEventManager().registerEvents(listener, this);

			// Initialise / register commands
			cmdHelper = new VBCommandHelper(this);
			new VBCommands(this).register();

			if (onlineMode && !canConnectToServers) {
				onlineMode = false;
			}

			if (onlineMode) {
				boolean validAPIKey = true;
				try {
					final JSONObject jobj = dataReqHandler
							.submitRequest(Utilities.oneEntryMap("key", apiKey));
					final String error = jobj.getString("error");
					if (error.toLowerCase().contains("invalid api key")) {
						validAPIKey = false;
						getLogger().severe("Your API key is invalid!");
						getLogger().severe("Running in offline mode!");
					}
				} catch (final DataRetrievalException e) {
					e.printStackTrace();
					getLogger().severe("Could not check if API key is valid!");
					getLogger().severe("Running in offline mode");
					validAPIKey = false;
				} catch (final JSONException e) {
					e.printStackTrace();
					getLogger().severe("Could not check if API key is valid!");
					getLogger().severe("Running in offline mode");
					validAPIKey = false;
				}

				if (validAPIKey) {
					final BanSynchroniser banSync = new BanSynchroniser(this);
					banSyncTask = scheduler.scheduleAsyncTask(this, banSync);

//                  final Map<String, String> postData = new THashMap<String, String>();
//                  postData.put("action", "isPremium");
//                  try {
//                      final JSONObject response = dataReqHandler.submitRequest(postData);
//                      premium = response.getBoolean("result");
//                  } catch (final JSONException e) {
//                      e.printStackTrace();
//                      getLogger().warning("Could not check if server is premium!");
//                      getLogger().warning("If your server is premium, you will not have premium features until you restart");
//                  } catch (final DataRetrievalException e) {
//                      e.printStackTrace();
//                      getLogger().warning("Could not check if server is premium!");
//                      getLogger().warning("If your server is premium, you will not have premium features until you restart");
//                  }
				} else {
					onlineMode = false;
				}
			}

			server.getEventManager().callDelayedEvent(
					new VolumetricBansEnabledEvent(this));
		} else if (platform == Platform.CLIENT) {
			// Client client = (Client) engine;
			/*
			 * TODO: Client side of the plugin. Ideas for the client side so far
			 * include:
			 * 
			 * GUI for admins
			 */
		}
		engine.getEventManager().callDelayedEvent(
				new VolumetricBansEnabledEvent(this));
	}

	/**
	 * Gets the request handler for data requests
	 * 
	 * @return The APIRequestHandler for data requests
	 */
	public APIRequestHandler getRequestHandler() {
		return dataReqHandler;
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
	 * Gets whether the server running the plugin is premium
	 * 
	 * @return Whether the server running the plugin has premium features
	 */
	public boolean isPremium() {
		return premium;
	}

	/**
	 * Gets the current state of online mode as a readable string
	 * 
	 * @return "online" if the plugin is online mode, else "offline"
	 */
	public String getOnlineModeState() {
		return onlineMode ? "on" : "off" + "line";
	}

	/**
	 * Sets the plugin to offline mode
	 * 
	 * @param online
	 *            Whether the new state should be online or offline
	 */
	public void setOnlineMode(final boolean online) {
		onlineMode = online;
	}

	/**
	 * Sets the plugin to offline mode because of a DataRetrievalException
	 * 
	 * @param cause
	 *            The cause of changing the plugin to offline mode
	 */
	public void setOfflineMode(final DataRetrievalException cause) {
		setOnlineMode(false);
		getLogger().warning("Changing to offline mode because of exception:");
		cause.printStackTrace();
	}

	/**
	 * Sets the tag of the given PluginLogger to the given String
	 * 
	 * @param logger
	 *            The PluginLogger to set the tag of
	 * @param string
	 *            The tag to set
	 */
	private void setTag(final PluginLogger logger, final String string) {
		logger.setTag(string);
	}
}
