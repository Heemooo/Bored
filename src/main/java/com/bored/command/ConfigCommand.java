package com.bored.command;

import cn.hutool.core.lang.Console;

import java.util.Deque;

public class ConfigCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + "    " + this.getOptionSyntax() + " " + this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        Console.log("  <command>   Display site config");
        Command.displayAvailableCommands();
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getDescription() {
        return "Display site config";
    }

    @Override
    public void execute(Deque<String> options) {

    }
}
