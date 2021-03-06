package com.bored.command;

import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
@Slf4j
public class VersionCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + " " + this.getOptionSyntax() + " " +this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        Console.log("  <command>   Display Bored version");
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
        Console.log("Bored runtime version v0.1.2020.5.13");
    }
}
