package io.github.jinlongliao.easy.start.context;

import io.github.jinlongliao.easy.server.IServer;
import io.github.jinlongliao.easy.start.context.container.IContainer;
import io.github.jinlongliao.easy.start.reflection.Reflection;
import io.github.jinlongliao.easy.config.server.ServerConfig;

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
    void setHttpServer(IServer httpServer);

    /**
     * @return
     */
    IServer getHttpServer();

    IContainer getIContainer();

    void start() throws Exception;
}
