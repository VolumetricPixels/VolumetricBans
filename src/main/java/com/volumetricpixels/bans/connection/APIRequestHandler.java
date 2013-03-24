package com.volumetricpixels.bans.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Handles JSON sent by the VolumetricBans servers
 */
public class APIRequestHandler {
	private final VolumetricBans plugin;
	private final String apiKey;

	private String apiServerHostName;
	private String actionCategory;

	public APIRequestHandler(VolumetricBans plugin, String actionCategory) {
		this.plugin = plugin;
		apiKey = plugin.getAPIKey();
		this.actionCategory = actionCategory;
	}

	public JSONObject retrieveJSONObject(Map<String, String> postData) {
		postData.put("key", apiKey);
		String urlReq = parsePostItems(postData);
		String jText = performAPIRequest(urlReq);
		return getJSONObject(jText);
	}

	public JSONObject getJSONObject(String jsonText) {
		try {
			return new JSONObject(jsonText);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String performAPIRequest(String data) {
		return performAPIRequest(data, apiServerHostName);
	}

	public String performAPIRequest(String data, String apiServer) {
		try {
			URL u = new URL(apiServer + "/api/" + actionCategory);
			URLConnection uc = u.openConnection();
			uc.setConnectTimeout(5500);
			uc.setReadTimeout(5500);
			uc.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(uc.getOutputStream());
			osw.write(data);
			osw.flush();
			StringBuilder b = new StringBuilder();
			BufferedReader r = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				b.append(line);
			}
			String result = b.toString();
			// Separate try block so close failures do not disrupt whole API
			// request
			try {
				osw.close();
				r.close();
			} catch (Exception ignore) {
			}
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	public String parsePostItems(Map<String, String> postData) {
		String data = null;
		try {
			for (Entry<String, String> entry : postData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (data.equals("")) {
					data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
				} else {
					data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
				}
			}
			return data;
		} catch (UnsupportedEncodingException ignore) {
		}
		return "";
	}

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
}
