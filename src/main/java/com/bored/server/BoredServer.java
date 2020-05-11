package com.bored.server;

import com.bored.Bored;
import com.bored.core.Loader;
import com.bored.listen.PageFilter;
import com.bored.listen.PageListener;
import com.bored.model.CompleteEnvironment;
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
        Bored.env(new CompleteEnvironment());
        Loader.start();
        startListener();

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
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

    private static void startListener() {
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000L);// 每隔1000毫秒扫描一次
        FileFilter filter = FileFilterUtils.and(new PageFilter());
        FileAlterationObserver observer = new FileAlterationObserver(Bored.env().getPagePath(), filter);
        observer.addListener(new PageListener());
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
