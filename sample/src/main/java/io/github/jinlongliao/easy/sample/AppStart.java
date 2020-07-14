package io.github.jinlongliao.easy.sample;

import io.github.jinlongliao.easy.common.annotation.AppStarting;
import io.github.jinlongliao.easy.start.AppRun;

/**
 * @author liaojinlong
 * @since 2020/7/10 15:56
 */
@AppStarting(packages = AppStart.class)
public class AppStart {
    public static void main(String[] args) {
        AppRun.run(AppStart.class, args);
    }
}
