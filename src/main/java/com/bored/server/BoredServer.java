package com.bored.server;

import com.bored.Bored;
import com.bored.core.Loader;
import com.bored.model.CompleteEnvironment;
import com.bored.server.handler.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.env(new CompleteEnvironment());
        Loader.start();

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new IndexHandler());
        handlers.addHandler(new URLHandler());
        handlers.addHandler(new NotFoundHandler());
        server.setStopTimeout(300000);
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        log.info("listening port {}, click http://127.0.0.1:{}", port, port);
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }


}
