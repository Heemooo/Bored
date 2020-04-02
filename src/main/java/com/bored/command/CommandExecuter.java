package com.bored.command;

@FunctionalInterface
public interface CommandExecuter {
    void execute(String command,String value);
}
