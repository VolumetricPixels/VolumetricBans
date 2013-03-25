package com.volumetricpixels.bans.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.punishment.Mute;
import com.volumetricpixels.bans.util.PArrayList;

public class PunishmentStorage {
	private final VolumetricBans plugin;
	private final File banStorageFile;
	private final JSONFileHandler bjsh;
	private final File muteStorageFile;
	private final JSONFileHandler mjsh;

	private PArrayList<Ban> bans;
	private PArrayList<Mute> mutes;

	public PunishmentStorage(VolumetricBans plugin) {
		this.plugin = plugin;

		banStorageFile = plugin.getFileSystem().getBanStorageFile();
		bjsh = new JSONFileHandler(banStorageFile);
		muteStorageFile = plugin.getFileSystem().getMuteStorageFile();
		mjsh = new JSONFileHandler(muteStorageFile);
		bans = new PArrayList<Ban>(plugin);
		mutes = new PArrayList<Mute>(plugin);
	}

	public void loadBans() throws StorageException, DataLoadException {
		JSONObject curJSONObject = null;
		bans.setCallEvents(false);
		bjsh.startReading();
		while ((curJSONObject = bjsh.read()) != null) {
			bans.add(Ban.fromJSONObject(plugin, curJSONObject));
		}
		bjsh.stopReading();
		bans.setCallEvents(true);
	}

	public void loadMutes() throws StorageException, DataLoadException {
		JSONObject curJSONObject = null;
		mutes.setCallEvents(false);
		mjsh.startReading();
		while ((curJSONObject = mjsh.read()) != null) {
			mutes.add(Mute.fromJSONObject(plugin, curJSONObject));
		}
		mjsh.stopReading();
		mutes.setCallEvents(true);
	}

	public void saveBans() throws StorageException {
		bjsh.backup();
		bjsh.delete();
		try {
			bjsh.startWriting();
			for (Ban ban : bans) {
				bjsh.write(ban.toJSONObject());
			}
			bjsh.stopWriting();
		} catch (StorageException e) {
			throw new StorageException("Error writing bans to file! A backup of your old bans should be found in /plugins/VolumetricBans/data/!", e);
		}
	}

	public void saveMutes() throws StorageException {
		mjsh.backup();
		mjsh.delete();
		try {
			mjsh.startWriting();
			for (Mute mute : mutes) {
				mjsh.write(mute.toJSONObject());
			}
			mjsh.stopWriting();
		} catch (StorageException e) {
			throw new StorageException("Error writing bans to file! A backup of your old bans should be found in /plugins/VolumetricBans/data/!", e);
		}
	}

	public List<Ban> getBans() {
		return bans;
	}

	public List<Mute> getMutes() {
		return mutes;
	}
}
