package com.bored.command.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Slf4j
public class BoredServer {
    public final static String CONTENT_TYPE = "text/html; charset=utf-8";
    public static Map<String, Page> urlMapping = new HashMap<>();

    @SneakyThrows
    public static void start(Integer port) {
        loading();
        port = Objects.isNull(port) ? Bored.port : port;
        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.addHandler(new AbstractHandler() {
            @Override
            @SneakyThrows
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
                String uri = request.getRequestURI();
                boolean isContains = urlMapping.containsKey(uri);
                if (isContains == Boolean.FALSE) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    String _404NotFount = "<p>" + request.getRequestURL() + " 404 Not Found</p>";
                    ServletUtil.write(response, _404NotFount, CONTENT_TYPE);
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    String content = urlMapping.get(uri).getContent();
                    ServletUtil.write(response, content, CONTENT_TYPE);
                }
                baseRequest.setHandled(true);
            }
        });
        //设置handler
        server.setHandler(handlers);
        //启动服务器
        server.start();
        //阻塞Jetty server的线程池，直到线程池停止
        server.join();
    }

    @SneakyThrows
    private static void loading() {
        var root = Bored.convertCorrectPath(System.getProperty("user.dir") + "/site-demo/content");
        var files = FileUtil.loopFiles(root);
        for (File file : files) {
            var filePath = file.getPath();
            var url = Bored.convertCorrectUrl(StrUtil.strip(filePath, root));
            var fileReader = new FileReader(file);
            var page = PageParser.parse(fileReader.readLines());
            page.setUrl(url);
            urlMapping.put(url, page);
            log.info("Mapping {}", url);
        }
    }
}
