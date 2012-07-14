package com.volumetricpixels.voxelbans.spout.event;

import org.spout.api.event.Event;

import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public abstract class VoxelBansEvent extends Event {
    
    private VoxelBansSpout vb;
    
    public VoxelBansEvent(VoxelBansSpout vb) {
        this.vb = vb;
    }
    
    public VoxelBansSpout getVoxelBans() {
        return vb;
    }
    
}
