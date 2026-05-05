package com.aliyun.alink.business.devicecenter.log;

import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class PerformanceLog {
    public static JSONObject getJsonObject(String... strArr) {
        if (strArr == null) {
            return null;
        }
        try {
            if (strArr.length < 2) {
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            int length = strArr.length / 2;
            for (int i = 0; i < length; i += 2) {
                try {
                    jSONObject.put(strArr[i], strArr[i + 1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jSONObject;
        } catch (Exception e2) {
            e2.printStackTrace();
            return new JSONObject();
        }
    }

    public static void trace(String str, String str2) {
    }

    public static void trace(String str, String str2, String str3, String str4, JSONObject jSONObject) {
    }

    public static void trace(String str, String str2, JSONObject jSONObject) {
    }
}
