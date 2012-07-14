package com.volumetricpixels.voxelbans.spout.punishments;

import org.spout.api.Spout;

import com.volumetricpixels.voxelbans.VBUtils;
import com.volumetricpixels.voxelbans.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.interfaces.Ban;
import com.volumetricpixels.voxelbans.interfaces.VBPunishments;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;
import com.volumetricpixels.voxelbans.spout.files.VBSpoutLocalBans;
import com.volumetricpixels.voxelbans.spout.files.VBSpoutMutes;

public class VBSpoutPunishments implements VBPunishments {
    
    private final VoxelBansSpout plugin;
    private final VBSpoutLocalBans bans;
    private final VBSpoutMutes mutes;
    private BanSynchronizer bs;
    
    public VBSpoutPunishments(VoxelBansSpout voxelBansSpout) {
        this.plugin = voxelBansSpout;
        this.bans = plugin.bans;
        this.mutes = plugin.mutes;
    }
    
    public void pluginEnabled() {
        this.bs = plugin.bs;
    }

    public void globalBanPlayer(String name, String reason, String admin) {
        plugin.gbts.addToTempList(VBUtils.newBan(name, reason, admin, true));
    }
    
    public void localBanPlayer(String name, String reason, String admin) {
        bans.banPlayer(name, reason, admin);
        if (Spout.getEngine().getPlayer(name, false) != null) {
            kickPlayer(name, reason);
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
    
    public void kickPlayer(String p, Object... reason) {
        Spout.getEngine().getPlayer(p, true).kick(reason);
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
