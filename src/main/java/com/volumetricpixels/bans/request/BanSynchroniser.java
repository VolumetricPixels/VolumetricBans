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
 * Synchronises global bans with the server every ~10 minutes (differs slightly
 * because of time taken to synchronise)
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
	 * Runs the ban synchronisation task in a loop, with 10 minutes between
	 * loops
	 */
	@Override
	public void run() {
		final File lastListFile = plugin.getFileManager().getBanSyncFile();
		final JSONFileHandler j = new JSONFileHandler(lastListFile);
		while (true) {
			if (lastList == null) {
				lastList = new ArrayList<Ban>();
				if (!lastListFile.exists()) {
					try {
						lastListFile.createNewFile();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				} else {
					JSONObject curJObj = null;
					try {
						j.startReading();
						while ((curJObj = j.read()) != null) {
							lastList.add(Ban.fromJSONObject(plugin, curJObj));
						}
						j.stopReading();
					} catch (final StorageException e) {
						e.printStackTrace();
					} catch (final DataLoadException e) {
						e.printStackTrace();
					}
				}
			}

			final List<Ban> list = plugin.getStorageHandler().getBans();
			for (final Ban ban : list) {
				if (!ban.isGlobal()) {
					continue;
				}

				boolean contains = false;
				a: for (final Ban b : lastList) {
					if (!b.isGlobal()) {
						continue a;
					}

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
				j.startWriting();
				for (final Ban ban : list) {
					j.write(ban.toJSONObject());
				}
				j.stopWriting();
			} catch (final StorageException e) {
				e.printStackTrace();
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
			if (add) {
				post.put("action", "ban");
				post.put("ban", ban.toJSONObject().toString());
			} else {
				post.put("action", "unban");
				post.put("player", ban.getPlayerName());
			}
			arh.submitRequest(post);
		} catch (final DataRetrievalException e) {
			e.printStackTrace();
		}
	}
}
