package com.volumetricpixels.bans.command;

import org.spout.api.command.Command;
import org.spout.api.command.CommandExecutor;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Represents a command that can be run in VolumetricBans. Helps out with
 * command registration
 */
public abstract class VBCommand implements CommandExecutor {
    /** The VolumetricBans plugin */
    protected final VolumetricBans plugin;

    /** The primary name of this command */
    protected String primary = null;

    /**
     * Creates a new VBCommand
     * 
     * @param primary
     *            The primary name of the command
     */
    protected VBCommand(final VolumetricBans plugin, final String primary) {
        this.plugin = plugin;
        this.primary = primary;
    }

    /**
     * Registers this command with the given parent.
     * 
     * @param parent
     *            The command to set this command as a subcommand of
     * 
     * @return A Command object representing this command
     */
    public final Command register(final Command parent) {
        if (primary == null) {
            return null; // Don't register
        }

        final Command cmd = parent.addSubCommand(plugin, primary);
        cmd.setExecutor(this);
        cmd.addAlias(getAliases());
        cmd.setPermissions(true, getPermissions());
        setupCommand(cmd);
        cmd.closeSubCommand();
        return cmd;
    }

    /**
     * Gets the aliases for this command
     * 
     * @return A String[] of aliases for this command
     */
    protected String[] getAliases() {
        return new String[0];
    }

    /**
     * Gets the permissions required to execute this command
     * 
     * @return A String[] of permissions for this command
     */
    protected String[] getPermissions() {
        return new String[] { "volumetricbans.admin." + primary };
    }

    /**
     * Sets up the command created
     * 
     * @param cmd
     *            The command to set up
     */
    public void setupCommand(final Command cmd) {
        cmd.setArgBounds(0, -1);
    }
}
