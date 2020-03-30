package com.bored.command.server;

import com.bored.command.Command;

public class ServerCommand implements Command {
    @Override
    public void exec(String[] args) {
        BoredServer.start();
    }
}
