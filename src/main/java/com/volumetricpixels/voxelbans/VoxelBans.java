package com.volumetricpixels.voxelbans;

import org.spout.api.Engine;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginManager;

import com.volumetricpixels.voxelbans.connection.JSONHandler;
import com.volumetricpixels.voxelbans.event.VoxelBansDisableEvent;
import com.volumetricpixels.voxelbans.event.VoxelBansEnableEvent;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.files.VBConfiguration;
import com.volumetricpixels.voxelbans.files.VBMuteFile;
import com.volumetricpixels.voxelbans.punishments.VBPunishmentHandler;

public class VoxelBans extends CommonPlugin {
    
    public final VBPermissions perms = VBPermissions.perms;
    public final VBPunishmentHandler punishments;
    public final VBBanFile bans;
    public final VBMuteFile mutes;
    public final JSONHandler jh;
    
    public final Object[] noPermsMessage = {
        ChatStyle.RED, "You don't have permission to do that!"
    };
    
    private Engine e;
    private EventManager em;
    private PluginManager pm;
    
    private String apiKey = "VoxelBansAPIKeyToDoGet";
    private VBConfiguration config = null;
    
    public VoxelBans() {
        this.punishments = new VBPunishmentHandler(this);
        this.bans = new VBBanFile(this);
        this.mutes = new VBMuteFile(this);
        this.jh = new JSONHandler(this);
    }
    
    public void onEnable() {
        this.config = new VBConfiguration(this);
        this.apiKey = config.getNode("API-Key").getString(null);
        this.e = getEngine();
        this.em = e.getEventManager();
        this.pm = e.getPluginManager();
        
        if (!apiKeyValid()) {
            System.out.println("[VoxelBans] You have not added an API Key to the config, or the entered key is invalid!");
            System.out.println("[VoxelBans] If you have an API Key, add it to the config!");
            System.out.println("[VoxelBans] If you have not obtained an API Key, go to voxelbans.net!");
            System.out.println("[VoxelBans] VoxelBans will disable until a valid Key is added!");
            pm.disablePlugin(this);
            return;
        }
        
        if (!isServerValid()) {
            System.out.println("[VoxelBans] An error occurred with your server:");
            if (isServerDisabled()) {
                System.out.println("Your server has been disabled by the VoxelBans administration!");
                System.out.println("To appeal this, view your server page and click 'Appeal Server Removal'");
                System.out.println("You must be logged in to VoxelBans on the account used to register!");
            } // TODO: Other reasons when added
            else {
                System.out.println("An error occurred with requesting the VoxelBans server!");
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
            // TODO: Further testing!
            return true;
        }
        return false;
    }
    
    public boolean isServerValid() {
        // TODO: Test for other things
        return !isServerDisabled();
    }
    
    public boolean isServerDisabled() {
        // TODO: Check for server being disabled
        return false;
    }
    
}
