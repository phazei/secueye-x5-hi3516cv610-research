package com.aliyun.iotx.linkvisual.media.audio.utils;

import android.os.Build;
import androidx.collection.ArrayMap;
import tools.LocationUtil;

/* JADX INFO: loaded from: classes2.dex */
public class AecUtils {
    private static final ArrayMap<String, String[]> HARDWARE_AEC_BLACK_DEVICE_LISTS;
    private static final ArrayMap<String, String[]> HARDWARE_AEC_IN_CALL_DEVICE_LISTS;
    private static final ArrayMap<String, String[]> HEADSET_BLACK_DEVICE_LISTS;

    static {
        ArrayMap<String, String[]> arrayMap = new ArrayMap<>();
        HARDWARE_AEC_BLACK_DEVICE_LISTS = arrayMap;
        ArrayMap<String, String[]> arrayMap2 = new ArrayMap<>();
        HARDWARE_AEC_IN_CALL_DEVICE_LISTS = arrayMap2;
        ArrayMap<String, String[]> arrayMap3 = new ArrayMap<>();
        HEADSET_BLACK_DEVICE_LISTS = arrayMap3;
        arrayMap.put("rockchip", new String[]{"rk3566_r"});
        arrayMap2.put(LocationUtil.MANUFACTURER_XIAOMI, new String[]{"cactus"});
        arrayMap2.put(LocationUtil.MANUFACTURER_OPPO, new String[]{"PBAM00"});
        arrayMap3.put(LocationUtil.MANUFACTURER_XIAOMI, new String[]{"laurus"});
    }

    public static boolean isForbiddenHeadset() {
        String[] strArr = HEADSET_BLACK_DEVICE_LISTS.get(Build.MANUFACTURER);
        if (strArr != null) {
            if (strArr.length == 0) {
                return true;
            }
            for (String str : strArr) {
                if (str.equals(Build.DEVICE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isModeInCallAEC() {
        String[] strArr = HARDWARE_AEC_IN_CALL_DEVICE_LISTS.get(Build.MANUFACTURER);
        if (strArr != null) {
            if (strArr.length == 0) {
                return true;
            }
            for (String str : strArr) {
                if (str.equals(Build.DEVICE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isModeNone() {
        String[] strArr = HARDWARE_AEC_BLACK_DEVICE_LISTS.get(Build.MANUFACTURER);
        if (strArr != null) {
            if (strArr.length == 0) {
                return true;
            }
            for (String str : strArr) {
                if (str.equals(Build.DEVICE)) {
                    return true;
                }
            }
        }
        return false;
    }
}
