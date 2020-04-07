package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.ResourceUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class DefaultHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        //var root = env.getRoot();
        //var site = env.getSiteConfig();
        var defaultPages = ResourceUtil.defaultPages;
        if (defaultPages.containsKey(uri)) {
            var paginationMap = ResourceUtil.paginationMap;
            //var templateWriter = Bored.of().getTemplateWriter1();
            //var pagination = paginationMap.get("/list/1.html");
            //if (Objects.isNull(pagination)) {
            //    pagination = paginationMap.get("/list/1");
            //}
            //templateWriter.addModel("pagination", pagination);
            //判断是否含有"pagination.data"
            var content = env.getJetTemplateHelper().parse(defaultPages.get(uri), new HashMap<String, Object>() {{
                put("test", "test");
            }});
            response.setStatus(HttpServletResponse.SC_OK);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
