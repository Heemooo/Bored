package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
public class NotFoundHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        var env = Bored.env();
        var site = env.getSiteConfig();
        var template = "404.html";
        var content = "404 not found";
        //var content = Bored.getTemplateWriter1().addModel("site", site).parseTemplate(path, template);
        var jetTemplateHelper = env.getJetTemplateHelper();
        if (jetTemplateHelper.checkTemplate(template)) {
            content = jetTemplateHelper.parse(template, new HashMap<>());
        }
        log.info("Not found {}", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        ServletUtil.write(response, content, "text/html;charset=utf-8");
        baseRequest.setHandled(true);
    }
}
