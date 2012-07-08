package com.volumetricpixels.voxelbans.connection;

import com.volumetricpixels.voxelbans.VoxelBans;

/**
 * Checks bans against the VoxelBans website
 */
public class BanChecker {
    
    //private VoxelBans plugin;
    
    BanChecker(VoxelBans plugin) {
        //this.plugin = plugin;
    }
    
    public boolean isGlobalBanned(String name) {
        return false;
    }
    
    public boolean isGlobalBannedFromServer(String name) {
        return false;
    }
    
    public boolean isLocalBanned(String name) {
        return false;
    }
    
    public boolean isTempBanned(String name) {
        return false;
    }
    
    public boolean isBannedAtAll(String name) {
        return isTempBanned(name) || isLocalBanned(name) || isGlobalBanned(name) || isGlobalBannedFromServer(name);
    }
    
}
