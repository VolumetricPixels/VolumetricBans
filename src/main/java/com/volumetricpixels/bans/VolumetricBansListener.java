package com.volumetricpixels.bans;

import java.util.concurrent.BlockingQueue;
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

public class VolumetricBansListener implements Listener {
	private final VolumetricBans plugin;
	private final PlayerChecker checker;
	private final APIRequestHandler arh;
	private final PunishmentManager pm;

	protected VolumetricBansListener(VolumetricBans plugin) {
		this.plugin = plugin;
		arh = new APIRequestHandler(plugin, "players");
		pm = plugin.getPunishmentManager();
		(checker = new PlayerChecker()).start();
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
				checker.queue.add(name);
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

	protected PlayerChecker getChecker() {
		return checker;
	}

	protected class PlayerChecker extends Thread {
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

		@Override
		public void run() {
			while (!isInterrupted()) {
				String playerName = "";
				try {
					playerName = queue.take();
				} catch (Exception e) {
					continue;
				}
				if (playerName != null) {
					try {
						if (APIRequestUtil.isPermaGlobalBanned(arh, playerName)) {
							Player player = plugin.getEngine().getPlayer(playerName, true);
							if (player != null) {
								player.kick(ChatStyle.RED, "You are permanently banned from VolumetricBans servers, see volumetricbans.net!");
								plugin.getLogger().info("Kicked globally permabanned player " + playerName + "!");
							}
						}
					} catch (DataRetrievalException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}
}
