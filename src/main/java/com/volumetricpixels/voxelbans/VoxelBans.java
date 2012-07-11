package com.volumetricpixels.voxelbans;

import org.spout.api.Engine;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginManager;

import com.volumetricpixels.voxelbans.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.connection.DataRetriever;
import com.volumetricpixels.voxelbans.event.VoxelBansDisableEvent;
import com.volumetricpixels.voxelbans.event.VoxelBansEnableEvent;
import com.volumetricpixels.voxelbans.files.GlobalBanTempSaver;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.files.VBConfiguration;
import com.volumetricpixels.voxelbans.files.VBMuteFile;
import com.volumetricpixels.voxelbans.punishments.VBPunishmentHandler;

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
    
    public BanSynchronizer bs;
    public DataRetriever mainDataRetriever;
    public GlobalBanTempSaver gbts;
    
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
        
        this.mainDataRetriever = new DataRetriever(this);
        this.gbts = new GlobalBanTempSaver(this);
        this.bs = new BanSynchronizer(this);
        
        punishments.pluginEnabled();
        
        if (!apiKeyValid()) {
            System.out.println("[VoxelBans] You have not added an API Key to the config, or the entered key is invalid!");
            System.out.println("[VoxelBans] If you have an API Key, add it to the config!");
            System.out.println("[VoxelBans] If you have not obtained an API Key, go to voxelbans.net!");
            System.out.println("[VoxelBans] VoxelBans will disable until a valid Key is added!");
            pm.disablePlugin(this);
            return;
        }
        
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

        em.callEvent(new VoxelBansEnableEvent(this));
        perms.update();
        bans.init();
    }
    
    public void onDisable() {
        em.callEvent(new VoxelBansDisableEvent(this));
    }

    public String getServerKey() {
        return apiKey;
    }
    
    public boolean apiKeyValid() {
        if (apiKey != null) {
            // Performs a server check; if it is null an exception was thrown, meaning there was probably an invalid API Key
            // There may be errors with this check, so it may have to be changed later.
            return mainDataRetriever.getAllBans() != null;
        }
        return false;
    }
    
    public boolean isServerValid() {
        return !isServerDisabled();
    }
    
    public boolean isServerDisabled() {
        return mainDataRetriever.isServerDisabled();
    }
    
}
