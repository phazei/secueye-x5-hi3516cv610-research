package com.alibaba.sdk.android.push.b;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import anet.channel.util.ALog;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.crashdefend.CrashDefendApi;
import com.alibaba.sdk.android.crashdefend.CrashDefendCallback;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.logger.LogLevel;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.IPushPermissionCallback;
import com.alibaba.sdk.android.push.e.a;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.alibaba.sdk.android.push.report.ReportManager;
import com.alibaba.sdk.android.sender.AlicloudSender;
import com.alibaba.sdk.android.sender.SdkInfo;
import com.hjq.permissions.Permission;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.utl.AccsLogger;
import com.taobao.agoo.control.data.AliasDO;

/* JADX INFO: loaded from: classes.dex */
public class b implements CloudPushService {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static b f2955a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a f2956b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f2957c;
    private a.InterfaceC0201a f;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f2958d = true;
    private boolean e = false;
    private boolean g = false;

    public static b a() {
        return f2955a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(final Context context, final CommonCallback commonCallback) {
        AmsLogger.getImportantLogger().i("call register");
        com.alibaba.sdk.android.push.f.a.a().a(new CommonCallback() { // from class: com.alibaba.sdk.android.push.b.b.28
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                commonCallback.onFailed(str, str2);
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                commonCallback.onSuccess(str);
                b.this.f2956b.b(context);
            }
        });
        ReportManager.getInstance(context).setAppKey(com.alibaba.sdk.android.ams.common.b.c.a().a());
        d();
        a(context, com.alibaba.sdk.android.ams.common.b.c.a().a(), com.alibaba.sdk.android.ams.common.b.c.a().d());
    }

    private synchronized void a(Context context, String str, String str2) {
        com.alibaba.sdk.android.push.a.a.a().a(context, str, str2, "-SNAPSHOT");
        com.alibaba.sdk.android.push.a.a.a().a(new com.alibaba.sdk.android.push.a.b() { // from class: com.alibaba.sdk.android.push.b.b.30
            @Override // com.alibaba.sdk.android.push.a.b
            public void a(boolean z) {
                ReportManager.getInstance().setReportSwitch(z);
            }
        });
    }

    private boolean a(String str, CommonCallback commonCallback, Runnable runnable) {
        if (this.f2957c == null) {
            AmsLogger.getImportantLogger().e("please call PushServiceFactory.init first");
            if (commonCallback != null) {
                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.u.copy().detail(str).build();
                commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
            }
            return false;
        }
        e();
        if (this.f2958d) {
            if (runnable == null) {
                return true;
            }
            runnable.run();
            return true;
        }
        AmsLogger.getImportantLogger().e("push disabled");
        if (commonCallback != null) {
            ErrorCode errorCodeBuild2 = com.alibaba.sdk.android.push.common.a.d.t.copy().detail(str).build();
            commonCallback.onFailed(errorCodeBuild2.getCode(), errorCodeBuild2.getMsg());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void b(Context context, final CommonCallback commonCallback) {
        if (this.f2956b == null) {
            com.alibaba.sdk.android.push.f.a.a().b(commonCallback);
        } else {
            this.f2956b.d(new CommonCallback() { // from class: com.alibaba.sdk.android.push.b.b.29
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    AmsLogger.getImportantLogger().i("turnOffPushChannel onFailed in doUnRegister");
                    com.alibaba.sdk.android.push.f.a.a().b(commonCallback);
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    AmsLogger.getImportantLogger().i("turnOffPushChannel success in doUnRegister");
                    com.alibaba.sdk.android.push.f.a.a().b(commonCallback);
                }
            });
        }
    }

    private void d() {
        SdkInfo sdkInfo = new SdkInfo();
        sdkInfo.setSdkId("push");
        sdkInfo.setSdkVersion("-SNAPSHOT");
        sdkInfo.setAppKey(com.alibaba.sdk.android.ams.common.b.c.a().a());
        if (com.alibaba.sdk.android.ams.common.a.a.a() != null) {
            AlicloudSender.asyncSend(com.alibaba.sdk.android.ams.common.a.a.a(), sdkInfo);
        } else {
            AlicloudSender.asyncSend(com.alibaba.sdk.android.ams.common.a.a.b(), sdkInfo);
        }
    }

    private void e() {
        Context context;
        if (this.f2956b != null || (context = this.f2957c) == null) {
            return;
        }
        this.f2956b = new a(context);
    }

    public void a(Context context) {
        AmsLogger.getImportantLogger().i("Initialize Mobile Push service...");
        this.f2957c = context;
        if (this.f2956b == null) {
            this.f2956b = new a(context);
        }
        if (this.e) {
            CrashDefendApi.registerCrashDefendSdk(context, "push", "-SNAPSHOT", 10, 5, new CrashDefendCallback() { // from class: com.alibaba.sdk.android.push.b.b.1
                @Override // com.alibaba.sdk.android.crashdefend.CrashDefendCallback
                public void onSdkClosed(int i) {
                    AmsLogger.getImportantLogger().e("crash limit exceeds, close forever");
                    b.this.f2958d = false;
                }

                @Override // com.alibaba.sdk.android.crashdefend.CrashDefendCallback
                public void onSdkStart(int i, int i2, int i3) {
                    b.this.f2958d = true;
                }

                @Override // com.alibaba.sdk.android.crashdefend.CrashDefendCallback
                public void onSdkStop(int i, int i2, int i3, long j) {
                    AmsLogger.getImportantLogger().e("crash limit exceeds");
                    b.this.f2958d = false;
                }
            });
        }
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void addAlias(final String str, final CommonCallback commonCallback) {
        a("addAlias", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.4
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(str, commonCallback);
            }
        });
    }

