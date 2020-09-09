package io.github.jinlongliao.easy.sample.filter;

import io.github.jinlongliao.easy.common.annotation.Filter;
import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.common.filter.IFilter;
import org.slf4j.Logger;


/**
 * 示例Demo
 *
 * @author liaojinlong
 * @since 2020/9/9 16:50
 */
@Filter
public class AppFilter implements IFilter {
    private static Logger log = io.github.jinlongliao.easy.reflection.util.LogUtil.findLogger(AppFilter.class);

    @Override
    public void before(FilterChain filterChain, HttpMethod method, Object... objects) {
        log.error("method = " + method + ",io.github.jinlongliao.easy.sample.filter.AppFilter.before");
        /**
         * 继续
         */
        filterChain.doBeforeFilter(method, objects);
    }

    @Override
    public Object after(FilterChain filterChain, HttpMethod method, Object result, Object... objects) {
        log.error("method = " + method + ", result = " + result + ", io.github.jinlongliao.easy.sample.filter.AppFilter.after ");
        /**
         * 继续
         */
        return filterChain.doAfterFilter(method, result, objects);
    }
}
