package com.bored.server;

import com.bored.Bored;
import com.bored.context.Context;
import com.bored.util.Paths;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyHttpServer {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public MyHttpServer(int port) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 100);
            httpServer.createContext("/", new HttpServerHandler());
            httpServer.setExecutor(EXECUTOR_SERVICE);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class HttpServerHandler implements HttpHandler {
        private static final ChildThreadExceptionHandler exceptionHandler = new ChildThreadExceptionHandler();

        @Override
        public void handle(HttpExchange exchange) {
            Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);
            String target = exchange.getRequestURI().toString();
            if (Paths.checkUrl(target)) {
                var url = Paths.getUrl(target);
                if (url.equals("/")) {
                    url = "/index";
                }
                Bored.url(url).ifPresent(context -> write(exchange, context));
            }
        }

        static void write(HttpExchange exchange, Context context) {
            try {
                exchange.sendResponseHeaders(200, context.bytes().length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            exchange.getResponseHeaders().set("Content-Type", context.contentType());
            OutputStream os = exchange.getResponseBody();
            try (os) {
                os.write(context.bytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 当线程抛出unckecked异常时,系统会自动调用该函数,但是是在抛出异常的线程内执行
         */
        public void uncaughtException(Thread thread, Throwable exception) {
            //example,   print   stack   trace
            System.out.println(thread.getId());
            exception.printStackTrace();
        }
    }

    public static class ChildThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(String.format("handle exception in child thread. %s", e));
        }
    }
}
