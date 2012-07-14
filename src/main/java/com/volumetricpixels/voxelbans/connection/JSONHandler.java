package com.volumetricpixels.voxelbans.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.voxelbans.VoxelBans;

/**
 * Handles JSON sent by the VoxelBans servers (done by Kwiebert)
 * @author DziNeIT
 */
public class JSONHandler {
    
    private final VoxelBans plugin;
    private final String apiKey;
    private String apiServerHostName;
    private String actionCategory;
    
    public JSONHandler(VoxelBans plugin2, String actionCategory) {
        this.plugin = plugin2;
        this.apiKey = plugin.getServerKey();
        this.actionCategory = actionCategory;
    }
    
    public JSONObject getData(String jsonText) {
        try {
            return new JSONObject(jsonText);
        } catch (JSONException e) {}
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> mainReq(Map<String, String> items) {
        Map<String,String> out = new HashMap<String, String>();
        String reqUrl = parseUrl(items);
        String jsonText = apiRequest(reqUrl);
        JSONObject output = getData(jsonText);
        if (output != null) {
            Iterator<String> i = output.keys();
            if (i != null) {
                while (i.hasNext()) {
                    String next = i.next();
                    try {
                        out.put(next, output.getString(next));
                    } catch (JSONException ignore) {}
                }
            }
        }
        return out;
    }
    
    public JSONObject handleJsonObject(Map<String, String> items) {
        String urlReq = parseUrl(items);
        String jText = apiRequest(urlReq);
        return getData(jText);
    }
    
    public String apiRequest(String data) {
        return apiRequest(data, apiServerHostName);
    }
    
    public String apiRequest(String data, String apiServer) {
        try {
            URL u = new URL(apiServer + "/api/" + actionCategory + "/" + apiKey);
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
            // Separate try block so close failures do not disrupt whole API Request
            try {
                osw.close();
                r.close();
            } catch (Exception ignore) {}
            return result;
        } catch (Exception e) {}
        return null;
    }
    
    public String parseUrl(Map<String, String> items) {
        String data = null;
        try {
            for (Entry<String, String> entry : items.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if (data.equals("")) {
                    data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                } else {
                    data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                }
            }
            return data;
        } catch (UnsupportedEncodingException ignore) {}
        return "";
    }
    
}
