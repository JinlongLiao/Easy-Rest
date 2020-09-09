package io.github.jinlongliao.easy.server.proxy.netty;


import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.common.serialization.Decode;
import io.github.jinlongliao.easy.common.serialization.Encode;
import io.github.jinlongliao.easy.server.proxy.AbstractEasyInvocationHandler;
import io.github.jinlongliao.easy.server.proxy.ComputerArgs;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:44
 */
public class NettyInvocationHandler extends AbstractEasyInvocationHandler<HttpServerRequest, HttpServerResponse> {
    private final FilterChain filterChain;
    private Object target;
    private Method method;
    private ComputerArgs computerArgs;
    private Decode decode = new Decode.DefaultDecode();
    private Encode encode = new Encode.DefaultEncode();

    public NettyInvocationHandler(Object target, Method method, FilterChain filterChain, ComputerArgs computerArgs) {
        this.target = target;
        this.method = method;
        this.filterChain = filterChain;
        this.computerArgs = computerArgs;
    }

    @Override
    public Object[] getArgs(Object[] args) {
        return computerArgs.computer(args);
    }

    @Override
    public FilterChain getFilter() {
        return filterChain;
    }


    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Encode getEncode() {
        return encode;
    }

    @Override
    public Decode getDecode() {
        return decode;
    }

    @Override
    protected Object invoke(HttpServerRequest request, HttpServerResponse response, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object invoke = getMethod().invoke(getTarget(), getArgs(args));

        invoke = getFilter().doAfterFilter(getHttpMethod(args), invoke, args);

        if (invoke == null) {
            invoke = "{\"message\":\"NOT FOUND " + args[0] + ":" + request.path() + "\"}";
        }
        Mono mono = Mono.from(response.sendString(Mono.just(
                (invoke instanceof String ? invoke : getEncode().encode(invoke)).toString()
        )));
        return mono;
    }

    public void setDecode(Decode decode) {
        this.decode = decode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }
}
