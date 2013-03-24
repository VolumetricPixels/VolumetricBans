package com.volumetricpixels.bans.command;

import org.spout.api.command.Command;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.command.commands.VBBanCommand;
import com.volumetricpixels.bans.command.commands.VBHelpCommand;
import com.volumetricpixels.bans.command.commands.VBLookupCommand;
import com.volumetricpixels.bans.command.commands.VBMuteCommand;
import com.volumetricpixels.bans.command.commands.VBUnbanCommand;
import com.volumetricpixels.bans.command.commands.VBUnmuteCommand;
import com.volumetricpixels.bans.command.commands.VBVersionCommand;

public class VBCommands {
	private final VolumetricBans plugin;
	/**
	 * The name of the command.
	 */
	private final String name;

	/**
	 * C'tor
	 * 
	 * @param name
	 *            The name of the base command
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
		new VBUnbanCommand(plugin).register(cmd);
		new VBUnmuteCommand(plugin).register(cmd);
		new VBVersionCommand(plugin).register(cmd);
	}
}
