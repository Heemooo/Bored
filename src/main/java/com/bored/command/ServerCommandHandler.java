package com.bored.command;

import com.bored.server.BoredServer;

public class ServerCommandHandler implements CommandHandler {
    @Override
    public void exec(String[] args) {
        BoredServer.start();
    }
}
