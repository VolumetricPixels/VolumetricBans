package com.volumetricpixels.bans.command;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.punishment.Mute;
import com.volumetricpixels.bans.util.TimeType;

/** The /vb mute command */
public class VBMuteCommand extends VBCommand {
    public VBMuteCommand(VolumetricBans plugin) {
        super(plugin, "mute");
    }

    /** {@inheritDoc} */
    @Override
    public void processCommand(CommandSource source, Command cmd, CommandContext context) throws CommandException {
        for (String perm : getPermissions()) {
            if (!source.hasPermission(perm)) {
                throw new CommandException("You don't have permission!");
            }
        }
        VBCommandHelper cmdHelper = plugin.getCommandHelper();
        String[] args = cmdHelper.getRawArgs(context.getRawArgs());
        try {
            long time = -1;
            TimeType tt = null;
            StringBuilder reason = new StringBuilder();

            String target = args[0];
            for (int i = 1; i < args.length; i++) {
                String argument = args[i].toLowerCase();
                if (argument.equals("t") || argument.equals("-t") || argument.equals("time") || argument.equals("-time")) {
                    String timeArg = args[++i];
                    long l = 0;
                    try {
                        l = Long.parseLong(timeArg.substring(0, timeArg.length() - 1));
                    } catch (NumberFormatException e) {
                        l = 6;
                        timeArg = "6h";
                    }
                    String unit = Character.toString(timeArg.charAt(timeArg.length() - 1));
                    tt = TimeType.parse(unit);
                    time = tt.toMinutes(l);
                    continue;
                }
                reason.append(argument);
                if (i != args.length - 1) {
                    reason.append(" ");
                }
            }
            plugin.getStorageHandler().getMutes().add(new Mute(plugin, target, reason.toString(), source.getName(), time));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandException("Invalid syntax, /vb mute <player> [-t(ime) time] [mute reason]\n" + "<> = Required argument, [] = Optional argument");
        }
    }
}
