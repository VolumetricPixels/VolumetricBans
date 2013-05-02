package com.volumetricpixels.bans.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.exception.DataRetrievalException;

/**
 * Utility methods for API requests, such as performing operations on
 * JSONObjects
 */
public final class APIRequestUtil {
    /**
     * Gets the Map of data inside a JSONObject
     * 
     * @param json
     *            The JSONObject to get the Map from
     * 
     * @return The Map created from the JSONObject's values
     */
    public static Map<String, Object> getMap(final JSONObject json) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final String name : JSONObject.getNames(json))
            try {
                map.put(name, json.get(name));
            } catch (final JSONException e) {
                e.printStackTrace();
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
        final Map<String, String> postData = new HashMap<String, String>();
        postData.put("action", "checkPermanentGlobalBan");
        postData.put("strictGlobal", handler.getPlugin().isStrictGlobal() ? "true" : "false");
        postData.put("playerName", playerName);
        final JSONObject response = handler.submitRequest(postData);
        try {
            return response.getBoolean("result");
        } catch (final JSONException e) {
            throw new DataRetrievalException("Could not check if player was permabanned globally!", e);
        }
    }
}
