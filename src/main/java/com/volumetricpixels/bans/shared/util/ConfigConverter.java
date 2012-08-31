package com.volumetricpixels.bans.shared.util;

import java.io.File;
import java.util.List;

import com.volumetricpixels.bans.bukkit.util.BukkitUtils;
import com.volumetricpixels.bans.spout.util.SpoutUtils;

public class ConfigConverter {
    
    private API api = SharedUtil.getApiInUse();
    private File f;

    public ConfigConverter(File file) {
        this.f = file;
    }

    public void setValue(String path, Object value) {
        switch (api) {
            case SPOUT:
                SpoutUtils.instance.setConfigNode(f, path, value);
                break;
            case BUKKIT:
                BukkitUtils.instance.setConfigValue(f, path, value);
                break;
            default:
                throw new UnsupportedOperationException("The API has not yet got a setValue implementation!");
        }
    }
    
    public List<String> getStringList(String path) {
        switch (api) {
            case SPOUT:
                return SpoutUtils.instance.getStringList(f, path);
            case BUKKIT:
                return BukkitUtils.instance.getStringList(f, path);
            default:
                throw new UnsupportedOperationException("The API has not yet got a getStringList implementation!");
        }
    }
    
}
