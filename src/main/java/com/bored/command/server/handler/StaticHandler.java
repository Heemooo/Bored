package com.bored.command.server.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.bored.command.server.SiteConfig;
import com.bored.core.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StaticHandler extends AbstractHandler implements Mapping {

    public StaticHandler(String rootPath, SiteConfig config) {
        init(rootPath, config);
    }

    private Map<String, String> mapping;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (mapping.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            String content = "";
            if (uri.contains("css")) {
                content = "text/css; charset=utf-8";
            }
            if (uri.contains("js")) {
                content = "application/javascript; charset=utf-8";
            }
            ServletUtil.write(response, mapping.get(uri), content);
            baseRequest.setHandled(true);
        }
    }

    @Override
    public void init(String rootPath, SiteConfig config) {
        mapping = new HashMap<>();
        var staticFilePath = Bored.convertCorrectPath(rootPath + "/themes/" + config.getTheme());
        var cssPath = Bored.convertCorrectPath(staticFilePath + "/static" + "/css");
        var jsPath = Bored.convertCorrectPath(staticFilePath + "/static" + "/js");
        loading(staticFilePath, cssPath);
        loading(staticFilePath, jsPath);
    }

    private void loading(String root, String path) {
        File[] files = FileUtil.ls(path);
        for (File file : files) {
            var url = Bored.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            var reader = new FileReader(file);
            mapping.put(url, reader.readString());
            log.info("Mapping {}:", url);
        }
    }
}
