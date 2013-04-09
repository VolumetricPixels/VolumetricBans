package com.volumetricpixels.bans;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Server;
import org.spout.api.chat.ChatArguments;
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
import com.volumetricpixels.bans.connection.UpdateRequester;
import com.volumetricpixels.bans.event.VolumetricBansDisabledEvent;
import com.volumetricpixels.bans.event.VolumetricBansEnabledEvent;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.exception.VolumetricBansInitialisationException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.storage.FileManager;
import com.volumetricpixels.bans.storage.PunishmentStorage;

/** The main class for the VolumetricBans plugin */
public final class VolumetricBans extends CommonPlugin {
    /** The PluginLogger prefix (tag) */
    public static final String LOGGER_TAG = "[VolumetricBans] ";

    // Config
    private String apiKey = "";
    private boolean onlineMode = true;
    private boolean strictGlobal = false;
    // Requests
    private boolean canConnectToServers = true;
    private boolean premium = false;
    // Managers
    private FileManager fileSystem;
    private PunishmentStorage storageHandler;
    private PunishmentManager punishManager;
    // Helpers
    private VBCommandHelper cmdHelper;
    // Listeners / tasks / runnables
    private VolumetricBansListener listener;
    private Task banSyncTask;
    private Task updateReqTask;

    /** {@inheritDoc} */
    @Override
    public void onEnable() {
        try {
            enableVolumetricBans();
        } catch (final VolumetricBansInitialisationException e) {
            getLogger().log(Level.SEVERE, "Initialisation exception!", e);
            if (e.isFatal()) {
                getLogger().severe("Fatal exception, disabling!");
                getEngine().getPluginManager().disablePlugin(this);
            }
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
            try {
                storageHandler.saveBans();
                storageHandler.saveMutes();
            } catch (final StorageException e) {
                getLogger().log(Level.SEVERE, "Could not save punishments!", e);
            }
        } else if (platform == Platform.CLIENT) {
            // TODO:
        }
        engine.getEventManager().callDelayedEvent(new VolumetricBansDisabledEvent(this));
    }

    private void enableVolumetricBans() throws VolumetricBansInitialisationException {
        final Engine engine = getEngine();
        final Platform platform = engine.getPlatform();
        setTag((PluginLogger) getLogger(), LOGGER_TAG);
        if (platform == Platform.SERVER || platform == Platform.PROXY) {
            final Server server = (Server) engine;
            final Scheduler scheduler = server.getScheduler();

            fileSystem = new FileManager(this);
            storageHandler = new PunishmentStorage(this);
            punishManager = new PunishmentManager(this);

            final YamlConfiguration config = new YamlConfiguration(fileSystem.getConfigFile());
            config.setWritesDefaults(true);

            try {
                config.load();
                config.save();
            } catch (final ConfigurationException e) {
                throw new VolumetricBansInitialisationException("Could not load configuration!", e, true);
            }

            boolean configurationUntouched = true;
            apiKey = config.getNode("api-key").getString("unspecified-api-key");
            if (apiKey.equals("unspecified-api-key")) {
                getLogger().warning("You haven't specified an API key");
                getLogger().warning("You must specify an API key for the plugin to work");
                getLogger().warning("Disabling VolumetricBans because no API key was found");
                server.getPluginManager().disablePlugin(this);
                return;
            }

            final APIRequestHandler handler = new APIRequestHandler(this, "data");
            onlineMode = config.getNode("online-mode").getBoolean(true);
            if (onlineMode) {
                getLogger().info("Running in online mode!");
                try {
                    // Check api key validity
                    final Map<String, String> postData = new HashMap<String, String>();
                    postData.put("action", "checkValidity");
                    JSONObject jO;
                    try {
                        jO = handler.submitRequest(postData);

                        if (!jO.getBoolean("result")) {
                            getLogger().severe("Invalid API key");
                            getLogger().severe("This means the key was never valid, or your server has been disabled by VolumetricBans staff");
                            getLogger().severe("Disabling VolumetricBans because of invalid API key");
                            getEngine().getPluginManager().disablePlugin(this);
                            return;
                        }
                    } catch (final DataRetrievalException e) {
                        getLogger().severe("As we could not connect to the servers, the plugin is now running in offline mode");
                        getLogger().severe("This means bans made will not be synchronised to the website until the plugin is in online mode");
                        getLogger().severe("You should still be able to use the plugin normally");
                        onlineMode = false;
                        new VolumetricBansInitialisationException("Could not connect to servers!", e, false).printStackTrace();
                    }
                } catch (final JSONException e) {
                    getLogger().severe("Could not check API key validity");
                    getLogger().severe("Disabling all web-based functionality");
                    canConnectToServers = false;
                    onlineMode = false;
                    new VolumetricBansInitialisationException("Invalid data received from servers, or error in parsing!", e, false).printStackTrace();
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
                } catch (final ConfigurationException e) {
                    e.printStackTrace();
                }
            }

            try {
                storageHandler.loadBans();
                storageHandler.loadMutes();
            } catch (final StorageException e) {
                throw new VolumetricBansInitialisationException("Could not load punishments!", e, true);
            } catch (final DataLoadException e) {
                throw new VolumetricBansInitialisationException("Could not load punishments!", e, true);
            }

            // Initialise / register listener
            listener = new VolumetricBansListener(this);
            server.getEventManager().registerEvents(listener, this);

            // Initialise / register commands
            cmdHelper = new VBCommandHelper(this);
            new VBCommands(this).register();

            if (onlineMode && !canConnectToServers) {
                onlineMode = canConnectToServers;
            }
            if (onlineMode) {
                final BanSynchroniser banSync = new BanSynchroniser(this);
                banSyncTask = scheduler.scheduleAsyncTask(this, banSync);
                final UpdateRequester updateReq = new UpdateRequester(this);
                updateReqTask = scheduler.scheduleAsyncTask(this, updateReq);

                final Map<String, String> postData = new HashMap<String, String>();
                postData.put("action", "checkPremiumServer");
                try {
                    final JSONObject response = handler.submitRequest(postData);
                    premium = response.getBoolean("result");
                } catch (final JSONException e) {
                    e.printStackTrace();
                    getLogger().warning("Could not check if server is premium!");
                    getLogger().warning("If your server is premium, you will not have premium features until you restart");
                } catch (final DataRetrievalException e) {
                    e.printStackTrace();
                    getLogger().warning("Could not check if server is premium!");
                    getLogger().warning("If your server is premium, you will not have premium features until you restart");
                }
            }

            server.getEventManager().callDelayedEvent(new VolumetricBansEnabledEvent(this));
        } else if (platform == Platform.CLIENT) {
            // Client client = (Client) engine;
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

    /** Sets the plugin to offline mode */
    public void setToOfflineMode() {
        onlineMode = false;
    }

    /**
     * Sets the plugin to offline mode because of a DataRetrievalException
     * 
     * @param cause
     *            The cause of changing the plugin to offline mode
     */
    public void setToOfflineMode(final DataRetrievalException cause) {
        setToOfflineMode();
        getLogger().warning("Changing to offline mode because of exception:");
        cause.printStackTrace();
    }

    /** Sets the plugin to online mode */
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
    private void setTag(final PluginLogger logger, final String string) {
        logger.setTag(ChatArguments.fromString(string));
    }

    public boolean isPremium() {
        return premium;
    }
}
