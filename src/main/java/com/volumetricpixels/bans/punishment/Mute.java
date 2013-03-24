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
import com.volumetricpixels.bans.util.Removable;
import com.volumetricpixels.bans.util.RemovableTimer;

public class Mute implements Removable {
	private static Calendar c = Calendar.getInstance();

	private final VolumetricBans plugin;

	private String reason;
	private String admin;
	private String player;
	// Temporary mute only fields
	private long time;
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

	private void initTimer() {
		plugin.getEngine().getScheduler().scheduleSyncRepeatingTask(plugin, new RemovableTimer(issued + time, this), 0L, 60000L, TaskPriority.HIGH);
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

	public String getPlayerName() {
		return player;
	}

	public Player getPlayer() {
		return plugin.getEngine().getPlayer(player, true);
	}

	public VolumetricBans getPlugin() {
		return plugin;
	}

	@Override
	public void remove() {
		plugin.getStorageHandler().getMutes().remove(this);
	}

	public JSONObject toJSONObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("player", player);
		map.put("reason", reason);
		map.put("admin", admin);
		map.put("time", time);
		map.put("issued", issued);
		return new JSONObject(map);
	}

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
