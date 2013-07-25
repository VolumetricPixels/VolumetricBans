package com.volumetricpixels.bans.storage;

/**
 * Implemented by anything that can be removed, used for timers
 */
public interface Deletable {
	/**
	 * Delete this Deletable
	 */
	void delete();
}
