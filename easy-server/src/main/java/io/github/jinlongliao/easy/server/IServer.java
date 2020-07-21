package io.github.jinlongliao.easy.server;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.proxy.EasyMethod;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import io.github.jinlongliao.easy.server.action.IRouter;

import javax.servlet.ServletException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Server 顶级接口
 *
 * @author liaojinlong
 * @since 2020/7/20 17:13
 */
public interface IServer {
    /**
     * 开启服务器
     *
     * @param serverConfig
     */
    void start(ServerConfig serverConfig) throws ServletException, Exception;

    /**
     * 构建路由
     *
     * @param router
     * @param methodCache
     * @param methodIRouterMap
     */
    void buildRouter(Map<HttpMethod, Map<String, IRouter>> router, Map<String, List<EasyMethod>> methodCache, Map<Method, IRouter> methodIRouterMap) throws IllegalAccessException, InstantiationException;

    /**
     * 添加路由
     *
     * @param router
     */
    void addRouter(Map<HttpMethod, Map<String, IRouter>> router);

    /**
     * @return 唯一名称
     */
    String getName();
}
