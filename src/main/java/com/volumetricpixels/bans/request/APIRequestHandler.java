package com.volumetricpixels.bans.request;

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

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataRetrievalException;

/**
 * Submits requests to the VolumetricBans API servers, and parses the returned
 * JSON data
 */
public final class APIRequestHandler {
    /** The VolumetricBans plugin */
    private final VolumetricBans plugin;
    /** The server API key */
    private final String apiKey;
    /** Utility Map for requests */
    private final Map<String, String> postMap = new HashMap<String, String>();
    /** The address for the API server */
    private final String apiServerHostName = "TODO";

    /**
     * Creates a new APIRequestHandler
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    public APIRequestHandler(final VolumetricBans plugin) {
        this.plugin = plugin;
        apiKey = plugin.getAPIKey();
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
    public JSONObject submitRequest(final Map<String, String> postData) throws DataRetrievalException {
        String urlReq = null;
        String json = null;
        String action = postData.remove("action");
        synchronized (this) {
            postMap.putAll(postData);
            postMap.put("key", apiKey);
            urlReq = parsePostItems(postMap);
            postMap.clear();

            if (!plugin.isPremium()) {
                json = doPerformRequest(action + "/" + urlReq);
            }
        }

        if (plugin.isPremium()) {
            json = doPerformRequest(urlReq);
        }

        try {
            return new JSONObject(json);
        } catch (final JSONException e) {
            throw new DataRetrievalException("Invalid data retrieved!", e);
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
    private String doPerformRequest(final String data) throws DataRetrievalException {
        try {
            final String action = data.split("/")[0];
            final URL url = new URL(apiServerHostName + "/query/" + action);
            final URLConnection urlConn = url.openConnection();
            urlConn.setConnectTimeout(5500);
            urlConn.setReadTimeout(5500);
            urlConn.setDoOutput(true);

            final OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
            out.write(data);
            out.flush();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            final StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            final String result = builder.toString();
            try {
                out.close();
                reader.close();
            } catch (final Exception ignore) {
            }
            return result;
        } catch (final Exception e) {
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
    private String parsePostItems(final Map<String, String> postData) throws DataRetrievalException {
        String data = null;
        try {
            for (final Entry<String, String> entry : postData.entrySet()) {
                final String key = entry.getKey();
                final String value = entry.getValue();
                if (data == null || data.equals("")) {
                    data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                } else {
                    data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                }
            }
            return data;
        } catch (final UnsupportedEncodingException e) {
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
