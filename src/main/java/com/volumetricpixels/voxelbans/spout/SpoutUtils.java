package com.volumetricpixels.voxelbans.spout;

import org.spout.api.Spout;

import com.volumetricpixels.voxelbans.shared.util.SharedUtil;

public class SpoutUtils extends SharedUtil {
    
    public static SpoutUtils instance;
    
    public void kickPlayer(String player, Object... reason) {
        Spout.getEngine().getPlayer(player, true).kick(reason);
    }
    
}
