package com.aliyun.iot.aep.sdk.framework.region;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.hook.OAApiHook;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.page.rn.router.RouterManager;
import com.aliyun.alink.sdk.bone.plugins.config.BoneConfig;
import com.aliyun.iot.aep.component.bundlemanager.BundleManager;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTAuthProvider;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTRequestPayloadCallback;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.base.ApiDataCallBack;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.network.BaseApiClient;
import com.aliyun.iot.aep.sdk.framework.region.RegionQueryApi;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.init.DownStreamHelper;
import com.aliyun.iot.aep.sdk.init.PushManagerHelper;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.xiaomi.mipush.sdk.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class RegionManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f4720a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f4721b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static List<IRegionChangeListener> f4722c = new ArrayList();

    public interface IRegionChangeListener {
        void onRegionChange(RegionInfo2 regionInfo2);
    }

    private static boolean a(String str, ApiDataCallBack apiDataCallBack) {
        return false;
    }

    public static void registerOAApiHook() {
        ConfigManager.getInstance().setApiHook(new OAApiHook() { // from class: com.aliyun.iot.aep.sdk.framework.region.RegionManager.1
            @Override // com.alibaba.sdk.android.openaccount.hook.OAApiHook
            public RpcResponse onInterceptRequest(RpcRequest rpcRequest) {
                ALog.d("RegionManager", rpcRequest.toString());
                return RegionManager.handleOARequest(rpcRequest);
            }

            @Override // com.alibaba.sdk.android.openaccount.hook.OAApiHook
            public boolean onInterceptResponse(RpcRequest rpcRequest, RpcResponse rpcResponse) {
                ALog.d("RegionManager", rpcResponse.toString());
                return true;
            }
        });
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse handleOARequest(com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest r8) {
        /*
            Method dump skipped, instruction units count: 426
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.aep.sdk.framework.region.RegionManager.handleOARequest(com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest):com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse");
    }

    private static void a(Map<String, Object> map) {
        String currentCountryAbbr = CountryManager.getCurrentCountryAbbr();
        if (TextUtils.isEmpty(currentCountryAbbr) || map == null) {
            return;
        }
        map.put("country", currentCountryAbbr);
    }

    private static String a(String str) {
        return c(str) ? DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL : "PHONE";
    }

    private static String b(String str) {
        if (c(str)) {
            return "";
        }
        String[] strArrSplit = str.split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        String str2 = strArrSplit.length > 1 ? strArrSplit[0] : "";
        return TextUtils.isEmpty(str2) ? "86" : str2;
    }

    private static boolean c(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.contains("@");
    }

    public static void queryRegRegion(ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "queryRegRegion");
        a(RegionQueryApi.Request.getRegRequest(), apiDataCallBack);
    }

    public static void queryLoginRegion(String str, String str2, String str3, ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "queryLoginRegion");
        if (a(str2, apiDataCallBack) || a(str, apiDataCallBack)) {
            return;
        }
        a(RegionQueryApi.Request.getLoginRequest(str, str2, str3), apiDataCallBack);
    }

    public static void queryThirdLoginRegion(String str, String str2, ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "queryGoogleLoginRegion");
        if (a(str2, apiDataCallBack) || a(str, apiDataCallBack)) {
            return;
        }
        a(RegionQueryApi.Request.getThirdLoginRequest(str, str2), apiDataCallBack);
    }

    private static void a(final RegionQueryApi.Request request, final ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "getRegionFromServer");
        if (request == null) {
            return;
        }
        String str = GlobalConfig.getInstance().getCountry() != null ? GlobalConfig.getInstance().getCountry().domainAbbreviation : "";
        request.setCountryCode(str);
        final String str2 = ((str != null && !str.isEmpty() && str.equals(CountryManager.COUNTRY_CHINA_ABBR)) || ((request.type == null || request.type.isEmpty() || !request.type.equals("PHONE") || request.phoneLocationCode == null || request.phoneLocationCode.isEmpty() || !request.phoneLocationCode.equals("86")) ? false : true)) ? RegionQueryApi.host : RegionQueryApi.global_host;
        b();
        BaseApiClient.send(str2, RegionQueryApi.path, "1.0.2", RegionQueryApi.authType, request, new ApiDataCallBack<RegionInfo2>() { // from class: com.aliyun.iot.aep.sdk.framework.region.RegionManager.3
            @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(RegionInfo2 regionInfo2) {
                ALog.d("RegionManager", regionInfo2 == null ? TmpConstant.GROUP_ROLE_UNKNOWN : regionInfo2.toString());
                RegionManager.b(regionInfo2);
                ApiDataCallBack apiDataCallBack2 = apiDataCallBack;
                if (apiDataCallBack2 != null) {
                    apiDataCallBack2.onSuccess(regionInfo2);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
            public void onFail(int i, String str3) {
                ALog.d("RegionManager", "getRegion   code:" + i + "   msg:" + str3);
                if (str2.equals(RegionQueryApi.global_host)) {
                    BaseApiClient.send(RegionQueryApi.host, RegionQueryApi.path, "1.0.2", RegionQueryApi.authType, request, new ApiDataCallBack<RegionInfo2>() { // from class: com.aliyun.iot.aep.sdk.framework.region.RegionManager.3.1
                        @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
                        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                        public void onSuccess(RegionInfo2 regionInfo2) {
                            ALog.d("RegionManager", regionInfo2 == null ? TmpConstant.GROUP_ROLE_UNKNOWN : regionInfo2.toString());
                            RegionManager.b(regionInfo2);
                            if (apiDataCallBack != null) {
                                apiDataCallBack.onSuccess(regionInfo2);
                            }
                        }

                        @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
                        public void onFail(int i2, String str4) {
                            ALog.d("RegionManager", str4);
                            if (apiDataCallBack != null) {
                                apiDataCallBack.onFail(i2, str4);
                            }
                        }
                    });
                    return;
                }
                ApiDataCallBack apiDataCallBack2 = apiDataCallBack;
                if (apiDataCallBack2 != null) {
                    apiDataCallBack2.onFail(i, str3);
                }
            }
        });
    }

    private static void b() {
        if (f4720a || IoTAPIClientImpl.getInstance().hasIoTAUthProvider(RegionQueryApi.authType)) {
            return;
        }
        IoTAPIClientImpl.getInstance().registerIoTAuthProvider(RegionQueryApi.authType, new IoTAuthProvider() { // from class: com.aliyun.iot.aep.sdk.framework.region.RegionManager.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook
            public void onInterceptSend(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTRequestPayloadCallback ioTRequestPayloadCallback) {
                ioTRequestPayloadCallback.onIoTRequestPayloadReady();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook
            public void onInterceptResponse(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTResponse ioTResponse, IoTCallback ioTCallback) {
                ioTCallback.onResponse(ioTRequest, ioTResponse);
            }
        });
        f4720a = true;
    }

    public static void setInterceptOA(boolean z) {
        f4721b = z;
    }

    public static boolean isInterceptOA() {
        return f4721b;
    }

    public static void setRegionInfo(RegionInfo2 regionInfo2) {
        ALog.d("RegionManager", "setRegionInfo:" + regionInfo2);
        if (regionInfo2 != null) {
            b(regionInfo2);
        }
    }

    public static void setRegionChina(ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "setRegionChina");
        RegionInfo2 regionChina = getRegionChina();
        b(regionChina);
        if (apiDataCallBack != null) {
            apiDataCallBack.onSuccess(regionChina);
        }
    }

    public static RegionInfo2 getRegionChina() {
        ALog.d("RegionManager", "getRegionChina");
        RegionInfo2 regionInfo2 = new RegionInfo2();
        regionInfo2.regionId = "china";
        regionInfo2.oaApiGatewayEndpoint = "sdk.openaccount.aliyun.com";
        regionInfo2.apiGatewayEndpoint = getApiAddr();
        regionInfo2.mqttEndpoint = c();
        regionInfo2.regionEnglishName = "china";
        return regionInfo2;
    }

    private static String c() {
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        return "test".equals(apiEnv) ? "iot-test-daily.iot-as-mqtt.unify.aliyuncs.com:1883" : "pre".equals(apiEnv) ? "ssl://100.67.80.75:80" : "";
    }

    public static String getApiAddr() {
        return "test".equals(GlobalConfig.getInstance().getApiEnv()) ? "api-performance.aliplus.com" : "api.link.aliyun.com";
    }

    public static void setRegionSingapore(ApiDataCallBack apiDataCallBack) {
        ALog.d("RegionManager", "setRegionSingapore");
        RegionInfo2 regionInfo2 = new RegionInfo2();
        regionInfo2.regionId = "";
        regionInfo2.oaApiGatewayEndpoint = "";
        regionInfo2.apiGatewayEndpoint = "";
        regionInfo2.mqttEndpoint = "";
        regionInfo2.regionEnglishName = "Singapore";
        b(regionInfo2);
        if (apiDataCallBack != null) {
            apiDataCallBack.onSuccess(regionInfo2);
        }
    }

    public static RegionInfo2 getStoredRegion() {
        ALog.d("RegionManager", "getStoredRegion");
        return GlobalConfig.getInstance().getRegionInfo();
    }

    public static String getStoredApiAddress() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.apiGatewayEndpoint;
    }

    public static String getStoredOAAddress() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.oaApiGatewayEndpoint;
    }

    public static String getStoredMqttAddress() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.mqttEndpoint;
    }

    public static String getStoredRegionName() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.regionEnglishName;
    }

    public static String getStoredPushAddress() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.pushChannelEndpoint;
    }

    public static String getStoredShortRegionId() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion == null) {
            return null;
        }
        return storedRegion.shortRegionId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(RegionInfo2 regionInfo2) {
        if (regionInfo2 == null || regionInfo2.isEqual(GlobalConfig.getInstance().getRegionInfo())) {
            return;
        }
        c(regionInfo2);
        doInitAfterRegionChange(regionInfo2);
    }

    private static void c(RegionInfo2 regionInfo2) {
        ALog.d("RegionManager", "storeRegion");
        if (regionInfo2 == null) {
            return;
        }
        GlobalConfig.getInstance().setRegionInfo(regionInfo2);
    }

    public static void doInitAfterRegionChange(final RegionInfo2 regionInfo2) {
        ALog.d("RegionManager", "doInitAfterRegionChange");
        if (regionInfo2 == null) {
            return;
        }
        if (!TextUtils.isEmpty(regionInfo2.apiGatewayEndpoint)) {
            IoTAPIClientImpl.getInstance().setDefaultHost(regionInfo2.apiGatewayEndpoint);
        }
        if (!TextUtils.isEmpty(regionInfo2.oaApiGatewayEndpoint)) {
            ConfigManager.getInstance().setApiGatewayHost(regionInfo2.oaApiGatewayEndpoint);
        }
        d(regionInfo2);
        if (SDKManager.isRNAvailable()) {
            String str = BoneConfig.get("region");
            if (!TextUtils.isEmpty(regionInfo2.regionEnglishName) && !TextUtils.equals(str, regionInfo2.regionEnglishName)) {
                try {
                    RouterManager.getInstance().clear();
                    BundleManager.getInstance().clearAll();
                } catch (Exception e) {
                    ALog.e("RegionManager", "doInitAfterRegionChange", e);
                }
                BoneConfig.set("region", regionInfo2.regionEnglishName);
            }
        }
        e(regionInfo2);
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.framework.region.RegionManager.5
            @Override // java.lang.Runnable
            public void run() {
                if (RegionManager.f4722c == null || RegionManager.f4722c.isEmpty()) {
                    return;
                }
                for (IRegionChangeListener iRegionChangeListener : RegionManager.f4722c) {
                    if (iRegionChangeListener != null) {
                        iRegionChangeListener.onRegionChange(regionInfo2);
                    }
                }
            }
        });
    }

    private static void d(RegionInfo2 regionInfo2) {
        DownStreamHelper.getInstance().startMqtt(regionInfo2.mqttEndpoint);
    }

    private static void e(RegionInfo2 regionInfo2) {
        PushManagerHelper.getInstance().initPush(AApplication.getInstance(), regionInfo2.regionEnglishName);
    }

    public static void registerRegionChangeListener(IRegionChangeListener iRegionChangeListener) {
        if (iRegionChangeListener != null) {
            f4722c.add(iRegionChangeListener);
        }
    }

    public static void unRegisterRegionChangeListener(IRegionChangeListener iRegionChangeListener) {
        if (iRegionChangeListener != null) {
            try {
                f4722c.remove(iRegionChangeListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isChinaRegion() {
        RegionInfo2 storedRegion = getStoredRegion();
        if (storedRegion != null) {
            return com.aliyun.alink.apiclient.constants.Constants.IOT_REGION_ID_DEFAULT.equals(storedRegion.regionId) || "china".equals(storedRegion.regionEnglishName);
        }
        return false;
    }
}
