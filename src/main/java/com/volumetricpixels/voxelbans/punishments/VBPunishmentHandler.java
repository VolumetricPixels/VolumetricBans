package com.volumetricpixels.voxelbans.punishments;

import org.spout.api.Spout;
import org.spout.api.player.Player;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.files.VBBanFile;

public class VBPunishmentHandler {
    
    private final VoxelBans plugin;
    private final VBBanFile bans;
    
    public VBPunishmentHandler(VoxelBans voxelBans) {
        this.plugin = voxelBans;
        this.bans = plugin.bans;
    }

    public void globalBanPlayer(String name, String reason, String admin) {
        bans.banPlayer(name, reason, admin, true);
        // TODO: Global
    }
    
    public void localBanPlayer(String name, String reason, String admin) {
        bans.banPlayer(name, reason, admin, false);
        if (Spout.getEngine().getPlayer(name, false) != null) {
            kickPlayer(Spout.getEngine().getPlayer(name, false), reason);
        }
    }
    
    public void tempBanPlayer(String name, String reason, String admin, long timeMinutes) {
        bans.banPlayer(name, reason, admin, timeMinutes);
    }
    
    public void unbanPlayer(String name) {
        if (isGlobalBanned(name)) {
            // TODO: Global unban
        } else if (isLocalBanned(name)) {
            // TODO: Local unban (temporarily banned players are also classed as locally banned)
        }
    }
    
    public void kickPlayer(Player p, Object... reason) {
        p.kick(reason);
    }
    
    public boolean isGlobalBanned(String player) {
        // TODO: Check
        return false;
    }
    
    public boolean isLocalBanned(String player) {
        // TODO: Checking
        return false;
    }
    
    public boolean isTempBanned(String player) {
        if (isLocalBanned(player)) {
            // TODO: Temp checking
            return true;
        }
        return false;
    }
    
}
