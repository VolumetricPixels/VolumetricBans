package com.volumetricpixels.voxelbans;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Engine;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginManager;

import com.volumetricpixels.voxelbans.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.connection.DataRetriever;
import com.volumetricpixels.voxelbans.connection.PlayerDataRetriever;
import com.volumetricpixels.voxelbans.event.VoxelBansDisableEvent;
import com.volumetricpixels.voxelbans.event.VoxelBansEnableEvent;
import com.volumetricpixels.voxelbans.files.GlobalBanTempSaver;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.files.VBConfiguration;
import com.volumetricpixels.voxelbans.files.VBMuteFile;
import com.volumetricpixels.voxelbans.integration.VBLogNRollback;
import com.volumetricpixels.voxelbans.integration.VBPluginIntegration;
import com.volumetricpixels.voxelbans.integration.VBStopDemHax;
import com.volumetricpixels.voxelbans.punishments.VBPunishmentHandler;

/**
 * VoxelBans for Spout Engine
 * @author DziNeIT
 */
public class VoxelBans extends CommonPlugin {
    
    public final VBPermissions perms = VBPermissions.perms;
    public final VBPunishmentHandler punishments;
    public final VBBanFile bans;
    public final VBMuteFile mutes;
    
    public final Object[] noPermsMessage = {
        ChatStyle.RED, "You don't have permission to do that!"
    };
    
    private Engine e;
    private EventManager em;
    private PluginManager pm;
    
    private String apiKey = "VoxelBansAPIKeyToDoGet";
    private VBConfiguration config = null;
    private List<VBPluginIntegration> integrations = new ArrayList<VBPluginIntegration>();
    
    // Web stuff and global ban temporary storing
    public BanSynchronizer bs;
    public DataRetriever mainDataRetriever;
    public GlobalBanTempSaver gbts;
    public PlayerDataRetriever pdr;
    
    public VoxelBans() {
        this.bans = new VBBanFile(this);
        this.mutes = new VBMuteFile(this);
        this.punishments = new VBPunishmentHandler(this);
    }
    
    public void onEnable() {
        this.config = new VBConfiguration(this);
        this.apiKey = config.getNode("API-Key").getString(null);
        this.e = getEngine();
        this.em = e.getEventManager();
        this.pm = e.getPluginManager();
        
        // Integrations: VoxelBans' way of using other plugins and enhancing them!
        setupIntegration(); // VBPluginIntegration extends Listener so all things to do with one plugin go in one class
        for (VBPluginIntegration pi : integrations) {
            if (pi.integrationEnabled()) {
                em.registerEvents(pi, this);
            }
        }
        
        // Init web stuff
        this.mainDataRetriever = new DataRetriever(this);
        this.gbts = new GlobalBanTempSaver(this);
        this.bs = new BanSynchronizer(this);
        this.pdr = new PlayerDataRetriever(this);
        
        // Tell the punishments handler the plugin is enabled so it can use methods from CommonPlugin
        punishments.pluginEnabled();
        
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
                System.out.println("Your server has been disabled by the VoxelBans administration!");
                System.out.println("To appeal this, view your server page and click 'Appeal Server Removal'");
                System.out.println("You must be logged in to VoxelBans on the account used to register!");
            } else {
                System.err.println("An error occurred with requesting the VoxelBans server!");
            }
            System.out.println("[VoxelBans] Disabling until issues resolved!");
            pm.disablePlugin(this);
        }

        // Call our enable event and update / init everything remaining to do
        perms.update();
        bans.init();
        em.callEvent(new VoxelBansEnableEvent(this));
    }
    
    public void onDisable() {
        // Call disable event for other plugins
        em.callEvent(new VoxelBansDisableEvent(this));
    }

    public String getServerKey() {
        return apiKey;
    }
    
    public VBConfiguration getConfig() {
        return config;
    }
    
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
    
    private void setupIntegration() {
        // Sets up integration
        if (pm.getPlugin("StopDemHax") != null) {
            integrations.add(new VBStopDemHax());
        }
        if (pm.getPlugin("LogNRollback") != null) {
            integrations.add(new VBLogNRollback());
        }
    }
    
}
