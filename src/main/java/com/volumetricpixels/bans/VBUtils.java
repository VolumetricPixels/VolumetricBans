package com.volumetricpixels.bans;

import com.volumetricpixels.bans.shared.perapi.Ban;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;
import com.volumetricpixels.bans.spout.punishments.SpoutBan;

/**
 * General utilities for VolumetricBans
 * @author DziNeIT
 */
public abstract class VBUtils {
    
    private static VolumetricBans vb;
    
    public static void init(VolumetricBans v) {
        vb = v;
    }
    
    public static Ban newBan(String player, String reason, String admin, boolean global) {
        if (vb instanceof VolumetricBansSpout) {
            return new SpoutBan(player, reason, admin, global);
        }
        return null;
    }
    
    public static Ban newBan(String player, String reason, String admin, long time) {
        if (vb instanceof VolumetricBansSpout) {
            return new SpoutBan(player, reason, admin, time);
        }
        return null;
    }
    
}
