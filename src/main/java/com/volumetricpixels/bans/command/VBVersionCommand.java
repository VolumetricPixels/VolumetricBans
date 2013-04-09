package com.volumetricpixels.bans.command;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;

/** The /vb version command */
public class VBVersionCommand extends VBCommand {
    public VBVersionCommand(final VolumetricBans plugin) {
        super(plugin, "version");
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext args) throws CommandException {
        for (final String perm : getPermissions()) {
            if (!source.hasPermission(perm)) {
                throw new CommandException("You don't have permission!");
            }
        }
        source.sendMessage(ChatStyle.GRAY, "Running VolumetricBans version " + plugin.getDescription().getVersion());
    }
}
