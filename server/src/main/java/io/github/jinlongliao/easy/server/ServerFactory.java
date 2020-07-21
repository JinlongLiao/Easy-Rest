package io.github.jinlongliao.easy.server;

import java.util.ServiceLoader;

/**
 * 工厂类
 *
 * @author liaojinlong
 * @since 2020/7/20 17:14
 */
public class ServerFactory {
    private static ServerFactory instance = null;

    static {
        final ServiceLoader<IServer> load = ServiceLoader.load(IServer.class);
        for (IServer iServer : load) {

        }
    }

    private ServerFactory() {
    }

    /**
     * io.github.jinlongliao.easy.server.IServer
     *
     * @return io.github.jinlongliao.easy.server.IServer
     */
    public IServer getServer() {
        return null;
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
