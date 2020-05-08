package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.model.Pagination;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 首页处理
 */
public class IndexHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        var isIndex = false;
        if (env.getSiteConfig().getEnableHtmlSuffix()) {
            isIndex = uri.equals("/") || uri.equals("/index.html");
        } else {
            isIndex = uri.equals("/") || uri.equals("/index");
        }
        if (isIndex) {
            var template = "index.html";
            var context = new HashMap<String, Object>() {{
                put("site", env.getSiteConfig());
                put("pages", env.getPageContainer().list());
                put("tags", env.getTags());
                put("categories", env.getCategories());
                var pagination = new Pagination();
                pagination.setTemplatePath(template);
                pagination.setUri(uri);
                put("pagination", pagination);
            }};
            var content = env.getJetTemplateHelper().parse(template, context);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
