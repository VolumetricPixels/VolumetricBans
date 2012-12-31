package com.volumetricpixels.bans.common.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.lib.org.json.JSONObject;

public class PlayerDataRetriever {
    private VolumetricBans plugin;

    public PlayerDataRetriever(VolumetricBans vb) {
        this.plugin = vb;
    }

    public boolean isBannedFromVBServers(String name) {
        boolean result = false;
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "checkGlobalBanned");
            postData.put("player", name);
            JSONObject jO = jH.handleJsonObject(postData);
            result = jO.getBoolean("result");
        } catch (Exception e) {
        }
        return result;
    }

    public int getReputation(String name) {
        int result = 10;
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "getRep");
            postData.put("player", name);
            JSONObject jO = jH.handleJsonObject(postData);
            result = jO.getInt("result");
        } catch (Exception e) {
        }
        return result;
    }

    public List<String> getServersBannedFrom(String name) {
        List<String> result = new ArrayList<String>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "getServersBannedFrom");
            postData.put("player", name);
            JSONObject jO = jH.handleJsonObject(postData);
            for (int i = 0; i <= jO.getJSONArray("result").length(); i++) {
                result.add(jO.getJSONArray("result").getString(i));
            }
        } catch (Exception e) {
        }
        return result;
    }
}
