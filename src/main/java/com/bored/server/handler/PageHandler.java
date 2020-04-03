package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.BoredUtil;
import com.bored.util.TemplateUtil;
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
        var pages = Bored.of().getPages();
        var root = Bored.of().getProps().getStr("root");
        var site = Bored.of().getSite();
        if (pages.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var page = pages.get(uri);
            var path = root + "/themes/" + site.getTheme() + "/layouts";
            var template = page.getType() + "/" + page.getLayout() + ".ftl";
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("site", site);
            var bored = BoredUtil.of();
            bored.setPages(Bored.of().getPageList());
            params.put("bored", bored);
            var content = TemplateUtil.parseTemplate(path, template, params);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
