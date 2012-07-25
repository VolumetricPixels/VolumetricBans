package com.volumetricpixels.voxelbans.spout;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.player.Player;

import com.volumetricpixels.voxelbans.spout.punishments.SpoutBan;

/**
 * Deals with Commands for VoxelBans
 * @author DziNeIT
 */
public class VBSpoutCommandHandler implements CommandExecutor {
    
    private VoxelBansSpout plugin;
    
    public VBSpoutCommandHandler(VoxelBansSpout plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        String name = cmd.getPreferredName();
        String[] args = context.getRawArgs();
        
        boolean vb = name.equalsIgnoreCase("vbans");
        boolean ban = name.equalsIgnoreCase("ban");
        boolean kick = name.equalsIgnoreCase("kick");
        boolean mute = name.equalsIgnoreCase("mute");
        boolean lookup = name.equalsIgnoreCase("lookup");
        boolean unban = name.equalsIgnoreCase("unban");
        boolean banlist = name.equalsIgnoreCase("banlist");
        boolean banreason = name.equalsIgnoreCase("banreason");
        
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
            } else if (args[0].equalsIgnoreCase("flags")) {
                List<Object> message = new ArrayList<Object>();
                message.add(ChatStyle.BLUE);
                if (plugin.perms.canGlobalBan(source.getName()) || !(source instanceof Player)) {
                    message.add("-G = Global Ban\n");
                }
                if (plugin.perms.canTempBan(source.getName()) || !(source instanceof Player)) {
                    message.add("-T=TimeValueInMinutes = Temporary Ban For The Specified Time\n");
                }
                source.sendMessage(message);
                source.sendMessage(ChatStyle.RED, "Please note flags are NOT case sensitive!");
                source.sendMessage(ChatStyle.RED, "If you think flags are unlisted here that should be, contact the admin!");
            } else {
                source.sendMessage(ChatStyle.RED, "Unknown subcommand of /vBans!");
            }
            return true;
        }
        
        if (ban) {
            // /ban command
            if (args.length < 1) {
                source.sendMessage(ChatStyle.RED, "Usage: /ban {flags} playerName {reason='Banned!'} - {ParamName=Default} = Optional!");
            } else {
                String arguments = null;
                for (int i = 1; args[i] != null; i++) {
                    arguments += args[i];
                    if (!arguments.endsWith(" ")) {
                        arguments += " ";
                    }
                }
                List<String> flags = new ArrayList<String>();
                for (String s : arguments.split(" ")) {
                    if (s.startsWith("-")) {
                        flags.add(s);
                    }
                }
                boolean global = false;
                boolean temporary = false;
                long time = 0;
                for (String s : flags) {
                    char flag = s.charAt(1);
                    if (flag == 'g' || flag == 'G') {
                        global = true;
                    } else if (flag == 't' || flag == 'T') {
                        if (!global) {
                            temporary = true;
                            if (flag == 't') {
                                try {
                                    time = Long.parseLong(s.replaceFirst("-t=", ""));
                                } catch (NumberFormatException e) {
                                    source.sendMessage(s.replaceFirst("-t=", ""), ChatStyle.RED, " is not a valid number!");
                                    return true;
                                }
                            } else {
                                try {
                                    time = Long.parseLong(s.replaceFirst("-T=", ""));
                                } catch (NumberFormatException e) {
                                    source.sendMessage(s.replaceFirst("-T=", ""), ChatStyle.RED, " is not a valid number!");
                                    return true;
                                }
                            }
                        } else {
                            source.sendMessage(ChatStyle.RED, "Global bans cannot be temporary!");
                            return true;
                        }
                    } else {
                        source.sendMessage(ChatStyle.RED, "Unrecognized Flag: " + flag);
                        return true;
                    }
                }
                String reason = "";
                if (global || temporary) {
                    for (int i = 2; args[i] != null; i++) {
                        reason += args[i];
                        if (!reason.endsWith(" ")) {
                            reason += " ";
                        }
                    }
                } else {
                    for (int i = 1; args[i] != null; i++) {
                        reason += args[i];
                        if (!reason.endsWith(" ")) {
                            reason += " ";
                        }
                    }
                }
                Player p2Ban = Spout.getEngine().getPlayer((global || temporary) ? args[2] : args[1], false);
                if (global) {
                    if (plugin.perms.canGlobalBan(source.getName()) || !(source instanceof Player)) {
                        plugin.punishments.globalBanPlayer(p2Ban.getName(), reason, source.getName());
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                } else if (temporary) {
                    if (plugin.perms.canTempBan(source.getName()) || !(source instanceof Player)) {
                        plugin.punishments.tempBanPlayer(p2Ban.getName(), reason, source.getName(), time);
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                } else {
                    if (plugin.perms.canLocalBan(source.getName()) || !(source instanceof Player)) {
                        plugin.punishments.localBanPlayer(p2Ban.getName(), reason, source.getName());
                    } else {
                        source.sendMessage(plugin.noPermsMessage);
                    }
                }
            }
            return true;
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
            return true;
        }
        
        if (kick) {
            // /kick command
            if (args.length > 0) {
                if (args.length == 1) {
                    Player p = Spout.getEngine().getPlayer(args[0], false);
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
                    Player p = Spout.getEngine().getPlayer(args[0], false);
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
                source.sendMessage(ChatStyle.RED, "Usage: /mute PlayerName {minutes=ConfigDefault} - Any params in {} are optional!");
            } else {
                if (args.length == 1) {
                    
                } else if (args.length == 2) {
                    
                }
            }
            return true;
        }
        
        if (lookup) {
            // /lookup command
            // TODO: Lookups (Requires Web Stuff)
            return true;
        }
        
        if (banlist) {
            // /banlist command
            // TODO: Pages
            if (plugin.perms.canViewBans(source.getName()) || !(source instanceof Player)) {
                List<Object> message = new ArrayList<Object>();
                message.add(ChatStyle.CYAN);
                message.add("Banned Players:\n");
                message.add(ChatStyle.BLUE);
                for (SpoutBan b : plugin.bans.getBans()) {
                    message.add(b.getPlayer() + " - " + b.getReason() + "\n");
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
                    source.sendMessage(plugin.bans.getBanReason(args[0]));
                } else {
                    source.sendMessage(ChatStyle.RED, "Usage: /BanReason PlayerName");
                }
            } else {
                source.sendMessage(plugin.noPermsMessage);
            }
            return true;
        }
        
        return false;
    }
    
    private void sendVbansHelp(CommandSource s) {
        // Sends a CommandSource help for /vBans
        List<Object> message = new ArrayList<Object>();
        message.add(ChatStyle.BRIGHT_GREEN);
        message.add("VoxelBans v" + plugin.getDescription().getVersion() + "\n");
        message.add(ChatStyle.BLUE);
        message.add("/vBans - Displays this message");
        message.add("/vBans status - Check the status of the VoxelBans server");
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
