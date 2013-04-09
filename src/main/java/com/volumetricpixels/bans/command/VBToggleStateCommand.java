package com.volumetricpixels.bans.command;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;

/** The /vb togglestate command */
public class VBToggleStateCommand extends VBCommand {
    public VBToggleStateCommand(final VolumetricBans plugin) {
        super(plugin, "togglestate");
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(final CommandSource source, final Command cmd, final CommandContext context) throws CommandException {
        for (final String perm : getPermissions()) {
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
