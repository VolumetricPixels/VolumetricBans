package com.volumetricpixels.voxelbans.punishments;

import java.io.File;

import org.spout.api.Spout;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VoxelBans;

public class Mute {
    
    private final String playerName;
    private final long timeFor;
    
    public Mute(String playerName, long timeFor) {
        this.playerName = playerName;
        this.timeFor = timeFor;
        Spout.getEngine().getScheduler().scheduleSyncRepeatingTask(Spout.getEngine().getPluginManager().getPlugin("VoxelBans"),
            new MuteTimer(this), 1000, 1000, TaskPriority.HIGHEST);
    }
    
    private void removeMute() {
        VoxelBans vb = (VoxelBans) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
        vb.punishments.unmutePlayer(playerName);
    }
    
    private static class MuteTimer implements Runnable {
        
        private final Mute m;
        private final long timeFor;
        private final YamlConfiguration yc;
        private long minutes;
        
        private MuteTimer(Mute m) {
            this.m = m;
            this.timeFor = m.timeFor;
            this.minutes = timeFor;
            
            yc = new YamlConfiguration(new File(Spout.getEngine().getPluginManager().getPlugin("VoxelBans").getDataFolder(),
                "doNotTouch" + File.separator + "muteTimes.yml"));
            
            try {
                yc.load();
                yc.getNode(m.playerName).setValue(minutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            minutes++;
            if (minutes >= timeFor) {
                m.removeMute();
                yc.getNode(m.playerName).remove();
            } else {
                yc.getNode(m.playerName).setValue(minutes);
            }
        }
        
    }
    
}
