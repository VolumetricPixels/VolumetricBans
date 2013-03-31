package com.volumetricpixels.bans.event;

import com.volumetricpixels.bans.VolumetricBans;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

public class VolumetricBansEnabledEvent extends Event {
	/**
	 * The event's HandlerList
	 */
	private static HandlerList handlers = new HandlerList();

	/**
	 * The VolumetricBans plugin
	 */
	private final VolumetricBans plugin;

	/**
	 * VolumetricBansEnabledEvent constructor
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public VolumetricBansEnabledEvent(VolumetricBans plugin) {
		this.plugin = plugin;
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
	 * {@inheritDoc}
	 */
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