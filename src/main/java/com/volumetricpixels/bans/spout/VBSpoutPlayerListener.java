package com.volumetricpixels.bans.spout;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerJoinEvent;

public class VBSpoutPlayerListener implements Listener {

    private VolumetricBansSpout plugin;

    public VBSpoutPlayerListener(VolumetricBansSpout plugin) {
        this.plugin = plugin;
    }

    @EventHandler(order = Order.LATEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (plugin.pdr.isBannedFromVBServers(name)) {
            p.kick(ChatStyle.CYAN, "[VolumetricBans] ", ChatStyle.RED, "You are permanently banned from VB servers! See voxelbans.net!");
            return;
        }
        if (plugin.bans.isBanned(name)) {
            p.kick(plugin.bans.getBanReason(name));
            return;
        }
        plugin.perms.update(e.getPlayer());
    }

}
