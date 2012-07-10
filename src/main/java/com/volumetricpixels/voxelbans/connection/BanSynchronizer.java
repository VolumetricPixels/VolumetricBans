package com.volumetricpixels.voxelbans.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

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
                if (jO.getString("Response").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key or servers are down!");
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
                if (jO.getString("Response").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key or servers are down!");
                }
            } catch (Exception ignore) {}
        }
    }
    
    private List<Ban> getGlobalBans() {
        List<Ban> result = new ArrayList<Ban>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "data");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "getGlobalBans");
            JSONObject jO = jH.handleJsonObject(urlItems);
            JSONArray jA = jO.getJSONArray("globalBans");
            for (int i = 0; i <= jA.length(); i++) {
                String s = String.valueOf(jA.get(i));
                String[] sArray = s.split(":");
                result.add(new Ban(sArray[0], sArray[1], sArray[2], true));
            }
        } catch (Exception e) {}
        return result;
    }
    
    private List<Ban> getLocalBans() {
        List<Ban> result = new ArrayList<Ban>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "data");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "getLocalBans");
            JSONObject jO = jH.handleJsonObject(urlItems);
            JSONArray jA = jO.getJSONArray("localBans");
            for (int i = 0; i <= jA.length(); i++) {
                String s = String.valueOf(jA.get(i));
                String[] sArray = s.split(":");
                result.add(new Ban(sArray[0], sArray[1], sArray[2], true));
            }
        } catch (Exception e) {}
        return result;
    }
    
    private List<Ban> getAllBans() {
        List<Ban> result = new ArrayList<Ban>();
        result.addAll(getLocalBans());
        result.addAll(getGlobalBans());
        return result;
    }
    
}
