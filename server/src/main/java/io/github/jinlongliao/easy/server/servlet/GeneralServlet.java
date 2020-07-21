package io.github.jinlongliao.easy.server.servlet;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.action.IRouter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GeneralServlet extends HttpServlet {
    private static Map<HttpMethod, Map<String, IRouter>> router;

    protected static void setRouter(Map<HttpMethod, Map<String, IRouter>> router) {
        GeneralServlet.router = router;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        final Map<String, IRouter> routerMap = router.get(HttpMethod.GET);
        final IRouter iRouter = routerMap.get(pathInfo);
        iRouter.router(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
