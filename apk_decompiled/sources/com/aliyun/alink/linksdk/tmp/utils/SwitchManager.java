package com.aliyun.alink.linksdk.tmp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SwitchManager {
    public static final String SWITCH_JSION_STRING = "[\"allPowerstate\",\"powerstate\",\"PowerSwitch\",\"LightSwitch\",\"WorkSwitch\",\"powerstate1\",\"powerstate2\",\"powerstate3\"]";
    public static final String SWITCH_JSION_STRING2 = "[\"allPowerstate\",\"powerstate\",\"PowerSwitch\",\"LightSwitch\",\"WorkSwitch\"]";
    private static final String TAG = "SwitchManager";

    public static boolean hasSwitch(String str) {
        return getSwitchList().contains(str.toLowerCase());
    }

    public static boolean hasSwitchV1(String str) {
        return getSwitchListV1().contains(str.toLowerCase());
    }

    public static List<String> getSwitchListV1() {
        String[] strArrSplit = SWITCH_JSION_STRING.replace("[", "").replace("]", "").replace("\"", "").toLowerCase().split(",");
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, strArrSplit);
        return arrayList;
    }

    public static List<String> getSwitchList() {
        String[] strArrSplit = SWITCH_JSION_STRING2.replace("[", "").replace("]", "").replace("\"", "").toLowerCase().split(",");
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, strArrSplit);
        return arrayList;
    }
}
