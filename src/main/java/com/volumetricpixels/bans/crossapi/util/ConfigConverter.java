package com.volumetricpixels.bans.crossapi.util;

import java.io.File;
import java.util.List;

import com.volumetricpixels.bans.spout.util.SpoutUtils;

public class ConfigConverter {

    private API api = GeneralUtil.getApiInUse();
    private File f;

    public ConfigConverter(File file) {
        this.f = file;
    }

    public void setValue(String path, Object value) {
        switch (api) {
            case SPOUT:
                SpoutUtils.instance.setConfigNode(f, path, value);
                break;
            default:
                throw new UnsupportedOperationException("The API has not yet got a setValue implementation!");
        }
    }

    public List<String> getStringList(String path) {
        switch (api) {
            case SPOUT:
                return SpoutUtils.instance.getStringList(f, path);
            default:
                throw new UnsupportedOperationException("The API has not yet got a getStringList implementation!");
        }
    }

}