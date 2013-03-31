package com.volumetricpixels.bans.command;

import com.volumetricpixels.bans.VolumetricBans;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

/**
 * The /vb unban command
 */
public class VBUnbanCommand extends VBCommand {
	public VBUnbanCommand(VolumetricBans plugin) {
		super(plugin, "unban");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext args) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		VBCommandHelper cmdHelper = plugin.getCommandHelper();
		String[] arguments = cmdHelper.getRawArgs(args.getRawArgs());
		try {
			String target = arguments[0];
			plugin.getPunishmentManager().removeBansOnPlayer(target);
			source.sendMessage(ChatStyle.GRAY, "Unbanned " + target);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /unban <player>");
		}
	}
}
