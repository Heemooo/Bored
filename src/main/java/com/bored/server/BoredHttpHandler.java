package com.bored.server;

import cn.hutool.json.JSONObject;
import com.bored.Bored;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoredHttpHandler implements HttpHandler {

    static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /*
     * PS: 必须按顺序设置响应: 添加响应头, 发送响应码和内容长度, 写出响应内容, 关闭处理器
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        service.execute(() -> {
            try {
                var uri = httpExchange.getRequestURI().toString();
                if (uri.equals("/")) {
                    uri = "/index" + Bored.config().getURLSuffix();
                }
                Bored.url(uri).ifPresent(url -> Response.writer(url.bytes(), 200, url.contentType(), httpExchange));
            } catch (Exception e) {
                Response.redirect("/404.html", httpExchange);
            }
        });
    }

    public static void response404(HttpExchange httpExchange) {
        var obj = new JSONObject();
        obj.put("msg", "404 not found");
        // 响应内容
        byte[] respContents = obj.toString().getBytes(StandardCharsets.UTF_8);
        response(respContents, 404, "application/json", httpExchange);
    }

    private static void response200(byte[] respContents, String contentType, HttpExchange httpExchange) {
        response(respContents, 200, contentType, httpExchange);
    }

    private static void response(byte[] respContents, int httpStatus, String contentType, HttpExchange httpExchange) {
        try {
            // 设置响应头
            httpExchange.getResponseHeaders().add("Content-Type", contentType);
            // 发送响应码和内容长度
            httpExchange.sendResponseHeaders(httpStatus, respContents.length);
            // 写出响应内容
            httpExchange.getResponseBody().write(respContents);
            // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
            httpExchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
