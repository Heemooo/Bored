package com.bored.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Response {

    public static void writer(byte[] respContents, int httpStatus, String contentType, HttpExchange httpExchange) {
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

    public static void redirect(String uri, HttpExchange httpExchange) {
        try {
            httpExchange.getResponseHeaders().add("Content-Type", "text/html;charset=utf8");
            httpExchange.getResponseHeaders().add("Location", uri);
            httpExchange.sendResponseHeaders(302, 0);
            httpExchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
