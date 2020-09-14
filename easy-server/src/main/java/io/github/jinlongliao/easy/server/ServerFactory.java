package io.github.jinlongliao.easy.server;

import io.github.jinlongliao.easy.common.exception.top.EasyRestRuntimeException;
import io.github.jinlongliao.easy.config.server.ServerConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂类
 *
 * @author liaojinlong
 * @since 2020/7/20 17:14
 */
public class ServerFactory {
    private static ServerFactory instance = null;
    private static final Map<String, IServer> SERVERS = new ConcurrentHashMap<>();
    public static final String DEFAULT = "DEFAULT";
    private IServer server;

    static {
        final ServiceLoader<IServer> load = ServiceLoader.load(IServer.class);
        for (IServer iServer : load) {
            final IServer absent = SERVERS.putIfAbsent(iServer.getName(), iServer);
            if (Objects.isNull(absent)) {
                throw new EasyRestRuntimeException(iServer.getName() + " already Register By:" + SERVERS.get(iServer.getName()).getName());
            }
        }
    }

    private ServerFactory() {
    }

    /**
     * io.github.jinlongliao.easy.server.IServer
     *
     * @return io.github.jinlongliao.easy.server.IServer
     */
    public IServer getServer(ServerConfig serverConfig) {
        if (server == null) {
            //双重检查加锁，只有在第一次实例化时，才启用同步机制，提高了性能。
            synchronized (ServerFactory.class) {
                if (server == null) {
                    server = SERVERS.get(serverConfig.getServerType());
                }
            }
        }
        return server;
    }

    public static ServerFactory getInstance() {
        if (instance == null) {
            //双重检查加锁，只有在第一次实例化时，才启用同步机制，提高了性能。
            synchronized (ServerFactory.class) {
                if (instance == null) {
                    instance = new ServerFactory();
                }
            }
        }
        return instance;
    }

}
