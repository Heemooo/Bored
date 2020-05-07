package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Category;
import com.bored.model.CompleteEnvironment;
import com.bored.model.Page;
import com.bored.model.Tag;
import com.bored.server.handler.*;
import com.bored.util.PageUtil;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.io.File;
import java.util.List;

@Slf4j
public class BoredServer {

    @SneakyThrows
    public static void start(int port) {
        Bored.of().setEnv(new CompleteEnvironment());
        PageUtil pageUtil = new PageUtil();
        List<Page> pages = pageUtil.loadPages();
        loadPages(pages);
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
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    private static void loadPages(List<Page> pages) {
        var env = Bored.of().getEnv();
        pages.forEach(page -> {
            if (StrUtil.isNotBlank(page.getPermLink())) {
                env.getPageMap().put(page.getPermLink(), page);
            }
            if (StrUtil.isNotBlank(page.getUrl())) {
                env.getPageMap().put(page.getUrl(), page);
            }
            page.getTags().forEach(tagName -> {
                if (env.getTags().containsKey(tagName)) {
                    env.getTags().get(tagName).getPages().add(page);
                } else {
                    Tag t = new Tag();
                    t.setName(tagName);
                    t.getPages().add(page);
                    t.setUrl(String.format("/tag/%s.%s", tagName, Bored.of().getEnv().getSiteConfig().getLayoutSuffix()));
                    env.getTags().put(tagName, t);
                }
            });
            page.getCategories().forEach(categoryName -> {
                if (env.getCategories().containsKey(categoryName)) {
                    env.getCategories().get(categoryName).getPages().add(page);
                } else {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.getPages().add(page);
                    category.setUrl(String.format("/category/%s.%s", env.getCategories(), Bored.of().getEnv().getSiteConfig().getLayoutSuffix()));
                    env.getCategories().put(categoryName, category);
                }
            });
        });
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
