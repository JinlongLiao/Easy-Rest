package io.github.jinlongliao.easy.start.context;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.start.action.IRouter;
import io.github.jinlongliao.easy.start.context.build.Container;
import io.github.jinlongliao.easy.start.context.build.IContainer;
import io.github.jinlongliao.easy.start.reflection.Reflection;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:01
 */
public class DefaultAppServerContext implements AppContext {
    private ServerConfig serverConfig;
    private Reflection reflection;
    private HttpServer httpServer;
    private IContainer container;

    public DefaultAppServerContext(ServerConfig serverConfig, Reflection reflection) {
        this.serverConfig = serverConfig;
        this.reflection = reflection;
        container = new Container(this);
    }

    @Override
    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    /**
     * 是什
     *
     * @return
     */
    @Override
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    /**
     * @return
     */
    @Override
    public Reflection getReflection() {
        return reflection;
    }

    /**
     * @return
     */
    @Override
    public HttpServer getHttpServer() {
        return httpServer;
    }

    @Override
    public IContainer getIContainer() {
        return container;
    }

    @Override
    public void start() {
        final Map<HttpMethod, Map<String, IRouter>> router = container.getRouter();
        Consumer<? super HttpServerRoutes> routesBuilder = (routers -> {
            router.forEach((httpMethod, iRouterMap) -> {
                if (httpMethod.equals(HttpMethod.GET)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.get(url, (req, res) -> iRouter.router(HttpMethod.GET,
                                req,
                                res));
                    }

                } else if (httpMethod.equals(HttpMethod.POST)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.post(url,
                                (req, res) -> iRouter.router(HttpMethod.POST,
                                        req,
                                        res));
                    }

                } else if (httpMethod.equals(HttpMethod.DEL)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.delete(url,
                                (req, res) -> iRouter.router(HttpMethod.DEL,
                                        req,
                                        res));
                    }

                } else if (httpMethod.equals(HttpMethod.PUT)) {
                    for (String url : iRouterMap.keySet()) {
                        final IRouter iRouter = iRouterMap.get(url);
                        routers.put(url,
                                (req, res) -> iRouter.router(HttpMethod.PUT,
                                        req,
                                        res));
                    }
                }
            });
        });

        getHttpServer()
                .route(routesBuilder)
                .bindNow().onDispose()
                .block();

    }


}
