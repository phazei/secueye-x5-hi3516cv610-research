package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: IoTMqttManager.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: IoTMqttManager.java */
    public static class C0218a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final a f4146a = new a();
    }

    public static a a() {
        return C0218a.f4146a;
    }

    public boolean b(byte[] bArr) {
        try {
            JSONObject jSONObjectOptJSONObject = new JSONObject(new String(bArr, "UTF-8")).optJSONObject("params");
            if ("awss.BindNotify".equals(jSONObjectOptJSONObject.optString("identifier"))) {
                if ("Unbind".equalsIgnoreCase(jSONObjectOptJSONObject.optJSONObject("value").optString("operation"))) {
                    return true;
                }
            }
            return false;
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String c(byte[] bArr) {
        try {
            JSONObject jSONObjectOptJSONObject = new JSONObject(new String(bArr, "UTF-8")).optJSONObject("params").optJSONObject("value");
            if (jSONObjectOptJSONObject != null) {
                return jSONObjectOptJSONObject.toString();
            }
            return null;
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String d(byte[] bArr) {
        try {
            return new JSONObject(new String(bArr, "UTF-8")).optJSONObject("params").optJSONObject("value").toString();
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean a(byte[] bArr) {
        try {
            return "iotx.endpoint.app.datasync".equals(new JSONObject(new String(bArr, "UTF-8")).optJSONObject("params").optString("identifier"));
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void a(String str, String str2) {
        Context contextC = com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().c();
        Intent intent = new Intent(str);
        intent.putExtra(Utils.EXTRA_MQTT_DATA, str2);
        LocalBroadcastManager.getInstance(contextC).sendBroadcast(intent);
    }
}
