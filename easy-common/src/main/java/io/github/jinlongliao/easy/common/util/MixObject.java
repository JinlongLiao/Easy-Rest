package io.github.jinlongliao.easy.common.util;

import io.github.jinlongliao.easy.reflection.util.LogUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:38
 */
public class MixObject {
    private static Logger log = LogUtil.findLogger(MixObject.class);

    public static synchronized void mix(Object object, Properties properties) {
        final Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            final String name = field.getName();
            if (properties.containsKey(name)) {
                field.setAccessible(true);
                try {
                    field.set(object, getRealValue(field, properties.get(name)));
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private static Object getRealValue(Field field, Object o) {
        final String type = field.getType().getName();
        Object value;
        if (String.class.getName().equals(type)) {
            value = o;
        } else if (Boolean.class.getName().equals(type) || "boolean".endsWith(type)) {
            value = Boolean.valueOf(o.toString());
        } else if (Integer.class.getName().equals(type) || "int".endsWith(type)) {
            value = Integer.valueOf(o.toString());
        } else if (Double.class.getName().equals(type) || "double".endsWith(type)) {
            value = Double.valueOf(o.toString());
        } else if (Float.class.getName().equals(type) || "float".endsWith(type)) {
            value = Float.valueOf(o.toString());
        } else {
            value = o;
        }
        return value;
    }
}
