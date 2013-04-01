package com.volumetricpixels.bans.command;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.connection.APIRequestHandler;
import com.volumetricpixels.bans.exception.DataRetrievalException;
import com.volumetricpixels.bans.util.APIRequestUtil;

/** The /vb lookup command */
public class VBLookupCommand extends VBCommand {
	private APIRequestHandler handler;

	public VBLookupCommand(VolumetricBans plugin) {
		super(plugin, "lookup");

		handler = new APIRequestHandler(plugin, "players");
	}

	/** {@inheritDoc} */
	@Override
	public void processCommand(CommandSource source, Command cmd, CommandContext args) throws CommandException {
		for (String perm : getPermissions()) {
			if (!source.hasPermission(perm)) {
				throw new CommandException("You don't have permission!");
			}
		}
		String target = null;
		try {
			target = plugin.getCommandHelper().getRawArgs(args.getRawArgs())[0];
			boolean globalPermaBan = APIRequestUtil.isPermaGlobalBanned(handler, target);
			source.sendMessage(ChatStyle.GRAY, "VolumetricBans", ChatStyle.RESET, " - ", ChatStyle.GOLD, "Statistics for " + target);
			source.sendMessage(ChatStyle.GRAY, "Is globally permabanned: " + globalPermaBan);
		} catch (DataRetrievalException e) {
			throw new CommandException("Could not retrieve data for " + target);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CommandException("Invalid syntax, /vb lookup <player>");
		}
	}
}
