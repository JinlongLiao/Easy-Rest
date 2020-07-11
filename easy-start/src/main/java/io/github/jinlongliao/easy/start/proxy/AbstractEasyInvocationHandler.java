package io.github.jinlongliao.easy.start.proxy;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.serialization.Decode;
import io.github.jinlongliao.easy.common.serialization.Encode;
import io.github.jinlongliao.easy.start.filter.IFilter;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:43
 */
public abstract class AbstractEasyInvocationHandler implements InvocationHandler {
    abstract Object[] getArgs(Object[] args);

    abstract IFilter[] getFilter();

    abstract Object getTarget();

    abstract Method getMethod();

    abstract Encode getEncode();

    abstract Decode getDecode();

    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return the value to return from the method invocation on the
     * proxy instance.  If the declared return type of the interface
     * method is a primitive type, then the value returned by
     * this method must be an instance of the corresponding primitive
     * wrapper class; otherwise, it must be a type assignable to the
     * declared return type.  If the value returned by this method is
     * {@code null} and the interface method's return type is
     * primitive, then a {@code NullPointerException} will be
     * thrown by the method invocation on the proxy instance.  If the
     * value returned by this method is otherwise not compatible with
     * the interface method's declared return type as described above,
     * a {@code ClassCastException} will be thrown by the method
     * invocation on the proxy instance.
     * @throws Throwable the exception to throw from the method
     *                   invocation on the proxy instance.  The exception's type must be
     *                   assignable either to any of the exception types declared in the
     *                   {@code throws} clause of the interface method or to the
     *                   unchecked exception types {@code java.lang.RuntimeException}
     *                   or {@code java.lang.Error}.  If a checked exception is
     *                   thrown by this method that is not assignable to any of the
     *                   exception types declared in the {@code throws} clause of
     *                   the interface method, then an
     *                   {@link UndeclaredThrowableException} containing the
     *                   exception that was thrown by this method will be thrown by the
     *                   method invocation on the proxy instance.
     * @see UndeclaredThrowableException
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final IFilter[] filter = getFilter();
        if (filter != null) {
            for (IFilter iFilter : filter) {
                iFilter.before(getHttpMethod(args), getHttpServerRequest(args), getHttpServerResponse(args));
            }
        }
        Object invoke = getMethod().invoke(getTarget(), getArgs(args));

        if (filter != null) {
            for (IFilter iFilter : filter) {
                invoke = iFilter.after(getHttpMethod(args), getHttpServerRequest(args), getHttpServerResponse(args), invoke);
            }
        }
        HttpServerResponse response = (HttpServerResponse) args[2];
        HttpServerRequest request = (HttpServerRequest) args[1];
        Mono mono = invoke == null ?
                Mono.empty() :
                Mono.from(response.sendString(Mono.just(
                        (invoke instanceof String ? invoke : getEncode().encode(invoke)).toString()
                )));
        return mono;
    }

    protected HttpServerRequest getHttpServerRequest(Object[] args) {
        return (HttpServerRequest) args[1];
    }

    protected HttpServerResponse getHttpServerResponse(Object[] args) {
        return (HttpServerResponse) args[2];
    }

    protected HttpMethod getHttpMethod(Object[] args) {
        return (HttpMethod) args[0];
    }
}
