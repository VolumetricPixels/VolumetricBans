package com.volumetricpixels.bans.command.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.punishment.Ban;
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
	public void execute(final CommandSource source, final Command cmd,
			final CommandArguments context) throws CommandException {
		try {
			List<String> allArgs = context.get();
			final String target = allArgs.get(0);
			if (target == null) {
				throw new CommandException("You must specify a target!");
			}
			final String timeStr = context.getString("t");
			int initialIndex = 1;
			if (timeStr != null) {
				initialIndex++;
			}

			String reason = context.popRemainingStrings(allArgs
					.get(initialIndex - 1));

			boolean temp = false;
			long time = -1;
			TimeUnit unit = null;

			if (timeStr != null) {
				boolean success = true;
				try {
					time = Long.parseLong(timeStr.substring(0,
							timeStr.length() - 2));
					unit = TimeUnit.parse(String.valueOf(timeStr.charAt(timeStr
							.length() - 1)));
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
				if (temp) {
					final Date date = new Date(Calendar.getInstance()
							.getTimeInMillis());
					date.setTime(date.getTime() + unit.toMillis(time));
					reason = "You are temporarily muted until "
							+ Ban.df.format(date);
				} else {
					reason = "You are muted!";
				}
			}

			plugin.getStorageHandler()
					.getMutes()
					.add(new Mute(plugin, target, reason.toString(), source
							.getName(), time));
		} catch (final ArrayIndexOutOfBoundsException e) {
			throw new CommandException(
					"Invalid syntax, /vb mute <player> [-t(ime) time] [mute reason]\n"
							+ "<> = Required argument, [] = Optional argument");
		}
	}
}
