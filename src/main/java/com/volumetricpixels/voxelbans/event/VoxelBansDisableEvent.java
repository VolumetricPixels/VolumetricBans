package com.volumetricpixels.voxelbans.event;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VoxelBansDisableEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private VoxelBans vb;
    
    public VoxelBansDisableEvent(VoxelBans vb) {
        this.vb = vb;
    }
    
    public VoxelBans getVoxelBans() {
        return this.vb;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
