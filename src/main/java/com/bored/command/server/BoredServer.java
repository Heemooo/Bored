package com.bored.command.server;

import cn.hutool.core.io.FileUtil;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class BoredServer {

    @SneakyThrows
    public static void start(Integer port) {
        port = Objects.isNull(port) ? Bored.PORT : port;
        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new UrlHandler());
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    private static void loading() {
        String root = System.getProperty("user.dir") + "/site-demo/content";
        List<File> files = FileUtil.loopFiles(root);
        for (File file : files) {
            System.out.println(file.getPath());
        }
    }

    public static void main(String[] args) {
        loading();
    }
}
