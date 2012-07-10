package com.volumetricpixels.voxelbans.event;

import org.spout.api.event.Event;

import com.volumetricpixels.voxelbans.VoxelBans;

public abstract class VoxelBansEvent extends Event {
    
    private VoxelBans vb;
    
    public VoxelBansEvent(VoxelBans vb) {
        this.vb = vb;
    }
    
    public VoxelBans getVoxelBans() {
        return vb;
    }
    
}
