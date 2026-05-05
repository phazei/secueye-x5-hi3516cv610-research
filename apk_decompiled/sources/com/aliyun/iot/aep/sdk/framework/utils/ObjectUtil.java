package com.aliyun.iot.aep.sdk.framework.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.google.gson.Gson;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ObjectUtil {
    public static Map<String, Object> objectToMap(Object obj) {
        try {
            return (Map) JSONObject.parseObject(new Gson().toJson(obj), Map.class);
        } catch (Exception e) {
            ALog.d("ObjectUtil", e.toString());
            return null;
        }
    }
}
