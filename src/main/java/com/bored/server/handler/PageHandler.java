package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PageHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        var pageContainer = env.getPageContainer();
        if (pageContainer.contains(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var html = pageContainer.get(uri);
            ServletUtil.write(response, html.content(), html.getContentType());
            baseRequest.setHandled(true);
        }
    }
}
