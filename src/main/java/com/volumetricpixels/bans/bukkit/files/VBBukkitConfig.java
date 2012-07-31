package com.volumetricpixels.bans.bukkit.files;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.volumetricpixels.bans.bukkit.VolumetricBansBukkit;
import com.volumetricpixels.bans.shared.perapi.VBConfig;

public class VBBukkitConfig implements VBConfig {
    
    public final YamlConfiguration config;
    
    public VBBukkitConfig(VolumetricBansBukkit plugin) {
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }
    
}
