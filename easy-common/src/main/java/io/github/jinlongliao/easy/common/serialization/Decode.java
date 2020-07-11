package io.github.jinlongliao.easy.common.serialization;

import java.util.Map;

/**
 * 序列化
 *
 * @author liaojinlong
 * @since 2020/7/11 21:49
 */
public interface Decode extends Serialization {
    /**
     * @param decode
     * @return
     */
    default Map decode(String decode) {
        return decode(decode, Map.class);
    }

    /**
     * 反序列化
     *
     * @param decode
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> T decode(String decode, Class<T> tClass);


    class DefaultDecode implements Decode {


        /**
         * 反序列化
         *
         * @param decode
         * @return /
         */
        @Override
        public <T> T decode(String decode, Class<T> tClass) {
            return gson.fromJson(decode, tClass);
        }
    }
}
