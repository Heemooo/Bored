package com.bored.command;

import cn.hutool.core.lang.Console;

/**
 * 命令输出
 */
public class CommandOut {

    private final static String UNKNOWN_COMMAND = "Error: unknown command '{}' for bored.";

    private final static String HELP_TIPS = "Run 'bored help' for usage.";

    public static void unknownCommand(String nowCommand) {
        Console.log(UNKNOWN_COMMAND, nowCommand);
        Console.log(HELP_TIPS);
    }

    public static void nullCommand() {
        Console.log("Error:please input command");
        Console.log(HELP_TIPS);
    }

    public static void main(String[] args) {
        unknownCommand("test");
    }

}
