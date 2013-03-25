package com.volumetricpixels.bans.command.commands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;

public class VBToggleStateCommand extends VBCommand {
	public VBToggleStateCommand(VolumetricBans plugin) {
		super(plugin, "togglestate");
	}

	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		if (plugin.isOnlineMode()) {
			plugin.setToOfflineMode();
		} else {
			plugin.setToOnlineMode();
		}
		source.sendMessage(ChatStyle.GRAY, "VolumetricBans is now running in ", plugin.isOnlineMode() ? "online" : "offline", " mode");
	}
}
