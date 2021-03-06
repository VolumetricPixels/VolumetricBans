package com.volumetricpixels.bans.storage;

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

/**
 * An ArrayList, modifed for VolumetricBans to call events for VB bans / mutes
 * when elements are added or removed from the List
 * 
 * @param <T>
 *            The type to be stored in the EventArrayList
 */
public class EventArrayList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 6649320621907259340L;

	/** VolumetricBans plugin */
	private final EventManager eventManager;

	/** Whether we are currently calling events for new additions / removals */
	private boolean events = true;

	/**
	 * Creates a new EventArrayList
	 * 
	 * @param plugin
	 *            VolumetricBans plugin instance for event calling
	 */
	public EventArrayList(final VolumetricBans plugin) {
		this.eventManager = plugin.getEngine().getEventManager();
	}

	/** {@inheritDoc} */
	@Override
	public boolean add(final T o) {
		if (events) {
			Cancellable c = null;
			if (o instanceof Ban) {
				c = eventManager.callEvent(new PlayerBanEvent((Ban) o));
			} else if (o instanceof Mute) {
				c = eventManager.callEvent(new PlayerMuteEvent((Mute) o));
			}
			if (c != null & c.isCancelled()) {
				return false;
			}
		}
		return super.add(o);
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(final Object o) {
		if (events) {
			Cancellable c = null;
			if (o instanceof Ban) {
				c = eventManager.callEvent(new PlayerUnbanEvent((Ban) o));
			} else if (o instanceof Mute) {
				c = eventManager.callEvent(new PlayerUnmuteEvent((Mute) o));
			}
			if (c != null & c.isCancelled()) {
				return false;
			}
		}
		return super.remove(o);
	}

	/**
	 * Gets whether the EventArrayList is currently calling events for new
	 * additions to / removals from the ArrayList
	 * 
	 * @return Whether events are being called
	 */
	public boolean isCallingEvents() {
		return events;
	}

	/**
	 * Sets whether the EventArrayList is currently calling events for new
	 * additions to / removals from the ArrayList
	 * 
	 * @param callEvents
	 *            Whether events should be called
	 */
	public void setCallEvents(final boolean callEvents) {
		events = callEvents;
	}
}
