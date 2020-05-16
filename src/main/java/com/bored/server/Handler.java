package com.bored.server;

import com.sun.net.httpserver.HttpHandler;

public interface Handler {
    void handle(String path, HttpHandler handler);
}
