package com.volumetricpixels.bans.util;

import java.util.ArrayList;

import org.spout.api.event.Cancellable;
import org.spout.api.event.EventManager;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.event.PlayerBanEvent;
import com.volumetricpixels.bans.event.PlayerMuteEvent;
import com.volumetricpixels.bans.event.PlayerUnbanEvent;
import com.volumetricpixels.bans.event.PlayerUnmuteEvent;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.punishment.Mute;

public class PArrayList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 6649320621907259340L;

	private VolumetricBans plugin;
	private boolean curState = true;

	public PArrayList(VolumetricBans plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean add(T o) {
		if (curState) {
			EventManager em = plugin.getEngine().getEventManager();
			Cancellable c = null;
			if (o instanceof Ban) {
				c = em.callEvent(new PlayerBanEvent((Ban) o));
			} else if (o instanceof Mute) {
				c = em.callEvent(new PlayerMuteEvent((Mute) o));
			}
			if (c != null & c.isCancelled()) {
				return false;
			}
		}
		return super.add(o);
	}

	@Override
	public boolean remove(Object o) {
		if (curState) {
			EventManager em = plugin.getEngine().getEventManager();
			Cancellable c = null;
			if (o instanceof Ban) {
				c = em.callEvent(new PlayerUnbanEvent((Ban) o));
			} else if (o instanceof Mute) {
				c = em.callEvent(new PlayerUnmuteEvent((Mute) o));
			}
			if (c != null & c.isCancelled()) {
				return false;
			}
		}
		return super.remove(o);
	}

	public boolean getState() {
		return curState;
	}

	public void setState(boolean state) {
		curState = state;
	}
}
