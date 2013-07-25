package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;

/**
 * The /vb togglestate command
 */
public class VBToggleStateCommand extends VBCommand {
	public VBToggleStateCommand(final VolumetricBans plugin) {
		super(plugin, "togglestate");
	}

	/** {@inheritDoc} */
	@Override
	public void execute(final CommandSource source, final Command cmd,
			final CommandArguments context) throws CommandException {
		if (plugin.isOnlineMode()) {
			plugin.setOnlineMode(false);
		} else {
			plugin.setOnlineMode(true);
		}
		source.sendMessage("Now running in" + plugin.getOnlineModeState()
				+ " mode");
	}
}
