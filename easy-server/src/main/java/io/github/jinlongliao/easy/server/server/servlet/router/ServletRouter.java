package io.github.jinlongliao.easy.server.server.servlet.router;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.action.IRouter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liaojinlong
 * @since 2020/7/20 18:44
 */
public interface ServletRouter extends IRouter {
    /**
     * 路由转跳
     *
     * @param method
     * @param objects
     * @return /
     */
    @Override
    default Object router(HttpMethod method, Object... objects) {
        this.servletRouter(method, ((HttpServletRequest) objects[0]), ((HttpServletResponse) objects[1]));
        return null;
    }

    /**
     * Servlet 路由
     *
     * @param method
     * @param httpServletRequest
     * @param httpServletResponse
     */
    void servletRouter(HttpMethod method, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
