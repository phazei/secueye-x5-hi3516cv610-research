package com.alibaba.sdk.android.openaccount.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.initialization.InitializationHandler;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.ReflectionUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.taobao.accs.common.Constants;
import com.ut.device.UTDevice;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DeviceManager implements InitializationHandler, EnvironmentChangeListener {
    public static final DeviceManager INSTANCE = new DeviceManager();
    private Context context;
    private String curDeviceInfo;

    @Autowired
    private ExecutorService executorService;
    private String imei;
    private String lastDeviceInfo;
    private boolean modelChanged;
    private String sdkDeviceId;

    @Autowired
    private SecurityGuardService securityGuardService;

    @Autowired
    private UserTrackerService userTrackerService;
    private String utdid;

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getRequestParameterKey() {
        return UTConstants.E_SDK_CONNECT_DEVICE_ACTION;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getResponseValueKey() {
        return UTConstants.E_SDK_CONNECT_DEVICE_ACTION;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public void handleResponseError(int i, String str) {
    }

    public void init(Context context) {
        this.context = context;
        _init();
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        this.utdid = null;
        this.curDeviceInfo = null;
        this.sdkDeviceId = null;
        this.lastDeviceInfo = null;
        _init();
    }

    private void _init() {
        if (TextUtils.isEmpty(this.utdid)) {
            this.utdid = UTDevice.getUtdid(this.context);
            if (TextUtils.isEmpty(this.utdid)) {
                recoverUtdidInfo(this.context);
            } else {
                saveDeviceInfo2Xml(Collections.singletonMap("deviceId", this.utdid));
            }
        }
        recoverSdkDeviceInfo(this.context);
        if (!"true".equals(OpenAccountSDK.getProperty("disableGetImei"))) {
            this.imei = getImei(this.context);
        }
        this.curDeviceInfo = Build.MODEL + Build.BRAND + Build.MANUFACTURER + this.utdid + this.imei + getYunOSDeviceId();
        if (this.curDeviceInfo.equals(this.lastDeviceInfo)) {
            return;
        }
        this.modelChanged = true;
    }

    public void setSdkDeviceId(String str) {
        this.sdkDeviceId = str;
        this.modelChanged = false;
        HashMap map = new HashMap();
        map.put(ConfigManager.getInstance().getEnvironment().name() + "_sdkDeviceId", str);
        map.put(AlinkConstants.KEY_DEVICE_INFO, this.curDeviceInfo);
        saveDeviceInfo2SecurityGuard(map);
        saveDeviceInfo2SP(map);
    }

    public String getUtdid() {
        return this.utdid;
    }

    public String getSdkDeviceId() {
        return this.sdkDeviceId;
    }

    private void recoverUtdidInfo(Context context) {
        try {
            SharedPreferences sharedPreferences = OpenAccountSDK.getAndroidContext().getSharedPreferences("onesdk_device", 0);
            if (this.utdid == null) {
                this.utdid = sharedPreferences.getString("deviceId", null);
            }
        } catch (Throwable unused) {
        }
    }

    private void recoverSdkDeviceInfo(Context context) {
        try {
            if (this.sdkDeviceId == null) {
                this.sdkDeviceId = this.securityGuardService.getValueFromDynamicDataStore(ConfigManager.getInstance().getEnvironment().name() + "_sdkDeviceId");
            }
            if (this.sdkDeviceId == null) {
                this.sdkDeviceId = context.getSharedPreferences("onesdk_device", 0).getString(ConfigManager.getInstance().getEnvironment().name() + "_sdkDeviceId", null);
            }
            this.lastDeviceInfo = this.securityGuardService.getValueFromDynamicDataStore(AlinkConstants.KEY_DEVICE_INFO);
        } catch (Throwable unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestRequirement() {
        return (TextUtils.isEmpty(this.sdkDeviceId) || this.modelChanged) ? 1 : 3;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public Object createRequestParameters() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.KEY_IMEI, this.imei);
            jSONObject.put("model", Build.MODEL);
            jSONObject.put("brand", Build.BOARD);
            jSONObject.put(DispatchConstants.PLATFORM_VERSION, String.valueOf(Build.VERSION.SDK_INT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public Object handleResponseValue(JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject;
        if (jSONObject == null) {
            return null;
        }
        int iOptInt = jSONObject.optInt("code");
        if (iOptInt == 1 && (jSONObjectOptJSONObject = jSONObject.optJSONObject("data")) != null && !TextUtils.isEmpty(jSONObjectOptJSONObject.optString("deviceId"))) {
            setSdkDeviceId(jSONObjectOptJSONObject.optString("deviceId"));
        }
        HashMap map = new HashMap();
        map.put("code", String.valueOf(iOptInt));
        map.put("traceId", jSONObject.optString("traceId"));
        map.put("msg", jSONObject.optString("message"));
        this.userTrackerService.sendCustomHit(UTConstants.E_SDK_CONNECT_RESULT, 19999, UTConstants.E_SDK_CONNECT_DEVICE_ACTION, 0L, iOptInt == 1 ? UTConstants.E_SDK_CONNECT_DEVICE_SUCCESS : UTConstants.E_SDK_CONNECT_DEVICE_FAILED, map);
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestServiceType() {
        return (TextUtils.isEmpty(this.sdkDeviceId) || this.modelChanged) ? 32 : 0;
    }

    @TargetApi(9)
    private void saveDeviceInfo2Xml(final Map<String, String> map) {
        this.executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.device.DeviceManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    SharedPreferences.Editor editorEdit = OpenAccountSDK.getAndroidContext().getSharedPreferences("onesdk_device", 0).edit();
                    for (Map.Entry entry : map.entrySet()) {
                        editorEdit.putString((String) entry.getKey(), (String) entry.getValue());
                    }
                    if (Build.VERSION.SDK_INT >= 9) {
                        editorEdit.apply();
                    } else {
                        editorEdit.commit();
                    }
                } catch (Throwable unused) {
                }
            }
        });
    }

    private void saveDeviceInfo2SecurityGuard(final Map<String, String> map) {
        this.executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.device.DeviceManager.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    for (Map.Entry entry : map.entrySet()) {
                        DeviceManager.this.securityGuardService.putValueInDynamicDataStore((String) entry.getKey(), (String) entry.getValue());
                    }
                } catch (Throwable unused) {
                }
            }
        });
    }

    private void saveDeviceInfo2SP(Map<String, String> map) {
        SharedPreferences.Editor editorEdit = this.context.getSharedPreferences("onesdk_device", 0).edit();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            editorEdit.putString(entry.getKey(), entry.getValue());
        }
        editorEdit.apply();
    }

    public String getYunOSDeviceId() {
        try {
            String str = (String) ReflectionUtils.invoke("android.os.SystemProperties", TmpConstant.PROPERTY_IDENTIFIER_GET, new String[]{String.class.getName(), String.class.getName()}, (Object) null, new Object[]{"ro.aliyun.clouduuid", null});
            return str == null ? "" : str;
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to read the yunOS id" + e.getMessage());
            return "";
        }
    }

    public String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return telephonyManager.getDeviceId();
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }

    public String getImei() {
        return this.imei;
    }
}
