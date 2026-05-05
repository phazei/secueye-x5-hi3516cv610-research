package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ui.LayoutMapping;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.IoTSmartImpl;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.APIGatewayHttpAdapterImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;
import com.aliyun.iot.aep.sdk.credential.IoTCredentialProviderImpl;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTAuthApiHook;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.framework.utils.BroadCastUtil;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class OpenAccountSDKDelegate extends SimpleSDKDelegateImp {
    public static final String ACTION_OA_INIT_DONE = "ACTION_OA_INIT_DONE";
    public static final String SUCCESS = "success";

    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(final Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        String authCode = GlobalConfig.getInstance().getAuthCode();
        String appKey = APIGatewayHttpAdapterImpl.getAppKey(application, authCode);
        JSONObject jSONObject = sDKConfigure.opts;
        if (SDKManager.isOAAvailable()) {
            OALoginAdapter oALoginAdapter = new OALoginAdapter(application);
            String storedOAAddress = RegionManager.getStoredOAAddress();
            if (!TextUtils.isEmpty(storedOAAddress)) {
                oALoginAdapter.setDefaultOAHost(storedOAAddress);
            }
            oALoginAdapter.init(apiEnv, authCode, new OALoginAdapter.OALoginAdapterInitResultCallback() { // from class: com.aliyun.iot.aep.sdk.init.OpenAccountSDKDelegate.1
                @Override // com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.OALoginAdapterInitResultCallback
                public void onInitSuccess() {
                    ALog.d("OpenAccountSDKDelegate", "onInitSuccess");
                    OpenAccountSDKDelegate.this.a((Context) application, true);
                }

                @Override // com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.OALoginAdapterInitResultCallback
                public void onInitFailed(int i, String str) {
                    ALog.d("OpenAccountSDKDelegate", "onInitFailed:" + i + str);
                    OpenAccountSDKDelegate.this.a((Context) application, false);
                }
            });
            LoginBusiness.init(application, oALoginAdapter, apiEnv);
            oALoginAdapter.setIsDebuggable(GlobalConfig.getInstance().getInitConfig().isDebug());
            try {
                a(application, oALoginAdapter, jSONObject);
            } catch (Exception e) {
                ALog.i("OpenAccountSDKDelegate", "Inject CustomUIConfig failed:" + e.toString());
            }
        }
        if (jSONObject != null) {
            try {
                if (jSONObject.has("disable_screen_protrait")) {
                    "true".equalsIgnoreCase(jSONObject.getString("disable_screen_protrait"));
                }
            } catch (Exception unused) {
            }
        }
        if (jSONObject != null) {
            try {
                if (jSONObject.has("disable_foreign_mobile_number")) {
                    "true".equalsIgnoreCase(jSONObject.getString("disable_foreign_mobile_number"));
                }
            } catch (Exception unused2) {
            }
        }
        IoTCredentialManageImpl.init(appKey);
        IoTCredentialProviderImpl ioTCredentialProviderImpl = new IoTCredentialProviderImpl(IoTCredentialManageImpl.getInstance(application));
        try {
            ioTCredentialProviderImpl.setApiHook(new IoTAuthApiHook() { // from class: com.aliyun.iot.aep.sdk.init.OpenAccountSDKDelegate.2
                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTAuthApiHook
                public void onInterceptResponse(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTResponse ioTResponse) {
                }

                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTAuthApiHook
                public void onInterceptSend(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload) {
                    IoTSmartImpl.getInstance().doIntercept(ioTRequest, ioTRequestPayload);
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
            ALog.e("OpenAccountSDKDelegate", "IoTAuthApiHook not found maybe, upgrade to com.aliyun.iot.aep.sdk:iot-credential:0.0.2.9 or above");
        }
        IoTAPIClientImpl.getInstance().registerIoTAuthProvider(AlinkConstants.KEY_IOT_AUTH, ioTCredentialProviderImpl);
        String string = "";
        if (jSONObject != null) {
            try {
                string = jSONObject.getString("aliyun_daily_create_iottoken_host");
            } catch (Exception unused3) {
            }
        }
        if (!TextUtils.isEmpty(string)) {
            IoTCredentialManageImpl.DefaultDailyALiYunCreateIotTokenRequestHost = string;
        }
        if (!SDKManager.isOAAvailable()) {
            return 0;
        }
        try {
            a();
            return 0;
        } catch (Exception e3) {
            e3.printStackTrace();
            return 0;
        }
    }

    private static void a() {
        OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.AccountPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.ChangeMobileFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.ChangeMobileFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.MobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
    }

    private static void a(Application application, OALoginAdapter oALoginAdapter, JSONObject jSONObject) throws JSONException, ClassNotFoundException {
        JSONArray jSONArray = jSONObject.getJSONArray("ui_config");
        if (jSONArray == null || jSONArray.length() == 0) {
            return;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            String string = ((JSONObject) jSONArray.get(i)).getString(AgooConstants.OPEN_ACTIIVTY_NAME);
            String string2 = ((JSONObject) jSONArray.get(i)).getString(TtmlNode.TAG_LAYOUT);
            if (((JSONObject) jSONArray.get(i)).has("is_select_mobile_country")) {
                "true".equalsIgnoreCase(((JSONObject) jSONArray.get(i)).getString("is_select_mobile_country"));
            }
            Class<?> cls = Class.forName(string);
            if (LoginActivity.class.isAssignableFrom(cls)) {
                oALoginAdapter.setDefaultLoginClass(cls);
                JSONObject jSONObject2 = ((JSONObject) jSONArray.get(i)).getJSONObject("params");
                HashMap map = new HashMap();
                if (jSONObject2 != null) {
                    Iterator<String> itKeys = jSONObject2.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        map.put(next, jSONObject2.getString(next));
                    }
                }
                if (!map.isEmpty()) {
                    oALoginAdapter.setDefaultLoginParams(map);
                }
            } else {
                MobileCountrySelectorActivity.class.isAssignableFrom(cls);
            }
            if (!TextUtils.isEmpty(string2)) {
                LayoutMapping.put(cls, Integer.valueOf(a(application, string2)));
            }
        }
    }

    private static int a(Application application, String str) {
        return application.getResources().getIdentifier(str, TtmlNode.TAG_LAYOUT, application.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context, boolean z) {
        Intent intent = new Intent(ACTION_OA_INIT_DONE);
        intent.putExtra("success", z);
        BroadCastUtil.broadCastSticky(context, intent);
    }
}
