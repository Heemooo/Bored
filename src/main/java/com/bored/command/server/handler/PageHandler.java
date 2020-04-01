package com.bored.command.server.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.bored.command.server.Page;
import com.bored.command.server.PageParser;
import com.bored.command.server.SiteConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageHandler extends AbstractHandler implements Mapping {

    public PageHandler(String rootPath, SiteConfig config) {
        init(rootPath, config);
    }

    private Map<String, Page> pageMapping;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        if (pageMapping.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            ServletUtil.write(response, pageMapping.get(uri).getContent(),"text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }

    @Override
    public void init(String rootPath, SiteConfig config) {
        pageMapping = new HashMap<>();
        PageParser pageParser = new PageParser(rootPath, config);
        var pages = pageParser.parse();
        pages.forEach(page -> page.getUrls().forEach(url -> {
            pageMapping.put(url, page);
            log.info("Mapping {}", url);
        }));
    }
}
