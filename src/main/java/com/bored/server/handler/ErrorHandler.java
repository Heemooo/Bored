package com.bored.server.handler;

import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ErrorHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/error."+ Bored.config().getURLSuffix());
    }
}
