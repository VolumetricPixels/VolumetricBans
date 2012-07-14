package com.volumetricpixels.voxelbans.shared.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.volumetricpixels.voxelbans.VoxelBans;

public class PlayerDataRetriever {
    
    private VoxelBans plugin;
    
    public PlayerDataRetriever(VoxelBans vb) {
        this.plugin = vb;
    }
    
    public boolean isBannedFromVBServers(String name) {
        boolean result = false;
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "checkGlobalBanned");
            urlItems.put("player", name);
            JSONObject jO = jH.handleJsonObject(urlItems);
            result = jO.getBoolean("result");
        } catch (Exception e) {}
        return result;
    }
    
    public int getReputation(String name) {
        int result = 10;
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "getRep");
            urlItems.put("player", name);
            JSONObject jO = jH.handleJsonObject(urlItems);
            result = jO.getInt("result");
        } catch (Exception e) {}
        return result;
    }
    
    public List<String> getServersBannedFrom(String name) {
        List<String> result = new ArrayList<String>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "player");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "getServersBannedFrom");
            urlItems.put("player", name);
            JSONObject jO = jH.handleJsonObject(urlItems);
            for (int i = 0; i <= jO.getJSONArray("result").length(); i++) {
                result.add(jO.getJSONArray("result").getString(i));
            }
        } catch (Exception e) {}
        return result;
    }
    
}
