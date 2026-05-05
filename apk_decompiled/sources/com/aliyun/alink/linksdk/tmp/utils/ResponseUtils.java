package com.aliyun.alink.linksdk.tmp.utils;

import com.aliyun.alink.linksdk.tools.ALog;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ResponseUtils {
    protected static final String TAG = "[Tmp]ResponseUtils";

    public static JSONObject getSuccessRspJson(JSONArray jSONArray) {
        return getRspJson(200, "success", jSONArray);
    }

    public static String getSuccessRspJson(JSONObject jSONObject) {
        return getRspJson(200, "success", jSONObject);
    }

    public static String getRspJson(int i, String str, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("code", i);
            jSONObject2.put("message", str);
            jSONObject2.put("data", jSONObject);
            ALog.d(TAG, "rsp bone json = " + jSONObject2.toString());
            return jSONObject2.toString();
        } catch (Exception e) {
            ALog.d(TAG, "getRspJson, e = " + e.toString());
            return null;
        }
    }

    public static JSONObject getRspJson(int i, String str, JSONArray jSONArray) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            jSONObject.put("data", jSONArray);
            return jSONObject;
        } catch (Exception e) {
            ALog.d(TAG, "getRspJson, e = " + e.toString());
            return null;
        }
    }

    public static String getSuccessRspJson(com.alibaba.fastjson.JSONObject jSONObject) {
        return getRspJson(200, "success", jSONObject);
    }

    public static String getRspJson(int i, String str, com.alibaba.fastjson.JSONObject jSONObject) {
        try {
            com.alibaba.fastjson.JSONObject jSONObject2 = new com.alibaba.fastjson.JSONObject();
            jSONObject2.put("code", (Object) Integer.valueOf(i));
            jSONObject2.put("message", (Object) str);
            jSONObject2.put("data", (Object) jSONObject);
            ALog.d(TAG, "rsp bone json = " + jSONObject2.toString());
            return jSONObject2.toString();
        } catch (Exception e) {
            ALog.d(TAG, "getRspJson, e = " + e.toString());
            return null;
        }
    }
}
