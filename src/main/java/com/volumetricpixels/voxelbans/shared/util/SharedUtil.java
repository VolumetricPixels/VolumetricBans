package com.volumetricpixels.voxelbans.shared.util;

import com.volumetricpixels.voxelbans.VBUtils;
import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;

/**
 * This class provides utilities for all API-Using packages.
 * @author DziNeIT
 */
public class SharedUtil extends VBUtils {
    
    private static API inUseAPI = API.UNKNOWN;
    
    public static void init(VoxelBans v) {
        VBUtils.init(v);
        if (v instanceof VoxelBansSpout) {
            inUseAPI = API.SPOUT;
        }
    }
    
    public static API getApiInUse() {
        return inUseAPI;
    }
    
}
