package com.volumetricpixels.bans.crossapi.util;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.crossapi.perapi.Ban;
import com.volumetricpixels.bans.spout.punishments.SpoutBan;

/**
 * This class provides utilities for all API-Using packages.
 * @author DziNeIT
 */
public class GeneralUtil {
    private static VolumetricBans vb;
    private static API inUseAPI = API.UNKNOWN;

    public static void init(VolumetricBans v) {
        vb = v;
        inUseAPI = v.getInUseAPI();
    }

    public static API getApiInUse() {
        return inUseAPI;
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
