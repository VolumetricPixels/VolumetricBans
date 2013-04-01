package com.volumetricpixels.bans.event;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Ban;

public class PlayerUnbanEvent extends Event implements Cancellable {
	/** The event's HandlerList */
	private static HandlerList handlers = new HandlerList();

	/** The VolumetricBans plugin */
	private final VolumetricBans plugin;
	/** The removed Ban object */
	private final Ban ban;

	/**
	 * PlayerUnbanEvent constructor
	 * 
	 * @param ban
	 *            The removed Ban
	 */
	public PlayerUnbanEvent(Ban ban) {
		this.plugin = ban.getPlugin();
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
	 * Gets the Ban that was removed
	 * 
	 * @return The removed Ban object
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
	public void setCancelled(boolean cancelled) {
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
