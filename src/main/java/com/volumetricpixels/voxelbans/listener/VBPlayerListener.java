package com.volumetricpixels.voxelbans.listener;

import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerJoinEvent;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBPlayerListener implements Listener {
    
    private VoxelBans plugin;
    
    public VBPlayerListener(VoxelBans plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(order = Order.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        plugin.perms.update(e.getPlayer());
    }
    
}
