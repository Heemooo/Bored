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

public class ArchiveHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        var isArchive = false;
        if (env.getSiteConfig().getEnableHtmlSuffix()) {
            isArchive = uri.equals("/archive.html");
        } else {
            isArchive = uri.equals("/archive");
        }
        if(isArchive){
            var template = "/base/archive.html";
            var context = new HashMap<String, Object>() {{
                put("site", env.getSiteConfig());
                put("tags", env.getTagContainer().list());
                put("pages", env.getPageContainer().list());
                put("categories", env.getCategoryContainer().list());
            }};
            var content = env.getJetTemplateHelper().parse(template, context);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
