package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.container.CategoryContainer;
import com.bored.container.PageContainer;
import com.bored.container.TagContainer;
import com.bored.model.CompleteEnvironment;
import com.bored.server.handler.*;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.File;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.of().setEnv(new CompleteEnvironment());
        Bored.of().getEnv().setPageContainer(new PageContainer());
        Bored.of().getEnv().setTagContainer(new TagContainer());
        Bored.of().getEnv().setCategoryContainer(new CategoryContainer());

        loadStatics();

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new DbHandler());
        handlers.addHandler(new IndexHandler());
        handlers.addHandler(new PageHandler());
        handlers.addHandler(new StaticHandler());
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

    private static void loadStatics() {
        var root = PathUtil.convertCorrectPath(Bored.of().getEnv().getThemePath());
        var path = PathUtil.convertCorrectPath(Bored.of().getEnv().getStaticPath());
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var url = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            Bored.of().getEnv().getStaticResources().put(url, file.getPath());
            log.info("Mapping static resource {}", url);
        }
    }
}
