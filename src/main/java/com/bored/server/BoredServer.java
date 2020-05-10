package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.Context;
import com.bored.core.Loader;
import com.bored.core.Page;
import com.bored.core.URL;
import com.bored.model.CompleteEnvironment;
import com.bored.model.PageFile;
import com.bored.server.handler.*;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.setEnv(new CompleteEnvironment());
        Loader.start();

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new IndexHandler());
        handlers.addHandler(new ArchiveHandler());
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
