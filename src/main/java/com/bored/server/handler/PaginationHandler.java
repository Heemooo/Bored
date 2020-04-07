package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.BoredUtil;
import com.bored.util.ResourceUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PaginationHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        var paginationMap = ResourceUtil.paginationMap;
        var uri = request.getRequestURI();
        if (paginationMap.containsKey(uri)) {
      /*      var pagination = paginationMap.get(uri);
            var root = Bored.of().getProps().getStr("root");
            var site = Bored.of().getSite();
            var bored = BoredUtil.of();
            bored.setPages(Bored.of().getPageList());
            response.setStatus(HttpServletResponse.SC_OK);
            var path = root + "/themes/" + site.getTheme() + "/layouts";
            var templateWriter = Bored.of().getTemplateWriter1();
            templateWriter.addModel("site", site);
            templateWriter.addModel("bored", bored);
            templateWriter.addModel("pagination", pagination);
            var content = templateWriter.parseTemplate(path, "/list.ftl");
            baseRequest.setHandled(true);
            ServletUtil.write(response, content, "text/html;charset=utf-8");*/
        }
    }
}
