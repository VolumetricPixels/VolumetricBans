package com.volumetricpixels.bans.event;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;

public class VolumetricBansEnabledEvent extends Event {
	private static HandlerList handlers = new HandlerList();

	private final VolumetricBans plugin;

	public VolumetricBansEnabledEvent(VolumetricBans plugin) {
		this.plugin = plugin;
	}

	public final VolumetricBans getPlugin() {
		return plugin;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
