package com.volumetricpixels.voxelbans.files;

import java.io.File;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBConfiguration extends ConfigurationHolderConfiguration {
    
    private VoxelBans pl;
    
    public VBConfiguration(VoxelBans pl) {
        super(new YamlConfiguration(new File(pl.getDataFolder(), "config.yml")));
        this.pl = pl;
        this.pl.getServerKey();
    }
    
    public void load() throws ConfigurationException {
        super.load();
        super.save();
    }
    
    public void save() throws ConfigurationException {
        super.save();
    }
    
}
