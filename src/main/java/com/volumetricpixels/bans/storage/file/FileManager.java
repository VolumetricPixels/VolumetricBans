package com.volumetricpixels.bans.storage.file;

import java.io.File;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Manages all files in VolumetricBans for easy access throughout the plugin
 */
public final class FileManager {
	/** Plugin instance */
	private final VolumetricBans plugin;
	// Main files (config, data files, etc)
	private final File config;
	private final File banStorage;
	private final File muteStorage;
	private final File banSync;

	/**
	 * FileManager constructor
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public FileManager(final VolumetricBans plugin) {
		this.plugin = plugin;

		config = getFile("config.yml");
		banStorage = getFile("data" + File.separator + "bans.json");
		muteStorage = getFile("data" + File.separator + "mutes.json");
		banSync = getFile("data" + File.separator + "bansync.json");
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
	 * Gets the VolumetricBans ban sync File
	 * 
	 * @return The VolumetricBans ban sync File
	 */
	public File getBanSyncFile() {
		return banSync;
	}

	/**
	 * Gets a file of given name within the VolumetricBans data folder
	 * 
	 * @param name
	 *            Name of the file to get
	 * 
	 * @return The file inside the VB data folder with given name
	 */
	public File getFile(final String name) {
		return new File(plugin.getDataFolder(), name);
	}
}
