package com.bored.server.handler;

import com.bored.Bored;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ArchiveHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var uri = request.getRequestURI();
        var env = Bored.of().getEnv();
        var isArchive = false;
        if (env.getSiteConfig().getEnableHtmlSuffix()) {
            isArchive = uri.equals("/archive.html");
        } else {
            isArchive = uri.equals("/archive");
        }

    }
}
