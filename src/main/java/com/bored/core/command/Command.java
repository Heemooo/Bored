package com.bored.core.command;

import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.List;

@Slf4j
public abstract class Command {

    private final static List<Command> COMMANDS = createCommand();

    private static List<Command> createCommand() {
        return List.of(new NewCommand(), new ServerCommand(), new VersionCommand(), new HelpCommand(), new PublishCommand());
    }

    public static List<Command> getCommands() {
        return COMMANDS;
    }

    void displayUsage() {
        log.info("Usage: Bored command " + getName() + " " + getOptionSyntax());
        displayOptionUsage();
    }

    public static void displayHelp() {
        log.info("Usage: Bored help <command> [<options>]");
        displayAvailableCommands();
    }

    static void displayAvailableCommands() {
        log.info("Available commands are:");
        for (Command c : COMMANDS) {
            log.info(c.outHelp());
        }
    }

    abstract public String outHelp();

    abstract public String getOptionSyntax();

    abstract public void displayOptionUsage();

    abstract public String getName();

    abstract public String getDescription();

    public abstract void execute(Deque<String> options);

    final protected void ensureMaxArgumentCount(Deque<String> options, int maxArgumentCount) {
        if (options.size() > maxArgumentCount) {
            userFailed("Too many arguments");
        }
    }

    final protected void ensureMinArgumentCount(Deque<String> options, int minArgumentCount) {
        if (options.size() < minArgumentCount) {
            userFailed("Too few arguments");
        }
    }

    final protected void userFailed(String message) {
        log.info(message);
        displayUsage();
        throw new IllegalArgumentException(message);
    }

    public static Command valueOf(String commandName) {
        for (Command command : COMMANDS) {
            if (command.getName().equals(commandName)) {
                return command;
            }
        }
        return null;
    }

}
