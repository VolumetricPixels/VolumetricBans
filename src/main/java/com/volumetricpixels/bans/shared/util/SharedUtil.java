package com.volumetricpixels.bans.shared.util;

import com.volumetricpixels.bans.VBUtils;
import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;

/**
 * This class provides utilities for all API-Using packages.
 * @author DziNeIT
 */
public class SharedUtil extends VBUtils {

    private static API inUseAPI = API.UNKNOWN;

    public static void init(VolumetricBans v) {
        VBUtils.init(v);
        if (v instanceof VolumetricBansSpout) {
            inUseAPI = API.SPOUT;
        }
    }

    public static API getApiInUse() {
        return inUseAPI;
    }

}
