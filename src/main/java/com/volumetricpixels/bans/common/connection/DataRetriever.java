package com.volumetricpixels.bans.common.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.common.util.GeneralUtil;
import com.volumetricpixels.bans.interfaces.Ban;
import com.volumetricpixels.bans.lib.org.json.JSONArray;
import com.volumetricpixels.bans.lib.org.json.JSONObject;

/**
 * Retrieves data from the server; Not timed, just utils for other classes in
 * this package. Deals only with server things, not locally stored.
 */
public class DataRetriever {
    private VolumetricBans plugin;

    public DataRetriever(VolumetricBans plugin) {
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
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "isServerDisabled");
            JSONObject jO = jH.handleJsonObject(postData);
            result = jO.getBoolean("result");
        } catch (Exception e) {
        }
        return result;
    }

    public List<Ban> getGlobalBans() {
        List<Ban> result = new ArrayList<Ban>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "data");
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "getGlobalBans");
            JSONObject jO = jH.handleJsonObject(postData);
            JSONArray jA = jO.getJSONArray("globalBans");
            for (int i = 0; i <= jA.length(); i++) {
                String s = String.valueOf(jA.get(i));
                String[] sArray = s.split(":");
                result.add(GeneralUtil.newBan(sArray[0], sArray[1], sArray[2], true));
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public List<Ban> getLocalBans() {
        List<Ban> result = new ArrayList<Ban>();
        try {
            JSONHandler jH = new JSONHandler(plugin, "data");
            Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "getLocalBans");
            JSONObject jO = jH.handleJsonObject(postData);
            JSONArray jA = jO.getJSONArray("localBans");
            for (int i = 0; i <= jA.length(); i++) {
                String s = String.valueOf(jA.get(i));
                String[] sArray = s.split(":");
                result.add(GeneralUtil.newBan(sArray[0], sArray[1], sArray[2], false));
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isVBServerOnline() {
        // TODO: Check
        return true;
    }
}
