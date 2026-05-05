package com.alibaba.sdk.android.tbrest.utils;

import com.huawei.hms.framework.common.ContainerUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence == null || (length = charSequence.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    public static String defaultString(String str, String str2) {
        return isBlank(str) ? str2 : str;
    }

    public static int hashCode(String str) {
        if (str.length() <= 0) {
            return 0;
        }
        int i = 0;
        for (char c2 : str.toCharArray()) {
            i = (i * 31) + c2;
        }
        return i;
    }

    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return ((String) obj).toString();
        }
        if (obj instanceof Integer) {
            return "" + ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return "" + ((Long) obj).longValue();
        }
        if (obj instanceof Double) {
            return "" + ((Double) obj).doubleValue();
        }
        if (obj instanceof Float) {
            return "" + ((Float) obj).floatValue();
        }
        if (obj instanceof Short) {
            return "" + ((int) ((Short) obj).shortValue());
        }
        if (obj instanceof Byte) {
            return "" + ((int) ((Byte) obj).byteValue());
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).toString();
        }
        if (obj instanceof Character) {
            return ((Character) obj).toString();
        }
        return obj.toString();
    }

    public static String convertMapToString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : map.keySet()) {
            String str2 = map.get(str);
            if (str2 != null && str != null) {
                if (z) {
                    if ("--invalid--".equals(str2)) {
                        stringBuffer.append(str);
                    } else {
                        stringBuffer.append(str + ContainerUtils.KEY_VALUE_DELIMITER + str2);
                    }
                    z = false;
                } else if (!"--invalid--".equals(str2)) {
                    stringBuffer.append(",");
                    stringBuffer.append(str + ContainerUtils.KEY_VALUE_DELIMITER + str2);
                } else {
                    stringBuffer.append(",");
                    stringBuffer.append(str);
                }
            }
        }
        return stringBuffer.toString();
    }
}
