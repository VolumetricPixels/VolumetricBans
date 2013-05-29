package com.volumetricpixels.bans.command.commands;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
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
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext context) throws CommandException {
        for (final String perm : getPermissions()) {
            if (!source.hasPermission(perm)) {
                throw new CommandException("You don't have permission!");
            }
        }

        source.sendMessage(ChatStyle.GRAY, "VolumetricBans is running in ", plugin.isOnlineMode() ? "online" : "offline", " mode");
    }
}