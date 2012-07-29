package com.volumetricpixels.bans.spout.punishments;

import org.spout.api.Spout;

import com.volumetricpixels.bans.shared.perapi.VBPunishTimers;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;

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
                VolumetricBansSpout vbs = (VolumetricBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
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
                VolumetricBansSpout vbs = (VolumetricBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
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
