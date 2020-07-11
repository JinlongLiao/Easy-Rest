package io.github.jinlongliao.easy.start.hook;

import io.github.jinlongliao.easy.start.context.AppContext;

/**
 * @author liaojinlong
 * @since 2020/7/10 14:02
 */
public interface AppContextAware {
    /**
     * 回调 ，注入
     *
     * @param appContext
     */
    void setResource(AppContext appContext);
}
