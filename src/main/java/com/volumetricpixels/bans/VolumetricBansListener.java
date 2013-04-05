package com.volumetricpixels.bans;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerLoginEvent;

import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.util.APIRequestUtil;

/** Listens to various events for VolumetricBans */
public final class VolumetricBansListener implements Listener {
    /** The VolumetricBans plugin */
    private final VolumetricBans plugin;
    /** The APIRequestHandler we are using */
    private final APIRequestHandler arh;
    /** The PunishmentManager instance */
    private final PunishmentManager pm;
    /** The checking thread pool */
    private final ExecutorService threadPool;

    /**
     * Creates a new VolumetricBansListener
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    protected VolumetricBansListener(VolumetricBans plugin) {
        this.plugin = plugin;
        arh = new APIRequestHandler(plugin, "players");
        pm = plugin.getPunishmentManager();
        threadPool = Executors.newCachedThreadPool();
    }

    @EventHandler(order = Order.LATEST_IGNORE_CANCELLED)
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (pm.isBanned(name)) {
            player.kick(pm.getBansForPlayer(name)[0].getReason());
            event.setAllowed(false);
            plugin.getLogger().info("Prevented banned player '" + name + "' from logging in!");
        } else {
            if (plugin.isOnlineMode()) {
                threadPool.submit(new PlayerChecker(name));
            }
        }
    }

    @EventHandler(order = Order.LATEST_IGNORE_CANCELLED)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (pm.isMuted(name)) {
            event.setCancelled(true);
        }
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    /** Checks global bans on players, and kicks accordingly */
    public final class PlayerChecker implements Runnable {
        private final String player;

        private PlayerChecker(String player) {
            this.player = player;
        }

        /** Cycles through the queue and checks players for global bans */
        @Override
        public void run() {
            if (player != null) {
                try {
                    if (APIRequestUtil.isPermaGlobalBanned(arh, player)) {
                        Player p = plugin.getEngine().getPlayer(player, true);
                        if (p != null) {
                            p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                            plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                        }
                    }
                } catch (DataRetrievalException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
