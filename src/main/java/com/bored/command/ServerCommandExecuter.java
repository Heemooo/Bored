package com.bored.command;

import com.bored.Bored;
import com.bored.server.BoredServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.util.Objects;

@Slf4j
public class ServerCommandExecuter implements CommandExecuter {
    @Override
    public void execute(String command, String value) {
        int port = Objects.nonNull(value) ? Integer.parseInt(value) : Bored.of().getPort();
        if (command.endsWith("debug")) {
            LogManager.getRootLogger().setLevel(Level.DEBUG);
        }
        Bored.of().setPort(port);
        BoredServer.start();
    }
}
