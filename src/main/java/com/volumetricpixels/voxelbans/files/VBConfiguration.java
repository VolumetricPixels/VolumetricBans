package com.volumetricpixels.voxelbans.files;

import java.io.File;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBConfiguration extends YamlConfiguration {
    
    public VBConfiguration(VoxelBans vb) {
        super(new File(vb.getDataFolder(), "config.yml"));
    }
    
}
