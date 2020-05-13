package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.core.URLS;
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
        var env = Bored.env();
        if (target.equals("/")) {
            target = "/index" + Bored.env().getSiteConfig().getURLSuffix();
        }
        if (URLS.contains(target)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var html = URLS.get(target);
            if (Objects.isNull(html.content())) {
                ServletUtil.write(response, html.getInputStream(), html.contentType());
            } else {
                ServletUtil.write(response, html.content(), html.contentType());
            }
            baseRequest.setHandled(true);
        }
    }
}
