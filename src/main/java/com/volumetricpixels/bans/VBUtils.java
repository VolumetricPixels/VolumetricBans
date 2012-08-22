package com.volumetricpixels.bans;

import com.volumetricpixels.bans.shared.perapi.Ban;
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
        switch (vb.getInUseAPI()) {
            case SPOUT:
                return new SpoutBan(player, reason, admin, global);
            default:
                throw new UnsupportedOperationException("The API being used has not got a Ban implementation!");
        }
    }
    
    public static Ban newBan(String player, String reason, String admin, long time) {
        switch (vb.getInUseAPI()) {
            case SPOUT:
                return new SpoutBan(player, reason, admin, time);
            default:
                throw new UnsupportedOperationException("The API being used has not got a Ban implementation!");
        }
    }
    
}
