package com.bored.command.server;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlHandler extends AbstractHandler {

    @Override
    @SneakyThrows
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        boolean isContains = Context.urlMapping.containsKey(uri);
        if (isContains == Boolean.FALSE) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            String _404NotFount = "<p>" + request.getRequestURL() + " 404 Not Found</p>";
            ServletUtil.write(response, _404NotFount, Bored.CONTENT_TYPE);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            String content = Context.urlMapping.get(uri);
            ServletUtil.write(response, content, Bored.CONTENT_TYPE);
        }
        baseRequest.setHandled(true);
    }
}
