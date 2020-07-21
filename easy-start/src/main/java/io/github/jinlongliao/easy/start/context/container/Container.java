package io.github.jinlongliao.easy.start.context.container;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.github.jinlongliao.easy.common.annotation.Component;
import io.github.jinlongliao.easy.common.annotation.RequestMapping;
import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.util.ClassUtils;
import io.github.jinlongliao.easy.server.ServerFactory;
import io.github.jinlongliao.easy.server.action.IRouter;
import io.github.jinlongliao.easy.start.context.AppContext;
import io.github.jinlongliao.easy.server.proxy.EasyMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:32
 */
public class Container implements IContainer {
    private AppContext appContext;
    private Map<String, Class> componentCache = new ConcurrentHashMap<>();
    private Map<String, List<EasyMethod>> methodCache = new ConcurrentHashMap<>();

    private Map<Method, IRouter> methodIRouterMap = new ConcurrentHashMap<>();
    /**
     * <pre>
     * - GET
     *  - URL,Method
     *  - URL,Method
     * </pre>
     */
    private Map<HttpMethod, Map<String, IRouter>> routers;

    {
        final HttpMethod[] httpMethods = HttpMethod.values();
        routers = new HashMap<>(httpMethods.length);
        for (HttpMethod httpMethod : httpMethods) {
            routers.put(httpMethod, new HashMap<>());
        }
    }

    public Container(AppContext appContext) {
        this.appContext = appContext;
    }


    /**
     * 构建 容器
     */
    @Override
    public void build() throws InstantiationException, IllegalAccessException {
        buildComponent();
        buildController();
        buildAction();
    }


    @Override
    public Map<HttpMethod, Map<String, IRouter>> getRouter() {
        return routers;
    }

    private void buildAction() throws IllegalAccessException, InstantiationException {
        ServerFactory.getInstance().getServer().buildRouter(routers,methodCache,methodIRouterMap);
    }



    /**
     * 构建Controller
     */
    private void buildController() {
        for (String key : componentCache.keySet()) {
            final Class aClass = componentCache.get(key);
            final Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (ClassUtils.OBJECT_METHOD.contains(method.getName())) {
                    continue;
                }
                final Map<String, Object> properties = ClassUtils.getProperties(method, RequestMapping.class);
                if (CollectionUtil.isEmpty(properties)) {
                    continue;
                } else {
                    String[] subPaths = (String[]) properties.get("value");
                    final HttpMethod[] httpMethods = (HttpMethod[]) properties.get("method");
                    final RequestMapping requestMapping = (RequestMapping) aClass.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        final String[] rootPaths = requestMapping.value();
                        Set<String> paths = new HashSet<>();
                        for (String subPath : subPaths) {
                            if (rootPaths != null && rootPaths.length > 0) {
                                for (String rootPath : rootPaths) {
                                    paths.add(rootPath + subPath);
                                }
                            } else {
                                paths.add(subPath);
                            }
                        }
                        subPaths = paths.toArray(new String[0]);
                    }
                    List<EasyMethod> easyMethods = methodCache.get(key);
                    if (easyMethods == null) {
                        easyMethods = new ArrayList<>();
                        methodCache.put(key, easyMethods);
                    }
                    easyMethods.add(new EasyMethod(aClass, method, ListUtil.toList(subPaths), ListUtil.toList(httpMethods)));
                }
            }
        }
    }

    /**
     * 扫描所有的组件
     */
    private void buildComponent() {
        final Class<Component> annotation = Component.class;
        final Set<Class<?>> classes = appContext.getReflection().reflectionType(annotation);
        classes.forEach(item -> {
            if (ClassUtils.isObject(item)) {
                String value =
                        ClassUtils.getProperties(item, annotation).get("value").toString();
                if (StrUtil.isEmpty(value)) {
                    value = StrUtil.lowerFirst(item.getSimpleName());
                }
                componentCache.put(value, item);
            }
        });
    }
}
