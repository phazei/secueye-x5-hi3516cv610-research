package com.aliyun.iot.aep.sdk.log;

import com.huawei.hms.push.constant.RemoteMessageConst;
import com.taobao.accs.AccsClientConfig;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class RuleManager {
    private static HashMap<String, HashMap<String, ArrayList<String>>> sRuleMap = new HashMap<>();
    private static HashMap<String, ArrayList<String>> sTagMap = new HashMap<>();
    private static ArrayList<String> sTags = new ArrayList<>();

    public static void addLevel(String str, String str2) {
    }

    public static void addTag(String str, String str2) {
        if (sTags.contains(str2)) {
            return;
        }
        sTags.add(str2);
    }

    public static boolean hasTag(String str, String str2) {
        return sTags.contains(str2);
    }

    public static void init() {
        sTagMap.put(RemoteMessageConst.Notification.TAG, sTags);
        sRuleMap.put(AccsClientConfig.DEFAULT_CONFIGTAG, sTagMap);
    }

    public static boolean levelMatch(String str, String str2) {
        return false;
    }

    public static void removeLevel(String str, String str2) {
    }

    public static void removeTag(String str, String str2) {
        if (sTags.contains(str2)) {
            sTags.remove(str2);
        }
    }
}
