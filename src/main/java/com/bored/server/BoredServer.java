package com.bored.server;

import com.bored.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.loadingConfig();
        Bored.loadingFiles();
        Bored.listingConfig();
        new HttpServer(port);
    }
}
