package com.volumetricpixels.voxelbans;

import org.spout.api.player.Player;

public class VBPunishmentHandler {
    
    private final VoxelBans plugin;
    
    public VBPunishmentHandler(VoxelBans voxelBans) {
        this.plugin = voxelBans;
    }

    public void globalBanPlayer(Player p, String reason) {
        String name = p.getName();
        // TODO: Banning
    }
    
    public void localBanPlayer(Player p, String reason) {
        String name = p.getName();
        // TODO: Banning
    }
    
    public void tempBanPlayer(Player p, String reason, long timeMinutes) {
        String name = p.getName();
        // TODO: Banning
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
        // TODO: Checking
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
