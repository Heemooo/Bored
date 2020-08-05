package com.bored.server;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.util.Paths;
import fi.iki.elonen.NanoHTTPD;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpServer extends NanoHTTPD {

    public HttpServer(int port){
        super(port);
        Console.log("Server started successfully! listening port {}, click http://127.0.0.1:{}", port, port);
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String target = session.getUri();
        byte[] content = {};
        String contentType = "application/json";
        Response.Status status = Response.Status.NOT_FOUND;
        try {
            if (Paths.checkUrl(target)) {
                var url = Paths.getUrl(target);
                if (url.equals("/")) {
                    url = "/index";
                }
                var context = Bored.url(url);
                content = context.bytes();
                contentType = context.contentType();
                status = Response.Status.OK;
            }
        } catch (Throwable e) {
            StringBuilder sOut = new StringBuilder(e.getMessage());
            sOut.append("\r\n");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                sOut.append("\tat ").append(s).append("\r\n");
            }
            Console.log(sOut);
            contentType = "text/plain";
            content = sOut.toString().getBytes();
            status = Response.Status.INTERNAL_ERROR;
        }
        return newFixedLengthResponse(status, contentType, new ByteArrayInputStream(content), content.length);
    }
}