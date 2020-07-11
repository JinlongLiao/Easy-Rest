package io.github.jinlongliao.easy.start.proxy;


import io.github.jinlongliao.easy.common.serialization.Decode;
import io.github.jinlongliao.easy.common.serialization.Encode;
import io.github.jinlongliao.easy.start.filter.IFilter;

import java.lang.reflect.Method;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:44
 */
public class CommonInvocationHandler extends AbstractEasyInvocationHandler {
    private Object target;
    private Method method;
    private IFilter[] iFilters;
    private ComputerArgs computerArgs;
    private Decode decode = new Decode.DefaultDecode();
    private Encode encode = new Encode.DefaultEncode();

    public CommonInvocationHandler(Object target, Method method, IFilter[] iFilters, ComputerArgs computerArgs) {
        this.target = target;
        this.method = method;
        this.iFilters = iFilters;
        this.computerArgs = computerArgs;
    }

    @Override
    Object[] getArgs(Object[] args) {
        return computerArgs.computer(args);
    }

    @Override
    IFilter[] getFilter() {
        return iFilters;
    }

    @Override
    Object getTarget() {
        return target;
    }

    @Override
    Method getMethod() {
        return method;
    }

    @Override
    Encode getEncode() {
        return encode;
    }

    @Override
    Decode getDecode() {
        return decode;
    }

    public void setDecode(Decode decode) {
        this.decode = decode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }
}
