package com.volumetricpixels.bans.command.commands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.CommandHelper;
import com.volumetricpixels.bans.command.VBCommand;

public class VBHelpCommand extends VBCommand {
	public VBHelpCommand(VolumetricBans plugin) {
		super(plugin, "help");
	}

	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		CommandHelper cmdHelper = plugin.getCommandHelper();
		cmdHelper.sendVBHelp(source, cmdHelper.getRawArgs(context.getRawArgs()));
	}
}
