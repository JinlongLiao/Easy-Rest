package io.github.jinlongliao.easy.server.server.servlet;

import cn.hutool.core.collection.CollectionUtil;
import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.action.IRouter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author liaojinlong
 * @since 2020/7/21 10:58
 */
public class GeneralServlet extends HttpServlet {
    private static Map<HttpMethod, Map<String, IRouter>> router;

    protected static void setRouter(Map<HttpMethod, Map<String, IRouter>> router) {
        GeneralServlet.router = router;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(HttpMethod.POST, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(HttpMethod.PUT, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(HttpMethod.DEL, req, resp);
    }

    protected void router(HttpMethod httpMethod, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String pathInfo = req.getPathInfo();
        final Map<String, IRouter> routerMap = router.get(httpMethod);
        if (CollectionUtil.isNotEmpty(routerMap)) {
            final IRouter iRouter = routerMap.get(pathInfo);
            if (iRouter != null) {
                iRouter.router(httpMethod, req, resp);
                return;
            }
        }
        resp.setStatus(404);
        resp.getWriter().write("{\"message\":\"NOT FOUND " + httpMethod.name() + ":" + pathInfo + "\"}");
    }
}
