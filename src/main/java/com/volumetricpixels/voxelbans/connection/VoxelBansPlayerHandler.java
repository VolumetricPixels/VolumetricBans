package com.volumetricpixels.voxelbans.connection;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VoxelBansPlayerHandler implements VoxelBansConnectionHandler {
    
    private VoxelBans plugin;
    private String hostName = "";
    
    public VoxelBansPlayerHandler(VoxelBans plugin, String hostName) {
        this.plugin = plugin;
        this.hostName = hostName;
    }
    
    /*
     * Player TODO:
     * Ways to get:
     * Player's Global Ban Count
     * Player's Global Bans (Map<String (server), String (reason)>)
     * Player's Rep
     * Player's Active Servers & Active Server Count
     */
    
}
