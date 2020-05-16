package com.bored.server;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.core.listen.ConfigFilter;
import com.bored.core.listen.ConfigListener;
import com.bored.core.loader.Loader;
import com.bored.core.model.Site;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.FileFilter;
import java.net.InetSocketAddress;

public class BoredHttpServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.config(Site.instance());
        Loader.start();
        startListener();

        // 创建 http 服务器, 绑定本地 8080 端口
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), Runtime.getRuntime().availableProcessors());
        // 创建上下文监听, "/" 表示匹配所有 URI 请求
        assert httpServer != null;
        httpServer.createContext("/", new BoredHttpHandler());
        // 启动服务
        httpServer.start();
        Console.log("Server started successfully! listening port {}, click http://127.0.0.1:{}", port, port);
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
