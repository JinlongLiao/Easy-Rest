package io.github.jinlongliao.easy.server;

import io.github.jinlongliao.easy.config.server.ServerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 工厂类
 *
 * @author liaojinlong
 * @since 2020/7/20 17:14
 */
public class ServerFactory {
    private static ServerFactory instance = null;
    private static final List<IServer> SERVERS = new ArrayList<>();
    public static final String DEFAULT = "DEFAULT";
    private IServer server;

    static {
        final ServiceLoader<IServer> load = ServiceLoader.load(IServer.class);
        for (IServer iServer : load) {
            SERVERS.add(iServer);
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
                    for (IServer iServer : SERVERS) {
                        if (iServer.getName().endsWith(serverConfig.getServerType())) {
                            server = iServer;
                        }
                    }
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
