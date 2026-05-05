package com.aliyun.iot.aep.sdk.apiclient.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.IoTHttpClientAdapterConfig;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponseImpl;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class APIGatewayHttpAdapterImpl implements IoTHttpClientAdapter {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static SecurityImpl f4543d = new SecurityImpl();
    public static int e = -1;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public e f4544a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IoTHttpClientAdapterConfig f4545b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Set<String> f4546c;

    public class a implements EventListener.Factory {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTHttpClientAdapterConfig f4547a;

        public a(APIGatewayHttpAdapterImpl aPIGatewayHttpAdapterImpl, IoTHttpClientAdapterConfig ioTHttpClientAdapterConfig) {
            this.f4547a = ioTHttpClientAdapterConfig;
        }

        @Override // okhttp3.EventListener.Factory
        public EventListener create(Call call) {
            return this.f4547a.getEventListener();
        }
    }

    public class b implements EventListener.Factory {
        public b(APIGatewayHttpAdapterImpl aPIGatewayHttpAdapterImpl) {
        }

        @Override // okhttp3.EventListener.Factory
        public EventListener create(Call call) {
            return new com.aliyun.iot.aep.sdk.apiclient.a();
        }
    }

    public class c implements HostnameVerifier {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTHttpClientAdapterConfig f4548a;

        public c(IoTHttpClientAdapterConfig ioTHttpClientAdapterConfig) {
            this.f4548a = ioTHttpClientAdapterConfig;
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            Log.d("APIGatewayImpl", " isDebug" + this.f4548a.isDebug());
            if (this.f4548a.isDebug()) {
                return true;
            }
            if (str == null) {
                Log.d("APIGatewayImpl", "hostname is null.");
                return false;
            }
            if (!APIGatewayHttpAdapterImpl.this.f4546c.contains(str)) {
                return false;
            }
            Log.d("APIGatewayImpl", "in verified set.");
            return true;
        }
    }

    public class d implements ApiCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTCallback f4550a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ IoTRequest f4551b;

        public d(APIGatewayHttpAdapterImpl aPIGatewayHttpAdapterImpl, IoTCallback ioTCallback, IoTRequest ioTRequest) {
            this.f4550a = ioTCallback;
            this.f4551b = ioTRequest;
        }

        @Override // com.alibaba.cloudapi.sdk.model.ApiCallback
        public void onFailure(ApiRequest apiRequest, Exception exc) {
            this.f4550a.onFailure(this.f4551b, exc);
        }

        @Override // com.alibaba.cloudapi.sdk.model.ApiCallback
        public void onResponse(ApiRequest apiRequest, ApiResponse apiResponse) {
            IoTResponseImpl ioTResponseImpl = new IoTResponseImpl();
            int code = apiResponse.getCode();
            ioTResponseImpl.setCode(code);
            ioTResponseImpl.setExtraResponseData(apiResponse);
            if (200 == code) {
                ioTResponseImpl.setRawData(apiResponse.getBody());
                try {
                    ioTResponseImpl.setData(new JSONObject(new String(apiResponse.getBody(), "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    this.f4550a.onFailure(this.f4551b, e);
                    return;
                } catch (JSONException e2) {
                    this.f4550a.onFailure(this.f4551b, e2);
                    return;
                }
            } else {
                ioTResponseImpl.setMessage(apiResponse.getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_ERROR_MESSAGE));
                ioTResponseImpl.setLocalizedMsg("服务器繁忙，请稍后试试！");
            }
            this.f4550a.onResponse(this.f4551b, ioTResponseImpl);
        }
    }

    public static class e extends HttpApiClient {
        public e() {
        }

        public OkHttpClient a() {
            Object objA = a(this, "client");
            if (objA instanceof OkHttpClient) {
                return (OkHttpClient) objA;
            }
            return null;
        }

        @Override // com.alibaba.cloudapi.sdk.client.HttpApiClient, com.alibaba.cloudapi.sdk.client.BaseApiClient
        public void sendAsyncRequest(ApiRequest apiRequest, ApiCallback apiCallback) {
            super.sendAsyncRequest(apiRequest, apiCallback);
        }

        public /* synthetic */ e(a aVar) {
            this();
        }

        public final Object a(Object obj, String str) {
            try {
                Class<? super Object> superclass = obj.getClass().getSuperclass();
                Log.i("IoTHttpAPIClient", "getField: " + superclass.getDeclaredFields());
                Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static class f implements X509TrustManager {
        public f() {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateNotYetValidException, CertificateExpiredException {
            if (x509CertificateArr == null) {
                throw new IllegalArgumentException("check Server x509Certificates is null");
            }
            if (x509CertificateArr.length < 0) {
                throw new IllegalArgumentException("check Server x509Certificates is empty");
            }
            Log.d("APIGatewayImpl", "checkServerTrusted:" + x509CertificateArr);
            for (X509Certificate x509Certificate : x509CertificateArr) {
                x509Certificate.checkValidity();
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public /* synthetic */ f(a aVar) {
            this();
        }
    }

    @Deprecated
    public APIGatewayHttpAdapterImpl(Application application, String str, String str2, Env env, String str3) {
        this(application, new IoTHttpClientAdapterConfig.Builder().setAppKey(str).setDefaultHost(str3).setApiEnv(env).setAuthCode(str2).build());
    }

    public static boolean b() {
        try {
            Class.forName("com.alibaba.wireless.security.open.SecurityGuardManager");
            return true;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            return false;
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public static void checkSecurityPicture(Context context, String str) {
        if (context.getResources().getIdentifier("yw_1222_" + str, CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, context.getPackageName()) == 0) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "can not find yw_1222_%s.jpg", str));
        }
    }

    public static String getAppKey(Context context, String str) {
        IStaticDataStoreComponent staticDataStoreComp;
        if (!b()) {
            String appKey = f4543d.getAppKey();
            Log.d("APIGatewayImpl", "getAppKey:" + appKey);
            return appKey;
        }
        try {
            SecurityGuardManager.setSilentMode(true);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
            if (securityGuardManager == null || (staticDataStoreComp = securityGuardManager.getStaticDataStoreComp()) == null) {
                return "";
            }
            return staticDataStoreComp.getAppKeyByIndex(e < 0 ? 0 : e, str);
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static void setAppKeyGetIndex(int i) {
        e = i;
    }

    public OkHttpClient getOkHttpClient() {
        e eVar = this.f4544a;
        if (eVar != null) {
            return eVar.a();
        }
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.adapter.IoTHttpClientAdapter
    public void send(IoTRequestWrapper ioTRequestWrapper, IoTCallback ioTCallback) {
        IoTRequest ioTRequest = ioTRequestWrapper.request;
        Method method = ioTRequest.getMethod();
        HttpMethod httpMethod = HttpMethod.POST_BODY;
        if (Method.GET == method) {
            httpMethod = HttpMethod.GET;
        } else if (Method.DELETE == method) {
            httpMethod = HttpMethod.DELETE;
        }
        Scheme scheme = Scheme.HTTPS;
        String host = ioTRequest.getHost();
        if (TextUtils.isEmpty(host)) {
            host = this.f4545b.getDefaultHost();
        }
        if (!TextUtils.isEmpty(host)) {
            this.f4546c.add(host);
        }
        String path = ioTRequest.getPath();
        if (com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme.HTTP == ioTRequest.getScheme()) {
            scheme = Scheme.HTTP;
        }
        HashMap map = new HashMap();
        map.put("x-ca-request-id", ioTRequest.getId());
        ApiRequest apiRequest = new ApiRequest(httpMethod, path);
        apiRequest.setHttpConnectionMode(HttpConnectionModel.MULTIPLE_CONNECTION);
        apiRequest.setScheme(scheme);
        apiRequest.setHost(host);
        apiRequest.setQuerys(map);
        apiRequest.setSignatureMethod("HmacSHA1");
        if (Env.RELEASE != this.f4545b.getApiEnv()) {
            apiRequest.addHeader("X-Ca-Stage", this.f4545b.getApiEnv().toString());
        }
        if (HttpMethod.POST_BODY != httpMethod) {
            throw new UnsupportedOperationException("目前仅支持 POST 方法");
        }
        apiRequest.setBody(ioTRequestWrapper.buildBody());
        try {
            this.f4544a.sendAsyncRequest(apiRequest, new d(this, ioTCallback, ioTRequest));
        } catch (Throwable th) {
            th.printStackTrace();
            if (ioTCallback != null) {
                ioTCallback.onFailure(ioTRequest, new Exception(th));
            }
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.adapter.IoTHttpClientAdapter
    public void setDefaultHost(String str) {
        IoTHttpClientAdapterConfig ioTHttpClientAdapterConfig = this.f4545b;
        if (ioTHttpClientAdapterConfig != null) {
            ioTHttpClientAdapterConfig.setDefaultHost(str);
        }
    }

    @SuppressLint({"TrulyRandom"})
    public static SSLSocketFactory a() {
        a aVar = null;
        try {
            SSLContext sSLContext = SSLContext.getInstance(SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            sSLContext.init(null, new TrustManager[]{new f(aVar)}, new SecureRandom());
            return sSLContext.getSocketFactory();
        } catch (Exception unused) {
            return null;
        }
    }

    public APIGatewayHttpAdapterImpl(Application application, IoTHttpClientAdapterConfig ioTHttpClientAdapterConfig) {
        this.f4546c = new HashSet();
        a aVar = null;
        this.f4544a = new e(aVar);
        HttpClientBuilderParams httpClientBuilderParams = new HttpClientBuilderParams();
        httpClientBuilderParams.setAppKey(ioTHttpClientAdapterConfig.getAppKey());
        httpClientBuilderParams.setAppSecret("123");
        httpClientBuilderParams.setHost(TextUtils.isEmpty(ioTHttpClientAdapterConfig.getDefaultHost()) ? "123" : ioTHttpClientAdapterConfig.getDefaultHost());
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setConnectionTimeout(ioTHttpClientAdapterConfig.getConnectTimeout());
        httpClientBuilderParams.setReadTimeout(ioTHttpClientAdapterConfig.getReadTimeout());
        httpClientBuilderParams.setWriteTimeout(ioTHttpClientAdapterConfig.getWriteTimeout());
        if (ioTHttpClientAdapterConfig.getEventListener() != null) {
            httpClientBuilderParams.setEventListenerFactory(new a(this, ioTHttpClientAdapterConfig));
        } else if (ioTHttpClientAdapterConfig.isDebug()) {
            Log.i("APIGatewayImpl", "set default okhttp event listener on debug situation.");
            httpClientBuilderParams.setEventListenerFactory(new b(this));
        }
        httpClientBuilderParams.setSocketFactory(ioTHttpClientAdapterConfig.getSocketFactory());
        httpClientBuilderParams.setHttpConnectionRetry(ioTHttpClientAdapterConfig.isHttpConnectionRetry());
        Log.d("APIGatewayImpl", "xx default host:" + ioTHttpClientAdapterConfig.getDefaultHost() + " isDebug" + ioTHttpClientAdapterConfig.isDebug());
        httpClientBuilderParams.setSslSocketFactory(a());
        httpClientBuilderParams.setX509TrustManager(new f(aVar));
        httpClientBuilderParams.setHostnameVerifier(new c(ioTHttpClientAdapterConfig));
        this.f4544a.init(httpClientBuilderParams);
        this.f4545b = ioTHttpClientAdapterConfig;
        SignerFactoryManager.registerSignerFactory("HmacSHA1", new com.aliyun.iot.aep.sdk.apiclient.b(application, ioTHttpClientAdapterConfig.getAppKey(), ioTHttpClientAdapterConfig.getAuthCode()));
    }
}
