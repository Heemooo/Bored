package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.URL;
import com.bored.model.CompleteEnvironment;
import com.bored.server.container.CategoryContainer;
import com.bored.server.container.PageContainer;
import com.bored.server.container.TagContainer;
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
        handlers.addHandler(new IndexHandler());
        handlers.addHandler(new ArchiveHandler());
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
            var uri = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            URL url = new URL();
            url.setFilePath(file.getPath());
            url.setUri(uri);
            url.setContentType(contentType(file.getName(), file.getPath()));
            url.setContext(null);
            url.setFullFilePath(Bored.of().getEnv().getOutputStaticPath());
            Bored.of().getEnv().getStaticResources().put(uri, url);
            log.info("Mapping static resource {}", uri);
        }
    }

    private static String contentType(String fileName, String filePath) {
        if (StrUtil.endWith(fileName, ".css")) {
            return "text/css; charset=utf-8";
        }
        if (StrUtil.endWith(fileName, ".js")) {
            return "application/javascript; charset=utf-8";
        }
        String contentType = FileUtil.getMimeType(filePath);
        if (StrUtil.isEmpty(contentType)) {
            return "application/octet-stream";
        }
        return contentType;
    }
}
