package io.github.jinlongliao.easy.common.filter;

import io.github.jinlongliao.easy.common.constant.HttpMethod;

import java.util.List;

/**
 * @author liaojinlong
 * @since 2020/9/9 13:54
 */
public interface FilterChain {
    /**
     * 添加Filter
     *
     * @param iFilter
     */
    void addFilter(IFilter iFilter);

    /**
     * 获取IFilter集合
     *
     * @return IFilter集合
     */
    List<IFilter> getAllFilters();

    /**
     * 责任链
     *
     * @param method
     * @param objects
     */
    void doBeforeFilter(HttpMethod method, Object... objects);

    /**
     * 责任链
     *
     * @param method
     * @param result
     * @param objects
     */
    Object doAfterFilter(HttpMethod method, Object result, Object... objects);
}
