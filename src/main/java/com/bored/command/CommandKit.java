package com.bored.command;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 命令输入解析
 *
 * @author @https://github.com/Heemooo
 * @since 2020-3-27
 */
public class CommandKit {
    private static final Map<String, CommandHandler> COMMAND_HANDLERS = new HashMap<>();

    public static CommandKit init() {
        COMMAND_HANDLERS.put("new", new NewCommandHandler());
        COMMAND_HANDLERS.put("version", new VersionCommandHandler());
        COMMAND_HANDLERS.put("server", new ServerCommandHandler());
        COMMAND_HANDLERS.put("help", new HelpCommandHandler());
        COMMAND_HANDLERS.put("compile", new CompileCommandHandler());
        return new CommandKit();
    }

    public void parse(String[] commands) {
        if (Objects.isNull(commands) || commands.length <= 0) {
            CommandOut.nullCommand();
            return;
        }
        String command = commands[0];
        CommandHandler commandHandler = COMMAND_HANDLERS.get(command);
        commandHandler.exec(commands);
    }

    public static void main(String[] args) {

        CommandKit.init().parse(args);
    }
}