package io.github.jinlongliao.easy.start.context;

import io.github.jinlongliao.easy.start.context.build.IContainer;
import io.github.jinlongliao.easy.start.reflection.Reflection;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import reactor.netty.http.server.HttpServer;

/**
 * @author liaojinlong
 * @since 2020/7/10 13:57
 */
public interface AppContext {
    /**
     * 是什
     *
     * @return
     */
    ServerConfig getServerConfig();

    /**
     * @return
     */
    Reflection getReflection();

    /**
     * @param httpServer
     */
    void setHttpServer(HttpServer httpServer);

    /**
     * @return
     */
    HttpServer getHttpServer();

    IContainer getIContainer();

    void start();
}
