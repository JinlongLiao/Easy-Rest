package io.github.jinlongliao.easy.common.annotation;

/**
 * @author liaojinlong
 * @since 2020/7/10 11:21
 */

public @interface Filter {
    String[] path() default {};
}
