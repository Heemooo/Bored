package com.bored.server;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void test(String[] args) {
        try {
            /*监听端口号，只要是8888就能接收到*/
            var serverSocket = new ServerSocket(8888);
            while (true) {
                //实例化客户端，固定套路，通过服务端接受的对象，生成相应的客户端实例
                var socket = serverSocket.accept();
                handle(socket);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void handle(Socket socket) {
        //获取客户端输入流，就是请求过来的基本信息：请求头，换行符，请求体
        var bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //接受HTTP请求，并解析数据
        String requestHeader;
        int contentLength = 0;
        while ((requestHeader = bufferedReader.readLine()) != null && !requestHeader.isEmpty()) {
            System.out.println(requestHeader);
            //获得GET参数
            if (requestHeader.startsWith("GET")) {
                int begin = requestHeader.indexOf("/?") + 2;
                int end = requestHeader.indexOf("HTTP/");
                String condition = requestHeader.substring(begin, end);
                System.out.println("GET参数是：" + condition);
            }
            //获得POST参数1.获取请求内容长度
            if (requestHeader.startsWith("Content-Length")) {
                int begin = requestHeader.indexOf("Content-Lengh:") + "Content-Length:".length();
                String postParamterLength = requestHeader.substring(begin).trim();
                contentLength = Integer.parseInt(postParamterLength);
                System.out.println("POST参数长度是：" + Integer.parseInt(postParamterLength));
            }
        }
        StringBuilder sb = new StringBuilder();
        if (contentLength > 0) {
            for (int i = 0; i < contentLength; i++) {
                sb.append((char) bufferedReader.read());
            }
            System.out.println("POST参数是：" + sb.toString());
        }
        /*发送回执*/
        var out = socket.getOutputStream();
        out.write("HTTP/1.1 200 OK\n".getBytes());
        out.write("Content-type:application/json\n".getBytes());
        out.write("Content-Length:15\n".getBytes());
        out.write("{\"json\":\"test\"}\n".getBytes());
        out.flush();
        socket.close();
    }

}
