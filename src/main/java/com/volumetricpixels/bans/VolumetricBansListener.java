package com.volumetricpixels.bans;

import java.util.HashMap;
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
    private ExecutorService threadPool;
    private PlayerCheckThread checker;
    private SafePlayerCache cache;

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
        if (plugin.isPremium()) {
            threadPool = Executors.newFixedThreadPool(5);
        } else {
            checker = new PlayerCheckThread();
        }
        cache = new SafePlayerCache();
        plugin.getEngine().getScheduler().scheduleAsyncTask(plugin, cache, true);
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
                if (plugin.isPremium()) {
                    if (cache.recent.containsKey(name)) {
                        if (cache.recent.get(name)) {
                            if (player != null) {
                                player.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                                plugin.getLogger().info("Kicked globally permabanned player " + name + "!");
                            }
                        } else {
                            return;
                        }
                    } else {
                        threadPool.submit(new PlayerChecker(name));
                    }
                } else {
                    checker.queue.add(name);
                }
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

    public final class PlayerCheckThread extends Thread {
        private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    String player = queue.take();
                    try {
                        if (cache.recent.containsKey(player)) {
                            if (cache.recent.get(player)) {
                                Player p = plugin.getEngine().getPlayer(player, true);
                                if (p != null) {
                                    p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                                    plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                                }
                            }
                            continue;
                        } else {
                            if (APIRequestUtil.isPermaGlobalBanned(arh, player)) {
                                Player p = plugin.getEngine().getPlayer(player, true);
                                if (p != null) {
                                    p.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
                                    plugin.getLogger().info("Kicked globally permabanned player " + player + "!");
                                }
                                cache.recent.put(player, true);
                            } else {
                                cache.recent.put(player, false);
                            }
                        }
                    } catch (DataRetrievalException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Checks global bans on players, and kicks accordingly */
    public final class PlayerChecker implements Runnable {
        private final String player;

        private PlayerChecker(String player) {
            this.player = player;
        }

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

    public final class SafePlayerCache implements Runnable {
        private static final int SLEEP_MILLIS = ((1000 * 60) * 20);

        private final Map<String, Boolean> recent = new HashMap<String, Boolean>();

        @Override
        public void run() {
            while (true) {
                recent.clear();

                try {
                    Thread.sleep(SLEEP_MILLIS);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
