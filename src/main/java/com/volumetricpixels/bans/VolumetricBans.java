package com.volumetricpixels.bans;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Server;
import org.spout.api.command.Command;
import org.spout.api.command.RootCommand;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;
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
import com.volumetricpixels.bans.exception.InitialisationException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.storage.FileManager;
import com.volumetricpixels.bans.storage.PunishmentStorage;

public class VolumetricBans extends CommonPlugin {
	private FileManager fileSystem;
	private PunishmentStorage storageHandler;
	private PunishmentManager punishManager;

	// Config / config values
	private YamlConfiguration config;
	private String apiKey;

	// Request stuff
	private APIRequestHandler utilityRequestHandler;
	private boolean canConnectToServers = true;

	// Command stuff
	private CommandHelper cmdHelper;

	// Runnables / tasks / listeners
	private VolumetricBansListener listener;
	private BanSynchroniser banSync;
	private Task spoutTaskBanSync;

	@Override
	public void onEnable() {
		Engine engine = getEngine();
		Platform platform = engine.getPlatform();
		if ((platform == Platform.SERVER) || (platform == Platform.PROXY)) {
			Server server = (Server) engine;

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

			apiKey = config.getNode("api-key").getString("unspecified-api-key");
			if (apiKey.equals("unspecified-api-key")) {
				getLogger().warning("You haven't specified an API key");
				getLogger().warning("You must specify an API key for the plugin to work");
				getLogger().warning("Disabling VolumetricBans because no API key was found");
				server.getPluginManager().disablePlugin(this);
			}

			utilityRequestHandler = new APIRequestHandler(this, "data");

			try {
				// Validity checks
				Map<String, String> postData = new HashMap<String, String>();
				postData.put("action", "checkValidity");
				JSONObject jO = utilityRequestHandler.retrieveJSONObject(postData);
				if (!jO.getBoolean("result")) {
					getLogger().severe("Invalid API key");
					getLogger().severe("This means the key was never valid, or your server has been disabled by VolumetricBans staff");
					getLogger().severe("Disabling VolumetricBans because of invalid API key");
					server.getPluginManager().disablePlugin(this);
				}
			} catch (JSONException e) {
				new InitialisationException("Could not connect to servers!", e).printStackTrace();
				getLogger().severe("Could not check API key validity");
				getLogger().severe("This probably means that the servers are down");
				getLogger().severe("Disabling all web-based functionality");
				canConnectToServers = false;
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

			if (canConnectToServers) {
				// TODO: Init request tasks etc
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
			if (spoutTaskBanSync != null) {
				spoutTaskBanSync.cancel();
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

	public boolean canConnectToServers() {
		return canConnectToServers;
	}

	public String getAPIKey() {
		return apiKey;
	}
}
