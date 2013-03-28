package com.volumetricpixels.bans.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataRetrievalException;

/**
 * Handles JSON sent by the VolumetricBans servers
 */
public class APIRequestHandler {
	private final VolumetricBans plugin;
	private final String apiKey;
	private final StringBuilder b = new StringBuilder();

	private String apiServerHostName;
	private String actionCategory;

	public APIRequestHandler(VolumetricBans plugin, String actionCategory) {
		this.plugin = plugin;
		apiKey = plugin.getAPIKey();
		this.actionCategory = actionCategory;
	}

	public JSONObject retrieveJSONObject(Map<String, String> postData) throws DataRetrievalException {
		postData.put("key", apiKey);
		String urlReq = parsePostItems(postData);
		String jText = performAPIRequest(urlReq);
		return getJSONObject(jText);
	}

	public JSONObject getJSONObject(String jsonText) throws DataRetrievalException {
		try {
			return new JSONObject(jsonText);
		} catch (JSONException e) {
			throw new DataRetrievalException("Invalid data received!", e);
		}
	}

	public String performAPIRequest(String data) throws DataRetrievalException {
		try {
			URL u = new URL(apiServerHostName + "/api/" + actionCategory);
			URLConnection uc = u.openConnection();
			uc.setConnectTimeout(5500);
			uc.setReadTimeout(5500);
			uc.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(uc.getOutputStream());
			osw.write(data);
			osw.flush();
			BufferedReader r = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				b.append(line);
			}
			String result = b.toString();
			// Separate try-catch so close failing doesn't disrupt request
			try {
				osw.close();
				r.close();
			} catch (Exception ignore) {
			}
			b.setLength(0); // So we can keep reusing the same builder
			return result;
		} catch (Exception e) {
			throw new DataRetrievalException("Error connecting to server!", e);
		}
	}

	public String parsePostItems(Map<String, String> postData) throws DataRetrievalException {
		String data = null;
		try {
			for (Entry<String, String> entry : postData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (data == null || data.equals("")) {
					data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
				} else {
					data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
				}
			}
			return data;
		} catch (UnsupportedEncodingException e) {
			throw new DataRetrievalException("System does not support UTF-8!", e);
		}
	}

	public VolumetricBans getPlugin() {
		return plugin;
	}
}
