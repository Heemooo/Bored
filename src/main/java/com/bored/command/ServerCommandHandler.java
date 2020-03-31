package com.bored.command;

import com.bored.command.server.BoredServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerCommandHandler implements CommandHandler {
    @Override
    public void execute(String command, String value) {
        if ("server".equals(command)) {
            BoredServer.start(null);
            return;
        }
        if ("server port".equals(command)) {
            try {
                int port = Integer.parseInt(value);
                BoredServer.start(port);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Bored server port [port].");
                log.error("port is number.");
            }

        }
    }
}
