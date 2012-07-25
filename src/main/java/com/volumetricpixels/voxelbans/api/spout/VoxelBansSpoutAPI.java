package com.volumetricpixels.voxelbans.api.spout;

import org.spout.api.Spout;

import com.volumetricpixels.voxelbans.api.VoxelBansAPI;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class VoxelBansSpoutAPI implements VoxelBansAPI {
    
    @Override
    public VoxelBansSpout getPlugin() {
        return (VoxelBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
    }
    
}
