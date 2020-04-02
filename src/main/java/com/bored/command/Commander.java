package com.bored.command;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
                .handler(new NewCommandExecuter()).build();
        Command help = Command.builder()
                .name("help")
                .targetParameter(List.of("new", "server", ""))
                .description("Display help document")
                .handler((command, value) -> Console.log("help")).build();
        Command version = Command.builder()
                .name("version")
                .description("Display bored version")
                .handler((command, value) -> Console.log("Bored static site generator {}", Bored.of().getVersion()))
                .build();
        Command server = Command.builder()
                .name("server")
                .targetParameter(List.of("port", ""))
                .allowAddTo("debug")
                .description("start server")
                .handler(new ServerCommandExecuter()).build();
        this.addCommand(newCommand).addCommand(help).addCommand(version).addCommand(server);
    }

    private static class CommanderHolder {
        static Commander INSTANCE = new Commander();
    }


    private Map<String, Command> commandsMap = new HashMap<>();


    private Commander addCommand(Command command) {
        List<String> targetParameter = command.getTargetParameter();
        if (Objects.isNull(targetParameter) || targetParameter.isEmpty()) {
            commandsMap.put(command.getName(), command);
        } else {
            targetParameter.forEach(child -> {
                String key = StrUtil.isEmpty(child) ? command.getName() : command.getName() + " " + child;
                commandsMap.put(key, command);
            });
        }
        return this;
    }


    private void parse(String args) {
        AtomicReference<String> longArg = new AtomicReference<>(args.trim());
        AtomicReference<Command> execute = new AtomicReference<>();
        AtomicReference<String> commandStr = new AtomicReference<>(StrUtil.EMPTY);
        AtomicReference<String> valueStr = new AtomicReference<>();
        commandsMap.forEach((name, command) -> {
            if (longArg.get().startsWith(name)) {
                AtomicReference<Boolean> hasAddToCommand = new AtomicReference<>(Boolean.FALSE);
                if (StrUtil.isNotEmpty(command.getAllowAddTo())) {
                    if (longArg.get().endsWith(command.getAllowAddTo())) {
                        hasAddToCommand.set(Boolean.TRUE);
                    }
                }
                String value = StrUtil.strip(longArg.get(), name).trim();
                if (hasAddToCommand.get()) {
                    name = name + " " + command.getAllowAddTo();
                    value = StrUtil.strip(value, command.getAllowAddTo()).trim();
                }
                if (name.length() > commandStr.get().length()) {
                    execute.set(command);
                    commandStr.set(name);
                    valueStr.set(value);
                }
            }
        });
        execute.get().getHandler().execute(commandStr.get(), valueStr.get());
    }

    public static void parse(String[] args) {
        StringBuilder argsBuilder = new StringBuilder();
        for (String arg : args) {
            argsBuilder.append(arg.trim()).append(" ");
        }
        CommanderHolder.INSTANCE.parse(argsBuilder.toString());
    }
}
