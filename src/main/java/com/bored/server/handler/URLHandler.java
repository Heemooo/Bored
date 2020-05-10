package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
public class URLHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        var env = Bored.env();
        var pageContainer = env.getUrls();
        if (pageContainer.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var html = pageContainer.get(uri);
            if (Objects.isNull(html.content())) {
                ServletUtil.write(response, html.getInputStream(), html.getContentType());
            } else {
                ServletUtil.write(response, html.content(), html.getContentType());
            }
            baseRequest.setHandled(true);
        }
    }
}