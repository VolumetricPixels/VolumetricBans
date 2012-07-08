package com.volumetricpixels.voxelbans.connection;

import java.util.List;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.punishments.Ban;

public class BanSynchronizer implements Runnable {
    
    private VoxelBans plugin;
    private VBBanFile bans;
    private BanChecker websiteChecker;
    
    public BanSynchronizer(VoxelBans plugin) {
        this.plugin = plugin;
        this.bans = this.plugin.bans;
        this.websiteChecker = new BanChecker(plugin);
    }
    
    @Override
    public void run() {
        List<Ban> banList = bans.getBans();
        for (Ban b : banList) {
            if (!websiteChecker.isBannedAtAll(b.getPlayer())) {
                if (b.isTemporary()) {
                    submitBan(b.getPlayer(), b.getReason(), b.getAdmin(), b.getTime());
                } else {
                    submitBan(b.getPlayer(), b.getReason(), b.getAdmin(), b.isGlobal());
                }
            } else {
                continue;
            }
        }
    }
    
    private void submitBan(String player, String reason, String admin, boolean global) {
        
    }
    
    private void submitBan(String player, String reason, String admin, long time) {
        
    }
    
}
