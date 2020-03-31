package com.bored.command;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.core.Bored;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Commander {

    public static void main(String[] args) {
        String[] c = {"help", "debug"};
        Commander.parse(c);
    }

    private Commander() {
        Command newCommand = Command.builder()
                .name("new")
                .targetParameter(List.of("theme", "site", "page"))
                .description("new something")
                .handler(new NewCommandHandler()).build();
        Command debug = Command.builder()
                .name("debug")
                .description("debug enable")
                .isEnd(true)
                .handler((command, value) -> LogManager.getRootLogger().setLevel(Level.DEBUG)).build();
        Command help = Command.builder()
                .name("help")
                .targetParameter(List.of("new", "server", ""))
                .description("Display help document")
                .isStart(true)
                .handler((command, value) -> {
                    Console.log("help");
                }).build();
        Command version = Command.builder()
                .name("version")
                .description("Display bored version")
                .isStart(true)
                .handler((command, value) -> Console.log("Bored static site generator {}", Bored.VERSION))
                .build();
        Command server = Command.builder()
                .name("server")
                .targetParameter(List.of("port", ""))
                .description("start server")
                .isStart(true)
                .handler(new ServerCommandHandler()).build();
        this.addCommand(newCommand).addCommand(debug).addCommand(help).addCommand(version).addCommand(server);
    }

    private static class CommanderHolder {
        static Commander INSTANCE = new Commander();
    }


    private List<Command> commands = new ArrayList<>();

    private Map<String, Command> commandsMap = new HashMap<>();


    private Commander addCommand(Command command) {
        commands.add(command);
        List<String> targetParameter = command.getTargetParameter();
        if (Objects.isNull(targetParameter) || targetParameter.isEmpty()) {
            commandsMap.put(command.getName(), command);
        } else {
            targetParameter.forEach(child -> {
                commandsMap.put(command.getName() + " " + child, command);
            });
        }
        return this;
    }


    private void parse(String args) {
        AtomicReference<String> longArg = new AtomicReference<>(args.trim());
        commandsMap.forEach((name, command) -> {
            if (command.isEnd() && longArg.get().endsWith(name)) {
                longArg.set(StrUtil.strip(longArg.get(), name));
                command.getHandler().execute(name, name);
            }
        });
        commandsMap.forEach((name, command) -> {
            if (command.isStart() && longArg.get().startsWith(name)) {
                command.getHandler().execute(name, StrUtil.strip(longArg.get(), name).trim());
            }
        });
    }

    public static void parse(String[] args) {
        StringBuilder argsBuilder = new StringBuilder();
        for (String arg : args) {
            argsBuilder.append(arg.trim()).append(" ");
        }
        CommanderHolder.INSTANCE.parse(argsBuilder.toString());
    }
}
