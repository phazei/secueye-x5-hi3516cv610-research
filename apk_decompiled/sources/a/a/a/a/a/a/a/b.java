package a.a.a.a.a.a.a;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: compiled from: DynamicHostRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f1114a = "https://iot-auth.ap-southeast-1.aliyuncs.com/auth/resource/mqtt";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String f1115b = "";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static OkHttpClient f1116c;

    /* JADX INFO: compiled from: DynamicHostRequest.java */
    public static class a implements Callback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ InterfaceC0000b f1117a;

        public a(InterfaceC0000b interfaceC0000b) {
            this.f1117a = interfaceC0000b;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            StringBuilder sb = new StringBuilder();
            sb.append("onFailure, e =");
            sb.append(iOException != null ? iOException.toString() : TmpConstant.GROUP_ROLE_UNKNOWN);
            a.a.a.a.a.a.a.a.a("DynamicHostRequest", sb.toString());
            InterfaceC0000b interfaceC0000b = this.f1117a;
            if (interfaceC0000b != null) {
                interfaceC0000b.b();
            }
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) {
            a.a.a.a.a.a.a.a.a("DynamicHostRequest", "onResponse()");
            try {
                String strString = response.body().string();
                a.a.a.a.a.a.a.a.a("DynamicHostRequest", "onResponse(), rsp = " + strString);
                JSONObject object = JSONObject.parseObject(strString);
                String string = object.getJSONObject("data").getJSONObject("resources").getJSONObject("mqtt").getString("host");
                int intValue = object.getJSONObject("data").getJSONObject("resources").getJSONObject("mqtt").getIntValue(RtspHeaders.Values.PORT);
                if (!TextUtils.isEmpty(string) && intValue != 0) {
                    String unused = b.f1115b = string + ":" + intValue;
                    a.a.a.a.a.a.a.a.a("DynamicHostRequest", "onResponse(), host = " + b.f1115b);
                    MqttConfigure.mqttHost = b.f1115b;
                    if (this.f1117a != null) {
                        this.f1117a.a();
                        return;
                    }
                    return;
                }
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("DynamicHostRequest", "onResponse(), error = " + e.toString());
            }
            InterfaceC0000b interfaceC0000b = this.f1117a;
            if (interfaceC0000b != null) {
                interfaceC0000b.b();
            }
        }
    }

    /* JADX INFO: renamed from: a.a.a.a.a.a.a.b$b, reason: collision with other inner class name */
    /* JADX INFO: compiled from: DynamicHostRequest.java */
    public interface InterfaceC0000b {
        void a();

        void b();
    }

    public static void a(InterfaceC0000b interfaceC0000b) {
        a.a.a.a.a.a.a.a.a("DynamicHostRequest", "action()");
        if (!TextUtils.isEmpty(f1115b)) {
            a.a.a.a.a.a.a.a.a("DynamicHostRequest", "host is not empty, " + f1115b);
            MqttConfigure.mqttHost = f1115b;
            return;
        }
        if (f1116c == null) {
            f1116c = new OkHttpClient();
        }
        f1116c.newCall(new Request.Builder().url(f1114a).build()).enqueue(new a(interfaceC0000b));
    }
}
