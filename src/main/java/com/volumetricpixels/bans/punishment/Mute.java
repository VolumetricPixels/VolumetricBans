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

/**
 * Represents a Mute
 */
public class Mute implements Deletable {
	/** The Calendar instance */
	private static Calendar c = Calendar.getInstance();

	/** VolumetricBans plugin, for timers and shit */
	private final VolumetricBans plugin;

	/** Reason for the ban */
	private String reason;
	/** Issuer of the ban */
	private String admin;
	/** Banned player */
	private String player;
	/** How long the ban lasts */
	private long time;
	/** When the ban was issued */
	private long issued;

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
	public Mute(VolumetricBans plugin, String player, String reason, String admin, long timeMins) {
		this.plugin = plugin;
		this.player = player;
		this.reason = reason;
		this.admin = admin;
		time = timeMins;
		issued = (c.getTimeInMillis() / 1000) / 60;
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
	private Mute(VolumetricBans plugin, String player, String reason, String admin, long time, long issued) {
		this.plugin = plugin;
		this.player = player;
		this.reason = reason;
		this.time = time;
		this.admin = admin;
		this.issued = issued;
		initTimer();
	}

	/**
	 * Initialises the ban timer for temporary bans
	 */
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
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * Sets the time, in minutes
	 * 
	 * @param time
	 *            Time in minutes the mute will last
	 */
	public void setTime(long time) {
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

	/**
	 * {@inheritDoc}
	 */
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("player", player);
		map.put("reason", reason);
		map.put("admin", admin);
		map.put("time", time);
		map.put("issued", issued);
		return new JSONObject(map);
	}

	/**
	 * Creates a Mute from the data in a JSONObject
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 * @param jo
	 *            The JSONObject to use the data from
	 * @return A Mute created from given JSONObject
	 * @throws DataLoadException
	 *             When we fail to parse the JSONObject
	 */
	public static Mute fromJSONObject(VolumetricBans plugin, JSONObject jo) throws DataLoadException {
		try {
			String player = jo.getString("player");
			String reason = jo.getString("reason");
			String admin = jo.getString("admin");
			long time = jo.getLong("time");
			long issued = jo.getLong("issued");
			return new Mute(plugin, player, reason, admin, time, issued);
		} catch (JSONException e) {
			throw new DataLoadException("Could not create a Mute object from given JSONObject", e);
		}
	}
}
