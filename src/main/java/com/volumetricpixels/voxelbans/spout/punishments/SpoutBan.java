package com.volumetricpixels.voxelbans.spout.punishments;

import java.io.File;

import org.spout.api.Spout;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.shared.perapi.Ban;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

public class SpoutBan implements Ban {
    
    private final String playerName;
    private final String reason;
    private final String admin;
    private final boolean global;
    private final long time;
    
    public SpoutBan(String player, String reason, String admin, boolean global) {
        this.playerName = player;
        this.reason = reason;
        this.admin = admin;
        this.global = global;
        this.time = -1;
    }
    
    public SpoutBan(String player, String reason, String admin, long time) {
        this.playerName = player;
        this.reason = reason;
        this.admin = admin;
        this.time = time;
        this.global = false;
        
        Spout.getEngine().getScheduler().scheduleAsyncRepeatingTask(Spout.getEngine().getPluginManager().getPlugin("VoxelBans"),
            new TempBanTimer(this, time), 0, 60000, TaskPriority.HIGHEST);
    }
    
    public String getPlayer() {
        return playerName;
    }
    
    public String getReason() {
        return reason;
    }
    
    public String getAdmin() {
        return admin;
    }
    
    public boolean isGlobal() {
        return global;
    }
    
    public boolean isTemporary() {
        return time != -1;
    }
    
    public long getTime() {
        return time;
    }
    
    private void deleteBan() {
        VoxelBansSpout vb = (VoxelBansSpout) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
        vb.bans.unbanPlayer(getPlayer());
    }
    
    private static class TempBanTimer implements Runnable {
        
        private final SpoutBan b;
        private final long timeFor;
        private final YamlConfiguration yc;
        private long minutes;
        
        private TempBanTimer(SpoutBan b, long timeFor) {
            this.b = b;
            this.timeFor = timeFor;
            this.minutes = timeFor;
            
            File timerFile = new File(Spout.getEngine().getPluginManager().getPlugin("VoxelBans").getDataFolder(),
                "doNotTouch" + File.separator + "banTimer.yml");
            if (!timerFile.exists()) {
                try {
                    timerFile.createNewFile();
                } catch (Exception ignore) {}
            }
            yc = new YamlConfiguration(timerFile);
            try {
                yc.load();
                yc.getNode(b.getPlayer()).setValue(minutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            minutes++;
            if (minutes > timeFor) {
                b.deleteBan();
                yc.getNode(b.getPlayer()).remove();
            } else {
                yc.getNode(b.getPlayer()).setValue(minutes);
            }
        }
        
    }
    
}
