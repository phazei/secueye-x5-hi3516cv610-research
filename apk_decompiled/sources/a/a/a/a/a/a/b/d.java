package a.a.a.a.a.a.b;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttPublishRequest;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.facebook.share.internal.ShareConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: compiled from: MobileRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class d extends MqttPublishRequest {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static AtomicLong f1156d = new AtomicLong();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1157a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Map<String, Object> f1158b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, Object> f1159c;

    public d(boolean z, String str, Map<String, Object> map, Object obj) {
        e eVarB = f.b().b(null);
        if (str.startsWith("/sys/") || eVarB == null) {
            this.topic = str;
        } else {
            this.topic = "/sys/" + eVarB.f1161b + "/" + eVarB.f1162c + "/app/up/" + str;
            this.topic = this.topic.replace("//", "/");
        }
        this.isRPC = z;
        if (z) {
            this.replyTopic = this.topic.replace("/up/", "/down/") + TmpConstant.URI_TOPIC_REPLY_POST;
        }
        if (obj == null) {
            new HashMap();
        }
        this.f1157a = f1156d.incrementAndGet() + "";
        this.msgId = this.f1157a;
        if (this.f1158b == null) {
            this.f1158b = new HashMap();
        }
        this.f1158b.put("version", "1.0");
        this.f1158b.put("time", System.currentTimeMillis() + "");
        if (this.f1159c == null) {
            this.f1159c = new HashMap();
        }
        if (eVarB != null) {
            this.f1159c.put(TmpConstant.KEY_CLIENT_ID, eVarB.f1162c + "&" + eVarB.f1161b);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("id", (Object) this.f1157a);
        jSONObject.put("system", (Object) this.f1158b);
        jSONObject.put(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, (Object) this.f1159c);
        jSONObject.put("params", obj);
        this.payloadObj = jSONObject.toJSONString();
    }
}
