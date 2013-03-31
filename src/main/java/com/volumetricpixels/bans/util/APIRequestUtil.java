package com.volumetricpixels.bans.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.exception.DataRetrievalException;

/**
 * Utility methods for API requests
 */
public class APIRequestUtil {
	/**
	 * Gets the Map of data inside a JSONObject
	 * 
	 * @param json
	 *            The JSONObject to get the Map from
	 * @return The Map created from the JSONObject's values
	 */
	public static Map<String, Object> getMap(JSONObject json) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String name : JSONObject.getNames(json)) {
			try {
				map.put(name, json.get(name));
			} catch (JSONException e) {
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
	 * @return Whether the given player is globally permabanned
	 * @throws DataRetrievalException
	 *             When something goes wrong retrieving or parsing data
	 */
	public static boolean isPermaGlobalBanned(APIRequestHandler handler, String playerName) throws DataRetrievalException {
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("action", "checkPermanentGlobalBan");
		postData.put("strictGlobal", Boolean.toString(handler.getPlugin().isStrictGlobal()));
		postData.put("playerName", playerName);
		JSONObject response = handler.retrieveJSONObject(postData);
		try {
			return response.getBoolean("result");
		} catch (JSONException e) {
			throw new DataRetrievalException("Could not check if player was permabanned globally!", e);
		}
	}
}
