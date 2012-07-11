package com.volumetricpixels.voxelbans.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.punishments.Ban;

/**
 * Retrieves data from the server;
 * Not timed, just utils for other classes in this package.
 * Deals only with server things, not locally stored.
 */
public class DataRetriever {
    
    private VoxelBans plugin;
    
    public DataRetriever(VoxelBans plugin) {
        this.plugin = plugin;
    }
    
    public List<Ban> getAllBans() {
        List<Ban> result = new ArrayList<Ban>();
        result.addAll(getGlobalBans());
        result.addAll(getLocalBans());
        return result;
    }
    
    public boolean isServerDisabled() {
        boolean result = false;
        try {
            JSONHandler jH = new JSONHandler(plugin, "data");
            Map<String, String> urlItems = new HashMap<String, String>();
            urlItems.put("action", "checkServerDisabled");
            JSONObject jO = jH.handleJsonObject(urlItems);
            if (jO.getBoolean("result") == true) {
                result = true;
            }
        } catch (Exception e) {}
        return result;
    }
    
    public List<Ban> getGlobalBans() {
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
            return result;
        } catch (Exception e) {}
        return null;
    }
    
    public List<Ban> getLocalBans() {
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
            return result;
        } catch (Exception e) {}
        return null;
    }
    
}
