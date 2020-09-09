package io.github.jinlongliao.easy.server.server.rtnetty;

import cn.hutool.core.collection.CollectionUtil;
import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.exception.top.EasyRestRuntimeException;
import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.common.filter.IFilter;
import io.github.jinlongliao.easy.server.proxy.netty.NettyInvocationHandler;
import io.github.jinlongliao.easy.server.proxy.ComputerArgs;
import io.github.jinlongliao.easy.server.proxy.EasyMethod;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import io.github.jinlongliao.easy.server.IServer;
import io.github.jinlongliao.easy.server.action.IRouter;
import io.github.jinlongliao.easy.server.server.rtnetty.router.NettyRouter;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author liaojinlong
 * @since 2020/7/20 18:16
 */

public class RtNettyServer implements IServer {
    private ServerConfig serverConfig;
    private HttpServer server;

    /**
     * 开启服务器
     *
     * @param serverConfig
     */
    @Override
    public void start(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.server =
                HttpServer.create()
                        .host(serverConfig.getHost())
                        .port(serverConfig.getPort());
    }

    /**
     * 构建路由
     *
     * @param routers
     * @param methodCache
     * @param methodIRouterMap
     */
    @Override
    public void buildRouter(Map<HttpMethod, Map<String, IRouter>> routers,
                            FilterChain filterChain,
                            Map<String, List<EasyMethod>> methodCache,
                            Map<Method, IRouter> methodIRouterMap) throws IllegalAccessException, InstantiationException {
        Map<Method, NettyRouter> methodIRouterMap2 = new HashMap<>(16);
        for (String key : methodCache.keySet()) {
            final List<EasyMethod> easyMethods = methodCache.get(key);
            for (EasyMethod easyMethod : easyMethods) {
                final Class aClass = easyMethod.getaClass();
                final Method method = easyMethod.getMethod();
                final List<String> urlPath = easyMethod.getUrlPath();
                List<HttpMethod> methods = easyMethod.getMethods();
                methods = CollectionUtil.isEmpty(methods) ? Arrays.asList(HttpMethod.values()) : methods;
                for (HttpMethod httpMethod : methods) {
                    NettyRouter router = null;
                    if (methodIRouterMap.containsKey(method)) {
                        router = methodIRouterMap2.get(method);
                    } else {
                        ComputerArgs computerArgs = getArgs(method);

                        final InvocationHandler invocationHandler = new NettyInvocationHandler(aClass.newInstance(),
                                method,
                                filterChain,
                                computerArgs);
                        router = (NettyRouter) Proxy.newProxyInstance(NettyRouter.class.getClassLoader(),
                                new Class[]{NettyRouter.class},
                                invocationHandler);
                        methodIRouterMap2.put(method, router);

                    }
                    for (String url : urlPath) {
                        routers.get(httpMethod).put(url, router);
                    }
                }
            }
        }
        methodIRouterMap.putAll(methodIRouterMap2);
    }

    private ComputerArgs getArgs(Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        int compute = 0;
        if (parameterTypes == null || parameterTypes.length < 1) {
        } else {
            for (Class<?> aClass : parameterTypes) {
                if (aClass.equals(HttpServerResponse.class)) {
                    compute += 2;
                } else if (aClass.equals(HttpServerRequest.class)) {
                    compute += 1;
                } else {
                    throw new EasyRestRuntimeException("参数配置 仅支持HttpServerRequest，HttpServerResponse ,不支持：" + aClass.getName());
                }
            }
        }
        return new ComputerArgs.Default(compute);
    }

    /**
     * 构建路由
     *
     * @param router
     */
    @Override
    public void addRouter(Map<HttpMethod, Map<String, IRouter>> router) {
        Consumer<? super HttpServerRoutes> routesBuilder = (routers -> {
            router.forEach((httpMethod, iRouterMap) -> {
                if (httpMethod.equals(HttpMethod.GET)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.get(url, (req, res) -> (Mono) (iRouter.router(HttpMethod.GET,
                                req,
                                res)));
                    }

                } else if (httpMethod.equals(HttpMethod.POST)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.post(url,
                                (req, res) -> (Mono) (iRouter.router(HttpMethod.POST,
                                        req,
                                        res)));
                    }

                } else if (httpMethod.equals(HttpMethod.DEL)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.delete(url,
                                (req, res) -> (Mono) (iRouter.router(HttpMethod.DEL,
                                        req,
                                        res)));
                    }

                } else if (httpMethod.equals(HttpMethod.PUT)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.put(url,
                                (req, res) -> (Mono) (iRouter.router(HttpMethod.PUT,
                                        req,
                                        res)));
                    }
                }
            });
        });

        server.route(routesBuilder)
                .bindNow().onDispose()
                .block();
    }

    @Override
    public String getName() {
        return "RT_NETTY";
    }

}
