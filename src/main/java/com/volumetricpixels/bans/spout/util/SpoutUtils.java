package com.volumetricpixels.bans.spout.util;

import org.spout.api.Spout;

import com.volumetricpixels.bans.shared.util.SharedUtil;

public class SpoutUtils extends SharedUtil {
    
    public static SpoutUtils instance;
    
    public void kickPlayer(String player, Object... reason) {
        Spout.getEngine().getPlayer(player, true).kick(reason);
    }
    
}
