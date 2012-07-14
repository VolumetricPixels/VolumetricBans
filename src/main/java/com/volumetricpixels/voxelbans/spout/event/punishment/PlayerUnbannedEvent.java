package com.volumetricpixels.voxelbans.spout.event.punishment;

public class PlayerUnbannedEvent {
    
    private String player;
    private String admin;
    
    public PlayerUnbannedEvent(String player, String admin) {
        this.player = player;
        this.admin = admin;
    }
    
    public String getPlayer() {
        return player;
    }
    
    public String getAdmin() {
        return admin;
    }
    
}
