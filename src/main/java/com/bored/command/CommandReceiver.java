package com.bored.command;


import com.bored.command.compile.CompileCommand;
import com.bored.command.debug.DebugCommand;
import com.bored.command.help.HelpCommand;
import com.bored.command.news.NewCommand;
import com.bored.command.server.ServerCommand;
import com.bored.command.version.VersionCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令接收器
 * @author @https://github.com/Heemooo
 * @since 2020-3-27
 */
@Slf4j
public final class CommandReceiver {
    private static final Map<String, Command> COMMAND_HANDLERS = new HashMap<>() {{
        put("new", new NewCommand());
        put("version", new VersionCommand());
        put("server", new ServerCommand());
        put("help", new HelpCommand());
        put("compile", new CompileCommand());
        put("debug", new DebugCommand());
    }};

    public static void receive(String[] commands) {
        String command = commands[0];
        COMMAND_HANDLERS.get(command).exec(commands);
    }
}