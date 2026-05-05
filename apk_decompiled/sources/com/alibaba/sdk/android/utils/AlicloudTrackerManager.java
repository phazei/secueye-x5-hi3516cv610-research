package com.alibaba.sdk.android.utils;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.utils.crashdefend.SDKMessageCallback;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.common.Constants;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AlicloudTrackerManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AlicloudTrackerManager f3210a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private c f34a = new c();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private com.alibaba.sdk.android.utils.crashdefend.b f35a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Map<String, AlicloudTracker> f3211c;

    private AlicloudTrackerManager(Application application) {
        this.f35a = null;
        HashMap map = new HashMap(4);
        map.put("kVersion", AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION);
        map.put(Constants.KEY_PACKAGE_NAME, application.getPackageName());
        this.f34a.a(application, map);
        this.f3211c = new HashMap();
        this.f35a = com.alibaba.sdk.android.utils.crashdefend.b.a(application, this.f34a);
    }

    public static synchronized AlicloudTrackerManager getInstance(Application application) {
        if (application == null) {
            return null;
        }
        if (f3210a == null) {
            f3210a = new AlicloudTrackerManager(application);
        }
        return f3210a;
    }

    public AlicloudTracker getTracker(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.e("AlicloudTrackerManager", "sdkId or sdkVersion is null");
            return null;
        }
        String str3 = str + str2;
        if (this.f3211c.containsKey(str3)) {
            return this.f3211c.get(str3);
        }
        AlicloudTracker alicloudTracker = new AlicloudTracker(this.f34a, str, str2);
        this.f3211c.put(str3, alicloudTracker);
        return alicloudTracker;
    }

    public boolean registerCrashDefend(String str, String str2, int i, int i2, SDKMessageCallback sDKMessageCallback) {
        if (this.f35a == null) {
            return false;
        }
        com.alibaba.sdk.android.utils.crashdefend.c cVar = new com.alibaba.sdk.android.utils.crashdefend.c();
        cVar.f44a = str;
        cVar.f46b = str2;
        cVar.f3220a = i;
        cVar.f3221b = i2;
        return this.f35a.m30a(cVar, sDKMessageCallback);
    }

    public void unregisterCrashDefend(String str, String str2) {
        this.f35a.d(str, str2);
    }
}
