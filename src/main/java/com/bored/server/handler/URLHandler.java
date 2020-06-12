package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@Slf4j
public class URLHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        if (Paths.checkUrl(target)) {
            var url = Paths.getUrl(target);
            if (url.equals("/")) {
                url = "/index";
            }
            Bored.url(url).ifPresent(context -> {
                ServletUtil.write(response, new ByteArrayInputStream(context.bytes()), context.contentType());
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
            });
        }
    }
}
