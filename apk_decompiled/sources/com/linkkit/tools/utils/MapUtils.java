package com.linkkit.tools.utils;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public class MapUtils {
    public static <K, V> Map<K, V> mapDifference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2) {
        HashMap map3 = new HashMap();
        map3.putAll(map);
        map3.putAll(map2);
        if (map2 != null) {
            map3.entrySet().removeAll(map2.entrySet());
        }
        return map3;
    }
}
