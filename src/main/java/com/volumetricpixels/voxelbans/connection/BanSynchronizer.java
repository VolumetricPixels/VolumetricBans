package com.volumetricpixels.voxelbans.connection;

import java.util.ArrayList;
import java.util.List;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.files.GlobalBanTempSaver;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.punishments.Ban;

/**
 * Deals only with website bans.
 */
public class BanSynchronizer implements Runnable {
    
    private VoxelBans plugin;
    private VBBanFile bans;
    private GlobalBanTempSaver gbts;
    
    public BanSynchronizer(VoxelBans plugin) {
        this.plugin = plugin;
        this.bans = this.plugin.bans;
        this.gbts = new GlobalBanTempSaver(plugin);
    }
    
    @Override
    public void run() {
        List<Ban> localBanList = new ArrayList<Ban>();
        localBanList.addAll(bans.getBans());
        for (Ban b : localBanList) {
            if (!isLocallyBanned(b.getPlayer())) {
                if (b.isTemporary()) {
                    submitBan(b.getPlayer(), b.getReason(), b.getAdmin(), b.getTime());
                } else {
                    submitBan(b.getPlayer(), b.getReason(), b.getAdmin(), b.isGlobal());
                }
            }
        }
        List<Ban> globalBanList = new ArrayList<Ban>();
        globalBanList.addAll(gbts.getBansToSubmit());
        for (Ban b : globalBanList) {
            if (!isGloballyBanned(b.getPlayer())) {
                submitBan(b.getPlayer(), b.getReason(), b.getAdmin(), true);
                try {
                    gbts.remove(this, b);
                } catch (IllegalAccessException neverHappens) {} // Exception only occurs if param 2 in remove is null
            }
        }
    }
    
    @SuppressWarnings("unused")
    private boolean isBannedAtAll(String player) {
        return getAllBans().contains(player);
    }
    
    private boolean isGloballyBanned(String player) {
        for (Ban b : getGlobalBans()) {
            if (b.getPlayer().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isLocallyBanned(String player) {
        for (Ban b : getLocalBans()) {
            if (b.getPlayer().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
    
    private void submitBan(String player, String reason, String admin, boolean global) {
        // TODO: Submit ban
    }
    
    private void submitBan(String player, String reason, String admin, long time) {
        // TODO: Submit ban
    }
    
    private List<Ban> getGlobalBans() {
        List<Ban> result = new ArrayList<Ban>();
        // TODO: Check
        return result;
    }
    
    private List<Ban> getLocalBans() {
        List<Ban> result = new ArrayList<Ban>();
        // TODO: Check
        return result;
    }
    
    private List<Ban> getAllBans() {
        List<Ban> result = new ArrayList<Ban>();
        result.addAll(getLocalBans());
        result.addAll(getGlobalBans());
        return result;
    }
    
}
