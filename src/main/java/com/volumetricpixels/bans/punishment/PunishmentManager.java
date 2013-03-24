package com.volumetricpixels.bans.punishment;

import java.util.ArrayList;
import java.util.List;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.storage.PunishmentStorage;

public class PunishmentManager {
	private final VolumetricBans plugin;
	private final PunishmentStorage store;

	public PunishmentManager(VolumetricBans plugin) {
		this.plugin = plugin;
		store = plugin.getStorageHandler();
	}

	public boolean addBan(Ban ban) {
		return store.getBans().add(ban);
	}

	public boolean removeBan(Ban ban) {
		return store.getBans().remove(ban);
	}

	public boolean addMute(Mute mute) {
		return store.getMutes().add(mute);
	}

	public boolean removeMute(Mute mute) {
		return store.getMutes().remove(mute);
	}

	public Ban[] getBansForPlayer(String player) {
		List<Ban> bans = new ArrayList<Ban>();
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equals(player)) {
				bans.add(ban);
			}
		}
		return bans.toArray(new Ban[0]);
	}

	public void removeBansOnPlayer(String player) {
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equalsIgnoreCase(player)) {
				store.getBans().remove(ban);
			}
		}
	}

	public void removeMutesOnPlayer(String player) {
		for (Mute mute : store.getMutes()) {
			if (mute.getPlayerName().equalsIgnoreCase(player)) {
				store.getMutes().remove(mute);
			}
		}
	}

	public boolean isBanned(String player) {
		for (Ban ban : store.getBans()) {
			if (ban.getPlayerName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

	public boolean isMuted(String player) {
		for (Mute mute : store.getMutes()) {
			if (mute.getPlayerName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}
}
