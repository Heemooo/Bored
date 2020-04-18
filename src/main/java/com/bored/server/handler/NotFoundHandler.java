package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class NotFoundHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        var env = Bored.of().getEnv();
        var site = env.getSiteConfig();
        var template = "404." + site.getLayoutSuffix();
        var content = "404 not found";
        //var content = Bored.of().getTemplateWriter1().addModel("site", site).parseTemplate(path, template);
        var jetTemplateHelper = env.getJetTemplateHelper();
        if (jetTemplateHelper.checkTemplate(template)) {
            content = jetTemplateHelper.parse(template, new HashMap<>());
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        ServletUtil.write(response, content, "text/html;charset=utf-8");
        baseRequest.setHandled(true);
    }
}
