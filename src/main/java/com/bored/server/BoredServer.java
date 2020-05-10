package com.bored.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.Context;
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
        loadStatics();
        loadPages();

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

    public static void loadPages() {
        var env = Bored.env();
        var files = FileUtil.loopFiles(env.getPagePath());
        List<Page> pages = new ArrayList<>();
        for (File file : files) {
            var pageFile = parse(file);
            var page = pageFile.toPage();
            Context context = new Context();
            context.setTime(page.getDate());
            context.setTitle(page.getTitle());
            context.setUrl(page.getPermLink());
            context.setType(pageFile.getFrontMatter().getType());
            context.setLayout(pageFile.getFrontMatter().getLayout());
            URL URL = new URL();
            var fullFilePath = String.format("%s/%s/%s", env.getOutputPath(), context.getType(), pageFile.getHtmlFileName());
            URL.setFullFilePath(fullFilePath);
            URL.setContext(context);
            URL.setUri(context.getUrl());
            URL.add("page", page);
            env.getUrls().put(URL.getUri(), URL);
            pages.add(page);
            log.info("Mapping page {}", URL.getUri());
        }
        env.setPages(pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList()));
        for (int i = 0, len = env.getPages().size(); i < len; i++) {
            if (i < (len - 1)) env.getPages().get(i).setNext(env.getPages().get(i + 1));
            if (i > 0) env.getPages().get(i).setPrev(env.getPages().get(i - 1));
        }
    }

    public static PageFile parse(File file) {
        var site = Bored.env().getSiteConfig();
        var pagePath = Bored.env().getPagePath();
        var filePath = file.getPath();
        var pageFile = new PageFile(file);
        var permLink = StrUtil.removePrefix(filePath, pagePath);
        permLink = PathUtil.convertCorrectUrl(StrUtil.removeSuffix(permLink, ".md") + site.getURLSuffix());
        pageFile.setPermLink(permLink);
        return pageFile;
    }

    private static void loadStatics() {
        var root = PathUtil.convertCorrectPath(Bored.env().getThemePath());
        var path = PathUtil.convertCorrectPath(Bored.env().getStaticPath());
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var uri = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            URL url = new URL();
            url.setFilePath(file.getPath());
            url.setUri(uri);
            url.setContentType(contentType(file.getName(), file.getPath()));
            url.setContext(null);
            url.setFullFilePath(Bored.env().getOutputStaticPath());
            Bored.env().getUrls().put(uri, url);
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
