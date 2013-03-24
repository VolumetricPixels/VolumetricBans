package com.volumetricpixels.bans.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.spout.api.chat.ChatSection;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;

public class CommandHelper {
	private final int COMMANDS_PER_PAGE = 7;
	private final List<String> vbansSubs;

	public CommandHelper(VolumetricBans plugin) {
		vbansSubs = new LinkedList<String>();
		for (String s : plugin.getEngine().getRootCommand().getChild("vbans").getChildNames()) {
			vbansSubs.add(s);
		}
	}

	public void sendVBHelp(CommandSource sender, String[] args) throws CommandException {
		int numCommands = vbansSubs.size();
		int pages = (int) Math.ceil(numCommands / COMMANDS_PER_PAGE);
		int page = 1;

		if (args.length > 0) {
			try {
				page = Integer.parseInt(args[0]);
				if (page > pages) {
					throw new CommandException("Invalid page number!");
				}
			} catch (NumberFormatException e) {
				throw new CommandException("Invalid page number!");
			}
		}

		if (pages > 1) {
			sender.sendMessage(ChatStyle.GOLD + "[Commands - Page 1/" + pages + "]");
		} else {
			sender.sendMessage(ChatStyle.GOLD + "[Commands]");
		}
		int orig = 7 * (page - 1);
		for (int cmdNo = orig; cmdNo < orig + 7; cmdNo++) {
			List<Object> list = new ArrayList<Object>();
			list.add(ChatStyle.CYAN);
			list.add("/vb " + vbansSubs.get(cmdNo));
			sender.sendMessage(list);
		}
	}

	public String[] getRawArgs(List<ChatSection> chatSections) {
		String[] array = new String[chatSections.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = chatSections.get(i).getPlainString();
		}
		return array;
	}
}
