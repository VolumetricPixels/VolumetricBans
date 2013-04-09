package com.volumetricpixels.bans.punishment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.spout.api.entity.Player;
import org.spout.api.scheduler.TaskPriority;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.exception.DataLoadException;
import com.volumetricpixels.bans.util.Deletable;
import com.volumetricpixels.bans.util.DeletableTimer;

/** Represents a Ban. */
public final class Ban implements Deletable {
    /** Calendar instance */
    private static final Calendar c = Calendar.getInstance();

    /** VolumetricBans plugin, for timers and shit */
    private final VolumetricBans plugin;

    /** Is the ban global? */
    private boolean global;
    /** Reason for the ban */
    private String reason;
    /** Issuer of the ban */
    private final String admin;
    /** Banned player */
    private final String player;
    /** Is the ban temporary? */
    private final boolean temporary;
    /** How long the ban lasts */
    private long time;
    /** When the ban was issued */
    private final long issued;

    /**
     * Ban constuctor for non-temp bans
     * 
     * @param plugin
     *            The VolumetricBans plugin
     * @param player
     *            The player banned
     * @param global
     *            Whether the ban is global
     * @param reason
     *            The reason for the ban
     * @param admin
     *            The admin who issued the ban
     */
    public Ban(final VolumetricBans plugin, final String player, final boolean global, final String reason, final String admin) {
        this.plugin = plugin;
        this.player = player;
        temporary = false;
        time = -1L;
        this.global = global;
        this.reason = reason;
        this.admin = admin;

        issued = c.getTimeInMillis() / 1000 / 60;
    }

    /**
     * Ban constructor for temp bans
     * 
     * @param plugin
     *            The VolumetricBans plugin
     * @param player
     *            The banned player
     * @param reason
     *            The ban reason
     * @param admin
     *            The admin who issued the ban
     * @param timeMins
     *            How long the ban should last in minutes
     */
    public Ban(final VolumetricBans plugin, final String player, final String reason, final String admin, final long timeMins) {
        this.plugin = plugin;
        this.player = player;
        temporary = true;
        this.reason = reason;
        this.admin = admin;
        time = timeMins;
        global = false;
        issued = c.getTimeInMillis() / 1000 / 60;
        initTimer();
    }

    /**
     * Used internally when creating a Ban from a JSONObject
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
     * @param temp
     *            Temporary
     */
    private Ban(final VolumetricBans plugin, final String player, final String reason, final String admin, final long time, final long issued, final boolean global, final boolean temp) {
        this.plugin = plugin;
        this.player = player;
        temporary = temp;
        this.reason = reason;
        this.time = time;
        this.admin = admin;
        this.issued = issued;
        this.global = global;
        if (temp) {
            initTimer();
        }
    }

    /** Initialises the ban timer for temporary bans */
    private void initTimer() {
        plugin.getEngine().getScheduler().scheduleSyncRepeatingTask(plugin, new DeletableTimer(issued + time, this), 0L, 60000L, TaskPriority.HIGH);
    }

    /**
     * Gets whether the ban is global
     * 
     * @return Whether the ban is global
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Gets the ban reason
     * 
     * @return The reason for the ban
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets how long the ban will last
     * 
     * @return How long the ban will last, in minutes
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets the time the ban was issued
     * 
     * @return When the ban was issued, in form of a long representing minutes
     *         from the epoch
     */
    public long getTimeIssued() {
        return issued;
    }

    /**
     * Sets whether the ban is global
     * 
     * @param global
     *            Whether the ban is global
     */
    public void setGlobal(final boolean global) {
        this.global = global;
    }

    /**
     * Sets the reason for the ban
     * 
     * @param reason
     *            The new ban reason
     */
    public void setReason(final String reason) {
        this.reason = reason;
    }

    /**
     * Sets the time, in minutes
     * 
     * @param time
     *            Time in minutes the ban will last
     */
    public void setTime(final long time) {
        this.time = time;
    }

    /**
     * Gets the name of the banned player
     * 
     * @return The banned player's name
     */
    public String getPlayerName() {
        return player;
    }

    /**
     * Gets the banned player's Player object
     * 
     * @return The banned player's Player object
     */
    public Player getPlayer() {
        return plugin.getEngine().getPlayer(player, true);
    }

    /** Gets the VolumetricBans plugin instance */
    public VolumetricBans getPlugin() {
        return plugin;
    }

    /** {@inheritDoc} */
    @Override
    public void delete() {
        plugin.getStorageHandler().getBans().remove(this);
    }

    /**
     * Converts the ban into a JSONObject
     * 
     * @return A JSONObject representing the ban
     */
    public JSONObject toJSONObject() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("player", player);
        map.put("temp", temporary);
        map.put("global", global);
        map.put("reason", reason);
        map.put("admin", admin);
        map.put("time", time);
        map.put("issued", issued);
        return new JSONObject(map);
    }

    /**
     * Creates a ban from a JSONObject
     * 
     * @param plugin
     *            The VolumetricBans plugin
     * @param jo
     *            The JSONObject to use for creation
     * 
     * @return A Ban created from the JSONObject
     * 
     * @throws DataLoadException
     *             When there is a problem parsing the JSONObject
     */
    public static Ban fromJSONObject(final VolumetricBans plugin, final JSONObject jo) throws DataLoadException {
        try {
            final String player = jo.getString("player");
            final boolean temp = jo.getBoolean("temp");
            final boolean global = jo.getBoolean("global");
            final String reason = jo.getString("reason");
            final String admin = jo.getString("admin");
            final long time = jo.getLong("time");
            final long issued = jo.getLong("issued");
            return new Ban(plugin, player, reason, admin, time, issued, global, temp);
        } catch (final JSONException e) {
            throw new DataLoadException("Could not create a Ban object from given JSONObject", e);
        }
    }
}
