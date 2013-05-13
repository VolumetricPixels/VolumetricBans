package com.volumetricpixels.bans.punishment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import org.spout.api.entity.Player;
import org.spout.api.scheduler.TaskPriority;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.storage.Deletable;
import com.volumetricpixels.bans.storage.DeletableTimer;

/**
 * Represents a Mute
 */
public final class Mute implements Deletable {
    /** The Calendar instance */
    private static final Calendar c = Calendar.getInstance();

    /** VolumetricBans plugin, for timers and shit */
    private final VolumetricBans plugin;

    /** Reason for the ban */
    private String reason;
    /** Issuer of the ban */
    private final String admin;
    /** Banned player */
    private final String player;
    /** How long the ban lasts */
    private long time;
    /** When the ban was issued */
    private final long issued;

    /**
     * Mute constructor
     * 
     * @param plugin
     *            The VolumetricMutes plugin
     * @param player
     *            The muted player
     * @param reason
     *            The mute reason
     * @param admin
     *            The admin who issued the mute
     * @param timeMins
     *            How long the mute should last in minutes
     */
    public Mute(final VolumetricBans plugin, final String player, final String reason, final String admin, final long timeMins) {
        this.plugin = plugin;
        this.player = player;
        this.reason = reason;
        this.admin = admin;
        time = timeMins;
        issued = c.getTimeInMillis() / 1000 / 60;
        initTimer();
    }

    /**
     * Used internally when creating a Mute from a JSONObject
     * 
     * @param plugin
     *            Plugin
     * @param player
     *            Player
     * @param reason
     *            Reason
     * @param admin
     *            Admin
     * @param time
     *            Length
     * @param issued
     *            Time Issued
     * @param global
     *            Global
     */
    private Mute(final VolumetricBans plugin, final String player, final String reason, final String admin, final long time, final long issued) {
        this.plugin = plugin;
        this.player = player;
        this.reason = reason;
        this.time = time;
        this.admin = admin;
        this.issued = issued;
        initTimer();
    }

    /** Initialises the ban timer for temporary bans */
    private void initTimer() {
        plugin.getEngine().getScheduler().scheduleSyncRepeatingTask(plugin, new DeletableTimer(issued + time, this), 0L, 60000L, TaskPriority.HIGH);
    }

    /**
     * Gets the mute reason
     * 
     * @return The reason for the mute
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets how long the mute will last
     * 
     * @return How long the mute will last, in minutes
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets the time the mute was issued
     * 
     * @return When the mute was issued, in form of a long representing minutes
     *         from the epoch
     */
    public long getTimeIssued() {
        return issued;
    }

    /**
     * Sets the reason for the mute
     * 
     * @param reason
     *            The new mute reason
     */
    public void setReason(final String reason) {
        this.reason = reason;
    }

    /**
     * Sets the time, in minutes
     * 
     * @param time
     *            Time in minutes the mute will last
     */
    public void setTime(final long time) {
        this.time = time;
    }

    /**
     * Gets the name of the muted player
     * 
     * @return The muted player's name
     */
    public String getPlayerName() {
        return player;
    }

    /**
     * The muted player's Player object
     * 
     * @return The Player object for the muted player
     */
    public Player getPlayer() {
        return plugin.getEngine().getPlayer(player, true);
    }

    /**
     * Gets the VolumetricBans plugin
     * 
     * @return The VolumetricBans plugin
     */
    public VolumetricBans getPlugin() {
        return plugin;
    }

    /** {@inheritDoc} */
    @Override
    public void delete() {
        plugin.getStorageHandler().getMutes().remove(this);
    }

    /**
     * Creates a JSONObject from the Mute
     * 
     * @return A JSONObject created from this Mute object
     */
    public JSONObject toJSONObject() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("player", player);
        map.put("reason", reason);
        map.put("admin", admin);
        map.put("time", time);
        map.put("issued", issued);
        return new JSONObject(map);
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + (admin == null ? 0 : admin.hashCode());
        result = prime * result + (int) (issued ^ issued >>> 32);
        result = prime * result + (player == null ? 0 : player.hashCode());
        result = prime * result + (reason == null ? 0 : reason.hashCode());
        result = prime * result + (int) (time ^ time >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Mute)) {
            return false;
        }
        final Mute other = (Mute) obj;
        if (admin == null) {
            if (other.admin != null) {
                return false;
            }
        } else if (!admin.equals(other.admin)) {
            return false;
        }
        if (issued != other.issued) {
            return false;
        }
        if (player == null) {
            if (other.player != null) {
                return false;
            }
        } else if (!player.equals(other.player)) {
            return false;
        }
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        if (time != other.time) {
            return false;
        }
        return true;
    }

    /**
     * Creates a Mute from the data in a JSONObject
     * 
     * @param plugin
     *            The VolumetricBans plugin
     * @param jo
     *            The JSONObject to use the data from
     * 
     * @return A Mute created from given JSONObject
     * 
     * @throws DataLoadException
     *             When we fail to parse the JSONObject
     */
    public static Mute fromJSONObject(final VolumetricBans plugin, final JSONObject jo) throws DataLoadException {
        try {
            final String player = jo.getString("player");
            final String reason = jo.getString("reason");
            final String admin = jo.getString("admin");
            final long time = jo.getLong("time");
            final long issued = jo.getLong("issued");
            return new Mute(plugin, player, reason, admin, time, issued);
        } catch (final JSONException e) {
            throw new DataLoadException("Could not create a Mute object from given JSONObject", e);
        }
    }
}
