package com.aliyun.iot.push;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import anet.channel.appmonitor.AppMonitor;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.push.honor.HonorRegister;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.report.ReportManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.push.PushInitConfig;
import com.aliyun.iot.push.impl.FCMRegister;
import com.aliyun.iot.push.impl.IoTMainlandPushManager;
import com.aliyun.iot.push.impl.IoTOverseasPushManager;
import com.aliyun.iot.push.utils.PushLogAdapter;
import com.aliyun.iot.push.utils.SPUtils;
import com.aliyun.iot.push.utils.SecurityGuardUtils;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.CallBack;

/* JADX INFO: loaded from: classes2.dex */
public class PushManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private AtomicBoolean f4915a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private AtomicBoolean f4916b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private IoTMainlandPushManager f4917c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private IoTOverseasPushManager f4918d;
    private volatile PushInitConfig e;
    public boolean enableUT;

    public interface BindUserListener {
        void onFailure(String str, String str2);

        void onSuccess(String str);
    }

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final PushManager f4930a = new PushManager();
    }

    private PushManager() {
        this.f4915a = new AtomicBoolean(false);
        this.f4916b = new AtomicBoolean(false);
        this.enableUT = false;
        this.f4917c = null;
        this.f4918d = null;
        this.e = null;
        this.f4917c = new IoTMainlandPushManager();
        this.f4918d = new IoTOverseasPushManager();
        a();
    }

    private void a() {
        try {
            if (this.f4916b.compareAndSet(false, true)) {
                PushLogAdapter pushLogAdapter = new PushLogAdapter();
                AmsLogger.addListener(pushLogAdapter);
                ALog.setLogAdapter(pushLogAdapter);
                ALog.isUseTlog = false;
            }
        } catch (Exception unused) {
            com.aliyun.iot.push.utils.ALog.w("PushManager", "initPushLog exception.");
        }
    }

    public static final PushManager getInstance() {
        return a.f4930a;
    }

    public void init(Application application, PushInitConfig pushInitConfig) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "init() PUSH SDK VERSION=2.1.2-6285743 called with: app = [" + application + "], config = [" + pushInitConfig + "]");
        if (application == null) {
            throw new IllegalArgumentException("context is null.");
        }
        if (pushInitConfig == null) {
            throw new IllegalArgumentException("push init config is null.");
        }
        if (TextUtils.isEmpty(pushInitConfig.getAuthCode())) {
            throw new IllegalArgumentException("authCode is null.");
        }
        if (pushInitConfig.getPushChannelType() == null) {
            throw new IllegalArgumentException("push channel type is null.");
        }
        if (Utils.hasSecurityGuardDep()) {
            SecurityGuardUtils.checkSecurityPicture(application, pushInitConfig.getAuthCode());
        }
        String appKey = SecurityGuardUtils.getAppKey(application, pushInitConfig.getAuthCode());
        if (TextUtils.isEmpty(appKey)) {
            throw new IllegalArgumentException("appKey is null.");
        }
        try {
            ALog.isUseTlog = false;
            if (!this.enableUT) {
                com.aliyun.iot.push.utils.ALog.d("PushManager", "close accs&awcn&push ut.");
                AppMonitor.setInstance(null);
                Constants.UT_OFF = true;
                ReportManager.getInstance(application).setReportSwitch(false);
            }
        } catch (Exception unused) {
        }
        int i = application.getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT >= 26 && i >= 26) {
            ((NotificationManager) application.getSystemService(RemoteMessageConst.NOTIFICATION)).createNotificationChannel(new NotificationChannel("IOT_CHANNEL", "iot-push", 4));
        }
        ACCSManager.setAppkey(application, appKey, 0);
        this.e = pushInitConfig;
        if (PushChannelType.IOT_OVERSEAS_CLOUD_PUSH.equals(pushInitConfig.getPushChannelType())) {
            this.f4918d.init(application, appKey, pushInitConfig);
        } else {
            if (PushChannelType.IOT_MAINLAND_CLOUD_PUSH.equals(pushInitConfig.getPushChannelType())) {
                this.f4917c.init(application, pushInitConfig);
                return;
            }
            throw new IllegalArgumentException("invalid public channel type");
        }
    }

    @Deprecated
    public void init(Application application, String str) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "init() called with: app = [" + application + "], authCode = [" + str + "]");
        init(application, new PushInitConfig.Builder().authCode(str).pushChannelType(PushChannelType.IOT_MAINLAND_CLOUD_PUSH).pushInitCallback(null).build());
    }

    public void deinit(Application application, final CallBack callBack) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "deinit() called with: application = [" + application + "], callBack = [" + callBack + "]");
        this.f4915a.set(false);
        if (this.e == null) {
            if (callBack != null) {
                callBack.onSuccess();
                return;
            }
            return;
        }
        if (this.f4917c != null && b()) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", "mainland: deinit() called with: application = [" + application + "], callBack = [" + callBack + "]");
            this.f4917c.deinit(application, new CallBack() { // from class: com.aliyun.iot.push.PushManager.1
                @Override // org.android.agoo.common.CallBack
                public void onSuccess() {
                    CallBack callBack2 = callBack;
                    if (callBack2 != null) {
                        callBack2.onSuccess();
                    }
                }

                @Override // org.android.agoo.common.CallBack
                public void onFailure(String str, String str2) {
                    CallBack callBack2 = callBack;
                    if (callBack2 != null) {
                        callBack2.onFailure(str, str2);
                    }
                }
            });
        }
        if (this.f4918d == null || b()) {
            return;
        }
        com.aliyun.iot.push.utils.ALog.d("PushManager", "overseas: deinit() called with: application = [" + application + "], callBack = [" + callBack + "]");
        this.f4918d.deinit(application, new CallBack() { // from class: com.aliyun.iot.push.PushManager.2
            @Override // org.android.agoo.common.CallBack
            public void onSuccess() {
                CallBack callBack2 = callBack;
                if (callBack2 != null) {
                    callBack2.onSuccess();
                }
            }

            @Override // org.android.agoo.common.CallBack
            public void onFailure(String str, String str2) {
                CallBack callBack2 = callBack;
                if (callBack2 != null) {
                    callBack2.onFailure(str, str2);
                }
            }
        });
    }

    public void clearAccsDiskCache(Application application, String str) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "clearAccsDiskCache() called");
        SPUtils.clearSPFile(application, "ACCS_SDK");
        SPUtils.clearSPFile(application, "ACCS_SDK_CHANNEL");
        SPUtils.clearSPFile(application, "ACCS_BIND" + str);
        SPUtils.clearSPFile(application, "Agoo_AppStore");
        SPUtils.clearSPFile(application, "AGOO_BIND");
    }

    public void initMiPush(Context context, String str, String str2) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "initMiPush() called with: context = [" + context + "], appId = [" + str + "], appKey = [" + str2 + "]");
        if (context == null) {
            throw new IllegalArgumentException("initMiPush context is null");
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("initMiPush appId is null");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("initMiPush appKey is null");
        }
        MiPushRegister.register(context, str, str2);
    }

    public void initHuaweiPush(Application application) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "initHuaweiPush() called with: context = [" + application + "]");
        if (application == null) {
            throw new IllegalArgumentException("initHuaweiPush context is null");
        }
        HuaWeiRegister.register(application);
    }

    public void initHonorPush(Application application) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "initHonorPush() called with: context = [" + application + "]");
        if (application == null) {
            throw new IllegalArgumentException("initHonorPush context is null");
        }
        HonorRegister.register(application);
    }

    public void initFCMPush(Application application, String str, String str2, String str3, String str4) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "initFCMPush() called with: context = [" + application + "], sendId = [" + str + "], applicationId = [" + str2 + "]");
        if (application == null) {
            throw new IllegalArgumentException("initFCMPush context is null");
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("initFCMPush sendId is null");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("initFCMPush applicationId is null");
        }
        if (this.e != null && this.e.getPushChannelType() == PushChannelType.IOT_OVERSEAS_CLOUD_PUSH) {
            FCMRegister.unregister();
            FCMRegister.register(application, str, str2, str3, str4);
            return;
        }
        throw new IllegalArgumentException("initFCMPush not support with this type");
    }

    public void unbindUser() {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "unbindUser() called");
        a("/uc/unbindPushChannel", "1.0.2", new BindUserListener() { // from class: com.aliyun.iot.push.PushManager.3
            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onFailure(String str, String str2) {
            }

            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onSuccess(String str) {
            }
        });
    }

    public void bindUser() {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "bindUser() called");
        a("/uc/bindPushChannel", "1.0.2", new BindUserListener() { // from class: com.aliyun.iot.push.PushManager.4
            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onFailure(String str, String str2) {
            }

            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onSuccess(String str) {
            }
        });
    }

    public boolean bindUserSafely(PushChannelType pushChannelType) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "bindUserSafety() called");
        PushChannelType deviceIdChannelType = getDeviceIdChannelType();
        if (deviceIdChannelType == null || deviceIdChannelType != pushChannelType) {
            return false;
        }
        a("/uc/bindPushChannel", "1.0.2", new BindUserListener() { // from class: com.aliyun.iot.push.PushManager.5
            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onFailure(String str, String str2) {
            }

            @Override // com.aliyun.iot.push.PushManager.BindUserListener
            public void onSuccess(String str) {
            }
        });
        return true;
    }

    public void unbindUser(BindUserListener bindUserListener) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "unbindUser() called");
        a("/uc/unbindPushChannel", "1.0.2", bindUserListener);
    }

    @Deprecated
    public void bindUser(BindUserListener bindUserListener) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "bindUser() called");
        a("/uc/bindPushChannel", "1.0.2", bindUserListener);
    }

    public boolean bindUserSafely(@NonNull PushChannelType pushChannelType, BindUserListener bindUserListener) {
        com.aliyun.iot.push.utils.ALog.d("PushManager", "bindUserSafely() called type：" + pushChannelType.getName());
        PushChannelType deviceIdChannelType = getDeviceIdChannelType();
        if (deviceIdChannelType != null && deviceIdChannelType == pushChannelType) {
            a("/uc/bindPushChannel", "1.0.2", bindUserListener);
            return true;
        }
        if (bindUserListener == null) {
            return false;
        }
        bindUserListener.onFailure(getDeviceId(), "deviceId dont match ChannelType ");
        return false;
    }

    public String getDeviceId() {
        if (this.e == null) {
            throw new IllegalStateException("push sdk not inited.");
        }
        if (this.e.getPushChannelType() == PushChannelType.IOT_OVERSEAS_CLOUD_PUSH) {
            return this.f4918d.getDeviceId();
        }
        if (this.e.getPushChannelType() == PushChannelType.IOT_MAINLAND_CLOUD_PUSH) {
            return this.f4917c.getDeviceId();
        }
        return null;
    }

    public PushChannelType getDeviceIdChannelType() {
        if (this.e == null) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", "getDeviceIdChannelType push sdk not inited.");
            return null;
        }
        String deviceId = getDeviceId();
        if (deviceId == null || deviceId.trim().equals("")) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", "getDeviceIdChannelType deviceId is null or empty");
            return null;
        }
        if (deviceId.toLowerCase().equals(deviceId)) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", "getDeviceIdChannelType IOT_MAINLAND_CLOUD_PUSH");
            return PushChannelType.IOT_MAINLAND_CLOUD_PUSH;
        }
        com.aliyun.iot.push.utils.ALog.d("PushManager", "getDeviceIdChannelType IOT_OVERSEAS_CLOUD_PUSH");
        return PushChannelType.IOT_OVERSEAS_CLOUD_PUSH;
    }

    private boolean b() {
        return this.e != null && this.e.getPushChannelType() == PushChannelType.IOT_MAINLAND_CLOUD_PUSH;
    }

    private boolean c() {
        return this.e != null && this.e.getPushChannelType() == PushChannelType.IOT_OVERSEAS_CLOUD_PUSH;
    }

    private void a(final String str, String str2, final BindUserListener bindUserListener) {
        final String deviceId;
        if (this.e == null) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", " request  pushInitConfig == null ");
        }
        if (b()) {
            deviceId = this.f4917c.getDeviceId();
            com.aliyun.iot.push.utils.ALog.d("PushManager", " request  isMainlandPush    deviceId = " + deviceId);
        } else {
            deviceId = null;
        }
        if (c()) {
            deviceId = this.f4918d.getDeviceId();
            com.aliyun.iot.push.utils.ALog.d("PushManager", " request  isOverseaPush    deviceId = " + deviceId);
        }
        if (deviceId == null) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", " request  deviceId == null");
        } else if (deviceId.trim().equals("")) {
            com.aliyun.iot.push.utils.ALog.d("PushManager", " request deviceId is empty");
            if (bindUserListener != null) {
                bindUserListener.onFailure("", "request deviceId is empty");
            }
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setScheme(Scheme.HTTPS).setPath(str).setApiVersion(str2).addParam("deviceType", "ANDROID").addParam("deviceId", deviceId).build(), new IoTCallback() { // from class: com.aliyun.iot.push.PushManager.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                com.aliyun.iot.push.utils.ALog.d("PushManager", str + " --->>> Failure");
                bindUserListener.onFailure(deviceId, exc.getMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                com.aliyun.iot.push.utils.ALog.d("PushManager", str + " --->>> Success");
                bindUserListener.onSuccess(deviceId);
            }
        });
    }
}
