package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;

/**
 * The /vb unmute command
 */
public class VBUnmuteCommand extends VBCommand {
	public VBUnmuteCommand(final VolumetricBans plugin) {
		super(plugin, "unmute");
	}

	/** {@inheritDoc} */
	@Override
	public void execute(final CommandSource source, final Command cmd,
			final CommandArguments args) throws CommandException {
		try {
			final String target = args.getString(0);
			plugin.getPunishmentManager().removeMutesOnPlayer(target);
			source.sendMessage("Unmuted " + target);
		} catch (final ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /unmute <player>");
		}
	}
}
