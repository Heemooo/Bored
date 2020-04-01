package com.bored.command.server.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ObjectUtil;
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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ImageHandler extends AbstractHandler implements Mapping {

    public ImageHandler(String rootPath, SiteConfig config) {
        init(rootPath, config);
    }

    private Map<String, String> mapping;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (mapping.containsKey(uri)) {
            response.setStatus(HttpServletResponse.SC_OK);
            var filePath = mapping.get(uri);
            var reader = new FileReader(filePath);
            ServletUtil.write(response, reader.getInputStream(), "image/jpeg;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }

    @Override
    public void init(String rootPath, SiteConfig config) {
        mapping = new HashMap<>();
        var staticFilePath = Bored.convertCorrectPath(rootPath + "/themes/" + config.getTheme());
        var imagePath = Bored.convertCorrectPath(staticFilePath + "/static" + "/images");
        loading(staticFilePath, imagePath);
    }

    private void loading(String root, String path) {
        File[] files = FileUtil.ls(path);
        for (File file : files) {
            var url = Bored.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            mapping.put(url, file.getPath());
            log.info("Mapping {}:", url);
        }
    }
}
