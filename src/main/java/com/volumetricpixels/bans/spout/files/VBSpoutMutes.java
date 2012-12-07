package com.volumetricpixels.bans.spout.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.bans.shared.perapi.VBMutes;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;
import com.volumetricpixels.bans.spout.punishments.VBSpoutPunishTimers.VBSpoutMuteTimer;

public class VBSpoutMutes implements VBMutes {
    
    private VolumetricBansSpout plugin;
    private boolean initialized = false;
    
    private File muteFile;
    private YamlConfiguration muteConfig;
    private final List<String> muted = new ArrayList<String>();
    
    public VBSpoutMutes(VolumetricBansSpout plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        if (initialized) {
            return;
        }
        
        this.muteFile = new File(plugin.getDataFolder(), "mutes.yml");
        this.muteConfig = new YamlConfiguration(muteFile);
        
        for (String s : muteConfig.getNode("Muted").getStringList()) {
            String[] sArray = s.split("=");
            VBSpoutMuteTimer vbsmt = plugin.bans.vbspt.new VBSpoutMuteTimer(sArray[0], Long.parseLong(sArray[1]));
            int id = Spout.getScheduler().scheduleSyncRepeatingTask(plugin, vbsmt, 0, 1000, TaskPriority.HIGHEST);
            vbsmt.setTaskId(id);
        }
        
        initialized = true;
    }
    
    public void mutePlayer(String player, long time) {
        muted.add(player + "=" + time);
        VBSpoutMuteTimer vbsmt = plugin.bans.vbspt.new VBSpoutMuteTimer(player, time);
        int id = Spout.getScheduler().scheduleSyncRepeatingTask(plugin, vbsmt, 0, 1000, TaskPriority.HIGHEST);
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
        muteConfig.getNode("Mutes").setValue(muted);
    }
    
}
