package com.alibaba.sdk.android.push.report;

import android.app.Application;
import android.content.Context;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;

/* JADX INFO: loaded from: classes.dex */
public class ReportManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f3165a = AmsLogger.getLogger("MPS:ReportManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ReportManager f3166b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f3167c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Context f3168d;
    private boolean e = true;

    private ReportManager(Context context) {
        this.f3167c = 0L;
        if (context == null || !(context.getApplicationContext() instanceof Application)) {
            return;
        }
        if (this.f3167c == 0) {
            this.f3167c = System.currentTimeMillis();
        }
        this.f3168d = context;
    }

    public static ReportManager getInstance() {
        return f3166b;
    }

    public static ReportManager getInstance(Context context) {
        if (f3166b == null) {
            synchronized (ReportManager.class) {
                if (f3166b == null) {
                    f3166b = new ReportManager(context);
                }
            }
        }
        return f3166b;
    }

    public void reportAppTransfer(String str, String str2, String str3) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportErrorInit(String str, String str2, String str3) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportErrorVipRequest(String str, String str2, String str3, String str4) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportPushArrive(String str, String str2, int i) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportThirdPushArrive(String str, String str2, int i, String str3) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportThirdPushOpen(String str, String str2, String str3) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void reportVipRequestTimeCost(String str, String str2, long j) {
        if (this.e) {
            return;
        }
        f3165a.e("report switch turned off");
    }

    public void setAppKey(String str) {
    }

    public void setReportSwitch(boolean z) {
        synchronized (ReportManager.class) {
            this.e = z;
        }
    }
}
