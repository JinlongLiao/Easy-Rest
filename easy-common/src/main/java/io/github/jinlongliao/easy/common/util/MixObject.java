package io.github.jinlongliao.easy.common.util;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:38
 */
public class MixObject {
    public static synchronized void mix(Object object, Properties properties) {
        final Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            final String name = field.getName();
            if (properties.contains(name)) {
                field.setAccessible(true);
                try {
                    field.set(object, properties.get(name));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
