package com.volumetricpixels.bans.storage;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.exception.StorageException;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.punishment.Mute;
import com.volumetricpixels.bans.util.PArrayList;

/**
 * Handles storage of punishments (bans and mutes) in files by making use of
 * JSONObjects
 */
public class PunishmentStorage {
    /** VolumetricBans plugin */
    private final VolumetricBans plugin;
    /** The File we are storing bans in */
    private final File banStorageFile;
    /** The JSONFileHandler for the bans file */
    private final JSONFileHandler bjsh;
    /** The File we are storing mutes in */
    private final File muteStorageFile;
    /** The JSONFileHandler for the mutes file */
    private final JSONFileHandler mjsh;

    /** PArrayList of stored bans */
    private PArrayList<Ban> bans;
    /** PArrayList of stored mutes */
    private PArrayList<Mute> mutes;

    /**
     * Creates a new instance of PunishmentStorage with the given plugin
     * instance
     * 
     * @param plugin
     *            The VolumetricBans plugin instance
     */
    public PunishmentStorage(VolumetricBans plugin) {
        this.plugin = plugin;

        banStorageFile = plugin.getFileManager().getBanStorageFile();
        bjsh = new JSONFileHandler(banStorageFile);
        muteStorageFile = plugin.getFileManager().getMuteStorageFile();
        mjsh = new JSONFileHandler(muteStorageFile);
        bans = new PArrayList<Ban>(plugin);
        mutes = new PArrayList<Mute>(plugin);
    }

    /**
     * Loads the bans from the ban storage file
     * 
     * @throws StorageException
     *             When there are problems with reading or parsing the file
     * @throws DataLoadException
     *             When there is a problem creating a ban from a JSONObject
     */
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

    /**
     * Loads the mutes from the mute storage file
     * 
     * @throws StorageException
     *             When there are problems with reading or parsing the file
     * @throws DataLoadException
     *             When there is a problem creating a mute from a JSONObject
     */
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

    /**
     * Saves the bans to the ban storage file
     * 
     * @throws StorageException
     *             When there are issues writing to the file
     */
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

    /**
     * Saves the mutes to the mute storage file
     * 
     * @throws StorageException
     *             When there are issues writing to the file
     */
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
            throw new StorageException("Error writing mutes to file! A backup of your old mutes should be found in /plugins/VolumetricBans/data/!", e);
        }
    }

    /**
     * Gets the ban List
     * 
     * @return The List of stored bans
     */
    public List<Ban> getBans() {
        return bans;
    }

    /**
     * Gets the mute List
     * 
     * @return The List of stored mutes
     */
    public List<Mute> getMutes() {
        return mutes;
    }
}
