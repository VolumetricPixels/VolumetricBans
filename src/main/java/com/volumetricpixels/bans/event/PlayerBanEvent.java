package com.volumetricpixels.bans.event;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Ban;

/**
 * Called when a player is banned
 */
public class PlayerBanEvent extends Event implements Cancellable {
	/** The event's HandlerList */
	private static HandlerList handlers = new HandlerList();

	/** The VolumetricBans instance */
	private final VolumetricBans plugin;
	/** The Ban that was added */
	private final Ban ban;

	/**
	 * PlayerBanEvent constructor
	 * 
	 * @param ban
	 *            The Ban added
	 */
	public PlayerBanEvent(final Ban ban) {
		plugin = ban.getPlugin();
		this.ban = ban;
	}

	/**
	 * Gets the VolumetricBans plugin
	 * 
	 * @return The VolumetricBans plugin
	 */
	public final VolumetricBans getPlugin() {
		return plugin;
	}

	/**
	 * Gets the Ban that was added
	 * 
	 * @return The added Ban object
	 */
	public final Ban getBan() {
		return ban;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCancelled() {
		return super.isCancelled();
	}

	/** {@inheritDoc} */
	@Override
	public void setCancelled(final boolean cancelled) {
		super.setCancelled(cancelled);
	}

	/** {@inheritDoc} */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the HandlerList for this event
	 * 
	 * @return This event's HandlerList
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
