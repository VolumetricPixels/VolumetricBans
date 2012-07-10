package com.volumetricpixels.voxelbans.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBMuteFile {
    
    private VoxelBans plugin;
    private boolean initialized = false;
    
    private File muteFile;
    private YamlConfiguration muteConfig;
    private final List<String> muted = new ArrayList<String>();
    
    public VBMuteFile(VoxelBans plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        if (initialized) {
            return;
        }
        
        this.muteFile = new File(plugin.getDataFolder(), "mutes.yml");
        this.muteConfig = new YamlConfiguration(muteFile);
        
        initialized = true;
    }
    
    public void mutePlayer(String player, long time) {
        muted.add(player + "=" + time);
        updateConfig(false);
    }
    
    public void unmutePlayer(String player) {
        for (String s : muted) {
            if (s.startsWith(player + "=")) {
                String[] split = s.split("=");
                muted.remove(player + "=" + split[1]);
                return;
            }
        }
        updateConfig(true);
    }
    
    public boolean isMuted(String player) {
        return muted.contains(player);
    }
    
    private void updateConfig(boolean unmute) {
        muteConfig.getNode("Mutes").setValue(muted);
    }
    
}