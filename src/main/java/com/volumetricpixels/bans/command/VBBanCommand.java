package com.volumetricpixels.bans.command;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Ban;
import com.volumetricpixels.bans.storage.PunishmentStorage;
import com.volumetricpixels.bans.util.TimeType;

/**
 * The /vb ban command
 */
public class VBBanCommand extends VBCommand {
	public VBBanCommand(VolumetricBans plugin) {
		super(plugin, "ban");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		VBCommandHelper cmdHelper = plugin.getCommandHelper();
		String[] args = cmdHelper.getRawArgs(context.getRawArgs());
		try {
			boolean global = false;
			boolean temp = false;
			long time = 0;
			TimeType tt = null;
			String reason = "You are banned from this server, see volumetricbans.net!";

			String target = args[0];
			for (int i = 1; i < args.length; i++) {
				String argument = args[i].toLowerCase();
				if (!global && argument.equals("t") || argument.equals("-t") || argument.equals("time") || argument.equals("-time")) {
					String timeArg = args[++i];
					long l = 0;
					try {
						l = Long.parseLong(timeArg.substring(0, timeArg.length() - 1));
					} catch (NumberFormatException e) {
						l = 6;
						timeArg = "6h";
					}
					String unit = Character.toString(timeArg.charAt(timeArg.length() - 1));
					tt = TimeType.parse(unit);
					time = tt.toMinutes(l);
					temp = true;
					continue;
				} else if (!temp && argument.equals("g") || argument.equals("-g") || argument.equals("global") || argument.equals("-global")) {
					global = true;
					continue;
				}
				reason += argument;
				if (i != args.length - 1) {
					reason += " ";
				}
			}
			PunishmentStorage sHandler = plugin.getStorageHandler();
			if (temp) {
				sHandler.getBans().add(new Ban(plugin, target, reason, source.getName(), time));
			} else {
				sHandler.getBans().add(new Ban(plugin, target, global, reason, source.getName()));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /vb ban <player> [-t(ime) time] [-g(lobal)] [ban reason]\n" + "<> = Required argument, [] = Optional argument");
		}
	}
}
