package com.volumetricpixels.bans.command.commands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.command.VBCommandHelper;

/**
 * The /vb unmute command
 */
public class VBUnmuteCommand extends VBCommand {
    public VBUnmuteCommand(final VolumetricBans plugin) {
        super(plugin, "unmute");
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext args) throws CommandException {
        for (final String perm : getPermissions()) {
            if (!source.hasPermission(perm)) {
                throw new CommandException("You don't have permission!");
            }
        }

        final VBCommandHelper cmdHelper = plugin.getCommandHelper();
        final String[] arguments = cmdHelper.getRawArgs(args.getRawArgs());

        try {
            final String target = arguments[0];
            plugin.getPunishmentManager().removeMutesOnPlayer(target);
            source.sendMessage(ChatStyle.GRAY, "Unmuted " + target);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new CommandException("Invalid syntax, /unmute <player>");
        }
    }
}
