package com.volumetricpixels.voxelbans.event.ban;

import org.spout.api.player.Player;

public class PlayerLocalBanEvent extends PlayerBanEvent {

    public PlayerLocalBanEvent(Player p, String reason) {
        super(p, reason);
    }
    
}
