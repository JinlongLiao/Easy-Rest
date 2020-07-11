package io.github.jinlongliao.easy.common.util;

import cn.hutool.core.util.ClassUtil;
import io.github.jinlongliao.easy.common.annotation.AliasFor;
import io.github.jinlongliao.easy.reflection.util.LogUtil;
import org.slf4j.Logger;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liaojinlong
 * @since 2020/7/10 15:13
 */
public class ClassUtils extends ClassUtil {
    private static Logger log = LogUtil.findLogger(ClassUtils.class);
    public static final Set<Class<? extends Annotation>> JDK_ANNOTATION = new HashSet() {
        {
            add(Documented.class);
            add(Inherited.class);
            add(Native.class);
            add(Repeatable.class);
            add(Retention.class);
            add(Target.class);
        }
    };

    public static final Set<String> OBJECT_METHOD = new HashSet() {
        {
            add("wait");
            add("equals");
            add("toString");
            add("hashCode");
            add("notify");
            add("notifyAll");
            add("getClass");
        }
    };

    public static boolean isObject(Class clazz) {
        final int modifiers = clazz.getModifiers();
        final boolean anInterface = Modifier.isInterface(modifiers);
        final boolean anAbstract = Modifier.isAbstract(modifiers);
        final boolean aFinal = Modifier.isFinal(modifiers);
        return !(anInterface || anAbstract | aFinal);
    }

    public static Map<String, Object> getProperties(Method aMethod, Class<? extends Annotation> annotation) {
        final Annotation clazzAnnotation = aMethod.getAnnotation(annotation);
        final Method[] methods = annotation.getDeclaredMethods();
        Map<String, Object> pro = new HashMap<>(methods.length);
        if (clazzAnnotation == null) {
            final Annotation[] annotations = aMethod.getAnnotations();
            for (Annotation ann : annotations) {
                findPro(annotation, ann, methods, pro);
            }
        } else {
            for (Method method : methods) {
                try {
                    final Object invoke = method.invoke(clazzAnnotation);
                    pro.put(method.getName(), invoke);
                    final AliasFor alias = method.getAnnotation(AliasFor.class);
                    if (alias != null) {
                        pro.put(alias.value(), invoke);
                    }
                } catch (ReflectiveOperationException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return pro;
    }

    public static Map<String, Object> getProperties(Class clazz, Class<? extends Annotation> annotation) {
        final Annotation clazzAnnotation = clazz.getAnnotation(annotation);
        final Method[] methods = annotation.getDeclaredMethods();
        Map<String, Object> pro = new HashMap<>(methods.length);
        if (clazzAnnotation == null) {
            final Annotation[] annotations = clazz.getAnnotations();
            for (Annotation ann : annotations) {
                findPro(annotation, ann, methods, pro);
            }
        } else {
            for (Method method : methods) {
                try {
                    final Object invoke = method.invoke(clazzAnnotation);
                    pro.put(method.getName(), invoke);
                    final AliasFor alias = method.getAnnotation(AliasFor.class);
                    if (alias != null) {
                        pro.put(alias.value(), invoke);
                    }
                } catch (ReflectiveOperationException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return pro;
    }

    private static boolean findPro(Class<? extends Annotation> annClass,
                                   Annotation ann,
                                   Method[] methods,
                                   Map<String, Object> pro) {
        boolean result = false;
        if (ann.annotationType().equals(annClass)) {
            for (Method method : methods) {
                try {
                    pro.put(method.getName(), method.invoke(ann));
                } catch (ReflectiveOperationException e) {
                    log.error(e.getMessage(), e);
                }
            }
            result = true;
        } else if (hasAnnotation(annClass, ann)) {
            final Annotation[] annotations = ann.annotationType().getAnnotations();
            for (Annotation annotation : annotations) {
                if (findPro(annClass, annotation, methods, pro)) {
                    methods = ann.annotationType().getDeclaredMethods();
                    for (Method method : methods) {
                        if (OBJECT_METHOD.contains(method.getName())) {
                            continue;
                        }
                        try {
                            final Object invoke = method.invoke(ann);
                            pro.put(method.getName(), invoke);
                            final AliasFor alias = method.getAnnotation(AliasFor.class);
                            if (alias != null) {
                                pro.put(alias.value(), invoke);
                            }
                        } catch (ReflectiveOperationException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static boolean hasAnnotation(AnnotatedElement annotatedElement, Annotation annotation) {
        final Annotation[] annotations = annotation.annotationType().getAnnotations();
        for (Annotation item : annotations) {
            if (JDK_ANNOTATION.contains(item.annotationType())) {
                continue;
            } else {
                if (item.annotationType().equals(annotatedElement)) {
                    return true;
                } else {
                    if (hasAnnotation(item.annotationType(), annotation)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
