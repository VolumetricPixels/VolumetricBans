package com.volumetricpixels.voxelbans;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.plugin.CommonPlugin;

import com.volumetricpixels.voxelbans.event.VoxelBansDisableEvent;

public class VoxelBans extends CommonPlugin {
    
    public final VBPermissions perms = VBPermissions.perms;
    public final VBPunishmentHandler punishments;
    
    public final Object[] noPermsMessage = {
        ChatStyle.RED, "You don't have permission to do that!"
    };
    
    public VoxelBans() {
        this.punishments = new VBPunishmentHandler(this);
    }
    
    public void onEnable() {
        perms.update();
    }
    
    public void onDisable() {
        getEngine().getEventManager().callEvent(new VoxelBansDisableEvent(this));
    }
    
}
