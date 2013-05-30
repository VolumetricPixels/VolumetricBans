package com.volumetricpixels.bans.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.spout.api.chat.ChatSection;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;

/**
 * Helps out with command responses
 */
public class VBCommandHelper {
    /** The amount of commands to display per page */
    private static final int COMMANDS_PER_PAGE = 7;

    /** Subcommands of /vbans */
    private final List<String> vbansSubs;

    /**
     * Creates a new VBCommandHelper
     * 
     * @param plugin
     *            The VolumetricBans plugin
     */
    public VBCommandHelper(final VolumetricBans plugin) {
        vbansSubs = new LinkedList<String>();

        for (final String s : plugin.getEngine().getRootCommand().getChild("vbans").getChildNames()) {
            vbansSubs.add(s);
        }
    }

    /**
     * Sends help for /vb to the sender
     * 
     * @param sender
     *            The CommandSource to send the help to
     * @param args
     *            The command arguments (if any)
     * 
     * @throws CommandException
     *             When there is a lack of good input
     */
    public void sendVBHelp(final CommandSource sender, final CommandContext args) throws CommandException {
        final int numCommands = vbansSubs.size();
        final int pages = (int) Math.ceil(numCommands / COMMANDS_PER_PAGE);
        int page = 1;

        if (args.length() > 0) {
            try {
                page = args.getInteger(0);
                if (page > pages || page < 0) {
                    throw new CommandException("Invalid page number!");
                }
            } catch (final NumberFormatException e) {
                throw new CommandException("Invalid page number!");
            }
        }

        if (pages > 1) {
            sender.sendMessage(ChatStyle.GOLD + "[Commands - Page 1/" + pages + "]");
        } else {
            sender.sendMessage(ChatStyle.GOLD + "[Commands]");
        }

        final int orig = 7 * (page - 1);
        for (int cmdNo = orig; cmdNo < orig + 7; cmdNo++) {
            final List<Object> list = new ArrayList<Object>();
            list.add(ChatStyle.CYAN);
            list.add("/vb " + vbansSubs.get(cmdNo));
            sender.sendMessage(list);
        }
    }
}
