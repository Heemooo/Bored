package com.bored.core.command;

import java.util.Deque;

public class VersionCommand extends Command {

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        println("  <command>   Display Bored version");
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Display Bored version";
    }

    @Override
    public void execute(Deque<String> options) {
        ensureMaxArgumentCount(options, 0);
        ensureMinArgumentCount(options, 0);
        println("Bored runtime version v0.1.2020.4.8");
    }
}
