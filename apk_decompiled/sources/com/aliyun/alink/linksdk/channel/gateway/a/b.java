package com.aliyun.alink.linksdk.channel.gateway.a;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: compiled from: GatewayRequest.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends MqttPublishRequest {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AtomicLong f4175a = new AtomicLong();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f4176b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4177c = "1.0";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f4178d;
    private Object e;

    public b(boolean z, PersistentConnectConfig persistentConnectConfig, String str, String str2, Map<String, Object> map, Object obj) {
        if (!TextUtils.isEmpty(str) && !str.startsWith("/sys/") && !str.startsWith("/ota/") && persistentConnectConfig != null) {
            this.topic = "/sys/" + persistentConnectConfig.productKey + "/" + persistentConnectConfig.deviceName + "/" + str;
            this.topic = this.topic.replace("//", "/");
        } else {
            this.topic = str;
        }
        this.f4178d = str2;
        this.isRPC = z;
        this.replyTopic = this.topic + TmpConstant.URI_TOPIC_REPLY_POST;
        if (obj == null) {
            this.e = new HashMap();
        } else {
            this.e = obj;
        }
        if (map != null && map.containsKey("qos")) {
            try {
                this.qos = Integer.parseInt(String.valueOf(map.get("qos")));
            } catch (Exception e) {
                e.printStackTrace();
                this.qos = 0;
            }
        }
        this.f4176b = f4175a.incrementAndGet() + "";
        this.msgId = this.f4176b;
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("id", (Object) this.f4176b);
        jSONObject.put("version", (Object) this.f4177c);
        jSONObject.put("params", obj);
        jSONObject.put("method", (Object) str2);
        this.payloadObj = jSONObject.toJSONString();
    }
}
