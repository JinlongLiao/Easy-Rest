package io.github.jinlongliao.easy.start.reflection;


import io.github.jinlongliao.easy.common.util.CollectionUtil;
import io.github.jinlongliao.easy.reflection.Reflections;
import io.github.jinlongliao.easy.reflection.scanners.impl.*;
import io.github.jinlongliao.easy.reflection.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * @author liaojinlong
 * @since 2020/7/10 11:44
 */
public class Reflection {
    private final Reflections reflections;

    public Reflection(Collection<String> basePackage, Collection<Class> javaPackage) {
        CollectionUtil.toNotNull(basePackage);
        CollectionUtil.toNotNull(javaPackage);
        reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(basePackage.toArray(new String[0]))
                .forJavaPackages(javaPackage.toArray(new Class[0]))
                .setScanners(
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner(),
                        new SubTypesScanner(),
                        new MethodAnnotationsScanner(),
                        new MethodParameterScanner(),
                        new MethodParameterNamesScanner(),
                        new MemberUsageScanner())
        );
    }

    public Set<Class<?>> reflectionType(Class<? extends Annotation> annotation) {
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
        return typesAnnotatedWith;
    }

    public Set<Method> reflectionMethod(Class<? extends Annotation> annotation) {
        final Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(annotation);
        return methodsAnnotatedWith;
    }

    public Set<Field> reflectionField(Class<? extends Annotation> annotation) {
        final Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(annotation);
        return fieldsAnnotatedWith;
    }

    public <T> Set<Class<? extends T>> reflectionClass(Class<T> annotation) {
        final Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(annotation);
        return subTypesOf;
    }

}
