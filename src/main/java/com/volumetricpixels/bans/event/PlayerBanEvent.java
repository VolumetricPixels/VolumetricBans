package com.volumetricpixels.bans.event;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Ban;

public class PlayerBanEvent extends Event implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final VolumetricBans plugin;
	private final Ban ban;

	public PlayerBanEvent(Ban ban) {
		this.plugin = ban.getPlugin();
		this.ban = ban;
	}

	public final VolumetricBans getPlugin() {
		return plugin;
	}

	public final Ban getBan() {
		return ban;
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
