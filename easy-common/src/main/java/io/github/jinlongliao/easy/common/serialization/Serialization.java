package io.github.jinlongliao.easy.common.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author liaojinlong
 * @since 2020/7/11 21:57
 */
public interface Serialization {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
}
