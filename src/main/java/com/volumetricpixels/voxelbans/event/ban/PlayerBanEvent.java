package com.volumetricpixels.voxelbans.event.ban;

import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.player.Player;

public abstract class PlayerBanEvent extends PlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    private String reason;

    public PlayerBanEvent(Player p, String reason) {
        super(p);
        this.reason = reason;
    }
    
    public String getReason() {
        return reason;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
