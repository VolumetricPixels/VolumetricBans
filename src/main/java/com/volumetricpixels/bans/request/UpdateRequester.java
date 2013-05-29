package com.volumetricpixels.bans.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import org.spout.api.Server;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataRetrievalException;

/**
 * Sends small messages to the API servers to inform them that the server is
 * online, along with some data. Also requests a response
 */
public final class UpdateRequester implements Runnable {
    /** 5 minutes (1000 millis / sec, 60 secs / min, 5 mins) */
    private static final int SLEEP_MILLIS = 1000 * 60 * 5;

    private final VolumetricBans plugin;
    private final APIRequestHandler arh;

    public UpdateRequester(final VolumetricBans plugin) {
        this.plugin = plugin;
        arh = plugin.getRequestHandler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        while (true) {
            final Map<String, String> postData = new HashMap<String, String>();
            postData.put("action", "update");
            postData.put("amtOnline", "" + ((Server) plugin.getEngine()).getOnlinePlayers().length);

            try {
                final JSONObject jo = arh.submitRequest(postData);
                final Iterator<String> it = jo.keys();
                while (it.hasNext()) {
                    final String key = it.next();
                    try {
                        parseResponse(key, jo.get(key));
                    } catch (final JSONException e) {
                        plugin.getLogger().log(Level.SEVERE, "Error parsing data from update!", e);
                    }
                }
            } catch (final DataRetrievalException e) {
                plugin.getLogger().log(Level.SEVERE, "Error handling data updates!", e);
            }

            try {
                Thread.sleep(SLEEP_MILLIS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseResponse(final String key, final Object response) {
        // TODO: Parse data
    }
}
