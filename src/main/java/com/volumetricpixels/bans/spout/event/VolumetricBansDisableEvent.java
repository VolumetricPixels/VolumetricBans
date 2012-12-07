package com.volumetricpixels.bans.spout.event;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.spout.VolumetricBansSpout;

public class VolumetricBansDisableEvent extends VolumetricBansEvent {
    private static final HandlerList handlers = new HandlerList();

    public VolumetricBansDisableEvent(VolumetricBansSpout vb) {
        super(vb);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
