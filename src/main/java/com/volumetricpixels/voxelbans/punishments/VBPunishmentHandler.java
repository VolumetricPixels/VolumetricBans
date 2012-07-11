package com.volumetricpixels.voxelbans.punishments;

import org.spout.api.Spout;
import org.spout.api.player.Player;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.files.VBMuteFile;

public class VBPunishmentHandler {
    
    private final VoxelBans plugin;
    private final VBBanFile bans;
    private final VBMuteFile mutes;
    private BanSynchronizer bs;
    
    public VBPunishmentHandler(VoxelBans voxelBans) {
        this.plugin = voxelBans;
        this.bans = plugin.bans;
        this.mutes = plugin.mutes;
    }
    
    public void pluginEnabled() {
        this.bs = plugin.bs;
    }

    public void globalBanPlayer(String name, String reason, String admin) {
        plugin.gbts.addToTempList(new Ban(name, reason, admin, true));
    }
    
    public void localBanPlayer(String name, String reason, String admin) {
        bans.banPlayer(name, reason, admin);
        if (Spout.getEngine().getPlayer(name, false) != null) {
            kickPlayer(Spout.getEngine().getPlayer(name, false), reason);
        }
    }
    
    public void tempBanPlayer(String name, String reason, String admin, long timeMinutes) {
        bans.banPlayer(name, reason, admin, timeMinutes);
    }
    
    public void unbanPlayer(String name) {
        if (isGlobalBanned(name)) {
            bs.removeBan(name);
        }
        if (isLocalBanned(name)) {
            bans.unbanPlayer(name);
            bs.removeBan(name);
        }
    }
    
    public void kickPlayer(Player p, Object... reason) {
        p.kick(reason);
    }
    
    public void mutePlayer(String player, long time) {
        mutes.mutePlayer(player, time);
    }
    
    public void unmutePlayer(String player) {
        mutes.unmutePlayer(player);
    }
    
    public boolean isMuted(String player) {
        return mutes.isMuted(player);
    }
    
    public boolean isGlobalBanned(String player) {
        for (Ban b : plugin.mainDataRetriever.getGlobalBans()) {
            if (b.getPlayer().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLocalBanned(String player) {
        if (bans.isBanned(player)) {
            return true;
        }
        for (Ban b : plugin.mainDataRetriever.getLocalBans()) {
            if (player.equalsIgnoreCase(b.getPlayer())) {
                return true;
            }
        }
        return false;
    }
    
}
