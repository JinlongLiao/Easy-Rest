package io.github.jinlongliao.easy.common.filter;

import io.github.jinlongliao.easy.common.constant.HttpMethod;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:30
 */
public interface IFilter {
    /**
     * @param method
     * @return /
     */
    default void before(FilterChain filterChain, HttpMethod method, Object... objects) {
        filterChain.doAfterFilter(method, objects);
    }


    /**
     * @param method
     * @param result
     * @return /
     */
    default Object after(FilterChain filterChain, HttpMethod method, Object result, Object... objects) {
        return filterChain.doAfterFilter(method, objects);
    }

}
