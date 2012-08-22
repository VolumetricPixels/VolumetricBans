package com.volumetricpixels.bans.bukkit.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.volumetricpixels.bans.bukkit.VolumetricBansBukkit;
import com.volumetricpixels.bans.bukkit.punishments.VBBukkitPunishTimers.VBBukkitMuteTimer;
import com.volumetricpixels.bans.shared.perapi.VBMutes;

public class VBBukkitMutes implements VBMutes {
    
    private VolumetricBansBukkit plugin;
    private boolean initialized = false;
    
    private File muteFile;
    private YamlConfiguration muteConfig;
    private final List<String> muted = new ArrayList<String>();
    
    public VBBukkitMutes(VolumetricBansBukkit plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        if (initialized) {
            return;
        }
        
        this.muteFile = new File(plugin.getDataFolder(), "mutes.yml");
        this.muteConfig = YamlConfiguration.loadConfiguration(muteFile);
        
        for (String s : muteConfig.getStringList("Muted")) {
            String[] sArray = s.split("=");
            VBBukkitMuteTimer vbsmt = ((VBBukkitLocalBans) plugin.getLocalBanHandler()).pt.new VBBukkitMuteTimer(sArray[0], Long.parseLong(sArray[1]));
            int id = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, vbsmt, 0, 1000);
            vbsmt.setTaskId(id);
        }
        
        initialized = true;
    }
    
    public void mutePlayer(String player, long time) {
        muted.add(player + "=" + time);
        VBBukkitMuteTimer vbsmt = ((VBBukkitLocalBans) plugin.getLocalBanHandler()).pt.new VBBukkitMuteTimer(player, time);
        int id = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, vbsmt, 0, 1000);
        vbsmt.setTaskId(id);
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
        muteConfig.set("Mutes", muted);
    }
    
}
