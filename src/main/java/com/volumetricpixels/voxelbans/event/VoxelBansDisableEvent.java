package com.volumetricpixels.voxelbans.event;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VoxelBansDisableEvent extends VoxelBansEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public VoxelBansDisableEvent(VoxelBans vb) {
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
