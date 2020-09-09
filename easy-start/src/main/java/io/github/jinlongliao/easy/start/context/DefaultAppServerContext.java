package io.github.jinlongliao.easy.start.context;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.IServer;
import io.github.jinlongliao.easy.server.ServerFactory;
import io.github.jinlongliao.easy.server.action.IRouter;
import io.github.jinlongliao.easy.start.context.container.Container;
import io.github.jinlongliao.easy.start.context.container.IContainer;
import io.github.jinlongliao.easy.start.reflection.Reflection;
import io.github.jinlongliao.easy.config.server.ServerConfig;

import java.util.Map;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:01
 */
public class DefaultAppServerContext implements AppContext {
    private ServerConfig serverConfig;
    private Reflection reflection;
    private IServer httpServer;
    private IContainer container;

    public DefaultAppServerContext(ServerConfig serverConfig, Reflection reflection) {
        this.serverConfig = serverConfig;
        this.reflection = reflection;
        container = new Container(this);
        httpServer = ServerFactory.getInstance().getServer(serverConfig);
    }

    @Override
    public void setHttpServer(IServer httpServer) {
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
    public IServer getHttpServer() {
        return httpServer;
    }

    @Override
    public IContainer getIContainer() {
        return container;
    }

    @Override
    public void start() throws Exception {
        httpServer.start(serverConfig);
        httpServer.addRouter(container.getRouter());
    }

}
