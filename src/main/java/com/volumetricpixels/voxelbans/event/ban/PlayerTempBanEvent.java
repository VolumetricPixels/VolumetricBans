package com.volumetricpixels.voxelbans.event.ban;

import org.spout.api.player.Player;

public class PlayerTempBanEvent extends PlayerLocalBanEvent {
    
    private long time;

    public PlayerTempBanEvent(Player p, long time, String reason) {
        super(p, reason);
        this.time = time;
    }
    
    // Time in seconds
    public long getTime() {
        return time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public long addTime(long time) {
        this.time += time;
        return this.time;
    }
    
}
