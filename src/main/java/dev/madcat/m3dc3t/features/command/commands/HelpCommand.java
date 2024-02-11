package dev.madcat.m3dc3t.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            HelpCommand.sendMessage("Commands: ");
            for (Command command : M3dC3t.commandManager.getCommands()) {
                HelpCommand.sendMessage(ChatFormatting.GRAY + M3dC3t.commandManager.getPrefix() + command.getName());
            }
        }
else HelpCommand.sendMessage("Module: module, <module>, <set/reset>, <setting>, <value>");
    }
}

