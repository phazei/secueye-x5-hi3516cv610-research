package com.aliyun.alink.linksdk.alcs.lpbs.utils;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class TopicUtils {
    private static final String EXPAND_SPLITE = ".";
    public static final String PATH_PRE_DEV = "/dev";
    private static final String URI_PATH_SPLITER = "/";

    public static String topicToRawDownTopic(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str) && str.startsWith(PATH_PRE_DEV)) {
            return str;
        }
        return "/sys/" + str2 + "/" + str3 + "/thing/model/down_raw";
    }

    public static String topicToRawUpTopic(String str, String str2) {
        return "/sys/" + str + "/" + str2 + "/thing/model/up_raw";
    }

    public static String combineStr(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public static String combineStr(String str, String str2) {
        return str + str2;
    }

    public static String formatTopicByMethod(String str, String str2, String str3, String str4) {
        return str3 + "/" + str + "/" + str2 + "/" + str4.replace(".", "/");
    }
}
