package com.volumetricpixels.voxelbans.punishments;

public class Ban {
    
    private final String playerName;
    private final String reason;
    private final String admin;
    private final boolean global;
    private final long time;
    
    public Ban(String player, String reason, String admin, boolean global) {
        this.playerName = player;
        this.reason = reason;
        this.admin = admin;
        this.global = global;
        this.time = -1;
    }
    
    public Ban(String player, String reason, String admin, long time) {
        this.playerName = player;
        this.reason = reason;
        this.admin = admin;
        this.time = time;
        this.global = false;
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
    
}
