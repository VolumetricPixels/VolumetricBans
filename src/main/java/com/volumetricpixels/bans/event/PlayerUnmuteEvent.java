package com.volumetricpixels.bans.event;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Mute;

public class PlayerUnmuteEvent extends Event implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final VolumetricBans plugin;
	private final Mute mute;

	public PlayerUnmuteEvent(Mute mute) {
		this.plugin = mute.getPlugin();
		this.mute = mute;
	}

	public final VolumetricBans getPlugin() {
		return plugin;
	}

	public final Mute getBan() {
		return mute;
	}

	@Override
	public boolean isCancelled() {
		return super.isCancelled();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
