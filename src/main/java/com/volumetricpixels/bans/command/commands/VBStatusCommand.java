package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;

/**
 * The /vb status command
 */
public class VBStatusCommand extends VBCommand {
	public VBStatusCommand(final VolumetricBans plugin) {
		super(plugin, "status");
	}

	/** {@inheritDoc} */
	@Override
	public void execute(final CommandSource source, final Command cmd,
			final CommandArguments context) throws CommandException {
		source.sendMessage("Running in " + plugin.getOnlineModeState()
				+ " mode");
	}
}
