package io.github.jinlongliao.easy.common.annotation;

import io.github.jinlongliao.easy.common.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:59
 */
@RequestMapping(method = {HttpMethod.GET})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface GetMapping {
    String[] value() default {};

    String[] name() default {};
}
