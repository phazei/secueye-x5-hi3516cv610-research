package com.alibaba.sdk.android.openaccount.rpc.cloudapi;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.rpc.RpcServerBizConstants;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.EventListener;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ApiGatewayRpcServiceImpl implements RpcService, EnvironmentChangeListener, InitResultCallback {
    private static final String TAG = "oa_rpc";

    @Autowired
    private DeviceManager deviceManager;
    private SyncApiClient mApiClient;

    @Autowired
    private SecurityGuardService securityGuardService;

    @Autowired
    private UserTrackerService userTrackerService;
    private String vid;

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public String apiPrefix() {
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public void degradeDaily() {
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public String getSource() {
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
    public void onFailure(int i, String str) {
    }

    @Override // com.alibaba.sdk.android.openaccount.callback.InitResultCallback
    public void onSuccess() {
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public void registerSessionInfo(String str) {
    }

    public void init() {
        HttpClientBuilderParams httpClientBuilderParams = new HttpClientBuilderParams();
        if (ConfigManager.getInstance().isDegradeHttps()) {
            httpClientBuilderParams.setScheme(Scheme.HTTP);
        } else {
            httpClientBuilderParams.setScheme(Scheme.HTTPS);
        }
        httpClientBuilderParams.setHost(ConfigManager.getInstance().getApiGatewayHost());
        httpClientBuilderParams.setAppKey(this.securityGuardService.getAppKey());
        HostnameVerifier hostnameVerifier = new HostnameVerifier() { // from class: com.alibaba.sdk.android.openaccount.rpc.cloudapi.ApiGatewayRpcServiceImpl.1
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                if (TextUtils.isEmpty(str)) {
                    return false;
                }
                String lowerCase = str.toLowerCase();
                return lowerCase.endsWith("aliyun.com") || lowerCase.endsWith("aliyuncs.com") || str.equalsIgnoreCase(ConfigManager.getInstance().getApiGatewayHost());
            }
        };
        if (ConfigManager.getInstance().isDebugOKHttp()) {
            httpClientBuilderParams.setEventListenerFactory(new EventListener.Factory() { // from class: com.alibaba.sdk.android.openaccount.rpc.cloudapi.ApiGatewayRpcServiceImpl.2
                @Override // okhttp3.EventListener.Factory
                public EventListener create(Call call) {
                    return new HttpsEventListener();
                }
            });
        }
        httpClientBuilderParams.setSslSocketFactory(createSSLSocketFactory());
        httpClientBuilderParams.setX509TrustManager(new TrustAllManager());
        httpClientBuilderParams.setHostnameVerifier(hostnameVerifier);
        this.mApiClient = SyncApiClient.build(httpClientBuilderParams);
        SignerFactoryManager.registerSignerFactory("HmacSHA1", new SecuritySignerFactory(OpenAccountSDK.getAndroidContext()));
    }

    private X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException unused) {
            throw new AssertionError();
        }
    }

    private SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager x509TrustManager) {
        try {
            SSLContext sSLContext = SSLContext.getInstance(SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            sSLContext.init(null, new TrustManager[]{x509TrustManager}, null);
            return sSLContext.getSocketFactory();
        } catch (GeneralSecurityException unused) {
            throw new AssertionError();
        }
    }

    @SuppressLint({"TrulyRandom"})
    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sSLContext = SSLContext.getInstance(SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            sSLContext.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            return sSLContext.getSocketFactory();
        } catch (Exception unused) {
            return null;
        }
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        private TrustAllManager() {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            for (X509Certificate x509Certificate : x509CertificateArr) {
                x509Certificate.checkValidity();
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public RpcResponse invoke(RpcRequest rpcRequest) {
        if (!CommonUtils.isNetworkAvailable()) {
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.code = MessageConstants.NETWORK_NOT_AVAILABLE;
            rpcResponse.message = MessageUtils.getMessageContent(MessageConstants.NETWORK_NOT_AVAILABLE, new Object[0]);
            if (ConfigManager.getInstance().getApiHook() != null) {
                ConfigManager.getInstance().getApiHook().onInterceptResponse(rpcRequest, rpcResponse);
            }
            return rpcResponse;
        }
        return invokeInternal(rpcRequest);
    }

    private RpcResponse invokeInternal(RpcRequest rpcRequest) {
        RpcResponse rpcResponseOnInterceptRequest;
        if (ConfigManager.getInstance().getApiHook() != null && (rpcResponseOnInterceptRequest = ConfigManager.getInstance().getApiHook().onInterceptRequest(rpcRequest)) != null && rpcResponseOnInterceptRequest.code != 1) {
            return rpcResponseOnInterceptRequest;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        AliSDKLogger.d(TAG, "host = " + ConfigManager.getInstance().getApiGatewayHost());
        try {
            RpcResponse rpcResponseProcessMtopResponse = processMtopResponse(this.mApiClient.httpPostSync(Scheme.HTTPS.getValue(), ConfigManager.getInstance().getApiGatewayHost(), buildPath(rpcRequest), null, null, buildQueryParam(rpcRequest), null, buildHeader()), System.currentTimeMillis() - jCurrentTimeMillis);
            if (rpcResponseProcessMtopResponse.code == 26101) {
                Result<String> resultRefreshSession = ((OpenAccountSessionService) Pluto.DEFAULT_INSTANCE.getBean(OpenAccountSessionService.class)).refreshSession(true);
                if (resultRefreshSession.isSuccess()) {
                    rpcResponseProcessMtopResponse = processMtopResponse(this.mApiClient.httpPostSync(Scheme.HTTPS.getValue(), ConfigManager.getInstance().getApiGatewayHost(), buildPath(rpcRequest), null, null, buildQueryParam(rpcRequest), null, buildHeader()), System.currentTimeMillis() - System.currentTimeMillis());
                } else {
                    AliSDKLogger.e(TAG, "fail to refresh session code : " + resultRefreshSession.code + ", rpc retry is skipped");
                }
            } else {
                int i = rpcResponseProcessMtopResponse.code;
            }
            if (ConfigManager.getInstance().getApiHook() != null) {
                if (!ConfigManager.getInstance().getApiHook().onInterceptResponse(rpcRequest, rpcResponseProcessMtopResponse)) {
                    return null;
                }
            }
            return rpcResponseProcessMtopResponse;
        } catch (Exception e) {
            AliSDKLogger.e(TAG, "fail to execute rpc", e);
            return null;
        }
    }

    private Map<String, String> buildHeader() {
        HashMap map = new HashMap();
        if (ConfigManager.getInstance().getEnvironment() == Environment.PRE) {
            map.put("X-Ca-Stage", "PRE");
        } else if (ConfigManager.getInstance().getEnvironment() == Environment.TEST) {
            map.put("X-Ca-Stage", "TEST");
        } else {
            map.put("X-Ca-Stage", "");
        }
        if (OpenAccountConfigs.extraRpcHttpHeaders != null) {
            map.putAll(OpenAccountConfigs.extraRpcHttpHeaders);
        }
        String str = this.vid;
        if (str != null) {
            map.put("vid", str);
        }
        if (getSource() != null) {
            map.put("source", getSource());
        }
        String sessionId = ((SessionManagerService) Pluto.DEFAULT_INSTANCE.getBean(SessionManagerService.class)).getSessionId();
        if (sessionId != null) {
            map.put("sid", sessionId);
        }
        return map;
    }

    private String buildPath(RpcRequest rpcRequest) {
        String str = "prd";
        if (ConfigManager.getInstance().getEnvironment() == Environment.PRE) {
            str = "stg";
        } else if (ConfigManager.getInstance().getEnvironment() == Environment.TEST) {
            str = "test";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/api/" + str + "/" + rpcRequest.target + ".json");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("path=");
        sb2.append(sb.toString());
        AliSDKLogger.d(TAG, sb2.toString());
        return sb.toString();
    }

    private Map<String, String> buildQueryParam(RpcRequest rpcRequest) {
        HashMap map = new HashMap();
        Map<String, Object> map2 = rpcRequest.params;
        AliSDKLogger.d(TAG, "request = " + JSONUtils.toJson(map2));
        if (map2 != null) {
            for (Map.Entry<String, Object> entry : map2.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null && (value instanceof HashMap)) {
                    try {
                        map.put(key, JSONUtils.toJsonObject((HashMap) value).toString());
                    } catch (Throwable unused) {
                    }
                } else {
                    map.put(key, String.valueOf(value));
                }
            }
        }
        return map;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        this.vid = null;
    }

    private RpcResponse processMtopResponse(ApiResponse apiResponse, long j) {
        RpcResponse rpcResponse = new RpcResponse();
        if (apiResponse != null && apiResponse.getCode() == 200 && apiResponse.getBody() != null) {
            rpcResponse.code = apiResponse.getCode();
            try {
                String str = new String(apiResponse.getBody());
                JSONObject jSONObjectOptJSONObject = new JSONObject(str).optJSONObject("data");
                AliSDKLogger.d(TAG, "rawData = " + str);
                if (jSONObjectOptJSONObject != null) {
                    rpcResponse.code = jSONObjectOptJSONObject.optInt("code");
                    rpcResponse.subCode = jSONObjectOptJSONObject.optInt("subCode");
                    rpcResponse.message = jSONObjectOptJSONObject.optString("message");
                    rpcResponse.type = jSONObjectOptJSONObject.optString("type");
                    rpcResponse.traceId = jSONObjectOptJSONObject.optString("traceId");
                    rpcResponse.data = jSONObjectOptJSONObject.optJSONObject("data");
                    if (rpcResponse.data == null) {
                        rpcResponse.arrayData = jSONObjectOptJSONObject.optJSONArray("data");
                    }
                    String strOptString = JSONUtils.optString(jSONObjectOptJSONObject, "vid");
                    if (strOptString != null) {
                        this.vid = strOptString;
                    }
                    String strOptString2 = JSONUtils.optString(jSONObjectOptJSONObject, "deviceId");
                    if (!TextUtils.isEmpty(strOptString2)) {
                        this.deviceManager.setSdkDeviceId(strOptString2);
                    }
                    HashMap map = new HashMap();
                    map.put("code", String.valueOf(rpcResponse.code));
                    map.put("traceId", String.valueOf(rpcResponse.traceId));
                    map.put("msg", String.valueOf(rpcResponse.message));
                    this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_SUCCESS, map);
                } else {
                    HashMap map2 = new HashMap();
                    map2.put("msg", "null biz data");
                    this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_FAILED, map2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (apiResponse != null) {
            rpcResponse.code = MessageConstants.GENERIC_RPC_ERROR;
            rpcResponse.message = MessageUtils.getMessageContent(MessageConstants.GENERIC_RPC_ERROR, Integer.valueOf(apiResponse.getCode()));
            HashMap map3 = new HashMap();
            map3.put("httpCode", String.valueOf(apiResponse.getCode()));
            map3.put("code", apiResponse.getCode() + "");
            map3.put("msg", apiResponse.getMessage());
            this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_FAILED, map3);
            String string = "";
            try {
                if (apiResponse.getHeaders().get("X-Ca-Error-Message") != null) {
                    string = apiResponse.getHeaders().get("X-Ca-Error-Message").toString();
                }
            } catch (Exception unused) {
            }
            AliSDKLogger.e(TAG, "Rpc error header : " + apiResponse.getHeaders() + ", body string : " + apiResponse.getBodyStr() + ", exception : " + apiResponse.getEx());
            AliSDKLogger.e(TAG, "Rpc error message : " + apiResponse.getMessage() + ", retCode : " + apiResponse.getCode() + ", responseCode : " + apiResponse.getCode() + ", xcaError ： " + string);
        }
        normalizeRpcResponse(rpcResponse);
        return rpcResponse;
    }

    private void normalizeRpcResponse(RpcResponse rpcResponse) {
        if (rpcResponse != null && rpcResponse.code == 26251) {
            rpcResponse.code = RpcServerBizConstants.SESSION_EXPIRED;
        }
    }
}
