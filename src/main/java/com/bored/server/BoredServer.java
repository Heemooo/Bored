package com.bored.server;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.core.loader.Loader;
import com.bored.core.listen.ConfigFilter;
import com.bored.core.listen.ConfigListener;
import com.bored.core.model.Site;
import com.bored.server.handler.NotFoundHandler;
import com.bored.server.handler.URLHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.FileFilter;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.config(Site.instance());
        Loader.start();
        startListener();

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new URLHandler());
        handlers.addHandler(new NotFoundHandler());
        server.setStopTimeout(300000);
        // 设置handler
        server.setHandler(handlers);
        // 启动服务器
        server.start();
        Console.log("Server started successfully! listening port {}, click http://127.0.0.1:{}", port, port);
        // 阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    private static void startListener() {
        // 每隔1000毫秒扫描一次
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000L);
        FileFilter filter = FileFilterUtils.and(new ConfigFilter());
        FileAlterationObserver observer = new FileAlterationObserver(Bored.ROOT, filter);
        observer.addListener(new ConfigListener());
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
