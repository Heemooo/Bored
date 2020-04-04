package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.BoredUtil;
import com.bored.util.TemplateUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        var root = Bored.of().getProps().getStr("root");
        var site = Bored.of().getSite();
        var bored = BoredUtil.of();
        bored.setPages(Bored.of().getPageList());
        var defaultPages = Bored.of().getDefaultPages();
        if (defaultPages.containsKey(uri)) {
            var paginationMap = Bored.of().getPaginationMap();
            response.setStatus(HttpServletResponse.SC_OK);
            var path = root + "/themes/" + site.getTheme() + "/layouts";
            Map<String, Object> params = new HashMap<>();
            params.put("site", site);
            params.put("bored", bored);
            var pagination = paginationMap.get("/list/1.html");
            if (Objects.isNull(pagination)) {
                pagination = paginationMap.get("/list/1");
            }
            params.put("pagination", pagination);
            //判断是否含有"pagination.data"
            var content = TemplateUtil.parseTemplate(path, defaultPages.get(uri), params);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
