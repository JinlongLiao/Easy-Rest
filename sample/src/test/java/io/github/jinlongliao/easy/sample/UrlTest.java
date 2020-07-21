package io.github.jinlongliao.easy.sample;

import cn.hutool.core.lang.Assert;
import io.github.jinlongliao.easy.common.annotation.AppStarting;
import io.github.jinlongliao.easy.sample.AppStart;
import io.github.jinlongliao.easy.start.AppRun;
import okhttp3.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;

/**
 * @author liaojinlong
 * @since 2020/7/13 19:20
 */
@AppStarting(packages = AppStart.class)
public class UrlTest {
    final Thread thread = new Thread(() -> {
        try {
            AppRun.run(AppStart.class);
        } catch (Exception ex) {
        }
    });

    @Before
    public void init() {
        thread.start();
    }

    @After
    public void stop() {
        thread.interrupt();
    }

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void post() throws IOException, InterruptedException {
        Thread.sleep(5000);
        final RequestBody body = RequestBody.create("{}", JSON);
        String url = "http://127.0.0.1:5921/test2";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        final String string = response.body().string();
        System.out.printf("RESULT:" + string);
//        Assert.notEmpty("返回结果", string);
    }
}
