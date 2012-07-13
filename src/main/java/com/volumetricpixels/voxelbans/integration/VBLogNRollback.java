package com.volumetricpixels.voxelbans.integration;

import org.spout.api.Spout;
import org.spout.api.plugin.Plugin;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBLogNRollback implements VBPluginIntegration {
    
    private VoxelBans vb;
    
    public VBLogNRollback() {
        vb = (VoxelBans) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
    }

    @Override
    public boolean integrationEnabled() {
        Plugin p = Spout.getEngine().getPluginManager().getPlugin("LogNRollback");
        if (p != null) {
            return p.isEnabled() && vb.getConfig().getNode("Integration.LogNRollback.Enable").getBoolean();
        }
        return false;
    }
    
}
