package com.bored.command.server;

import com.bored.command.server.handler.ImageHandler;
import com.bored.command.server.handler.NotFoundHandler;
import com.bored.command.server.handler.PageHandler;
import com.bored.command.server.handler.StaticHandler;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.util.Objects;

@Slf4j
public class BoredServer {

    private String rootPath;
    private SiteConfig siteConfig;

    public BoredServer(String rootPath) {
        this.rootPath = rootPath;
        this.siteConfig = loadSiteConfig();
    }

    @SneakyThrows
    public static void start(String rootPath, Integer port) {
        var boredServer = new BoredServer(rootPath);
        port = Objects.isNull(port) ? Bored.port : port;
        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new PageHandler(boredServer.rootPath, boredServer.siteConfig));
        handlers.addHandler(new StaticHandler(boredServer.rootPath, boredServer.siteConfig));
        handlers.addHandler(new ImageHandler(boredServer.rootPath, boredServer.siteConfig));
        handlers.addHandler(new NotFoundHandler());
        server.setStopTimeout(300000);
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    private SiteConfig loadSiteConfig() {
        return Bored.loadTomlFile(this.rootPath + "/config.toml", SiteConfig.class);
    }
}
