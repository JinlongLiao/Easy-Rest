package io.github.jinlongliao.easy.sample.component;

import io.github.jinlongliao.easy.common.annotation.Component;
import io.github.jinlongliao.easy.common.annotation.GetMapping;
import io.github.jinlongliao.easy.common.annotation.RequestMapping;
import reactor.netty.http.server.HttpServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaojinlong
 * @since 2020/7/10 16:18
 */
@Component
@RequestMapping
public class AppComponent {
    @GetMapping("/test2")
    public Map<String, Object> test1(HttpServerRequest httpServerRequest) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", 200);
        result.put("desc", 88888);
        return result;
    }
}
