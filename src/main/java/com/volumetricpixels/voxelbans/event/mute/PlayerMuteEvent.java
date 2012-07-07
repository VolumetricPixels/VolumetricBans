package com.volumetricpixels.voxelbans.event.mute;

import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.player.Player;

public class PlayerMuteEvent extends PlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();

    public PlayerMuteEvent(Player p) {
        super(p);
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
