package com.volumetricpixels.bans.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.volumetricpixels.bans.shared.TimeType;
import com.volumetricpixels.bans.shared.perapi.Ban;

public class VBBukkitCommandHandler implements CommandExecutor {
    
    private VolumetricBansBukkit plugin;
    
    @Override
    public boolean onCommand(CommandSender source, Command cmd, String lbl, String[] args) {
        String name = cmd.getName().toLowerCase();
        
        boolean ban = name.equals("ban");
        boolean gban = name.equals("gban");
        boolean tban = name.equals("tban");
        boolean banreason = name.equals("banreason");
        boolean vb = name.equals("vbans");
        boolean banlist = name.equals("banlist");
        boolean kick = name.equals("kick");
        boolean unban = name.equals("unban");
        boolean mute = name.equals("mute");
        boolean unmute = name.equals("unmute");
        boolean lookup = name.equals("lookup");
        
        boolean commandIsValid = vb || ban || kick || mute || unmute || lookup || unban || banlist || banreason || gban || tban;
        
        if (!commandIsValid) {
            // We didn't register that command, how did it get here?
            return false;
        }
        
        if (vb) {
            // /vBans command
            if (args.length != 1) {
                sendVbansHelp(source);
                return true;
            }
            if (args[0].equalsIgnoreCase("status")) {
                if (plugin.perms.isAdmin(source.getName()) || !(source instanceof Player)) {
                    source.sendMessage(returnStatusMessage());
                } else {
                    source.sendMessage(plugin.noPermsMessage);
                }
            } else {
                source.sendMessage(ChatColor.RED + "Unknown subcommand of /vBans!");
            }
            return true;
        }
        
        if (ban) {
            if (!plugin.perms.canLocalBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return true;
            }
            // /ban command
            if (args.length < 1) {
                source.sendMessage(ChatColor.RED + "Usage: /ban playerName {reason='Banned!'} - " +
                    "Locally ban a player for the reason given, or 'banned' if blank!");
            } else {
                String reason = "";
                for (int i = 1; args[i] != null; i++) {
                    reason += args[i];
                    if (!reason.endsWith(" ")) {
                        reason += " ";
                    }
                }
                plugin.getPunishmentHandler().localBanPlayer(args[1], reason, source.getName());
            }
            return true;
        }
        
        if (gban) {
            if (!plugin.perms.canGlobalBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return true;
            }
            if (args.length < 2) {
                source.sendMessage(ChatColor.RED + "Usage: /gban [playerName] [reason ban be multiple words] - Globally ban player for the reason given!");
            } else {
                String reason = "";
                for (int i = 1; args[i] != null; i++) {
                    reason += args[i];
                    if (!reason.endsWith(" ")) {
                        reason += " ";
                    }
                }
                plugin.getPunishmentHandler().globalBanPlayer(args[1], reason, source.getName());
            }
        }
        
        if (tban) {
            if (!plugin.perms.canTempBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return true;
            }
            if (args.length < 3) {
                source.sendMessage(ChatColor.RED + "Usage: /tban [playerName] [timeInt] [m/d/h] {reason} - Temp ban a player!");
            } else {
                String player = args[0];
                int timeInt = Integer.parseInt(args[1]);
                TimeType tt = TimeType.parse(args[2]);
                
                String reason = null;
                for (int i = 3; args[i] != null; i++) {
                    if (i == 3) {
                        reason = args[i];
                    } else {
                        reason += args[i];
                    }
                }
                
                switch (tt) {
                    case MINUTES:
                        plugin.getPunishmentHandler().tempBanPlayer(player, reason, source.getName(), timeInt);
                        break;
                    case HOURS:
                        plugin.getPunishmentHandler().tempBanPlayer(player, reason, source.getName(), timeInt * 60);
                        break;
                    case DAYS:
                        plugin.getPunishmentHandler().tempBanPlayer(player, reason, source.getName(), timeInt * 60 * 24);
                        break;
                    default:
                        source.sendMessage(ChatColor.RED + "Unrecognized time type! Use m, h or d (minutes, hours or days)!");
                        break;
                }
            }
        }
        
        if (unban) {
            // /unban command
            if (args.length == 1) {
                if (plugin.perms.canUnban(source.getName()) || !(source instanceof Player)) {
                    plugin.getPunishmentHandler().unbanPlayer(args[0]);
                } else {
                    source.sendMessage(plugin.noPermsMessage);
                }
            } else {
                source.sendMessage(ChatColor.RED + "Usage: /unban PlayerName");
            }
            return true;
        }
        
        if (kick) {
            // /kick command
            if (args.length > 0) {
                if (args.length == 1) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        if (plugin.perms.canKick(source.getName())) {
                            plugin.getPunishmentHandler().kickPlayer(p.getName(), "Kicked!");
                        } else {
                            source.sendMessage(plugin.noPermsMessage);
                        }
                    } else {
                        source.sendMessage(ChatColor.RED + "That player is not online!");
                    }
                } else {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        List<Object> kickMessage = new ArrayList<Object>();
                        int i = 0;
                        for (String s : args) {
                            if (i > 1) {
                                kickMessage.add(s + " ");
                            }
                            i++;
                        }
                        if (plugin.perms.canKick(source.getName())) {
                            plugin.getPunishmentHandler().kickPlayer(p.getName(), kickMessage);
                        } else {
                            source.sendMessage(plugin.noPermsMessage);
                        }
                    } else {
                        source.sendMessage(ChatColor.RED + "That player is not online!");
                    }
                }
            }
            return true;
        }
        
        if (mute) {
            // /mute command
            // Handle perms at the start for mutes because the permission is always the same
            if (!(plugin.perms.canMute(source.getName())) && source instanceof Player) {
                source.sendMessage(plugin.noPermsMessage);
                return true;
            }
            if (args.length == 0) {
                source.sendMessage(ChatColor.RED + "Usage: /mute PlayerName {minutes=ConfigDefault} - Any params in {} are optional!");
            } else {
                if (args.length == 1) {
                    Player toBan = Bukkit.getPlayer(args[0]);
                    if (plugin.perms.canMute(source.getName())) {
                        if (toBan != null) {
                            plugin.getMuteHandler().mutePlayer(toBan.getName(), plugin.getVBConfig().config.getLong("Mutes.Default-Length"));
                            source.sendMessage(ChatColor.GRAY + "Muted: " + toBan.getName() + "!");
                        } else {
                            source.sendMessage(ChatColor.RED + "That player is offline!");
                        }
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                } else if (args.length == 2) {
                    Player toBan = Bukkit.getPlayer(args[0]);
                    if (plugin.perms.canMute(source.getName())) {
                        if (toBan != null) {
                            try {
                                plugin.getMuteHandler().mutePlayer(toBan.getName(), Long.parseLong(args[1]));
                                source.sendMessage(ChatColor.GRAY + "Muted: " + toBan.getName() + "!");
                            } catch (NumberFormatException e) {
                                source.sendMessage(ChatColor.RED + args[1] + "Is not a valid number!");
                            }
                        } else {
                            source.sendMessage(ChatColor.RED + "That player is offline!");
                        }
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                }
            }
            return true;
        }
        
        if (unmute) {
            if (plugin.perms.canUnmute(source.getName())) {
                if (args[0] != null) {
                    plugin.getMuteHandler().unmutePlayer(args[0]);
                } else {
                    source.sendMessage(ChatColor.RED + "Correct Usage: /UnMute PlayerName");
                }
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
        }
        
        if (lookup) {
            // /lookup command
            // TODO: Lookups
            return true;
        }
        
        if (banlist) {
            // /banlist command
            // TODO: Pages
            if (plugin.perms.canViewBans(source.getName()) || !(source instanceof Player)) {
                String message = null;
                message = ChatColor.AQUA + "";
                message += "Banned Players:\n";
                message += ChatColor.BLUE + "";
                for (Ban b : plugin.getLocalBanHandler().getBans()) {
                    message += b.getPlayer() + " - " + b.getReason() + "\n";
                }
                source.sendMessage(message);
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
            return true;
        }
        
        if (banreason) {
            // /banreason command
            if (plugin.perms.canViewBans(source.getName()) || !(source instanceof Player)) {
                if (args.length > 0) {
                    source.sendMessage(plugin.getLocalBanHandler().getBanReason(args[0]));
                } else {
                    source.sendMessage(ChatColor.RED + "Usage: /BanReason PlayerName");
                }
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
            return true;
        }
        
        return false;
    }
    
    private void sendVbansHelp(CommandSender s) {
        // Sends a CommandSender help for /vBans
        String message= null;
        message = ChatColor.GREEN + "";
        message += "VolumetricBans v" + plugin.getDescription().getVersion() + "\n";
        message += ChatColor.BLUE + "";
        message += "/vBans - Displays this message\n";
        message += "/vBans status - Check the status of the VolumetricBans server\n";
        message += "/vBans flags - Display possible flags for /ban";
        s.sendMessage(message);
    }
    
    private String returnStatusMessage() {
        if (serverOnline()) {
            return ChatColor.LIGHT_PURPLE + "The server is online!";
        } else {
            return ChatColor.DARK_RED + "The server is down!";
        }
    }
    
    private boolean serverOnline() {
        return plugin.getMainDataRetriever().isVBServerOnline();
    }
    
}
