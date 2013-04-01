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

/** Handles JSON sent by the VolumetricBans servers */
public class APIRequestHandler {
	/** The VolumetricBans plugin */
	private final VolumetricBans plugin;
	/** The server API key */
	private final String apiKey;

	/** The address for the API server */
	private String apiServerHostName;
	/** The category of action this APIRequestHandler handles */
	private String actionCategory;

	/**
	 * Creates a new APIRequestHandler
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 * @param actionCategory
	 *            This APIRequestHandler's category
	 */
	public APIRequestHandler(VolumetricBans plugin, String actionCategory) {
		this.plugin = plugin;
		apiKey = plugin.getAPIKey();
		this.actionCategory = actionCategory;
	}

	/**
	 * Retrieves JSON data from the server
	 * 
	 * @param postData
	 *            The post data to submit
	 * 
	 * @return A JSONObject parsed from data the server sent
	 * 
	 * @throws DataRetrievalException
	 *             When we fail to retrieve data
	 */
	public JSONObject retrieveJSONObject(Map<String, String> postData) throws DataRetrievalException {
		postData.put("key", apiKey);
		String urlReq = parsePostItems(postData);
		String jText = performAPIRequest(urlReq);
		return getJSONObject(jText);
	}

	/**
	 * Creates a JSONObject from given JSON text
	 * 
	 * @param jsonText
	 *            A String of JSON data
	 * 
	 * @return A JSONObject created from the jsonText
	 * 
	 * @throws DataRetrievalException
	 *             When a JSONObject can't be created
	 */
	public JSONObject getJSONObject(String jsonText) throws DataRetrievalException {
		try {
			return new JSONObject(jsonText);
		} catch (JSONException e) {
			throw new DataRetrievalException("Invalid data received!", e);
		}
	}

	/**
	 * Performs an API request
	 * 
	 * @param data
	 *            Post data to submit
	 * 
	 * @return The returned data from the server
	 * 
	 * @throws DataRetrievalException
	 *             When we fail to retrieve data
	 */
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
			StringBuilder b = new StringBuilder();
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
			return result;
		} catch (Exception e) {
			throw new DataRetrievalException("Error connecting to server!", e);
		}
	}

	/**
	 * Parses a Map of post items into a UTF-8 string
	 * 
	 * @param postData
	 *            A Map of data to parse
	 * 
	 * @return A UTF-8 string of post data
	 * 
	 * @throws DataRetrievalException
	 *             When the system doesn't support UTF-8
	 */
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

	/**
	 * Gets the VolumetricBans plugin
	 * 
	 * @return The VolumetricBans plugin
	 */
	public VolumetricBans getPlugin() {
		return plugin;
	}
}
