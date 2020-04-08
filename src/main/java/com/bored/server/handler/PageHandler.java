package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        var site = env.getSiteConfig();
        var pages = ResourceUtil.pages;
        if (pages.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var page = pages.get(uri);
            var template = page.getType() + "/" + page.getLayout() + "." + site.getLayoutSuffix();
            Map<String, Object> context = new HashMap<>() {{
                put("page", page);
                put("pages", ResourceUtil.pageList);
            }};
            var content = env.getJetTemplateHelper().parse(template, context);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
