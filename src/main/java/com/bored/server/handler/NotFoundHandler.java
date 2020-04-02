package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.TemplateUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class NotFoundHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var root = Bored.of().getProps().getStr("root");
        var site = Bored.of().getSite();
        var path = root + "/themes/" + site.getTheme() + "/layouts";
        var template = "404.ftl";
        var params = new HashMap<>() {{
            put("site", site);
        }};
        var content = TemplateUtil.parseTemplate(path, template, params);
        if (content.isBlank()) {
            content = "<p>404 not found<p>";
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        ServletUtil.write(response, content, "text/html;charset=utf-8");
        baseRequest.setHandled(true);
    }
}
