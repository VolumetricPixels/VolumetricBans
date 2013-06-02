package com.volumetricpixels.bans.util;

import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.request.APIRequestHandler;

/**
 * Utility methods for VolumetricBans, such as performing operations on
 * JSONObjects
 */
public final class Utilities {
    /**
     * Gets the Map of data inside a JSONObject
     * 
     * @param json
     *            The JSONObject to get the Map from
     * 
     * @return The Map created from the JSONObject's values
     */
    public static Map<String, Object> getMap(final JSONObject json) {
        final Map<String, Object> map = new THashMap<String, Object>();
        for (final String name : JSONObject.getNames(json)) {
            try {
                map.put(name, json.get(name));
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Checks whether the given player is globally permabanned using the given
     * APIRequestHandler
     * 
     * @param handler
     *            The APIRequestHandler to use. This should be the player type
     * @param playerName
     *            The name of the player to check for a global permaban
     * 
     * @return Whether the given player is globally permabanned
     * 
     * @throws DataRetrievalException
     *             When something goes wrong retrieving or parsing data
     */
    public static boolean isPermaGlobalBanned(final APIRequestHandler handler, final String playerName) throws DataRetrievalException {
        final Map<String, String> postData = new THashMap<String, String>();
        postData.put("action", "isBanned");
        postData.put("playerName", playerName);
        final JSONObject response = handler.submitRequest(postData);
        try {
            return response.getBoolean("result");
        } catch (final JSONException e) {
            throw new DataRetrievalException("Could not check if player was permabanned globally!", e);
        }
    }

    public static <A, B> Map<A, B> oneEntryMap(final A key, final B value) {
        final Map<A, B> map = new THashMap<A, B>();
        map.put(key, value);
        return map;
    }

    public static String[] genConfigHeader(VolumetricBans vb) {
        List<String> lines = new ArrayList<String>();
        String version = vb.getDescription().getVersion();
        lines.add("VolumetricBans " + version + " - " + VolumetricBans.WEBSITE_URL);
        lines.add("Developed by VolumetricPixels - http://volumetricpixels.com");
        lines.add("");
        lines.add("Configuration File (" + version + ")");
        return lines.toArray(new String[lines.size()]);
    }
}
