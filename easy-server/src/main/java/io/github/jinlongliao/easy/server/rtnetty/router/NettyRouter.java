package io.github.jinlongliao.easy.server.rtnetty.router;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.server.action.IRouter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import reactor.core.publisher.Mono;

/**
 * netty 路由
 *
 * @author liaojinlong
 * @since 2020/7/20 18:11
 */
public interface NettyRouter extends IRouter {

    /**
     * 路由转跳
     *
     * @param method
     * @param objects
     * @return /
     */
    @Override
    default Object router(HttpMethod method, Object... objects) {
        return this.nettyRouter(method, ((HttpRequest) objects[0]), ((HttpResponse) objects[1]));
    }

    /**
     * Netty 专用
     *
     * @param method
     * @param httpRequest
     * @param httpResponse
     * @return /
     */
    Mono nettyRouter(HttpMethod method, HttpRequest httpRequest, HttpResponse httpResponse);
}
