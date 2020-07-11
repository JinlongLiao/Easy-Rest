package io.github.jinlongliao.easy.common.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author liaojinlong
 * @since 2020/7/10 11:58
 */
public class CollectionUtil {
    public static <T> Collection<T> toNotNull(Collection<T> collection) {
        if (Objects.isNull(collection)) {
            collection = new HashSet<>();
        }
        return collection;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return Objects.isNull(collection) && collection.isEmpty();
    }

    public static <T> boolean isEmpty(Collection<T>... collections) {
        boolean result = false;
        for (Collection<T> collection : collections) {
            if (Objects.isNull(collection) && collection.isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }


}
