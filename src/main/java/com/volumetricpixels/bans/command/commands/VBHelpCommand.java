package com.volumetricpixels.bans.command.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.VBCommand;
import com.volumetricpixels.bans.command.VBCommandHelper;

/**
 * The /vb help command
 */
public class VBHelpCommand extends VBCommand {
    public VBHelpCommand(final VolumetricBans plugin) {
        super(plugin, "help");
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final CommandSource source, final Command cmd, final CommandArguments context) throws CommandException {
        final VBCommandHelper cmdHelper = plugin.getCommandHelper();
        cmdHelper.sendVBHelp(source, context);
    }
}
