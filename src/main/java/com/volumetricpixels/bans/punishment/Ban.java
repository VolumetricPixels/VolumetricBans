package com.volumetricpixels.bans.punishment;

import gnu.trove.map.hash.THashMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * Represents a Ban
 */
public final class Ban implements Deletable {
    /** DateFormat instance */
    public static final DateFormat df = new SimpleDateFormat("d mon yyyy hh:mm:ss GMT");

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
    private Ban(final VolumetricBans plugin, final String player, final String reason, final String admin, final long time, final long issued, final boolean global,
            final boolean temp) {
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
        final Map<String, Object> map = new THashMap<String, Object>();
        map.put("player", player);
        map.put("temp", temporary);
        map.put("global", global);
        map.put("reason", reason);
        map.put("issuer", admin);
        final Date date = new Date();
        date.setTime(issued);
        final String s = df.format(date);
        date.setTime(date.getTime() + time * 1000 * 60);
        final String a = df.format(date);
        map.put("date", s);
        map.put("end", a);
        return new JSONObject(map);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (admin == null ? 0 : admin.hashCode());
        result = prime * result + (global ? 1231 : 1237);
        result = prime * result + (int) (issued ^ issued >>> 32);
        result = prime * result + (player == null ? 0 : player.hashCode());
        result = prime * result + (reason == null ? 0 : reason.hashCode());
        result = prime * result + (temporary ? 1231 : 1237);
        result = prime * result + (int) (time ^ time >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Ban)) {
            return false;
        }
        final Ban other = (Ban) obj;
        if (admin == null) {
            if (other.admin != null) {
                return false;
            }
        } else if (!admin.equals(other.admin)) {
            return false;
        }
        if (global != other.global) {
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
        if (temporary != other.temporary) {
            return false;
        }
        if (time != other.time) {
            return false;
        }
        return true;
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
            final String admin = jo.getString("issuer");
            final String date = jo.getString("date");
            final String end = jo.getString("end");
            final long dateData = df.parse(date).getTime() / 1000 / 60;
            final long endData = df.parse(end).getTime() / 1000 / 60;
            return new Ban(plugin, player, reason, admin, endData - dateData, dateData, global, temp);
        } catch (final JSONException e) {
            throw new DataLoadException("Could not create a Ban object from given JSONObject", e);
        } catch (final ParseException e) {
            throw new DataLoadException("Could not parse time!");
        }
    }
}
