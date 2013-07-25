package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.request.APIRequestHandler;
import com.volumetricpixels.bans.util.Utilities;

/**
 * The /vb lookup command
 */
public class VBLookupCommand extends VBCommand {
	private final APIRequestHandler handler;

	public VBLookupCommand(final VolumetricBans plugin) {
		super(plugin, "lookup");
		handler = plugin.getRequestHandler();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(final CommandSource source, final Command cmd,
			final CommandArguments args) throws CommandException {
		final String target = args.get().get(0);
		try {
			final boolean globalPermaBan = Utilities.isPermaGlobalBanned(
					handler, target);
			source.sendMessage(target);
			source.sendMessage("Is globally permabanned: " + globalPermaBan);
		} catch (final DataRetrievalException e) {
			throw new CommandException("Could not retrieve data for " + target);
		} catch (final ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /vb lookup <player>");
		}
	}
}
