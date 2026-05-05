package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;

/* JADX INFO: compiled from: MqttAlinkProtocolHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {
    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return JSONObject.parseObject(str).getString("id");
    }
}
