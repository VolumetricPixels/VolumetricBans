package com.volumetricpixels.bans.spout.event;

import org.spout.api.event.Event;

import com.volumetricpixels.bans.spout.VolumetricBansSpout;

public abstract class VolumetricBansEvent extends Event {
    
    private VolumetricBansSpout vb;
    
    public VolumetricBansEvent(VolumetricBansSpout vb) {
        this.vb = vb;
    }
    
    public VolumetricBansSpout getVoxelBans() {
        return vb;
    }
    
}
