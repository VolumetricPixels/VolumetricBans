package com.volumetricpixels.bans.storage;

import com.volumetricpixels.bans.VolumetricBans;

import java.io.File;

/**
 * Manages all files in VolumetricBans.
 */
public class FileManager {
	/**
	 * Plugin instance
	 */
	private final VolumetricBans plugin;

	// Main files (config, data files, etc)
	private File config;
	private File banStorage;
	private File muteStorage;

	/**
	 * FileManager constructor
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public FileManager(VolumetricBans plugin) {
		this.plugin = plugin;
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
	 * @return The file inside the VB data folder with given name
	 */
	public File getFile(String name) {
		return new File(plugin.getDataFolder(), name);
	}
}
