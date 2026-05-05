package com.aliyun.iot.aep.sdk.framework.utils;

import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class MailHelper {
    public static boolean isEmail(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(str).matches();
    }
}
