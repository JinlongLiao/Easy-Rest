package io.github.jinlongliao.easy.start.context.container;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.common.filter.IFilter;
import io.github.jinlongliao.easy.server.action.IRouter;

import java.util.Collection;
import java.util.Map;

/**
 * @author liaojinlong
 * @since 2020/7/10 15:04
 */
public interface IContainer {

    /**
     * 构建 容器
     */
    void build() throws InstantiationException, IllegalAccessException;


    /**
     * 路由
     *
     * @return /
     */
    Map<HttpMethod, Map<String, IRouter>> getRouter();

    /**
     * 过滤器责任链
     *
     * @return /
     */
    FilterChain getFilterChain();

}
