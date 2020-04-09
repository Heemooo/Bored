package com.bored.server.handler;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.bored.db.Db;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DbHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getRequestURI().equals("/db.html")) {
            String sql = request.getParameter("sql");
            response.setStatus(HttpServletResponse.SC_OK);
            var data = JSONUtil.parseArray(Db.getPages());
            ServletUtil.write(response, data.toString(), "application/json;charset=utf-8");
            baseRequest.setHandled(true);
        }
    }
}
