package com.volumetricpixels.bans.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.exception.DataRetrievalException;

public class APIRequestUtil {
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

	public static boolean isPermaGlobalBanned(APIRequestHandler handler, String playerName) throws DataRetrievalException {
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("action", "checkPermanentGlobalBan");
		postData.put("playerName", playerName);
		JSONObject response = handler.retrieveJSONObject(postData);
		try {
			return response.getBoolean("result");
		} catch (JSONException e) {
			throw new DataRetrievalException("Could not check if player was permabanned globally!", e);
		}
	}
}
