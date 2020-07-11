package io.github.jinlongliao.easy.common.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 序列化
 *
 * @author liaojinlong
 * @since 2020/7/11 21:49
 */
public interface Encode extends Serialization {
    /**
     * 序列化
     *
     * @param object
     * @return /
     */
    String encode(Object object);

    class DefaultEncode implements Encode {


        /**
         * @param object
         * @return /
         */
        @Override
        public String encode(Object object) {
            return gson.toJson(object);
        }
    }
}


