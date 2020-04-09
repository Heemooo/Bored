package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.db.Db;
import com.youbenzi.mdtool.tool.MDTool;
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
        var pages = Db.getPages();
        if (pages.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var page = pages.get(uri);
            page.setContent(MDTool.markdown2Html(page.getContent()));
            //var toc = AtxMarkdownToc.newInstance().charset("UTF-8").write(false).subTree(false).genTocFile(filePath);
            //page.setToc(toc.getTocLines());
            var template = page.getType() + "/" + page.getLayout() + "." + site.getLayoutSuffix();
            Map<String, Object> context = new HashMap<>() {{
                put("page", page);
            }};
            var content = env.getJetTemplateHelper().parse(template, context);
            ServletUtil.write(response, content, "text/html;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
