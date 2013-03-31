package com.volumetricpixels.bans.punishment;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.storage.PunishmentStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages punishments in VolumetricBans
 */
public class PunishmentManager {
	/**
	 * The VolumetricBans plugin
	 */
	private final VolumetricBans plugin;
	/**
	 * The PunishmentStorage instance
	 */
	private final PunishmentStorage store;

	/**
	 * Creates a new PunishmentManager
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public PunishmentManager(VolumetricBans plugin) {
		this.plugin = plugin;
		store = plugin.getStorageHandler();
	}

	/**
	 * Adds a Ban
	 * 
	 * @param ban
	 *            The Ban object to add
	 * @return Whether we added the ban successfully
	 */
	public boolean addBan(Ban ban) {
		return store.getBans().add(ban);
	}

	/**
	 * Removes a Ban
	 * 
	 * @param ban
	 *            The Ban object to remove
	 * @return Whether we removed the ban successfully
	 */
	public boolean removeBan(Ban ban) {
		return store.getBans().remove(ban);
	}

	/**
	 * Adds a Mute
	 * 
	 * @param mute
	 *            The Mute object to add
	 * @return Whether we added the mute successfully
	 */
	public boolean addMute(Mute mute) {
		return store.getMutes().add(mute);
	}

	/**
	 * Removes a Mute
	 * 
	 * @param mute
	 *            The Mute object to remove
	 * @return Whether we removed the mute successfully
	 */
	public boolean removeMute(Mute mute) {
		return store.getMutes().remove(mute);
	}

	/**
	 * Gets a Ban[] of all bans on the given player
	 * 
	 * @param player
	 *            The player to get bans for
	 * @return A Ban[] of bans the given player has received
	 */
	public Ban[] getBansForPlayer(String player) {
		List<Ban> bans = new ArrayList<Ban>();
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equals(player)) {
				bans.add(ban);
			}
		}
		return bans.toArray(new Ban[0]);
	}

	/**
	 * Removes all bans on the given player
	 * 
	 * @param player
	 *            The player to remove bans on
	 */
	public void removeBansOnPlayer(String player) {
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equalsIgnoreCase(player)) {
				store.getBans().remove(ban);
			}
		}
	}

	/**
	 * Removes all mutes on the given player
	 * 
	 * @param player
	 *            The player to remove mutes on
	 */
	public void removeMutesOnPlayer(String player) {
		for (Mute mute : store.getMutes()) {
			if (mute.getPlayerName().equalsIgnoreCase(player)) {
				store.getMutes().remove(mute);
			}
		}
	}

	/**
	 * Checks if the given player is banned
	 * 
	 * @param player
	 *            The player to check ban status for
	 * @return Whether the given player is banned
	 */
	public boolean isBanned(String player) {
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given player is muted
	 * 
	 * @param player
	 *            The player to check mute status for
	 * @return Whether the given player is muted
	 */
	public boolean isMuted(String player) {
		for (Mute mute : store.getMutes()) {
			if (mute.getPlayerName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}
}
