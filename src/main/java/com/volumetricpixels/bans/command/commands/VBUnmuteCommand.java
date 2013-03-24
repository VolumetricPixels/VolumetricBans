package com.volumetricpixels.bans.command.commands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.CommandHelper;
import com.volumetricpixels.bans.command.VBCommand;

public class VBUnmuteCommand extends VBCommand {
	public VBUnmuteCommand(VolumetricBans plugin) {
		super(plugin, "unmute");
	}

	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext args) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		CommandHelper cmdHelper = plugin.getCommandHelper();
		String[] arguments = cmdHelper.getRawArgs(args.getRawArgs());
		try {
			String target = arguments[0];
			plugin.getPunishmentManager().removeMutesOnPlayer(target);
			source.sendMessage(ChatStyle.GRAY, "Unmuted " + target);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /unmute <player>");
		}
	}
}
