package com.volumetricpixels.voxelbans.listener;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.player.Player;

import com.volumetricpixels.voxelbans.VoxelBans;

public class VBPlayerListener implements Listener {
    
    private VoxelBans plugin;
    
    public VBPlayerListener(VoxelBans plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(order = Order.LATEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (plugin.pdr.isBannedFromVBServers(name)) {
            p.kick(ChatStyle.CYAN, "[VoxelBans] ", ChatStyle.RED, "You are permanently banned from VB servers! See voxelbans.net!");
            return;
        }
        if (plugin.bans.isBanned(name)) {
            p.kick(plugin.bans.getBanReason(name));
            return;
        }
        plugin.perms.update(e.getPlayer());
    }
    
}
