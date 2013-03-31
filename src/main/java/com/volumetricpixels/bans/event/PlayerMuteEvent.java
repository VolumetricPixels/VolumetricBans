package com.volumetricpixels.bans.event;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Mute;

public class PlayerMuteEvent extends Event implements Cancellable {
    /** The event's HandlerList */
    private static HandlerList handlers = new HandlerList();

    /** The VolumetricBans plugin */
    private final VolumetricBans plugin;
    /** The added Mute object */
    private final Mute mute;

    /**
     * PlayerMuteEvent constructor
     *
     * @param mute The added Mute object
     */
    public PlayerMuteEvent(Mute mute) {
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
     * Gets the Mute that was added
     *
     * @return The added Mute object
     */
    public final Mute getMute() {
        return mute;
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
     * Gets the event's HandlerList
     *
     * @return The HandlerList for the event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
