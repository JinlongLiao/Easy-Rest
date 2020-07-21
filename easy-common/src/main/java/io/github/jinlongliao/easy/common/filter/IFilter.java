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
    default boolean before(HttpMethod method, Object... objects) {
        return true;
    }


    /**
     * @param method
     * @param result
     * @return /
     */
    default Object after(HttpMethod method, Object result, Object... objects) {
        return result;
    }

}
