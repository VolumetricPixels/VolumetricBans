package com.volumetricpixels.bans.shared.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.shared.perapi.Ban;
import com.volumetricpixels.bans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;

/**
 * Deals only with website bans.
 * Synchronizes bans (adds to website)
 * @author DziNeIT
 */
public class BanSynchronizer implements Runnable {
    
    private VolumetricBans plugin;
    private VBLocalBans bans;
    private GlobalBanStorer gbts;
    private DataRetriever dr;
    
    public BanSynchronizer(VolumetricBans plugin) {
        this.plugin = plugin;
        this.bans = this.plugin.getLocalBanHandler();
        this.gbts = this.plugin.getGlobalBanStorer();
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
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "unban");
            postData.put("player", player);
            JSONObject jO = jH.handleJsonObject(postData);
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
                Map<String, String> postData = new HashMap<String, String>();
                postData.put("action", "submitBan");
                postData.put("player", player);
                postData.put("reason", reason);
                postData.put("admin", admin);
                postData.put("time", String.valueOf(time));
                JSONObject jO = jH.handleJsonObject(postData);
                if (jO.getString("result").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key!");
                }
            } catch (Exception ignore) {}
        } else {
            try {
                JSONHandler jH = new JSONHandler(plugin, "bans");
                Map<String, String> postData = new HashMap<String, String>();
                postData.put("action", "submitBan");
                postData.put("player", player);
                postData.put("reason", reason);
                postData.put("admin", admin);
                postData.put("global", String.valueOf(b.isGlobal()));
                JSONObject jO = jH.handleJsonObject(postData);
                if (jO.getString("response").equalsIgnoreCase("OK")) {
                    gbts.remove(this, b);
                } else {
                    System.err.println("[VoxelBans] JSON ban response was not OK! Possible invalid server API Key!");
                }
            } catch (Exception ignore) {}
        }
    }
    
}
