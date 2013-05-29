package com.volumetricpixels.bans;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerLoginEvent;

import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.punishment.PunishmentManager;
import com.volumetricpixels.bans.request.APIRequestHandler;
import com.volumetricpixels.bans.util.Utilities;

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
    /** Player checker for non-premium services */
    private final PlayerCheckThread checker;
    /** Cache of safe and banned players */
    private final SafePlayerCache cache;

    /**
     * Creates a new VolumetricBansListener
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    protected VolumetricBansListener(final VolumetricBans plugin) {
        this.plugin = plugin;
        arh = plugin.getRequestHandler();
        pm = plugin.getPunishmentManager();

        if (plugin.isPremium()) {
            threadPool = Executors.newFixedThreadPool(5);
            checker = null;
        } else {
            checker = new PlayerCheckThread();
            threadPool = null;
        }
        cache = new SafePlayerCache();

        plugin.getEngine().getScheduler().scheduleAsyncTask(plugin, cache, true);
    }

    @EventHandler(order = Order.LATEST_IGNORE_CANCELLED)
    public void onPlayerJoin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        if (pm.isBanned(name)) {
            player.kick(pm.getBansForPlayer(name)[0].getReason());
            event.setAllowed(false);
            plugin.getLogger().info("Prevented banned player '" + name + "' from logging in!");
        } else if (plugin.isOnlineMode()) {
            if (plugin.isPremium()) {
                if (cache.recent.containsKey(name)) {
                    if (cache.recent.get(name) && player != null) {
                        player.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                        plugin.getLogger().info("Kicked globally permabanned player " + name + "!");
                    }
                } else {
                    threadPool.submit(new PlayerChecker(name));
                }
            } else {
                checker.queue.add(name);
            }
        }
    }

    @EventHandler(order = Order.LATEST_IGNORE_CANCELLED)
    public void onPlayerChat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        if (pm.isMuted(name)) {
            event.setCancelled(true);
        }
    }

    /**
     * Gets the thread pool used by the listener
     * 
     * @return This listener's thread pool
     */
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    /**
     * Checks players for global bans
     */
    public final class PlayerCheckThread extends Thread {
        private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    final String player = queue.take();
                    try {
                        if (cache.recent.containsKey(player)) {
                            if (cache.recent.get(player)) {
                                final Player p = plugin.getEngine().getPlayer(player, true);
                                if (p != null) {
                                    p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                                    plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                                }
                            }
                            continue;
                        } else {
                            final boolean banned = Utilities.isPermaGlobalBanned(arh, player);
                            if (banned) {
                                final Player p = plugin.getEngine().getPlayer(player, true);
                                if (p != null) {
                                    p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                                    plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                                }
                                cache.recent.put(player, true);
                            } else {
                                cache.recent.put(player, false);
                            }
                        }
                    } catch (final DataRetrievalException e) {
                        e.printStackTrace();
                    }
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks global bans on players, and kicks accordingly
     */
    public final class PlayerChecker implements Runnable {
        private final String player;

        private PlayerChecker(final String player) {
            this.player = player;
        }

        @Override
        public void run() {
            if (player != null) {
                try {
                    if (Utilities.isPermaGlobalBanned(arh, player)) {
                        final Player p = plugin.getEngine().getPlayer(player, true);
                        if (p != null) {
                            p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                            plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                        }
                    }
                } catch (final DataRetrievalException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Caches non-globally banned players for 30 mins to reduce strain on
     * servers (e.g people consistently try to join when they are banned)
     */
    public final class SafePlayerCache implements Runnable {
        // 1000 = 1s * 60 = 1m * 60 = 1hr
        private static final int SLEEP_MILLIS = 1000 * 60 * 60;

        final Map<String, Boolean> recent = new THashMap<String, Boolean>();

        @Override
        public void run() {
            while (true) {
                recent.clear();

                try {
                    Thread.sleep(SLEEP_MILLIS);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }
    }
}
