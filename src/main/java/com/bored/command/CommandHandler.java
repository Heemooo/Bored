package com.bored.command;

@FunctionalInterface
public interface CommandHandler {
    void execute(String command,String value);
}
