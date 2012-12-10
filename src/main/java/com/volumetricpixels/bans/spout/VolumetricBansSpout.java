package com.volumetricpixels.bans.spout;

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginManager;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.api.spout.VolumetricBansSpoutAPI;
import com.volumetricpixels.bans.crossapi.VBGlobalBanStorer;
import com.volumetricpixels.bans.crossapi.VBPunishments;
import com.volumetricpixels.bans.crossapi.connection.BanSynchronizer;
import com.volumetricpixels.bans.crossapi.connection.DataRetriever;
import com.volumetricpixels.bans.crossapi.connection.PlayerDataRetriever;
import com.volumetricpixels.bans.crossapi.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.crossapi.perapi.VBLocalBans;
import com.volumetricpixels.bans.crossapi.perapi.VBMutes;
import com.volumetricpixels.bans.crossapi.util.API;
import com.volumetricpixels.bans.crossapi.util.GeneralUtil;
import com.volumetricpixels.bans.spout.client.VBSpoutClientHandler;
import com.volumetricpixels.bans.spout.event.VolumetricBansDisableEvent;
import com.volumetricpixels.bans.spout.event.VolumetricBansEnableEvent;
import com.volumetricpixels.bans.spout.files.VBSpoutConfig;
import com.volumetricpixels.bans.spout.files.VBSpoutLocalBans;
import com.volumetricpixels.bans.spout.files.VBSpoutMutes;
import com.volumetricpixels.bans.spout.util.SpoutUtils;

/**
 * VolumetricBans for Spout Engine
 * @author DziNeIT
 */
public class VolumetricBansSpout extends CommonPlugin implements VolumetricBans {
    public final VBSpoutPermissions perms = VBSpoutPermissions.perms;
    public final VBPunishments punishments;
    public final VBSpoutLocalBans bans;
    public final VBSpoutMutes mutes;

    public final Object[] noPermsMessage = {
            ChatStyle.RED, "You don't have permission to do that!"
    };

    private Engine e;
    private EventManager em;
    private PluginManager pm;

    private String apiKey = "VolumetricBansAPIKeyToDoGet";
    private VBSpoutConfig config = null;
    private VBSpoutCommandHandler cmdHandler = null;
    private VolumetricBansSpoutAPI api = null;

    // Web stuff and global ban temporary storing
    public BanSynchronizer bs;
    public DataRetriever mainDataRetriever;
    public VBGlobalBanStorer gbts;
    public PlayerDataRetriever pdr;

    public VolumetricBansSpout() {
        this.bans = new VBSpoutLocalBans(this);
        this.mutes = new VBSpoutMutes(this);
        this.punishments = new VBPunishments(this);
    }

    /** CommonPlugin Overriden Methods **/

