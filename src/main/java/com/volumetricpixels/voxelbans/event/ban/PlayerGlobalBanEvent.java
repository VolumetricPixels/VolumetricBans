package com.volumetricpixels.voxelbans.event.ban;

import org.spout.api.player.Player;

public class PlayerGlobalBanEvent extends PlayerBanEvent {

    public PlayerGlobalBanEvent(Player p, String reason) {
        super(p, reason);
    }
    
}
