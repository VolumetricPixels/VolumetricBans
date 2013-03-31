package com.volumetricpixels.bans.event;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Mute;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

public class PlayerUnmuteEvent extends Event implements Cancellable {
	/**
	 * The event's HandlerList
	 */
	private static HandlerList handlers = new HandlerList();

	/**
	 * The VolumetricBans plugin
	 */
	private final VolumetricBans plugin;
	/**
	 * The removed Mute object
	 */
	private final Mute mute;

	/**
	 * PlayerUnmuteEvent constructor
	 * 
	 * @param mute
	 *            The removed Mute
	 */
	public PlayerUnmuteEvent(Mute mute) {
		this.plugin = mute.getPlugin();
		this.mute = mute;
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
	 * Gets the Mute that was removed
	 * 
	 * @return The removed Mute object
	 */
	public final Mute getMute() {
		return mute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		return super.isCancelled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
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
