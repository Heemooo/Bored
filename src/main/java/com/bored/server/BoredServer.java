package com.bored.server;

import com.bored.Bored;
import com.bored.model.Site;
import com.bored.server.handler.*;
import com.bored.util.ResourceUtil;
import com.bored.util.TomlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

@Slf4j
public class BoredServer {


    @SneakyThrows
    public static void start() {
        var port = Bored.of().getPort();
        var root = Bored.of().getProps().getStr("root");
        var site = TomlUtil.loadTomlFile(root + "/config.toml", Site.class);
        Bored.of().setSite(site);
        ResourceUtil.init();
        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new PageHandler());
        handlers.addHandler(new StaticHandler());
        //handlers.addHandler(new ImageHandler());
        handlers.addHandler(new NotFoundHandler());
        server.setStopTimeout(300000);
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }
}
