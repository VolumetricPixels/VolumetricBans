package com.volumetricpixels.voxelbans.spout;

import org.spout.api.Engine;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginManager;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.shared.VBPunishments;
import com.volumetricpixels.voxelbans.shared.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.shared.connection.DataRetriever;
import com.volumetricpixels.voxelbans.shared.connection.PlayerDataRetriever;
import com.volumetricpixels.voxelbans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.voxelbans.shared.perapi.VBConfig;
import com.volumetricpixels.voxelbans.shared.perapi.VBLocalBans;
import com.volumetricpixels.voxelbans.shared.perapi.VBMutes;
import com.volumetricpixels.voxelbans.shared.util.API;
import com.volumetricpixels.voxelbans.shared.util.SharedUtil;
import com.volumetricpixels.voxelbans.spout.event.VoxelBansDisableEvent;
import com.volumetricpixels.voxelbans.spout.event.VoxelBansEnableEvent;
import com.volumetricpixels.voxelbans.spout.files.SpoutGlobalBanStorer;
import com.volumetricpixels.voxelbans.spout.files.VBSpoutLocalBans;
import com.volumetricpixels.voxelbans.spout.files.VBSpoutConfig;
import com.volumetricpixels.voxelbans.spout.files.VBSpoutMutes;

/**
 * VoxelBans for Spout Engine
 * @author DziNeIT
 */
public class VoxelBansSpout extends CommonPlugin implements VoxelBans {
    
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
    
    private String apiKey = "VoxelBansAPIKeyToDoGet";
    private VBSpoutConfig config = null;
    private VBSpoutCommandHandler cmdHandler = null;
    
    // Web stuff and global ban temporary storing
    public BanSynchronizer bs;
    public DataRetriever mainDataRetriever;
    public SpoutGlobalBanStorer gbts;
    public PlayerDataRetriever pdr;
    
    public VoxelBansSpout() {
        this.bans = new VBSpoutLocalBans(this);
        this.mutes = new VBSpoutMutes(this);
        this.punishments = new VBPunishments(this);
    }
    
    /** CommonPlugin Overriden Methods **/
    
    @Override
    public void onEnable() {
        this.config = new VBSpoutConfig(this);
        this.apiKey = config.getNode("API-Key").getString(null);
        this.e = getEngine();
        this.em = e.getEventManager();
        this.pm = e.getPluginManager();
        
        // Init web stuff
        this.mainDataRetriever = new DataRetriever(this);
        this.gbts = new SpoutGlobalBanStorer(this);
        this.bs = new BanSynchronizer(this);
        this.pdr = new PlayerDataRetriever(this);
        
        // Init command vars and setup commands
        // Then set help for commands
        this.cmdHandler = new VBSpoutCommandHandler(this);
        Command rootCmd = e.getRootCommand();
        rootCmd.addSubCommand(e, "vbans").setExecutor(cmdHandler).setHelp("VoxelBans main command!");
        rootCmd.addSubCommand(e, "ban").setExecutor(cmdHandler).setHelp("Banning command!");
        rootCmd.addSubCommand(e, "kick").setExecutor(cmdHandler).setHelp("Kicking command!");
        rootCmd.addSubCommand(e, "mute").setExecutor(cmdHandler).setHelp("Muting command!");
        rootCmd.addSubCommand(e, "banreason").setExecutor(cmdHandler).setHelp("Gets the ban reason for a player!");
        rootCmd.addSubCommand(e, "banlist").setExecutor(cmdHandler).setHelp("List bans!");
        rootCmd.addSubCommand(e, "lookup").setExecutor(cmdHandler).setHelp("Lookup a player!");
        rootCmd.addSubCommand(e, "unban").setExecutor(cmdHandler).setHelp("Unbanning command!");
        
        /*
         * Tell the punishments handler the plugin is enabled so it can use methods from CommonPlugin and initialize the utils 
         * (SharedUtil.init also calls VBUtils.init)
         */
        SharedUtil.init(this);
        punishments.pluginEnabled();
        SpoutUtils.instance = new SpoutUtils();
        
        // Check API Key validity
        if (!apiKeyValid()) {
            System.out.println("[VoxelBans] You have not added an API Key to the config, or the entered key is invalid!");
            System.out.println("[VoxelBans] If you have an API Key, add it to the config!");
            System.out.println("[VoxelBans] If you have not obtained an API Key, go to voxelbans.net!");
            System.out.println("[VoxelBans] VoxelBans will disable until a valid Key is added!");
            pm.disablePlugin(this);
            return;
        }
        
        // Check for server validity (E.G. It is disabled)
        if (!isServerValid()) {
            System.err.println("[VoxelBans] An error occurred with your server:");
            if (isServerDisabled()) {
                System.out.println("[VoxelBans] Your server has been disabled by the VoxelBans administration!");
                System.out.println("[VoxelBans] To appeal this, view your server page and click 'Appeal Server Removal'");
                System.out.println("[VoxelBans] You must be logged in to VoxelBans on the account used to register!");
                System.out.println("[VoxelBans] Disabling until issues resolved!");
                pm.disablePlugin(this);
            } else {
                System.out.println("[VoxelBans] There was a request error to the VoxelBans servers! Continuing!");
                return;
            }
        }
        
        // Call our enable event and update / init everything remaining to do
        perms.update();
        bans.init();
        em.callEvent(new VoxelBansEnableEvent(this));
    }
    
    @Override
    public void onDisable() {
        // Call disable event for other plugins
        em.callEvent(new VoxelBansDisableEvent(this));
    }
    
    /** VoxelBans Interface Overriden Methods **/
    
    @Override
    public String getServerKey() {
        return apiKey;
    }
    
    @Override
    public VBConfig getVBConfig() {
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
        // Checks if the VoxelBans admins disabled the server
        return mainDataRetriever.isServerDisabled();
    }
    
}
