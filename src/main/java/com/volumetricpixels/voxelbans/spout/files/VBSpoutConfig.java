package com.volumetricpixels.voxelbans.spout.files;

import java.io.File;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.interfaces.VBConfig;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class VBSpoutConfig extends YamlConfiguration implements VBConfig {
    
    public VBSpoutConfig(VoxelBansSpout vb) {
        super(new File(vb.getDataFolder(), "config.yml"));
    }
    
}
