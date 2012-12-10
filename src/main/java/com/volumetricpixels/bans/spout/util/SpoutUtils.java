package com.volumetricpixels.bans.spout.util;

import java.io.File;
import java.util.List;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.bans.crossapi.util.GeneralUtil;

public class SpoutUtils extends GeneralUtil {

    public static SpoutUtils instance;

    public void kickPlayer(String player, Object... reason) {
        ((Server) Spout.getEngine()).getPlayer(player, true).kick(reason);
    }

    public void setConfigNode(File f, String path, Object value) {
        new YamlConfiguration(f).getNode(path).setValue(value);
    }

    public List<String> getStringList(File f, String path) {
        return new YamlConfiguration(f).getNode(path).getStringList();
    }

}
