package com.bored.server;

import com.bored.Bored;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoredHttpHandler implements HttpHandler {

    static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /*
     * PS: 必须按顺序设置响应: 添加响应头, 发送响应码和内容长度, 写出响应内容, 关闭处理器
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        var uri = httpExchange.getRequestURI().toString();
        if (uri.equals("/")) {
            uri = "/index" + Bored.config().getURLSuffix();
        }
        Bored.url(uri).ifPresent(url -> Response.writer(url.bytes(), 200, url.contentType(), httpExchange));
    }
}
