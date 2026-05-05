package a.a.a.a.a.a.b;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: compiled from: MobileAuthHttpRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static boolean f1133a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String f1134b = "";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f1135c = "1.0.0";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static Context f1136d = null;
    public static IoTAPIClient e = null;
    public static String f = null;
    public static String g = null;
    public static String h = null;
    public static String i = "";

    /* JADX INFO: compiled from: MobileAuthHttpRequest.java */
    public static class a implements IoTCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ InterfaceC0001b f1137a;

        public a(InterfaceC0001b interfaceC0001b) {
            this.f1137a = interfaceC0001b;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            a.a.a.a.a.a.a.a.b("MobileAuthHttpRequest", "onErrorResponse(), error = " + exc.toString());
            this.f1137a.a(exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            StringBuilder sb = new StringBuilder();
            sb.append("onResponse(),rsp = ");
            sb.append((ioTResponse == null || ioTResponse.getData() == null) ? "" : ioTResponse.getData().toString());
            a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", sb.toString());
            try {
                String message = ioTResponse.getMessage();
                int code = ioTResponse.getCode();
                JSONObject jSONObject = (JSONObject) ioTResponse.getData();
                if (code != 200) {
                    this.f1137a.a(message);
                    return;
                }
                String string = jSONObject.getString("deviceName");
                String string2 = jSONObject.getString("deviceSecret");
                String string3 = jSONObject.getString("productKey");
                if (TextUtils.isEmpty(string3) || TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
                    return;
                }
                e eVar = new e(string3, string, string2);
                eVar.f1160a = b.f1134b;
                this.f1137a.a(eVar);
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.b("MobileAuthHttpRequest", "onResponse(), error = " + e.toString());
                this.f1137a.a(e.toString());
            }
        }
    }

    /* JADX INFO: renamed from: a.a.a.a.a.a.b.b$b, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MobileAuthHttpRequest.java */
    public interface InterfaceC0001b {
        void a(e eVar);

        void a(String str);
    }

    /* JADX INFO: compiled from: MobileAuthHttpRequest.java */
    public static class c implements Tracker {
        public c() {
        }

        public static String a(IoTRequest ioTRequest) {
            StringBuilder sb = new StringBuilder("Request:");
            sb.append("\r\n");
            sb.append("url:");
            sb.append(ioTRequest.getScheme());
            sb.append(HttpConstant.SCHEME_SPLIT);
            sb.append(ioTRequest.getHost() == null ? "" : ioTRequest.getHost());
            sb.append(ioTRequest.getPath());
            sb.append("\r\n");
            sb.append("apiVersion:");
            sb.append(ioTRequest.getAPIVersion());
            sb.append("\r\n");
            sb.append("params:");
            sb.append(ioTRequest.getParams() == null ? "" : JSON.toJSONString(ioTRequest.getParams()));
            sb.append("\r\n");
            return sb.toString();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            a.a.a.a.a.a.a.a.c("APIGatewaySDKDelegate.Tracker", "onFailure:\r\n" + a(ioTRequest) + "ERROR-MESSAGE:" + exc.getMessage());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onRawFailure(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
            a.a.a.a.a.a.a.a.a("APIGatewaySDKDelegate.Tracker", "onRawFailure:\r\n" + a(ioTRequestWrapper) + "ERROR-MESSAGE:" + exc.getMessage());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onRawResponse(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
            a.a.a.a.a.a.a.a.a("APIGatewaySDKDelegate.Tracker", "onRawResponse:\r\n" + a(ioTRequestWrapper) + a(ioTResponse));
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onRealSend(IoTRequestWrapper ioTRequestWrapper) {
            a.a.a.a.a.a.a.a.a("APIGatewaySDKDelegate.Tracker", "onRealSend:\r\n" + a(ioTRequestWrapper));
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            a.a.a.a.a.a.a.a.c("APIGatewaySDKDelegate.Tracker", "onResponse:\r\n" + a(ioTRequest) + a(ioTResponse));
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
        public void onSend(IoTRequest ioTRequest) {
            a.a.a.a.a.a.a.a.c("APIGatewaySDKDelegate.Tracker", "onSend:\r\n" + a(ioTRequest));
        }

        public /* synthetic */ c(a aVar) {
            this();
        }

        public static String a(IoTRequestWrapper ioTRequestWrapper) {
            IoTRequest ioTRequest = ioTRequestWrapper.request;
            StringBuilder sb = new StringBuilder("Request:");
            sb.append("\r\n");
            sb.append("id:");
            sb.append(ioTRequestWrapper.payload.getId());
            sb.append("\r\n");
            sb.append("url:");
            sb.append(ioTRequest.getScheme());
            sb.append(HttpConstant.SCHEME_SPLIT);
            sb.append(ioTRequestWrapper.request.getHost());
            sb.append(ioTRequest.getPath());
            sb.append("\r\n");
            sb.append("apiVersion:");
            sb.append(ioTRequest.getAPIVersion());
            sb.append("\r\n");
            sb.append("params:");
            sb.append(ioTRequest.getParams() == null ? "" : JSON.toJSONString(ioTRequest.getParams()));
            sb.append("\r\n");
            return sb.toString();
        }

        public static String a(IoTResponse ioTResponse) {
            StringBuilder sb = new StringBuilder("Response:");
            sb.append("\r\n");
            sb.append("id:");
            sb.append(ioTResponse.getId());
            sb.append("\r\n");
            sb.append("code:");
            sb.append(ioTResponse.getCode());
            sb.append("\r\n");
            sb.append("message:");
            sb.append(ioTResponse.getMessage());
            sb.append("\r\n");
            sb.append("localizedMsg:");
            sb.append(ioTResponse.getLocalizedMsg());
            sb.append("\r\n");
            sb.append("data:");
            sb.append(ioTResponse.getData() == null ? "" : ioTResponse.getData().toString());
            sb.append("\r\n");
            return sb.toString();
        }
    }

    public static String b() {
        String defaultHost = IoTAPIClientImpl.getInstance().getDefaultHost();
        if (TextUtils.isEmpty(defaultHost)) {
            defaultHost = "api.link.aliyun.com";
        }
        a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "getDefaulHost(), " + defaultHost);
        return defaultHost;
    }

    public static void a(Context context, MobileConnectConfig mobileConnectConfig, InterfaceC0001b interfaceC0001b) {
        a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "request()");
        if (context == null || mobileConnectConfig == null || !mobileConnectConfig.checkValid() || interfaceC0001b == null) {
            return;
        }
        f1136d = context;
        h = mobileConnectConfig.appkey;
        Scheme scheme = Scheme.HTTPS;
        f1134b = b();
        if (!TextUtils.isEmpty(mobileConnectConfig.authServer)) {
            f1134b = mobileConnectConfig.authServer;
        }
        if (f1134b.equals("api-performance.aliplus.com")) {
            scheme = Scheme.HTTP;
        }
        if (!TextUtils.isEmpty(mobileConnectConfig.securityGuardAuthcode)) {
            i = mobileConnectConfig.securityGuardAuthcode;
        }
        if (f == null) {
            f = a.a.a.a.a.a.c.a.a(32);
        }
        a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "request(), deviceSn = " + f);
        if (g == null) {
            g = a.a.a.a.a.a.c.a.a(8);
        }
        HashMap map = new HashMap();
        String str = System.currentTimeMillis() + "";
        map.put("appKey", h);
        map.put("timestamp", str);
        map.put(TmpConstant.KEY_CLIENT_ID, g);
        map.put("deviceSn", f);
        String strA = a(map);
        a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "signed str = " + strA);
        if (!TextUtils.isEmpty(strA)) {
            map.put("sign", strA);
        }
        map.remove("appKey");
        try {
            IoTRequest ioTRequestBuild = new IoTRequestBuilder().setScheme(scheme).setHost(f1134b).setPath("/app/aepauth/handle").setApiVersion(f1135c).addParam("authInfo", (Map) map).build();
            if (e == null) {
                try {
                    IoTAPIClientImpl.InitializeConfig initializeConfig = new IoTAPIClientImpl.InitializeConfig();
                    initializeConfig.appKey = h;
                    initializeConfig.host = f1134b;
                    initializeConfig.apiEnv = Env.RELEASE;
                    if (f1133a) {
                        IoTAPIClientImpl.getInstance().setPerformanceTracker(new c(null));
                    }
                    IoTAPIClientImpl.getInstance().init(context, initializeConfig);
                } catch (Exception e2) {
                    a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "init api gateway error," + e2.toString());
                }
                e = new IoTAPIClientFactory().getClient();
            }
            e.send(ioTRequestBuild, new a(interfaceC0001b));
        } catch (Exception e3) {
            a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "request error, e = " + e3.toString());
            e3.printStackTrace();
            interfaceC0001b.a(e3.toString());
        }
    }

    public static String a(Map<String, String> map) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("appKey");
        arrayList.add(TmpConstant.KEY_CLIENT_ID);
        arrayList.add("deviceSn");
        arrayList.add("timestamp");
        String strA = "";
        for (String str : arrayList) {
            strA = a(strA, str, map.get(str));
        }
        a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "sign(), toSignStr = " + strA);
        if (Utils.hasSecurityGuardDep()) {
            try {
                ISecureSignatureComponent secureSignatureComp = SecurityGuardManager.getInstance(f1136d).getSecureSignatureComp();
                HashMap map2 = new HashMap();
                map2.put("INPUT", strA);
                SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                securityGuardParamContext.appKey = h;
                securityGuardParamContext.paramMap = map2;
                securityGuardParamContext.requestType = 3;
                try {
                    return secureSignatureComp.signRequest(securityGuardParamContext, i);
                } catch (SecException e2) {
                    a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "sign(),signe req error,e" + e2.toString());
                    e2.printStackTrace();
                    return null;
                }
            } catch (SecException e3) {
                a.a.a.a.a.a.a.a.a("MobileAuthHttpRequest", "sign(), create sg manager error, e" + e3.toString());
                e3.printStackTrace();
                return null;
            }
        }
        return new SecurityImpl().sign(strA, "HmacSHA1");
    }

    public static String a(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = objArr[i2];
            stringBuffer.append(obj != null ? obj.toString() : "");
        }
        return stringBuffer.toString();
    }
}
