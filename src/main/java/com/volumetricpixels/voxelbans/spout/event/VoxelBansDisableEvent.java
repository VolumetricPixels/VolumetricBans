package com.volumetricpixels.voxelbans.spout.event;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class VoxelBansDisableEvent extends VoxelBansEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public VoxelBansDisableEvent(VoxelBansSpout vb) {
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
