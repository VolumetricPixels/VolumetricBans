package com.volumetricpixels.bans.spout.files;

import java.io.File;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.bans.interfaces.VBConfig;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;

public class VBSpoutConfig extends YamlConfiguration implements VBConfig {

    public VBSpoutConfig(VolumetricBansSpout vb) {
        super(new File(vb.getDataFolder(), "config.yml"));
    }

}
