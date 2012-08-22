package com.volumetricpixels.bans.bukkit.punishments;

import org.bukkit.Bukkit;

import com.volumetricpixels.bans.bukkit.VolumetricBansBukkit;
import com.volumetricpixels.bans.shared.perapi.VBPunishTimers;

public class VBBukkitPunishTimers implements VBPunishTimers {
    
    public class VBBukkitBanTimer implements Runnable {
        
        String player;
        long time;
        
        int taskId;
        
        public VBBukkitBanTimer(String player, long time) {
            this.player = player;
            this.time = time;
        }

        @Override
        public void run() {
            time--;
            if (time <= 0) {
                VolumetricBansBukkit vbs = (VolumetricBansBukkit) Bukkit.getPluginManager().getPlugin("VolumetricBans");
                vbs.getLocalBanHandler().unbanPlayer(player);
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }

        public void setTaskId(int id) {
            this.taskId = id;
        }
        
    }
    
    public class VBBukkitMuteTimer implements Runnable {
        
        String player;
        long time;
        
        int taskId;
        
        public VBBukkitMuteTimer(String player, long time) {
            this.player = player;
            this.time = time;
        }

        @Override
        public void run() {
            time--;
            if (time <= 0) {
                VolumetricBansBukkit vbs = (VolumetricBansBukkit) Bukkit.getPluginManager().getPlugin("VolumetricBans");
                vbs.getMuteHandler().unmutePlayer(player);
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }

        public void setTaskId(int id) {
            this.taskId = id;
        }
        
    }
    
}
