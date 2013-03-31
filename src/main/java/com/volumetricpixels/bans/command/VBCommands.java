package com.volumetricpixels.bans.command;

import com.volumetricpixels.bans.VolumetricBans;
import org.spout.api.command.Command;

/**
 * Registration for /vb commands
 */
public class VBCommands {
	/**
	 * The VolumetricBans plugin
	 */
	private final VolumetricBans plugin;
	/**
	 * The name of the command
	 */
	private final String name;

	/**
	 * C'tor
	 * 
	 * @param plugin
	 *            The plugin object
	 */
	public VBCommands(VolumetricBans plugin) {
		this.plugin = plugin;
		name = "vbans";
	}

	/**
	 * Registers this command with Spout.
	 * 
	 * @return The registered command.
	 */
	public final Command register() {
		Command command = plugin.getEngine().getRootCommand().addSubCommand(plugin, name);
		setup(command);
		command.closeSubCommand();
		return command;
	}

	/**
	 * Sets up the command by adding subcommands etc
	 * 
	 * @param cmd
	 *            The Command to add this command to as a subcommand
	 */
	public void setup(Command cmd) {
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
