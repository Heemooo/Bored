package com.bored.command;

import com.bored.command.server.BoredServer;
import com.bored.core.Bored;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.util.Objects;

@Slf4j
public class ServerCommandHandler implements CommandHandler {
    @Override
    public void execute(String command, String value) {
        Integer port = Objects.nonNull(value) ? Integer.parseInt(value) : Bored.port;
        if (command.endsWith("debug")) {
            LogManager.getRootLogger().setLevel(Level.DEBUG);
        }
        BoredServer.start(Bored.convertCorrectPath(System.getProperty("user.dir") + "/site-demo"), port);
    }
}
