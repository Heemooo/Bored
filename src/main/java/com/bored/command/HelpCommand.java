package com.bored.command;


import java.util.Deque;

public class HelpCommand extends Command {

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        println("  <command>   The name of the command to get help for");
        Command.displayAvailableCommands();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Display help about a command";
    }

    @Override
    public void execute(Deque<String> options) {
        if (options.isEmpty()) {
            displayUsage();
        } else {
            ensureMaxArgumentCount(options, 1);
            String commandName = options.remove();
            Command c = Command.valueOf(commandName);
            if (c == null) {
                userFailed("Unknown command " + commandName);
                return;
            }
            c.displayUsage();
        }
    }
}
