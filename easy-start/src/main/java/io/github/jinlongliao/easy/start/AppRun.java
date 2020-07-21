package io.github.jinlongliao.easy.start;

import io.github.jinlongliao.easy.common.annotation.AppStarting;
import io.github.jinlongliao.easy.common.util.ClassUtils;
import io.github.jinlongliao.easy.start.hook.AppContextAware;
import io.github.jinlongliao.easy.start.reflection.Reflection;
import io.github.jinlongliao.easy.config.init.SystemInit;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import io.github.jinlongliao.easy.start.context.AppContext;
import io.github.jinlongliao.easy.start.context.DefaultAppServerContext;
import io.github.jinlongliao.easy.reflection.util.LogUtil;
import org.slf4j.Logger;
import reactor.netty.http.server.HttpServer;

import java.util.*;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:45
 */
public class AppRun {
    private static AppContext appContext;
    private static Logger log;

    public static void run(String... args) {
        StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                try {
                    run(Class.forName(stackTraceElement.getClassName()), args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    public static void run(Class clazz, String... args) {
        initLog();
        try {
            initContext(clazz, args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
        startApp(appContext.getServerConfig());
    }

    private static void startApp(ServerConfig serverConfig) {
        final Set<Class<? extends AppContextAware>> classes = appContext
                .getReflection()
                .reflectionClass(AppContextAware.class);
        appContext.getReflection().reflectionClass(AppContextAware.class);
        if (Objects.nonNull(classes)) {
            for (Class<?> aClass : classes) {
                if (!ClassUtils.isObject(aClass)) {
                    continue;
                }
                try {
                    ((AppContextAware) aClass.newInstance()).setResource(appContext);
                } catch (ReflectiveOperationException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        try {
            appContext.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }

    private static void initContext(Class clazz, String[] args) throws IllegalAccessException, InstantiationException {
        final ServerConfig serverConfig = new SystemInit().init(args);
        AppStarting annotation = (AppStarting) clazz.getAnnotation(AppStarting.class);
        Set<String> packages = new HashSet<>();
        Set<Class> javaPackages = new HashSet<>();
        boolean init = true;
        if (Objects.nonNull(annotation)) {
            final Class[] packages1 = annotation.packages();
            final String[] scanner = annotation.scanner();
            if (packages1.length > 0) {
                javaPackages.addAll(Arrays.asList(packages1));
                init = false;
            }
            if (scanner.length > 0) {
                packages.addAll(Arrays.asList(scanner));
                init = false;
            }
        }
        if (init) {
            packages.add(clazz.getPackage().getName());
        }
        appContext = new DefaultAppServerContext(serverConfig, new Reflection(packages, javaPackages));
        appContext.getIContainer().build();
    }

    private static void initLog() {
        log = LogUtil.findLogger(AppRun.class);
    }
}
