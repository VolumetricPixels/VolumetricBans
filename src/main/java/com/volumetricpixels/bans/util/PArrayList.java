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

/**
 * Basically an ArrayList, but calls events for VB bans / mutes when elements
 * are added or removed from the List
 * 
 * @param <T>
 *            The type to be stored in the ArrayList
 */
public class PArrayList<T> extends ArrayList<T> {
    private static final long serialVersionUID = 6649320621907259340L;

    /** VolumetricBans plugin */
    private final VolumetricBans plugin;

    /** Are we currently calling events for new additions / removals */
    private boolean events = true;

    /**
     * Creates a new PArrayList
     * 
     * @param plugin
     *            VolumetricBans plugin instance for event calling
     */
    public PArrayList(VolumetricBans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean add(T o) {
        if (events) {
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
        if (events) {
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

    /**
     * Gets whether the PArrayList is currently calling events for new additions
     * to / removals from the ArrayList
     * 
     * @return Whether events are being called
     */
    public boolean isCallingEvents() {
        return events;
    }

    /**
     * Sets whether the PArrayList is currently calling events for new additions
     * to / removals from the ArrayList
     * 
     * @param callEvents
     *            Whether events should be called
     */
    public void setCallEvents(boolean callEvents) {
        events = callEvents;
    }
}
