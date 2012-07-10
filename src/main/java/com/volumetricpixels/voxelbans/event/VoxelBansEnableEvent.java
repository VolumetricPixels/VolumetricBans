package com.volumetricpixels.voxelbans.event;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VoxelBansEnableEvent extends VoxelBansEvent {
    
    private static HandlerList handlers = new HandlerList();

    public VoxelBansEnableEvent(VoxelBans vb) {
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
