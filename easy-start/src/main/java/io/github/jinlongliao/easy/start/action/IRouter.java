package io.github.jinlongliao.easy.start.action;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:35
 */
public interface IRouter {
    /**
     * 路由转跳
     *
     * @param method
     * @param httpServerRequest
     * @param httpServerResponse
     * @return /
     */
    Mono router(HttpMethod method, HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse);
}
