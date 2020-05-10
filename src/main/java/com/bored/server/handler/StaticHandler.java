package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.bored.Bored;
import com.bored.core.URL;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
public class StaticHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        var uri = request.getRequestURI();
        response.setStatus(HttpServletResponse.SC_OK);
        var statics = Bored.of().getEnv().getStaticResources();
        if (statics.containsKey(uri)) {
            URL url = statics.get(uri);
            if(Objects.isNull(url.content())){
                ServletUtil.write(response, url.getInputStream(), url.getContentType());
            }
            baseRequest.setHandled(true);
        }
    }
}
