package com.aliyun.alink.apiclient.utils;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Convertor {
    public static Map<String, String> getMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        HashMap map2 = new HashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            map2.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
        }
        return map2;
    }
}
