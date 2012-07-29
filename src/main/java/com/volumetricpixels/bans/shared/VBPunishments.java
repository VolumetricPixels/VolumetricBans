package com.volumetricpixels.bans.shared;

import com.volumetricpixels.bans.VBUtils;
import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.shared.connection.BanSynchronizer;
import com.volumetricpixels.bans.shared.perapi.Ban;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;
import com.volumetricpixels.bans.shared.perapi.VBMutes;
import com.volumetricpixels.bans.spout.util.SpoutUtils;

public class VBPunishments {
    
    private final VolumetricBans plugin;
    private final VBLocalBans bans;
    private final VBMutes mutes;
    private BanSynchronizer bs;
    
    public VBPunishments(VolumetricBans voxelBans) {
        this.plugin = voxelBans;
        this.bans = plugin.getLocalBanHandler();
        this.mutes = plugin.getMuteHandler();
    }
    
    public void pluginEnabled() {
        this.bs = plugin.getBanSynchronizer();
    }

    public void globalBanPlayer(String name, String reason, String admin) {
        plugin.getGlobalBanStorer().addToTempList(VBUtils.newBan(name, reason, admin, true));
    }
    
    public void localBanPlayer(String name, String reason, String admin) {
        bans.banPlayer(name, reason, admin);
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
    
    public void kickPlayer(String player, Object... kickMessage) {
        switch (plugin.getInUseAPI()) {
            case SPOUT:
                SpoutUtils.instance.kickPlayer(player, kickMessage);
            default:
                throw new UnsupportedOperationException("Kicking is not yet supported in " + plugin.getInUseAPI().name());
        }
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
        for (Ban b : plugin.getMainDataRetriever().getGlobalBans()) {
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
        for (Ban b : plugin.getMainDataRetriever().getLocalBans()) {
            if (player.equalsIgnoreCase(b.getPlayer())) {
                return true;
            }
        }
        return false;
    }
    
}
