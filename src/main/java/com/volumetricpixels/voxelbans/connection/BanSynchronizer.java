package com.volumetricpixels.voxelbans.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.files.GlobalBanTempSaver;
import com.volumetricpixels.voxelbans.files.VBBanFile;
import com.volumetricpixels.voxelbans.org.json.JSONObject;
import com.volumetricpixels.voxelbans.punishments.Ban;

/**
 * Deals only with website bans.
 * Synchronizes bans (adds to website)
 * @author DziNeIT
 */
public class BanSynchronizer implements Runnable {
    
    private VoxelBans plugin;
    private VBBanFile bans;
    private GlobalBanTempSaver gbts;
    private DataRetriever dr;
    
    public BanSynchronizer(VoxelBans plugin) {
        this.plugin = plugin;
        this.bans = this.plugin.bans;
        this.gbts = this.plugin.gbts;
        this.dr = new DataRetriever(plugin);
    }
    
    @Override
    public void run() {
        List<Ban> localBanList = new ArrayList<Ban>();
        localBanList.addAll(bans.getBans());
        for (Ban b : localBanList) {
            if (!isLocallyBanned(b.getPlayer())) {
                if (b.isTemporary()) {
                    submitBan(b, true);
                } else {
                    submitBan(b, false);
                }
            }
        }
        List<Ban> globalBanList = new ArrayList<Ban>();
        globalBanList.addAll(gbts.getBansToSubmit());
        for (Ban b : globalBanList) {
            if (!isGloballyBanned(b.getPlayer())) {
                submitBan(b, false);
                try {
                    gbts.remove(this, b);
                } catch (IllegalAccessException neverHappens) {} // Exception only occurs if param 1 in remove is null
            }
        }
    }
    
    @SuppressWarnings("unused")
    private boolean isBannedAtAll(String player) {
        return dr.getAllBans().contains(player);
    }
    
    private boolean isGloballyBanned(String player) {
        for (Ban b : dr.getGlobalBans()) {
            if (b.getPlayer().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isLocallyBanned(String player) {
        for (Ban b : dr.getLocalBans()) {
            if (b.getPlayer().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeBan(String player) {
        try {
            JSONHandler jH = new JSONHandler(plugin, "bans");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "unban");
            urlItems.put("player", player);
            JSONObject jO = jH.handleJsonObject(urlItems);
            if (!jO.getString("result").equalsIgnoreCase("OK")) {
                System.err.println("[VoxelBans] JSON unban response was not OK! Possible invalid server API Key!");
            } else {
                
            }
        } catch (Exception e) {}
    }
    
    private void submitBan(Ban b, boolean temp) {
        String player = b.getPlayer();
        String reason = b.getReason();
        String admin = b.getAdmin();
        if (temp) {
            long time = b.getTime();
            try {
                JSONHandler jH = new JSONHandler(plugin, "bans");
                Map<String, String> urlItems = new HashMap<String, String>();
                urlItems.put("action", "submitBan");
                urlItems.put("player", player);
                urlItems.put("reason", reason);
                urlItems.put("admin", admin);
                urlItems.put("time", String.valueOf(time));
                JSONObject jO = jH.handleJsonObject(urlItems);
                if (jO.getString("result").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key!");
                }
            } catch (Exception ignore) {}
        } else {
            try {
                JSONHandler jH = new JSONHandler(plugin, "bans");
                Map<String, String> urlItems = new HashMap<String, String>();
                urlItems.put("action", "submitBan");
                urlItems.put("player", player);
                urlItems.put("reason", reason);
                urlItems.put("admin", admin);
                urlItems.put("global", String.valueOf(b.isGlobal()));
                JSONObject jO = jH.handleJsonObject(urlItems);
                if (jO.getString("response").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key!");
                }
            } catch (Exception ignore) {}
        }
    }
    
}
