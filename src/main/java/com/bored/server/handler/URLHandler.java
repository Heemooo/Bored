package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.setting.dialect.Props;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class URLHandler extends AbstractHandler {

    private final static Props MEDIA_TYPES = new Props("media.types.properties");

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        if (target.equals("/")) {
            target = "/index" + Bored.config().getURLSuffix();
        }
        Bored.url(target).ifPresent(context -> {
            ServletUtil.write(response, new ByteArrayInputStream(context.bytes()), context.getContentType());
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        });
    }
}
