package com.volumetricpixels.bans.bukkit.util;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class BukkitUtils {

    public static BukkitUtils instance;
    
    public List<String> getStringList(File f, String path) {
        return YamlConfiguration.loadConfiguration(f).getStringList(path);
    }
    
    public void setConfigValue(File f, String path, Object value) {
        YamlConfiguration.loadConfiguration(f).set(path, value);
    }
    
}
