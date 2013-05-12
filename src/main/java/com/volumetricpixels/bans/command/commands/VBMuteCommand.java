package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.command.VBCommandHelper;
import com.volumetricpixels.bans.punishment.Mute;
import com.volumetricpixels.bans.util.TimeUnit;

/**
 * The /vb mute command
 */
public class VBMuteCommand extends VBCommand {
    public VBMuteCommand(final VolumetricBans plugin) {
        super(plugin, "mute");
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
            long time = -1;
            TimeUnit tt = null;
            final StringBuilder reason = new StringBuilder();

            final String target = args[0];
            for (int i = 1; i < args.length; i++) {
                final String argument = args[i].toLowerCase();
                if (argument.equals("t") || argument.equals("-t") || argument.equals("time") || argument.equals("-time")) {
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
                    continue;
                }

                reason.append(argument);
                if (i != args.length - 1) {
                    reason.append(" ");
                }
            }
            plugin.getStorageHandler().getMutes().add(new Mute(plugin, target, reason.toString(), source.getName(), time));
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new CommandException("Invalid syntax, /vb mute <player> [-t(ime) time] [mute reason]\n" + "<> = Required argument, [] = Optional argument");
        }
    }
}