    public a.InterfaceC0201a b() {
        return this.f;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void bindAccount(final String str, final CommonCallback commonCallback) {
        a("bindAccount", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.31
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(str, commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void bindPhoneNumber(final String str, final CommonCallback commonCallback) {
        a("bindPhoneNumber", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.25
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.d(str, commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void bindTag(final int i, final String[] strArr, final String str, final CommonCallback commonCallback) {
        a("bindTag", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.33
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(i, strArr, str, commonCallback);
            }
        });
    }

    public boolean c() {
        return this.g;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void checkPushChannelStatus(final CommonCallback commonCallback) {
        a("checkPushChannelStatus", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.20
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.f(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void clearNotifications() {
        a("clearNotifications", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.17
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b();
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void clickMessage(final CPushMessage cPushMessage) {
        a("clickMessage", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.22
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(cPushMessage);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void closeDoNotDisturbMode() {
        a("closeDoNotDisturbMode", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.9
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(false);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void dismissMessage(final CPushMessage cPushMessage) {
        a("dismissMessage", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.21
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(cPushMessage);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public String getDeviceId() {
        if (a("getDeviceId", (CommonCallback) null, (Runnable) null)) {
            return this.f2956b.a();
        }
        return null;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public String getUTDeviceId() {
        if (a("getUTDeviceId", (CommonCallback) null, (Runnable) null)) {
            return this.f2956b.a(this.f2957c);
        }
        return null;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void listAliases(final CommonCallback commonCallback) {
        a("listAlias", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.6
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void listTags(final int i, final CommonCallback commonCallback) {
        a("listTags", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.3
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(i, commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void onAppStart() {
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public synchronized void register(final Context context, final CommonCallback commonCallback) {
        if (context != null) {
            a("register", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.12
                @Override // java.lang.Runnable
                public void run() {
                    ALog.setUseTlog(false);
                    ACCSClient.changeNetworkSdkLoggerToAccs();
                    b.this.a(context, commonCallback);
                }
            });
            return;
        }
        AmsLogger.getImportantLogger().e("context null");
        if (commonCallback != null) {
            ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.q.copy().detail("register context null").build();
            commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        }
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void register(Context context, String str, String str2, CommonCallback commonCallback) {
        if (commonCallback != null) {
            ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.v.copy().msg("请使用PushServiceFactory.init(Context appContext, String appKey, String appSecret)动态设置appKey appSecret").build();
            commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        }
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void removeAlias(final String str, final CommonCallback commonCallback) {
        a(AliasDO.JSON_CMD_REMOVEALIAS, commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.5
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.c(str, commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void requestNotificationPermission(Activity activity2, int i, IPushPermissionCallback iPushPermissionCallback) {
        if (Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(activity2, Permission.POST_NOTIFICATIONS) != -1) {
            return;
        }
        if (!(ContextCompat.checkSelfPermission(activity2, Permission.POST_NOTIFICATIONS) == -1)) {
            if (iPushPermissionCallback != null) {
                iPushPermissionCallback.onPushPermissionGranted();
            }
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity2, Permission.POST_NOTIFICATIONS)) {
            ActivityCompat.requestPermissions(activity2, new String[]{Permission.POST_NOTIFICATIONS}, i);
        } else if (iPushPermissionCallback != null) {
            iPushPermissionCallback.onPushPermissionForbidden();
        }
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setAppSecret(final String str) {
        a("setAppSecret", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.15
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.c(str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setAppkey(final String str) {
        a("setAppKey", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.14
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setDebug(final boolean z) {
        a("setDebug", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.27
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(z);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setDoNotDisturb(final int i, final int i2, final int i3, final int i4, final CommonCallback commonCallback) {
        a("setDoNotDisturb", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.8
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(i, i2, i3, i4, commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setEnableCrashDefend(boolean z) {
        this.e = z;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setLargeIconDownloadListener(a.InterfaceC0201a interfaceC0201a) {
        this.f = interfaceC0201a;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setLogLevel(final int i) {
        a("setLogLevel", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.7
            @Override // java.lang.Runnable
            public void run() {
                AmsLogger.log_level = i;
                int i2 = 0;
                ALog.setUseTlog(false);
                ACCSClient.changeNetworkSdkLoggerToAccs();
                int i3 = i;
                if (i3 == -1) {
                    AccsLogger.enable(false);
                    i2 = 5;
                } else if (i3 == 0 || i3 == 2 || i3 == 1) {
                    AccsLogger.enable(true);
                    switch (i) {
                        case 0:
                            AccsLogger.setLevel(LogLevel.WARN);
                            i2 = 3;
                            break;
                        case 1:
                            AccsLogger.setLevel(LogLevel.INFO);
                            ALog.setLevel(2);
                            return;
                        default:
                            AccsLogger.setLevel(LogLevel.DEBUG);
                            break;
                    }
                } else {
                    return;
                }
                ALog.setLevel(i2);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setNotificationLargeIcon(final Bitmap bitmap) {
        a("setNotificationLargeIcon", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.11
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(bitmap);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setNotificationShowInGroup(boolean z) {
        this.g = z;
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setNotificationSmallIcon(final int i) {
        a("setNotificationSmallIcon", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.13
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(i);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setNotificationSoundFilePath(final String str) {
        a("setNotificationSoundFilePath", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.10
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setPushIntentService(final Class cls) {
        a("setPushIntentService", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.24
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(cls);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void setSecurityGuardAuthCode(final String str) {
        a("setSecurityGuardAuthCode", (CommonCallback) null, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.16
            @Override // java.lang.Runnable
            public void run() {
                AmsLogger.getImportantLogger().i("setSecurityGuardAuthCode");
                b.this.f2956b.d(str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void turnOffPushChannel(final CommonCallback commonCallback) {
        a("turnOffPushChannel", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.19
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.d(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void turnOnPushChannel(final CommonCallback commonCallback) {
        a("turnOnPushChannel", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.18
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.e(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public synchronized void unRegister(final Context context, @Nullable final CommonCallback commonCallback) {
        if (context == null) {
            AmsLogger.getImportantLogger().e("unRegister context null");
            ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.q.copy().detail("unRegister context null").build();
            commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        } else {
            if (this.f2956b == null) {
                this.f2956b = new a(context);
            }
            a("unRegister", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.23
                @Override // java.lang.Runnable
                public void run() {
                    b.this.b(context, commonCallback);
                }
            });
        }
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void unbindAccount(final CommonCallback commonCallback) {
        a("unbindAccount", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.32
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.a(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void unbindPhoneNumber(final CommonCallback commonCallback) {
        a("unbindPhoneNumber", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.26
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.c(commonCallback);
            }
        });
    }

    @Override // com.alibaba.sdk.android.push.CloudPushService
    public void unbindTag(final int i, final String[] strArr, final String str, final CommonCallback commonCallback) {
        a("unBindTag", commonCallback, new Runnable() { // from class: com.alibaba.sdk.android.push.b.b.2
            @Override // java.lang.Runnable
            public void run() {
                b.this.f2956b.b(i, strArr, str, commonCallback);
            }
        });
    }
}
