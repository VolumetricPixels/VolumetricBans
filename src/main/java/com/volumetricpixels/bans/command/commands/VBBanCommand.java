package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.command.VBCommandHelper;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.storage.PunishmentStorage;
import com.volumetricpixels.bans.util.TimeUnit;

/**
 * The /vb ban command
 */
public class VBBanCommand extends VBCommand {
    public VBBanCommand(final VolumetricBans plugin) {
        super(plugin, "ban");
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext context) throws CommandException {
        for (final String perm : getPermissions()) {
            if (!source.hasPermission(perm)) {
                throw new CommandException("You don't have permission!");
            }
        }

        final VBCommandHelper cmdHelper = plugin.getCommandHelper();
        final String[] args = cmdHelper.getRawArgs(context.getRawArgs());

        try {
            boolean global = false;
            boolean temp = false;
            long time = 0;
            TimeUnit tt = null;
            final StringBuilder reason = new StringBuilder();

            final String target = args[0];
            for (int i = 1; i < args.length; i++) {
                final String argument = args[i].toLowerCase();
                if (!global && argument.equals("t") || argument.equals("-t") || argument.equals("time") || argument.equals("-time")) {
                    String timeArg = args[++i];
                    long l = 0;
                    try {
                        l = Long.parseLong(timeArg.substring(0, timeArg.length() - 1));
                    } catch (final NumberFormatException e) {
                        l = 6;
                        timeArg = "6h";
                    }
                    final String unit = Character.toString(timeArg.charAt(timeArg.length() - 1));
                    tt = TimeUnit.parse(unit);
                    time = tt.toMinutes(l);
                    temp = true;
                    continue;
                } else if (!temp && argument.equals("g") || argument.equals("-g") || argument.equals("global") || argument.equals("-global")) {
                    global = true;
                    continue;
                }

                reason.append(argument);
                if (i != args.length - 1) {
                    reason.append(" ");
                }
            }

            if (reason.toString().equals("")) {
                if (!global) {
                    reason.append("You are banned from this server, see volumetricbans.net!");
                } else {
                    throw new CommandException("Global bans must have reasons!");
                }
            }

            final PunishmentStorage sHandler = plugin.getStorageHandler();
            if (temp) {
                sHandler.getBans().add(new Ban(plugin, target, reason.toString(), source.getName(), time));
            } else {
                sHandler.getBans().add(new Ban(plugin, target, global, reason.toString(), source.getName()));
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new CommandException("Invalid syntax, /vb ban <player> [-t(ime) time] [-g(lobal)] [ban reason]\n" + "<> = Required argument, [] = Optional argument");
        }
    }
}
