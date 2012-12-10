package com.volumetricpixels.bans.spout;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.ChatSection;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.crossapi.TimeType;
import com.volumetricpixels.bans.crossapi.perapi.Ban;

/**
 * Deals with Commands for VolumetricBans
 * @author DziNeIT
 */
public class VBSpoutCommandHandler implements CommandExecutor {

    private VolumetricBansSpout plugin;

    public VBSpoutCommandHandler(VolumetricBansSpout plugin) {
        this.plugin = plugin;
    }

    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        String name = cmd.getPreferredName();
        List<ChatSection> csl = context.getRawArgs();
        String[] args = new String[csl.size()];

        for (int i = 0; i < csl.size(); i++) {
            args[i] = csl.get(i).getPlainString();
        }

        boolean vb = name.equalsIgnoreCase("vbans");
        boolean ban = name.equalsIgnoreCase("ban");
        boolean gban = name.equalsIgnoreCase("gban");
        boolean tban = name.equalsIgnoreCase("tban");
        boolean kick = name.equalsIgnoreCase("kick");
        boolean mute = name.equalsIgnoreCase("mute");
        boolean unmute = name.equalsIgnoreCase("unmute");
        boolean lookup = name.equalsIgnoreCase("lookup");
        boolean unban = name.equalsIgnoreCase("unban");
        boolean banlist = name.equalsIgnoreCase("banlist");
        boolean banreason = name.equalsIgnoreCase("banreason");

        boolean commandIsValid = vb || ban || kick || mute || unmute || lookup || unban || banlist || banreason || gban || tban;

        if (!commandIsValid) {
            // We didn't register that command, how did it get here?
            return;
        }

        if (vb) {
            // /vBans command
            if (args.length != 1) {
                sendVbansHelp(source);
                return;
            }
            if (args[0].equalsIgnoreCase("status")) {
                if (plugin.perms.isAdmin(source.getName()) || !(source instanceof Player)) {
                    source.sendMessage(returnStatusMessage());
                } else {
                    source.sendMessage(plugin.noPermsMessage);
                }
            } else {
                source.sendMessage(ChatStyle.RED, "Unknown subcommand of /vBans!");
            }
            return;
        }

        if (ban) {
            if (!plugin.perms.canLocalBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return;
            }
            // /ban command
            if (args.length < 1) {
                source.sendMessage(ChatStyle.RED, "Usage: /ban playerName {reason='Banned!'} - " +
                        "Locally ban a player for the reason given, or 'banned' if blank!");
            } else {
                String reason = "";
                for (int i = 1; args[i] != null; i++) {
                    reason += args[i];
                    if (!reason.endsWith(" ")) {
                        reason += " ";
                    }
                }
                plugin.punishments.localBanPlayer(args[1], reason, source.getName());
            }
            return;
        }

        if (gban) {
            if (!plugin.perms.canGlobalBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return;
            }
            if (args.length < 2) {
                source.sendMessage(ChatStyle.RED, "Usage: /gban [playerName] [reason ban be multiple words] - Globally ban player for the reason given!");
            } else {
                String reason = "";
                for (int i = 1; args[i] != null; i++) {
                    reason += args[i];
                    if (!reason.endsWith(" ")) {
                        reason += " ";
                    }
                }
                plugin.punishments.globalBanPlayer(args[1], reason, source.getName());
            }
        }

