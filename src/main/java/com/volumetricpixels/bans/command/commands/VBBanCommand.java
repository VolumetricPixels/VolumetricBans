package com.volumetricpixels.bans.command.commands;

import java.util.Calendar;
import java.util.Date;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
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
    public void execute(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        try {
            final String target = context.getString(0);
            if (target == null) {
                throw new CommandException("You must specify a target!");
            }
            final String timeStr = context.getFlagString('t');
            final String globalStr = context.getFlagString('g');
            int initialIndex = 1;
            if (timeStr != null) {
                initialIndex++;
            }
            if (globalStr != null) {
                initialIndex++;
            }

            String reason = context.getJoinedString(initialIndex);
            final boolean global = globalStr.equals("yes") || Boolean.parseBoolean(globalStr);

            boolean temp = false;
            long time = -1;
            TimeUnit unit = null;

            if (timeStr != null) {
                boolean success = true;
                try {
                    time = Long.parseLong(timeStr.substring(0, timeStr.length() - 2));
                    unit = TimeUnit.parse(String.valueOf(timeStr.charAt(timeStr.length() - 1)));
                    time = (long) unit.toMinutes(time);
                } catch (final NumberFormatException e) {
                    success = false;
                }
                if (unit == null) {
                    success = false;
                }
                if (!success) {
                    throw new CommandException("Invalid time given!");
                } else {
                    temp = true;
                }
            }

            if (reason == null || reason.equals("")) {
                if (!global) {
                    if (temp) {
                        final Date date = new Date(Calendar.getInstance().getTimeInMillis());
                        date.setTime(date.getTime() + unit.toMillis(time));
                        reason = "You are temporarily banned from this server until " + Ban.df.format(date);
                    } else {
                        reason = "You are banned from this server, see volumetricbans.net!";
                    }
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
            throw new CommandException("Invalid syntax, /vb ban <player> [-t time] [-g] [ban reason]\n" + "<> = Required argument, [] = Optional argument");
        }
    }
}
