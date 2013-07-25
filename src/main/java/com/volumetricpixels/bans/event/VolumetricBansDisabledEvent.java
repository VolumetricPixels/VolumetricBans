package com.volumetricpixels.bans.event;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Called when the plugin is disabled
 */
public class VolumetricBansDisabledEvent extends Event {
	/** The event's HandlerList */
	private static HandlerList handlers = new HandlerList();

	/** The VolumetricBans plugin */
	private final VolumetricBans plugin;

	/**
	 * VolumetricBansDisabledEvent constructor
	 * 
	 * @param plugin
	 *            The VolumetricBans plugin
	 */
	public VolumetricBansDisabledEvent(final VolumetricBans plugin) {
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
