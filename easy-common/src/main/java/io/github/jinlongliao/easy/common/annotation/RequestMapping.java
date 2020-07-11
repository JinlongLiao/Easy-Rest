package io.github.jinlongliao.easy.common.annotation;

import io.github.jinlongliao.easy.common.constant.HttpMethod;

import java.lang.annotation.*;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD,ElementType.TYPE})
public @interface RequestMapping {
    @AliasFor("name")
    String[] value() default {};

    @AliasFor("value")
    String[] name() default {};

    HttpMethod[] method() default {HttpMethod.POST, HttpMethod.GET};
}
