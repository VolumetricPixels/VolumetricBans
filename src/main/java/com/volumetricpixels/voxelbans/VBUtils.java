package com.volumetricpixels.voxelbans;

import com.volumetricpixels.voxelbans.shared.perapi.Ban;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;
import com.volumetricpixels.voxelbans.spout.punishments.SpoutBan;

/**
 * General utilities for VoxelBans
 * @author DziNeIT
 */
public abstract class VBUtils {
    
    private static VoxelBans vb;
    
    public static void init(VoxelBans v) {
        vb = v;
    }
    
    public static Ban newBan(String player, String reason, String admin, boolean global) {
        if (vb instanceof VoxelBansSpout) {
            return new SpoutBan(player, reason, admin, global);
        }
        return null;
    }
    
    public static Ban newBan(String player, String reason, String admin, long time) {
        if (vb instanceof VoxelBansSpout) {
            return new SpoutBan(player, reason, admin, time);
        }
        return null;
    }
    
}
