package com.bored.core.command;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class CommandExecute {
    public static void start(String[] args) {
        Deque<String> argList = new LinkedList<>(Arrays.asList(args));
        if (argList.isEmpty()) {
            Command.displayHelp();
            return;
        }
        String command = argList.remove();
        for (Command c : Command.getCommands()) {
            if (c.getName().equals(command)) {
                try {
                    c.execute(argList);
                } catch (IllegalArgumentException iae) {
                    return; // already handled by command
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        System.out.println("Unknown command " + command + ".");
        Command.displayHelp();
    }
}
