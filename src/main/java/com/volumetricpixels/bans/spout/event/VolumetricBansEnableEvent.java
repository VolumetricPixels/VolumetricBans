package com.volumetricpixels.bans.spout.event;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.bans.spout.VolumetricBansSpout;

public class VolumetricBansEnableEvent extends VolumetricBansEvent {
    
    private static HandlerList handlers = new HandlerList();

    public VolumetricBansEnableEvent(VolumetricBansSpout vb) {
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
