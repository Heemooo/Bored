package com.bored.server;

import com.bored.core.Bored;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

public class BoredServer {

    @SneakyThrows
    public static void start() {
        Server server = new Server(Bored.PORT);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new UrlHandler());
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }
}
