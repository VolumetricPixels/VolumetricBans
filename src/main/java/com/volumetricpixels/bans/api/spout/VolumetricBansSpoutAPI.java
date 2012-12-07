package com.volumetricpixels.bans.api.spout;

import org.spout.api.Spout;

import com.volumetricpixels.bans.api.VolumetricBansAPI;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;

public class VolumetricBansSpoutAPI implements VolumetricBansAPI {
    @Override
    public VolumetricBansSpout getPlugin() {
        return (VolumetricBansSpout) Spout.getEngine().getPluginManager().getPlugin("VolumetricBans");
    }
}
