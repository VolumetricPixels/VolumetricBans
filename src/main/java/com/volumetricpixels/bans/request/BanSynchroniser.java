package com.volumetricpixels.bans.request;

import gnu.trove.map.hash.THashMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lib.org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.storage.file.JSONFileHandler;

/**
 * Synchronises bans with the server every ~10 minutes (differs slightly because
 * of time taken to synchronise)
 */
public final class BanSynchroniser implements Runnable {
    /** 10 minutes (1000 millis / sec, 60 secs / min, 10 mins) */
    private static final int SLEEP_MILLIS = 1000 * 60 * 10;

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
    public BanSynchroniser(final VolumetricBans plugin) {
        this.plugin = plugin;
        arh = plugin.getRequestHandler();
    }

    /**
     * Runs the ban synchronisation
     */
    @Override
    public void run() {
        while (true) {
            if (lastList == null) {
                lastList = new ArrayList<Ban>();
                final File lastListFile = plugin.getFileManager().getBanSyncFile();
                if (!lastListFile.exists()) {
                    try {
                        lastListFile.createNewFile();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    final JSONFileHandler j = new JSONFileHandler(lastListFile);
                    JSONObject curJObj = null;
                    try {
                        while ((curJObj = j.read()) != null) {
                            lastList.add(Ban.fromJSONObject(plugin, curJObj));
                        }
                    } catch (final StorageException e) {
                        e.printStackTrace();
                    } catch (final DataLoadException e) {
                        e.printStackTrace();
                    }
                }
            }

            final List<Ban> list = plugin.getStorageHandler().getBans();
            for (final Ban ban : list) {
                boolean contains = false;
                for (final Ban b : lastList) {
                    if (!list.contains(b)) {
                        sendBan(ban, false);
                    }
                    if (ban.equals(b)) {
                        contains = true;
                    }
                }
                if (!contains) {
                    sendBan(ban, true);
                }
            }

            try {
                Thread.sleep(SLEEP_MILLIS);
            } catch (final InterruptedException e) {
                break;
            }
        }
    }

    private void sendBan(final Ban ban, final boolean add) {
        try {
            final Map<String, String> post = new THashMap<String, String>();
            post.put("action", "ban");
            post.put("ban", ban.toJSONObject().toString());
            post.put("add", Boolean.toString(add));
            arh.submitRequest(post);
        } catch (final DataRetrievalException e) {
            e.printStackTrace();
        }
    }
}
