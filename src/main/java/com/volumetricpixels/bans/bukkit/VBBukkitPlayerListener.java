package com.volumetricpixels.bans.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.volumetricpixels.bans.bukkit.VolumetricBansBukkit;

public class VBBukkitPlayerListener implements Listener {
    
private VolumetricBansBukkit plugin;
    
    public VBBukkitPlayerListener(VolumetricBansBukkit plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (plugin.getPlayerDataRetriever().isBannedFromVBServers(name)) {
            p.kickPlayer(ChatColor.AQUA + "[VolumetricBans] " + ChatColor.RED + "You are permanently banned from VB servers! See voxelbans.net!");
            return;
        }
        if (plugin.getLocalBanHandler().isBanned(name)) {
            p.kickPlayer(plugin.getLocalBanHandler().getBanReason(name));
            return;
        }
        plugin.perms.update(e.getPlayer());
    }
    
}
