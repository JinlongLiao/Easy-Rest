package io.github.jinlongliao.easy.server.proxy.servlet;

import io.github.jinlongliao.easy.common.filter.IFilter;
import io.github.jinlongliao.easy.common.serialization.Decode;
import io.github.jinlongliao.easy.common.serialization.Encode;
import io.github.jinlongliao.easy.server.proxy.AbstractEasyInvocationHandler;
import io.github.jinlongliao.easy.server.proxy.ComputerArgs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author liaojinlong
 * @since 2020/7/20 21:20
 */
public class ServletInvocationHandler extends AbstractEasyInvocationHandler<HttpServletRequest, HttpServletResponse> {
    private Object target;
    private Method method;
    private IFilter[] iFilters;
    private ComputerArgs computerArgs;
    private Decode decode = new Decode.DefaultDecode();
    private Encode encode = new Encode.DefaultEncode();

    public ServletInvocationHandler(Object target, Method method, IFilter[] iFilters, ComputerArgs computerArgs) {
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
    protected Object invoke(HttpServletRequest request, HttpServletResponse response, Object[] args) throws InvocationTargetException, IllegalAccessException, IOException {
        Object invoke = getMethod().invoke(getTarget(), getArgs(args));

        if (getFilter() != null) {
            for (IFilter iFilter : getFilter()) {
                invoke = iFilter.after(getHttpMethod(args), getHttpServerRequest(args), getHttpServerResponse(args), invoke);
            }
        }
        if (Objects.nonNull(invoke)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(encode.encode(invoke));
        }
        return null;
    }
}
