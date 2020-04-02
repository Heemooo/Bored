package com.bored.server.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class StaticHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        response.setStatus(HttpServletResponse.SC_OK);
        var statics = Bored.of().getStatics();
        if (statics.containsKey(uri)) {
            String contentType = "application/octet-stream";
            String filePath = statics.get(uri);
            if (uri.endsWith("css")) {
                contentType = "text/css; charset=utf-8";
            } else if (uri.endsWith("js")) {
                contentType = "application/javascript; charset=utf-8";
            } else if (uri.contains("images")) {
                contentType = FileUtil.getMimeType(filePath);
            }
            var reader = new FileReader(filePath);
            ServletUtil.write(response, reader.getInputStream(), contentType);
            baseRequest.setHandled(true);
        }
    }
}
