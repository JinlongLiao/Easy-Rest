package io.github.jinlongliao.easy.start.filter;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:30
 */
public interface IFilter {
    /**
     * @param method
     * @param httpServerRequest
     * @param httpServerResponse
     * @return /
     */
    default boolean before(HttpMethod method, HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse) {
        return true;
    }


    /**
     * @param method
     * @param httpServerRequest
     * @param httpServerResponse
     * @param result
     * @return /
     */
    default Object after(HttpMethod method, HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse, Object result) {
        return result;
    }

}
