package io.github.jinlongliao.easy.server.filter;

import io.github.jinlongliao.easy.common.constant.HttpMethod;
import io.github.jinlongliao.easy.common.filter.FilterChain;
import io.github.jinlongliao.easy.common.filter.IFilter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liaojinlong
 * @since 2020/9/9 13:58
 */
public class DefaultFilterChain implements FilterChain {
    private List<IFilter> filters;
    private AtomicInteger index = new AtomicInteger(0);

    public DefaultFilterChain(List<IFilter> filters) {
        this.filters = filters;
    }

    @Override
    public void addFilter(IFilter iFilter) {
        filters.add(iFilter);
    }

    @Override
    public List<IFilter> getAllFilters() {
        return filters;
    }

    @Override
    public void doBeforeFilter(HttpMethod method, Object... objects) {
        if (index.get() < this.getAllFilters().size()) {
            final int andIncrement = index.getAndIncrement();
            this.getAllFilters().get(andIncrement).before(this, method, objects);
        } else {
            index.set(0);
        }
    }

    @Override
    public Object doAfterFilter(HttpMethod method, Object result, Object... objects) {
        if (index.get() < this.getAllFilters().size()) {
            final int andIncrement = index.getAndIncrement();
            result = this.getAllFilters().get(andIncrement).after(this, method, result,objects);
        } else {
            index.set(0);
        }
        return result;
    }
}
