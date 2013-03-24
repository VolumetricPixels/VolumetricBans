package com.volumetricpixels.bans.connection;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Ban;

public class BanSynchroniser implements Runnable {
	private VolumetricBans plugin;
	private APIRequestHandler arh;

	public BanSynchroniser(VolumetricBans plugin) {
		this.plugin = plugin;

		arh = new APIRequestHandler(plugin, "bans");
	}

	@Override
	public void run() {
		// TODO: This will probably be changed, IDK how efficient sending a
		// JSONArray via a post request is...
		JSONArray array = new JSONArray();
		for (Ban ban : plugin.getStorageHandler().getBans()) {
			array.put(APIRequestHandler.getMap(ban.toJSONObject()));
		}
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("action", "updateBans");
		postData.put("jsonarray", array.toString());
		JSONObject jo = arh.retrieveJSONObject(postData);
		try {
			if ((jo == null) || !jo.getBoolean("result")) {
				plugin.getLogger().warning("Failed to synchronise bans with VolumetricBans servers!");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