    @Override
    public void onEnable() {
        this.api = new VolumetricBansSpoutAPI();

        if (getEngine() instanceof Server) {
            this.config = new VBSpoutConfig(this);
            this.apiKey = config.getNode("API-Key").getString(null);
            this.e = getEngine();
            this.em = e.getEventManager();
            this.pm = e.getPluginManager();

            // Init web stuff
            this.mainDataRetriever = new DataRetriever(this);
            this.gbts = new VBGlobalBanStorer(this);
            this.bs = new BanSynchronizer(this);
            this.pdr = new PlayerDataRetriever(this);

            // Init command vars and setup commands
            this.cmdHandler = new VBSpoutCommandHandler(this);
            Command rootCmd = e.getRootCommand();
            rootCmd.addSubCommand(e, "vbans").setExecutor(cmdHandler).setHelp("VolumetricBans main command!");
            rootCmd.addSubCommand(e, "ban").setExecutor(cmdHandler).setHelp("Banning command!");
            rootCmd.addSubCommand(e, "kick").setExecutor(cmdHandler).setHelp("Kicking command!");
            rootCmd.addSubCommand(e, "mute").setExecutor(cmdHandler).setHelp("Muting command!");
            rootCmd.addSubCommand(e, "banreason").setExecutor(cmdHandler).setHelp("Gets the ban reason for a player!");
            rootCmd.addSubCommand(e, "banlist").setExecutor(cmdHandler).setHelp("List bans!");
            rootCmd.addSubCommand(e, "lookup").setExecutor(cmdHandler).setHelp("Lookup a player!");
            rootCmd.addSubCommand(e, "unban").setExecutor(cmdHandler).setHelp("Unbanning command!");

            // Tell the punishments handler the plugin is enabled and give the util class the plugin instance
            GeneralUtil.init(this);
            punishments.pluginEnabled();
            SpoutUtils.instance = new SpoutUtils();

            // Check API Key validity
            if (!apiKeyValid()) {
                System.out.println("[VolumetricBans] You have not added an API Key to the config, or the entered key is invalid!");
                System.out.println("[VolumetricBans] If you have an API Key, add it to the config!");
                System.out.println("[VolumetricBans] If you have not obtained an API Key, go to the website!");
                System.out.println("[VolumetricBans] VolumetricBans will disable until a valid Key is added!");
                pm.disablePlugin(this);
                return;
            }

            // Check for server validity (E.G. It is disabled)
            if (!isServerValid()) {
                System.err.println("[VolumetricBans] An error occurred with your server:");
                if (isServerDisabled()) {
                    System.out.println("[VolumetricBans] Your server has been disabled by the VolumetricBans administration!");
                    System.out.println("[VolumetricBans] To appeal this, view your server page and click 'Appeal Server Removal'");
                    System.out.println("[VolumetricBans] You must be logged in to VolumetricBans on the account used to register!");
                    System.out.println("[VolumetricBans] Disabling until issues resolved!");
                    pm.disablePlugin(this);
                } else {
                    System.out.println("[VolumetricBans] There was a request error to the VolumetricBans servers! Continuing!");
                    return;
                }
            }

            // Initialise handlers and permissions
            perms.update();
            bans.init();

            // Call enable event
            em.callEvent(new VolumetricBansEnableEvent(this));
        } else if (getEngine() instanceof Client) {
            new VBSpoutClientHandler().start();
        } else {
            throw new IllegalStateException("VolumetricBans does not support proxy servers!");
        }
    }

    @Override
    public void onDisable() {
        // Call disable event for other plugins
        em.callEvent(new VolumetricBansDisableEvent(this));
    }

    /** VolumetricBans Interface Overriden Methods **/

    @Override
    public VolumetricBansSpoutAPI getAPI() {
        return api;
    }

    @Override
    public String getServerKey() {
        return apiKey;
    }

    @Override
    public VBSpoutConfig getVBConfig() {
        return config;
    }

    @Override
    public VBLocalBans getLocalBanHandler() {
        return bans;
    }

    @Override
    public VBMutes getMuteHandler() {
        return mutes;
    }

    @Override
    public BanSynchronizer getBanSynchronizer() {
        return bs;
    }

    @Override
    public GlobalBanStorer getGlobalBanStorer() {
        return gbts;
    }

    @Override
    public DataRetriever getMainDataRetriever() {
        return mainDataRetriever;
    }

    @Override
    public API getInUseAPI() {
        return API.SPOUT;
    }

    @Override
    public VBPunishments getPunishmentHandler() {
        return punishments;
    }

    @Override
    public PlayerDataRetriever getPlayerDataRetriever() {
        return pdr;
    }

    /** Util Methods **/

    public boolean apiKeyValid() {
        if (apiKey != null) {
            // Performs a server check; if it is null an exception was thrown, meaning there was probably an invalid API Key
            // There may be errors with this check, so it will probably have to be changed later.
            return mainDataRetriever.getAllBans() != null;
        }
        return false;
    }

    public boolean isServerValid() {
        // Checks all possible server validity errors
        return !isServerDisabled();
    }

    public boolean isServerDisabled() {
        // Checks if the VolumetricBans admins disabled the server
        return mainDataRetriever.isServerDisabled();
    }
}
