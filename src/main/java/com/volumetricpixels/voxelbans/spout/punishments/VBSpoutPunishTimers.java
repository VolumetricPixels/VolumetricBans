package com.volumetricpixels.voxelbans.spout.punishments;

import org.spout.api.Spout;

import com.volumetricpixels.voxelbans.shared.perapi.VBPunishTimers;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class VBSpoutPunishTimers implements VBPunishTimers {
    
    public class VBSpoutBanTimer implements VBBanTimer {
        
        private String player;
        private long time;
        
        private int taskId;
        
        public VBSpoutBanTimer(String player, long time) {
            this.player = player;
            this.time = time;
        }

        @Override
        public void run() {
            time--;
            if (time == 0) {
                VoxelBansSpout vbs = (VoxelBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
                vbs.bans.unbanPlayer(player);
                Spout.getScheduler().cancelTask(taskId);
            }
        }
        
        public VBSpoutBanTimer setTaskId(int id) {
            this.taskId = id;
            return this;
        }
        
    }
    
    public class VBSpoutMuteTimer implements VBMuteTimer {
        
        private String player;
        private long time;
        
        private int taskId;
        
        public VBSpoutMuteTimer(String player, long time) {
            this.player = player;
            this.time = time;
        }
        
        @Override
        public void run() {
            time--;
            if (time == 0) {
                VoxelBansSpout vbs = (VoxelBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
                vbs.mutes.unmutePlayer(player);
                Spout.getScheduler().cancelTask(taskId);
            }
        }
        
        public VBSpoutMuteTimer setTaskId(int id) {
            this.taskId = id;
            return this;
        }
        
    }
    
}
