package com.http.helper;

import java.util.HashMap;

/* JADX INFO: loaded from: classes3.dex */
public class HttpUtils {
    public static HashMap<String, String> getDefaultRequestHeader() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/x-www-form-urlencoded");
        return map;
    }

    private static String makeUA() {
        return System.getProperty("os.version", "JavaUtil UA");
    }

    public static HashMap<String, String> getParamMap(String... strArr) {
        if (strArr == null) {
            return null;
        }
        int length = strArr.length;
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < length; i += 2) {
            int i2 = i + 1;
            if (i2 >= length) {
                break;
            }
            if (!isEmpty(strArr[i])) {
                map.put(strArr[i], strArr[i2]);
            }
        }
        return map;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
