package io.github.jinlongliao.easy.server.server.servlet;

import cn.hutool.core.collection.CollectionUtil;
import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.exception.top.EasyRestRuntimeException;
import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import io.github.jinlongliao.easy.server.IServer;
import io.github.jinlongliao.easy.server.ServerFactory;
import io.github.jinlongliao.easy.server.action.IRouter;
import io.github.jinlongliao.easy.server.proxy.ComputerArgs;
import io.github.jinlongliao.easy.server.proxy.EasyMethod;
import io.github.jinlongliao.easy.server.proxy.servlet.ServletInvocationHandler;
import io.github.jinlongliao.easy.server.server.servlet.router.ServletRouter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author liaojinlong
 * @since 2020/7/20 17:55
 */
public class ServletServer implements IServer {
    private ServerConfig serverConfig;

    /**
     * 开启服务器
     *
     * @param serverConfig
     */
    @Override
    public void start(ServerConfig serverConfig) throws Exception {
        this.serverConfig = serverConfig;
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(ServletServer.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName(serverConfig.getServerName())
                .addServlets(
                        Servlets.servlet("GeneralServlet", GeneralServlet.class)
                                .addMapping("/*"));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", manager.start());

        Undertow server = Undertow.builder()
                .addHttpListener(serverConfig.getPort(), serverConfig.getHost())
                .setHandler(path)
                .build();
        server.start();
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
        Map<Method, ServletRouter> methodIRouterMap2 = new HashMap<>(16);
        for (String key : methodCache.keySet()) {
            final List<EasyMethod> easyMethods = methodCache.get(key);
            for (EasyMethod easyMethod : easyMethods) {
                final Class aClass = easyMethod.getaClass();
                final Method method = easyMethod.getMethod();
                final List<String> urlPath = easyMethod.getUrlPath();
                List<HttpMethod> methods = easyMethod.getMethods();
                methods = CollectionUtil.isEmpty(methods) ? Arrays.asList(HttpMethod.values()) : methods;
                for (HttpMethod httpMethod : methods) {
                    ServletRouter router = null;
                    if (methodIRouterMap.containsKey(method)) {
                        router = methodIRouterMap2.get(method);
                    } else {
                        ComputerArgs computerArgs = getArgs(method);

                        final InvocationHandler invocationHandler = new ServletInvocationHandler(aClass.newInstance(),
                                method,
                                filterChain,
                                computerArgs);

                        router = (ServletRouter) Proxy.newProxyInstance(ServletRouter.class.getClassLoader(),
                                new Class[]{ServletRouter.class},
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
                if (aClass.equals(HttpServletRequest.class)) {
                    compute += 2;
                } else if (aClass.equals(HttpServletResponse.class)) {
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
        GeneralServlet.setRouter(router);
    }

    @Override
    public String getName() {
        return ServerFactory.DEFAULT;
    }
}
