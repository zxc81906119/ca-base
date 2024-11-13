package com.redhat.cleanbase.common.utils;

import lombok.NonNull;
import lombok.val;

import java.util.List;
import java.util.Map;

public final class MapPathUtils {

    private MapPathUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T getFromMapOrList(@NonNull Object mapOrList, @NonNull String levelExpression) {
        try {
            val eachLevel = levelExpression.split("[.\\[\\]]");
            Object o = mapOrList;
            for (int i = 1; i < eachLevel.length; i++) {
                val thisLevel = eachLevel[i];
                if (thisLevel.isEmpty()) {
                    continue;
                }
                // 判斷上一個
                if (o instanceof List<?> list) {
                    o = list.get(Integer.parseInt(thisLevel));
                } else if (o instanceof Map<?, ?> map) {
                    o = map.get(thisLevel);
                } else {
                    return null;
                }
            }
            return CastUtils.cast(o);
        } catch (Exception e) {
            return null;
        }
    }
}
