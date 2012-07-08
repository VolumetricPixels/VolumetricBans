package com.volumetricpixels.voxelbans;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.plugin.CommonPlugin;

import com.volumetricpixels.voxelbans.event.VoxelBansDisableEvent;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.files.VBConfiguration;
import com.volumetricpixels.voxelbans.punishments.VBPunishmentHandler;

public class VoxelBans extends CommonPlugin {
    
    public final VBPermissions perms = VBPermissions.perms;
    public final VBPunishmentHandler punishments;
    public final VBBanFile bans;
    
    public final Object[] noPermsMessage = {
        ChatStyle.RED, "You don't have permission to do that!"
    };
    
    private String apiKey = null;
    private VBConfiguration config = null;
    
    public VoxelBans() {
        this.punishments = new VBPunishmentHandler(this);
        this.bans = new VBBanFile(this);
    }
    
    public void onEnable() {
        perms.update();
        this.config = new VBConfiguration(this);
        this.apiKey = config.getNode("Server-Key").getString();
        bans.init();
    }
    
    public void onDisable() {
        getEngine().getEventManager().callEvent(new VoxelBansDisableEvent(this));
    }

    public String getServerKey() {
        return apiKey;
    }
    
}
