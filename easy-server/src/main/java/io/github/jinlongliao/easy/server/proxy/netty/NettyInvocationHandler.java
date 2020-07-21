package io.github.jinlongliao.easy.server.proxy.netty;


import io.github.jinlongliao.easy.common.serialization.Decode;
import io.github.jinlongliao.easy.common.serialization.Encode;
import io.github.jinlongliao.easy.common.filter.IFilter;
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
    private Object target;
    private Method method;
    private IFilter[] iFilters;
    private ComputerArgs computerArgs;
    private Decode decode = new Decode.DefaultDecode();
    private Encode encode = new Encode.DefaultEncode();

    public NettyInvocationHandler(Object target, Method method, IFilter[] iFilters, ComputerArgs computerArgs) {
        this.target = target;
        this.method = method;
        this.iFilters = iFilters;
        this.computerArgs = computerArgs;
    }

    @Override
    public Object[] getArgs(Object[] args) {
        return computerArgs.computer(args);
    }

    @Override
    public IFilter[] getFilter() {
        return iFilters;
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

        if (getFilter() != null) {
            for (IFilter iFilter : getFilter()) {
                invoke = iFilter.after(getHttpMethod(args), getHttpServerRequest(args), getHttpServerResponse(args), invoke);
            }
        }
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
