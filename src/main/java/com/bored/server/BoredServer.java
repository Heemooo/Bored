package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.db.model.Page;
import com.bored.model.CompleteEnvironment;
import com.bored.server.handler.DbHandler;
import com.bored.server.handler.NotFoundHandler;
import com.bored.server.handler.PageHandler;
import com.bored.server.handler.StaticHandler;
import com.bored.util.PageUtil;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.of().setEnv(new CompleteEnvironment());
        PageUtil pageUtil = new PageUtil();
        List<Page> pages = pageUtil.loadPages();
        Bored.of().getEnv().setPageList(pages);
        Bored.of().getEnv().setPageMap(loadPages(pages));
        Bored.of().getEnv().setStaticResources(loadStatics());

        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new DbHandler());
        handlers.addHandler(new PageHandler());
        handlers.addHandler(new StaticHandler());
        handlers.addHandler(new NotFoundHandler());
        server.setStopTimeout(300000);
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    private static Map<String, Page> loadPages(List<Page> pages) {
        Map<String, Page> pageMap = new HashMap<>();
        pages.forEach(page -> {
            if (StrUtil.isNotBlank(page.getPermLink())) {
                pageMap.put(page.getPermLink(), page);
            }
            if (StrUtil.isNotBlank(page.getUrl())) {
                pageMap.put(page.getUrl(), page);
            }
        });
        return pageMap;
    }

    private static Map<String, String> loadStatics() {
        var rootPath = PathUtil.convertCorrectPath(Bored.of().getEnv().getThemePath());
        var staticPath = PathUtil.convertCorrectPath(Bored.of().getEnv().getStaticPath());
        return loading(rootPath, staticPath);
    }

    private static Map<String, String> loading(String root, String path) {
        var resource = new HashMap<String, String>();
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var url = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            resource.put(url, file.getPath());
            log.info("Mapping static resource {}", url);
        }
        return resource;
    }
}
