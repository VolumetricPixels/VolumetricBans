package com.volumetricpixels.voxelbans.spout.event.ban;

import org.spout.api.event.HandlerList;

import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class VoxelBansEnableEvent extends VoxelBansEvent {
    
    private static HandlerList handlers = new HandlerList();

    public VoxelBansEnableEvent(VoxelBansSpout vb) {
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
