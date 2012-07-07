package com.volumetricpixels.voxelbans.connection;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VoxelBansServerHandler implements VoxelBansConnectionHandler {
    
    private VoxelBans plugin;
    private String hostName = "";
    
    public VoxelBansServerHandler(VoxelBans plugin, String hostName) {
        this.plugin = plugin;
        this.hostName = hostName;
    }
    
    /*
     * Server TODO:
     * Ways to get:
     * Server Owner;
     * Set or List of Banned Players;
     * Ban Count
     * Global Ban Count
     * Temp Ban Count
     * Local Ban Count
     */
    
}
