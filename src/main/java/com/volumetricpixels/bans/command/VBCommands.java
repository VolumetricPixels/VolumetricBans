package com.volumetricpixels.bans.command;

import org.spout.api.command.Command;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.commands.VBBanCommand;
import com.volumetricpixels.bans.command.commands.VBHelpCommand;
import com.volumetricpixels.bans.command.commands.VBLookupCommand;
import com.volumetricpixels.bans.command.commands.VBMuteCommand;
import com.volumetricpixels.bans.command.commands.VBStatusCommand;
import com.volumetricpixels.bans.command.commands.VBToggleStateCommand;
import com.volumetricpixels.bans.command.commands.VBUnbanCommand;
import com.volumetricpixels.bans.command.commands.VBUnmuteCommand;
import com.volumetricpixels.bans.command.commands.VBVersionCommand;

/**
 * Registration for /vb commands
 */
public class VBCommands {
	/** The VolumetricBans plugin */
	private final VolumetricBans plugin;
	/** The name of the command */
	private final String name;

	/**
	 * C'tor
	 * 
	 * @param plugin
	 *            The plugin object
	 */
	public VBCommands(final VolumetricBans plugin) {
		this.plugin = plugin;
		name = "vbans";
	}

	/**
	 * Registers this command with Spout.
	 * 
	 * @return The registered command.
	 */
	public final Command register() {
		final Command command = plugin.getEngine().getCommandManager()
				.getCommand(name);
		setup(command);
		return command;
	}

	/**
	 * Sets up the command by adding subcommands etc
	 * 
	 * @param cmd
	 *            The Command to add this command to as a subcommand
	 */
	public void setup(final Command cmd) {
		new VBBanCommand(plugin).register(cmd);
		new VBHelpCommand(plugin).register(cmd);
		new VBLookupCommand(plugin).register(cmd);
		new VBMuteCommand(plugin).register(cmd);
		new VBStatusCommand(plugin).register(cmd);
		new VBToggleStateCommand(plugin).register(cmd);
		new VBUnbanCommand(plugin).register(cmd);
		new VBUnmuteCommand(plugin).register(cmd);
		new VBVersionCommand(plugin).register(cmd);
	}
}
