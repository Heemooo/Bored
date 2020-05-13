package com.bored.core.command;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
@Slf4j
public class HelpCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + "    " + this.getOptionSyntax() + " " +this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        log.info("  <command>   The name of the command to get help for");
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
