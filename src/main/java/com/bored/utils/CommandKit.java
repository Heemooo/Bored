package com.bored.utils;


import cn.hutool.core.lang.Console;
import com.bored.command.*;
import com.bored.command.compile.CompileCommand;
import com.bored.command.debug.DebugCommand;
import com.bored.command.help.HelpCommand;
import com.bored.command.news.NewCommand;
import com.bored.command.server.ServerCommand;
import com.bored.command.version.VersionCommand;

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
    private static final Map<String, Command> COMMAND_HANDLERS = new HashMap<>();

    public static CommandKit init() {
        COMMAND_HANDLERS.put("new", new NewCommand());
        COMMAND_HANDLERS.put("version", new VersionCommand());
        COMMAND_HANDLERS.put("server", new ServerCommand());
        COMMAND_HANDLERS.put("help", new HelpCommand());
        COMMAND_HANDLERS.put("compile", new CompileCommand());
        COMMAND_HANDLERS.put("debug", new DebugCommand());
        return new CommandKit();
    }

    public void parse(String[] commands) {
        if (Objects.isNull(commands) || commands.length <= 0) {
            Console.log("Error:please input command");
            Console.log("Run 'bored help' for usage.");
            return;
        }
        String command = commands[0];
        Command commandHandler = COMMAND_HANDLERS.get(command);
        commandHandler.exec(commands);
    }

    public static void main(String[] args) {

        CommandKit.init().parse(args);
    }
}