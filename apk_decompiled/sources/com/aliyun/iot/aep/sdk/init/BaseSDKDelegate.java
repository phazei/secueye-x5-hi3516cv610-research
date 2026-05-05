package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityInit;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.page.rn.InitializationHelper;
import com.aliyun.alink.sdk.bone.plugins.BaseBoneServiceFactory;
import com.aliyun.alink.sdk.bone.plugins.config.BoneConfig;
import com.aliyun.iot.aep.page.rn.j;
import com.aliyun.iot.aep.routerexternal.RouterExternal;
import com.aliyun.iot.aep.routerexternal.RouterServiceFactory;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.APIGatewayHttpAdapterImpl;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import com.aliyun.iot.aep.sdk.bridge.core.BoneServiceFactoryRegistry;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class BaseSDKDelegate extends SimpleSDKDelegateImp {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        int i = -1 == a(application, sDKConfigure, map) ? -1 : 0;
        if (SDKManager.isRNAvailable()) {
            if (-1 == b(application, sDKConfigure, map)) {
                i = -1;
            }
        } else {
            ALog.e("APIGatewaySDKDelegate", "isRNAvailable false");
            BoneServiceFactoryRegistry.register(new BaseBoneServiceFactory());
            if (SDKManager.isRNLibAvailable()) {
                ALog.e("APIGatewaySDKDelegate", "isRNLibAvailable true");
                BoneServiceFactoryRegistry.register(new j());
            } else if (SDKManager.isRouterExternalLibAvailable()) {
                ALog.e("APIGatewaySDKDelegate", "isRouterExternalLibAvailable true");
                BoneServiceFactoryRegistry.register(new RouterServiceFactory());
            } else {
                ALog.e("APIGatewaySDKDelegate", "isRouterExternalLibAvailable false");
            }
        }
        if (-1 == c(application, sDKConfigure, map)) {
            i = -1;
        }
        DownStreamHelper.getInstance().initBreeze(application);
        return i;
    }

    int a(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        int errorCode = -1;
        try {
            errorCode = SecurityInit.Initialize(application);
        } catch (Exception e) {
            ALog.e("APIGatewaySDKDelegate", "e security-sdk-initialize-failed", e);
        } catch (JAQException e2) {
            ALog.e("APIGatewaySDKDelegate", "security-sdk-initialize-failed", (Exception) e2);
            errorCode = e2.getErrorCode();
        } catch (Throwable th) {
            ALog.e("APIGatewaySDKDelegate", "t security-sdk-initialize-failed" + th);
        }
        Env env = Env.RELEASE;
        String authCode = GlobalConfig.getInstance().getAuthCode();
        String language = GlobalConfig.getInstance().getLanguage();
        String storedApiAddress = RegionManager.getStoredApiAddress();
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        ALog.d("APIGatewaySDKDelegate", "host1:" + storedApiAddress);
        if ("pre".equals(apiEnv)) {
            env = Env.PRE;
        } else if ("test".equals(apiEnv)) {
            env = Env.TEST;
        }
        if (TextUtils.isEmpty(storedApiAddress)) {
            storedApiAddress = RegionManager.getApiAddr();
        }
        ALog.d("APIGatewaySDKDelegate", "host2:" + storedApiAddress);
        IoTAPIClientImpl.InitializeConfig initializeConfig = new IoTAPIClientImpl.InitializeConfig();
        if (TextUtils.isEmpty(storedApiAddress)) {
            storedApiAddress = "api.link.aliyun.com";
        }
        initializeConfig.host = storedApiAddress;
        initializeConfig.apiEnv = env;
        initializeConfig.authCode = authCode;
        if (GlobalConfig.getInstance().getInitConfig() != null) {
            initializeConfig.isDebug = GlobalConfig.getInstance().getInitConfig().isDebug();
        }
        IoTAPIClientImpl ioTAPIClientImpl = IoTAPIClientImpl.getInstance();
        ioTAPIClientImpl.init(application, initializeConfig);
        ioTAPIClientImpl.setLanguage(language);
        try {
            if (ioTAPIClientImpl.getOkHttpClient() != null && ioTAPIClientImpl.getOkHttpClient().dispatcher() != null) {
                ALog.i("APIGatewaySDKDelegate", "setMaxRequestsPerHost = 32.");
                ioTAPIClientImpl.getOkHttpClient().dispatcher().setMaxRequestsPerHost(32);
            } else {
                ALog.w("APIGatewaySDKDelegate", "okhttpclient == null, setMaxRequestsPerHost failed.");
            }
        } catch (Exception e3) {
            ALog.e("APIGatewaySDKDelegate", "setMaxRequestsPerHost failed. e=" + e3);
        }
        return errorCode;
    }

    int b(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        String str = IoTSmart.PRODUCT_ENV_PROD;
        String boneEnv = GlobalConfig.getInstance().getBoneEnv();
        String language = GlobalConfig.getInstance().getLanguage();
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        if ("PRE".equalsIgnoreCase(apiEnv)) {
            str = "test";
        } else if ("TEST".equalsIgnoreCase(apiEnv)) {
            str = "development";
        }
        String storedRegionName = RegionManager.getStoredRegionName();
        if (SDKManager.isRNAvailable() && !TextUtils.isEmpty(storedRegionName)) {
            try {
                BoneConfig.set("region", storedRegionName);
            } catch (Throwable th) {
                ALog.e("APIGatewaySDKDelegate", "BoneConfig. t=" + th);
            }
        }
        RouterExternal.getInstance().init(application, boneEnv);
        InitializationHelper.initialize(application, boneEnv, str, language);
        return 0;
    }

    int c(Application application, SDKConfigure sDKConfigure, Map<String, String> map) throws JSONException {
        boolean z;
        boolean z2;
        MobileConnectConfig mobileConnectConfig = new MobileConnectConfig();
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        String authCode = GlobalConfig.getInstance().getAuthCode();
        mobileConnectConfig.appkey = APIGatewayHttpAdapterImpl.getAppKey(application, authCode);
        mobileConnectConfig.securityGuardAuthcode = authCode;
        JSONObject jSONObject = sDKConfigure.opts;
        JSONObject jSONObject2 = null;
        if (jSONObject != null) {
            try {
                jSONObject2 = jSONObject.getJSONObject(apiEnv.toLowerCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String string = "";
        if (jSONObject2 != null) {
            try {
                string = jSONObject2.getString("channelHost");
                z = !RequestConstant.FALSE.equalsIgnoreCase(jSONObject2.getString("autoSelectChannelHost"));
                try {
                    z2 = !RequestConstant.FALSE.equalsIgnoreCase(jSONObject2.getString("isCheckChannelRootCrt"));
                } catch (JSONException e2) {
                    e = e2;
                    e.printStackTrace();
                    z2 = false;
                }
            } catch (JSONException e3) {
                e = e3;
                z = false;
            }
        } else {
            z2 = false;
            z = false;
        }
        String storedMqttAddress = RegionManager.getStoredMqttAddress();
        if (!TextUtils.isEmpty(storedMqttAddress)) {
            string = storedMqttAddress;
        }
        mobileConnectConfig.autoSelectChannelHost = z;
        mobileConnectConfig.channelHost = string;
        mobileConnectConfig.isCheckChannelRootCrt = z2;
        DownStreamHelper.getInstance().startMqttOne(mobileConnectConfig);
        ALog.d("APIGatewaySDKDelegate", "initialized");
        return 0;
    }
}
