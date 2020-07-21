package io.github.jinlongliao.easy.server.action;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:35
 */
public interface IRouter {
    /**
     * 路由转跳
     *
     * @param method
     * @param objects
     * @return /
     */
    Object router(HttpMethod method, Object... objects);
}
