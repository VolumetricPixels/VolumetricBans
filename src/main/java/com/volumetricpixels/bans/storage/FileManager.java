package com.volumetricpixels.bans.storage;

import java.io.File;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Manages all files in VolumetricBans.
 */
public class FileManager {
	/** Plugin instance */
	private final VolumetricBans plugin;

	// Main files (config, data files, etc)
	private File config;
	private File banStorage;
	private File muteStorage;

	/**
	 * VBFileSystem constructor
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public FileManager(VolumetricBans plugin) {
		this.plugin = plugin;
	}

	public File getConfigFile() {
		return config;
	}

	public File getBanStorageFile() {
		return banStorage;
	}

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
