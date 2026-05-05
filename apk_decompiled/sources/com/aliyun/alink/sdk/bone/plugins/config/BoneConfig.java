package com.aliyun.alink.sdk.bone.plugins.config;

import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.ServiceMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@BoneService(mode = ServiceMode.ALWAYS_NEW, name = BoneConfig.API_NAME)
public class BoneConfig extends BaseBoneService {
    public static final String API_NAME = "BoneConfig";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Map<String, String> f4464a = new ConcurrentHashMap();

    @BoneMethod
    public void get(JSONArray jSONArray, BoneCallback boneCallback) {
        String[] strArr = new String[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            strArr[i] = jSONArray.optString(i);
        }
        JSONObject jSONObject = new JSONObject();
        for (String str : strArr) {
            try {
                String str2 = f4464a.get(str);
                if (str2 == null) {
                    str2 = "";
                }
                jSONObject.put(str, str2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boneCallback.success(jSONObject);
    }

    @BoneMethod
    public void getAll(BoneCallback boneCallback) {
        JSONObject jSONObject = new JSONObject();
        for (String str : f4464a.keySet()) {
            try {
                String str2 = f4464a.get(str);
                if (str2 == null) {
                    str2 = "";
                }
                jSONObject.put(str, str2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boneCallback.success(jSONObject);
    }

    @BoneMethod
    public void set(JSONObject jSONObject, BoneCallback boneCallback) {
        HashMap map = new HashMap(jSONObject.length());
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            map.put(next, jSONObject.optString(next));
        }
        f4464a.putAll(map);
        boneCallback.success(new JSONObject());
    }

    public static String get(String str) {
        return f4464a.get(str);
    }

    public static Map<String, String> getAll() {
        return new HashMap(f4464a);
    }

    public static String set(String str, String str2) {
        return f4464a.put(str, str2);
    }

    public static void setAll(Map<String, String> map) {
        f4464a.putAll(map);
    }
}
