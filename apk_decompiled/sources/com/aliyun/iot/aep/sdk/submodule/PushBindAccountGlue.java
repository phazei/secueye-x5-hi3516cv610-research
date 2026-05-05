package com.aliyun.iot.aep.sdk.submodule;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.listener.IoTTokenCreatedListener;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.framework.utils.ThreadUtils;
import com.aliyun.iot.aep.sdk.init.PushManagerHelper;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.aliyun.iot.push.PushChannelType;
import com.aliyun.iot.push.PushManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes2.dex */
public class PushBindAccountGlue extends SimpleSDKDelegateImp {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AtomicBoolean f4883a = new AtomicBoolean(false);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f4884b = 1000;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f4885c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Runnable f4886d = new Runnable() { // from class: com.aliyun.iot.aep.sdk.submodule.PushBindAccountGlue.3
        @Override // java.lang.Runnable
        public void run() {
            if (PushBindAccountGlue.this.f4883a.get()) {
                PushBindAccountGlue.this.f4883a.set(false);
            } else {
                PushBindAccountGlue.this.b(AApplication.getInstance());
            }
        }
    };

    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(final Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        if (!SDKManager.isPushAvailable()) {
            return 0;
        }
        try {
            IoTCredentialManageImpl.getInstance(application).registerIotTokenCreatedListener(new IoTTokenCreatedListener() { // from class: com.aliyun.iot.aep.sdk.submodule.PushBindAccountGlue.1
                @Override // com.aliyun.iot.aep.sdk.credential.listener.IoTTokenCreatedListener
                public void onIoTTokenCreated() {
                    ALog.d("PushBindAccountGlue", "onIoTTokenCreated");
                    PushBindAccountGlue.this.b(application);
                }
            });
        } catch (Exception unused) {
            ALog.d("PushBindAccountGlue", "registerIotTokenCreatedListener error");
        }
        if (a(application)) {
            a();
        }
        return 0;
    }

    private static boolean a(Application application) {
        boolean zEquals = application.getPackageName().equals(a(application, Process.myPid()));
        ALog.d("PushBindAccountGlue", "isMainProcess:" + zEquals);
        return zEquals;
    }

    private static String a(Context context, int i) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        if (activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == i) {
                return runningAppProcessInfo.processName;
            }
        }
        return "";
    }

    private void a() {
        try {
            ALog.d("PushBindAccountGlue", "registerBeforeLogout");
            ((OALoginAdapter) LoginBusiness.getLoginAdapter()).registerCompleteBeforeLogoutListener(new OALoginAdapter.OnCompleteBeforeLogoutListener() { // from class: com.aliyun.iot.aep.sdk.submodule.PushBindAccountGlue.2
                @Override // com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.OnCompleteBeforeLogoutListener
                public void doAction(OALoginAdapter.ActionOnBeforeLogoutResultCallback actionOnBeforeLogoutResultCallback) {
                    ALog.d("PushBindAccountGlue", "registerBeforeLogout doAction");
                    PushManagerHelper.getInstance().unbindUser(actionOnBeforeLogoutResultCallback);
                    PushBindAccountGlue.this.f4883a.set(false);
                }
            });
        } catch (Exception e) {
            ALog.e("PushBindAccountGlue", "registerBeforeLogout error");
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Application application) {
        IoTCredentialManageImpl ioTCredentialManageImpl;
        ALog.d("PushBindAccountGlue", "bindAccount");
        if (LoginBusiness.isLogin() && (ioTCredentialManageImpl = IoTCredentialManageImpl.getInstance(application)) != null) {
            if (!TextUtils.isEmpty(ioTCredentialManageImpl.getIoTToken())) {
                d(application);
            }
            c(application);
        }
    }

    private void c(Application application) {
        ALog.d("PushBindAccountGlue", "retryBindAccount :" + this.f4885c);
        this.f4884b = Math.min(this.f4884b * 2, 64000);
        int i = this.f4885c;
        this.f4885c = i + 1;
        if (i < 10) {
            ThreadUtils.retryInMain(this.f4886d, this.f4884b);
        }
    }

    private void d(Application application) {
        ALog.d("PushBindAccountGlue", "bindAccountInternal");
        IoTCredentialManageImpl ioTCredentialManageImpl = IoTCredentialManageImpl.getInstance(application);
        if (ioTCredentialManageImpl == null || TextUtils.isEmpty(ioTCredentialManageImpl.getIoTToken())) {
            return;
        }
        if (GlobalConfig.getInstance().getInitConfig().getRegionType() != 1 && !RegionManager.isChinaRegion()) {
            a(PushChannelType.IOT_OVERSEAS_CLOUD_PUSH);
        } else {
            a(PushChannelType.IOT_MAINLAND_CLOUD_PUSH);
        }
    }

    private void a(PushChannelType pushChannelType) {
        try {
            PushManager.getInstance().bindUserSafely(pushChannelType, new PushManager.BindUserListener() { // from class: com.aliyun.iot.aep.sdk.submodule.PushBindAccountGlue.4
                @Override // com.aliyun.iot.push.PushManager.BindUserListener
                public void onFailure(String str, String str2) {
                }

                @Override // com.aliyun.iot.push.PushManager.BindUserListener
                public void onSuccess(String str) {
                    PushBindAccountGlue.this.f4883a.set(true);
                    PushBindAccountGlue.this.b();
                }
            });
        } catch (Exception e) {
            ALog.d("PushBindAccountGlue", "bindUserSafely:" + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        ThreadUtils.remove(this.f4886d);
        c();
    }

    private void c() {
        this.f4884b = 1000;
        this.f4885c = 0;
    }
}