        if (tban) {
            if (!plugin.perms.canTempBan(source.getName()) || !(source instanceof Player)) {
                source.sendMessage(plugin.noPermsMessage);
                return;
            }
            if (args.length < 3) {
                source.sendMessage(ChatStyle.RED, "Usage: /tban [playerName] [timeInt] [m/d/h] {reason} - Temp ban a player!");
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
                        plugin.punishments.tempBanPlayer(player, reason, source.getName(), timeInt);
                        break;
                    case HOURS:
                        plugin.punishments.tempBanPlayer(player, reason, source.getName(), timeInt * 60);
                        break;
                    case DAYS:
                        plugin.punishments.tempBanPlayer(player, reason, source.getName(), timeInt * 60 * 24);
                        break;
                    default:
                        source.sendMessage(ChatStyle.RED, "Unrecognized time type! Use m, h or d (minutes, hours or days)!");
                        break;
                }
            }
        }

        if (unban) {
            // /unban command
            if (args.length == 1) {
                if (plugin.perms.canUnban(source.getName()) || !(source instanceof Player)) {
                    plugin.punishments.unbanPlayer(args[0]);
                } else {
                    source.sendMessage(plugin.noPermsMessage);
                }
            } else {
                source.sendMessage(ChatStyle.RED, "Usage: /unban PlayerName");
            }
            return;
        }

        if (kick) {
            // /kick command
            if (args.length > 0) {
                if (args.length == 1) {
                    Player p = ((Server) Spout.getEngine()).getPlayer(args[0], false);
                    if (p != null) {
                        if (plugin.perms.canKick(source.getName())) {
                            plugin.punishments.kickPlayer(p.getName(), "Kicked!");
                        } else {
                            source.sendMessage(plugin.noPermsMessage);
                        }
                    } else {
                        source.sendMessage(ChatStyle.RED, "That player is not online!");
                    }
                } else {
                    Player p = ((Server) Spout.getEngine()).getPlayer(args[0], false);
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
                            plugin.punishments.kickPlayer(p.getName(), kickMessage);
                        } else {
                            source.sendMessage(plugin.noPermsMessage);
                        }
                    } else {
                        source.sendMessage(ChatStyle.RED, "That player is not online!");
                    }
                }
            }
            return;
        }

        if (mute) {
            // /mute command
            // Handle perms at the start for mutes because the permission is always the same
            if (!(plugin.perms.canMute(source.getName())) && source instanceof Player) {
                source.sendMessage(plugin.noPermsMessage);
                return;
            }
            if (args.length == 0) {
                source.sendMessage(ChatStyle.RED, "Usage: /mute PlayerName {minutes=ConfigDefault} - Any params in {} are optional!");
            } else {
                if (args.length == 1) {
                    Player toBan = ((Server) Spout.getEngine()).getPlayer(args[0], true);
                    if (plugin.perms.canMute(source.getName())) {
                        if (toBan != null) {
                            plugin.mutes.mutePlayer(toBan.getName(), plugin.getVBConfig().getNode("Mutes.Default-Length").getLong());
                            source.sendMessage(ChatStyle.GRAY, "Muted: " + toBan.getName() + "!");
                        } else {
                            source.sendMessage(ChatStyle.RED, "That player is offline!");
                        }
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                } else if (args.length == 2) {
                    Player toBan = ((Server) Spout.getEngine()).getPlayer(args[0], true);
                    if (plugin.perms.canMute(source.getName())) {
                        if (toBan != null) {
                            try {
                                plugin.mutes.mutePlayer(toBan.getName(), Long.parseLong(args[1]));
                                source.sendMessage(ChatStyle.GRAY, "Muted: " + toBan.getName() + "!");
                            } catch (NumberFormatException e) {
                                source.sendMessage(ChatStyle.RED, args[1], "Is not a valid number!");
                            }
                        } else {
                            source.sendMessage(ChatStyle.RED, "That player is offline!");
                        }
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                }
            }
            return;
        }

        if (unmute) {
            if (plugin.perms.canUnmute(source.getName())) {
                if (args[0] != null) {
                    plugin.mutes.unmutePlayer(args[0]);
                } else {
                    source.sendMessage(ChatStyle.RED, "Correct Usage: /UnMute PlayerName");
                }
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
        }

        if (lookup) {
            // /lookup command
            // TODO: Lookups
            return;
        }

        if (banlist) {
            // /banlist command
            // TODO: Pages
            if (plugin.perms.canViewBans(source.getName()) || !(source instanceof Player)) {
                List<Object> message = new ArrayList<Object>();
                message.add(ChatStyle.CYAN);
                message.add("Banned Players:\n");
                message.add(ChatStyle.BLUE);
                for (Ban b : plugin.bans.getBans()) {
                    message.add(b.getPlayer() + " - " + b.getReason() + "\n");
                }
                source.sendMessage(message);
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
            return;
        }

        if (banreason) {
            // /banreason command
            if (plugin.perms.canViewBans(source.getName()) || !(source instanceof Player)) {
                if (args.length > 0) {
                    source.sendMessage(plugin.bans.getBanReason(args[0]));
                } else {
                    source.sendMessage(ChatStyle.RED, "Usage: /BanReason PlayerName");
                }
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
            return;
        }
    }

    private void sendVbansHelp(CommandSource s) {
        // Sends a CommandSource help for /vBans
        List<Object> message = new ArrayList<Object>();
        message.add(ChatStyle.BRIGHT_GREEN);
        message.add("VolumetricBans v" + plugin.getDescription().getVersion() + "\n");
        message.add(ChatStyle.BLUE);
        message.add("/vBans - Displays this message");
        message.add("/vBans status - Check the status of the VolumetricBans server");
        message.add("/vBans flags - Display possible flags for /ban");
        s.sendMessage(message);
    }

    private Object[] returnStatusMessage() {
        if (serverOnline()) {
            return new Object[] { ChatStyle.PURPLE, "The server is online!" };
        } else {
            return new Object[] { ChatStyle.DARK_RED, "The server is down!" };
        }
    }

    private boolean serverOnline() {
        return plugin.mainDataRetriever.isVBServerOnline();
    }

}
