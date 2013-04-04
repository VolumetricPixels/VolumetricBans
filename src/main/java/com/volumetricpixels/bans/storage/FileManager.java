package com.volumetricpixels.bans.storage;

import java.io.File;

import com.volumetricpixels.bans.VolumetricBans;

/** Manages all files in VolumetricBans. */
public final class FileManager {
    /** Plugin instance */
    private final VolumetricBans plugin;
    // Main files (config, data files, etc)
    private final File config;
    private final File banStorage;
    private final File muteStorage;

    /**
     * FileManager constructor
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    public FileManager(VolumetricBans plugin) {
        this.plugin = plugin;

        config = getFile("config.yml");
        banStorage = getFile("data" + File.separator + "bans.json");
        muteStorage = getFile("data" + File.separator + "mutes.json");
    }

    /**
     * Gets the VolumetricBans configuration File
     * 
     * @return The VolumetricBans configuration File
     */
    public File getConfigFile() {
        return config;
    }

    /**
     * Gets the VolumetricBans ban storage File
     * 
     * @return The VolumetricBans ban storage File
     */
    public File getBanStorageFile() {
        return banStorage;
    }

    /**
     * Gets the VolumetricBans mute storage File
     * 
     * @return The VolumetricBans mute storage File
     */
    public File getMuteStorageFile() {
        return muteStorage;
    }

    /**
     * Gets a file of given name within the VolumetricBans data folder
     * 
     * @param name
     *            Name of the file to get
     * 
     * @return The file inside the VB data folder with given name
     */
    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name);
    }
}
