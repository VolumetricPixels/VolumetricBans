package com.volumetricpixels.bans.connection;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.util.APIRequestUtil;

public class BanSynchroniser implements Runnable {
	// 10 minutes (1000 millis / sec, 60 secs / min, 10 mins)
	private static final int SLEEP_MILLIS = ((1000 * 60) * 10);

	private VolumetricBans plugin;
	private APIRequestHandler arh;

	public BanSynchroniser(VolumetricBans plugin) {
		this.plugin = plugin;

		arh = new APIRequestHandler(plugin, "bans");
	}

	@Override
	public void run() {
		// TODO: This will probably be changed, seeing as we are currently
		// sending a banlist via post args. Probably not very efficient or fast,
		// although depending on how website is done it might be fine. Anyhow
		// this is just a temporary thing until we get everything sorted
		while (true) {
			JSONArray array = new JSONArray();
			for (Ban ban : plugin.getStorageHandler().getBans()) {
				array.put(APIRequestUtil.getMap(ban.toJSONObject()));
			}

			Map<String, String> postData = new HashMap<String, String>();
			postData.put("action", "updateBans");
			postData.put("jsonarray", array.toString());
			JSONObject jo = null;
			try {
				jo = arh.retrieveJSONObject(postData);
			} catch (DataRetrievalException e) {
				plugin.setToOfflineMode(e);
				return;
			}

			try {
				if ((jo == null) || !jo.getBoolean("result")) {
					plugin.getLogger().warning("Failed to synchronise bans with VolumetricBans servers!");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(SLEEP_MILLIS);
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
}
