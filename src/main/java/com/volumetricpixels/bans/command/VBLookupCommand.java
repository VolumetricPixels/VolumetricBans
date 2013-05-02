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

/**
 * The /vb lookup command
 */
public class VBLookupCommand extends VBCommand {
    private final APIRequestHandler handler;

    public VBLookupCommand(final VolumetricBans plugin) {
        super(plugin, "lookup");
        handler = plugin.getPlayerReqHandler();
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext args) throws CommandException {
        for (final String perm : getPermissions())
            if (!source.hasPermission(perm))
                throw new CommandException("You don't have permission!");

        String target = plugin.getCommandHelper().getRawArgs(args.getRawArgs())[0];
        try {
            final boolean globalPermaBan = APIRequestUtil.isPermaGlobalBanned(handler, target);
            source.sendMessage(ChatStyle.GOLD, "VolumetricBans", ChatStyle.GRAY, " - Statistics for ", ChatStyle.BRIGHT_GREEN, target);
            source.sendMessage(ChatStyle.GRAY, "Is globally permabanned: " + globalPermaBan);
        } catch (final DataRetrievalException e) {
            throw new CommandException("Could not retrieve data for " + target);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new CommandException("Invalid syntax, /vb lookup <player>");
        }
    }
}
