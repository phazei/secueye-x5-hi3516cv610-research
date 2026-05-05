package com.alibaba.sdk.android.tool;

import com.huawei.hms.framework.common.ContainerUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class StringUtils {
    public static String convertMaoToString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                if (z) {
                    sb.append(key + ContainerUtils.KEY_VALUE_DELIMITER + value);
                    z = false;
                } else {
                    sb.append(",");
                    sb.append(key + ContainerUtils.KEY_VALUE_DELIMITER + value);
                }
            }
        }
        return sb.toString();
    }

    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Integer) {
            return "" + ((Integer) obj);
        }
        if (obj instanceof Long) {
            return "" + ((Long) obj);
        }
        if (obj instanceof Double) {
            return "" + ((Double) obj);
        }
        if (obj instanceof Float) {
            return "" + ((Float) obj);
        }
        if (obj instanceof Short) {
            return "" + ((Short) obj);
        }
        if (!(obj instanceof Byte)) {
            return obj instanceof Boolean ? ((Boolean) obj).toString() : obj instanceof Character ? ((Character) obj).toString() : obj.toString();
        }
        return "" + ((Byte) obj);
    }

    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence != null && (length = charSequence.length()) != 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(charSequence.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }
}
