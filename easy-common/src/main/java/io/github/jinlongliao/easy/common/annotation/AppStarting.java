package io.github.jinlongliao.easy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liaojinlong
 * @since 2020/7/10 11:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AppStarting {
    String[] scanner() default {};

    Class[] packages() default {};
}
