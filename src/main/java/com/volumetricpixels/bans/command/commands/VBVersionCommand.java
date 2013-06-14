package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;

/**
 * The /vb version command
 */
public class VBVersionCommand extends VBCommand {
    public VBVersionCommand(final VolumetricBans plugin) {
        super(plugin, "version");
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments args) throws CommandException {
        source.sendMessage("Running VolumetricBans version " + plugin.getDescription().getVersion());
    }
}
