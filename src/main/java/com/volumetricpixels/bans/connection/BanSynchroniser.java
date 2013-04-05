package com.volumetricpixels.bans.connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.storage.JSONFileHandler;

/** Synchronises bans with the server every ~10 minutes */
public final class BanSynchroniser implements Runnable {
    /** 10 minutes (1000 millis / sec, 60 secs / min, 10 mins) */
    private static final int SLEEP_MILLIS = ((1000 * 60) * 10);

    /** The VolumetricBans plugin */
    private final VolumetricBans plugin;
    /** The APIRequestHandler we are using */
    private final APIRequestHandler arh;

    /** Last banlist on sync */
    private List<Ban> lastList;

    /**
     * Creates a new BanSynchroniser
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    public BanSynchroniser(VolumetricBans plugin) {
        this.plugin = plugin;
        arh = new APIRequestHandler(plugin, "bans");
    }

    /** Runs the ban synchronisation */
    @Override
    public void run() {
        while (true) {
            hi: if (lastList == null) {
                lastList = new ArrayList<Ban>();
                File lastListFile = plugin.getFileManager().getBanSyncFile();
                if (!lastListFile.exists()) {
                    try {
                        lastListFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break hi;
                }
                JSONFileHandler j = new JSONFileHandler(lastListFile);
                JSONObject curJObj = null;
                try {
                    while ((curJObj = j.read()) != null) {
                        lastList.add(Ban.fromJSONObject(plugin, curJObj));
                    }
                } catch (StorageException e) {
                } catch (DataLoadException e) {
                    e.printStackTrace();
                }
            }
            List<Ban> list = plugin.getStorageHandler().getBans();
            for (Ban ban : list) {
                boolean found1 = false;
                for (Ban ban1 : lastList) {
                    if (ban.getPlayerName().equalsIgnoreCase(ban1.getPlayerName())) {
                        found1 = true;
                    }
                }
                if (!found1) {
                    sendBan(ban, true);
                }
            }
            for (Ban ban : lastList) {
                boolean found = false;
                for (Ban ban1 : list) {
                    if (ban.getPlayerName().equalsIgnoreCase(ban1.getPlayerName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    sendBan(ban, false);
                }
            }
            try {
                Thread.sleep(SLEEP_MILLIS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void sendBan(Ban ban, boolean add) {
        try {
            Map<String, String> post = new HashMap<String, String>();
            post.put("action", "ban");
            post.put("ban", ban.toJSONObject().toString());
            post.put("add", Boolean.toString(add));
            arh.submitRequest(post);
        } catch (DataRetrievalException e) {
            e.printStackTrace();
        }
    }
}
