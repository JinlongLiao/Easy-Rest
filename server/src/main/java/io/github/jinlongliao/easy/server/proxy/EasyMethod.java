package io.github.jinlongliao.easy.server.proxy;

import io.github.jinlongliao.easy.common.constant.HttpMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaojinlong
 * @since 2020/7/10 17:01
 */
public class EasyMethod {
    private Class aClass;
    private Method method;
    private List<HttpMethod> methods;
    private List<String> urlPath;

    public EasyMethod() {
        methods = new ArrayList<>();
        urlPath = new ArrayList<>();
    }

    public EasyMethod(Class aClass, Method method, List<String> urlPath, List<HttpMethod> methods) {
        this.aClass = aClass;
        this.method = method;
        this.methods = methods;
        this.urlPath = urlPath;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<HttpMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<HttpMethod> methods) {
        this.methods = methods;
    }

    public List<String> getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(List<String> urlPath) {
        this.urlPath = urlPath;
    }
}
