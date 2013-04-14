package com.volumetricpixels.bans.punishment;

import java.util.ArrayList;
import java.util.List;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.storage.PunishmentStorage;

/**
 * Manages punishments in VolumetricBans
 */
public final class PunishmentManager {
    /** The PunishmentStorage instance */
    private final PunishmentStorage store;

    /**
     * Creates a new PunishmentManager
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    public PunishmentManager(final VolumetricBans plugin) {
        store = plugin.getStorageHandler();
    }

    /**
     * Adds a Ban
     * 
     * @param ban
     *            The Ban object to add
     * 
     * @return Whether we added the ban successfully
     */
    public boolean addBan(final Ban ban) {
        return store.getBans().add(ban);
    }

    /**
     * Removes a Ban
     * 
     * @param ban
     *            The Ban object to remove
     * 
     * @return Whether we removed the ban successfully
     */
    public boolean removeBan(final Ban ban) {
        return store.getBans().remove(ban);
    }

    /**
     * Adds a Mute
     * 
     * @param mute
     *            The Mute object to add
     * 
     * @return Whether we added the mute successfully
     */
    public boolean addMute(final Mute mute) {
        return store.getMutes().add(mute);
    }

    /**
     * Removes a Mute
     * 
     * @param mute
     *            The Mute object to remove
     * 
     * @return Whether we removed the mute successfully
     */
    public boolean removeMute(final Mute mute) {
        return store.getMutes().remove(mute);
    }

    /**
     * Gets a Ban[] of all bans on the given player
     * 
     * @param player
     *            The player to get bans for
     * 
     * @return A Ban[] of bans the given player has received
     */
    public Ban[] getBansForPlayer(final String player) {
        final List<Ban> bans = new ArrayList<Ban>();
        for (final Ban ban : store.getBans()) {
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
    public void removeBansOnPlayer(final String player) {
        for (final Ban ban : store.getBans()) {
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
    public void removeMutesOnPlayer(final String player) {
        for (final Mute mute : store.getMutes()) {
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
     * 
     * @return Whether the given player is banned
     */
    public boolean isBanned(final String player) {
        for (final Ban ban : store.getBans()) {
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
     * 
     * @return Whether the given player is muted
     */
    public boolean isMuted(final String player) {
        for (final Mute mute : store.getMutes()) {
            if (mute.getPlayerName().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
}
