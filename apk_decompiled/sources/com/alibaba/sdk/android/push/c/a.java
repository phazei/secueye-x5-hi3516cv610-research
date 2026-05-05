package com.alibaba.sdk.android.push.c;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.push.channel.KeepChannelService;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f3037a = AmsLogger.getLogger("MPS:KeepLiveManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Context f3038b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static a f3039c = null;

    private a() {
    }

    public static a a() {
        if (f3039c == null) {
            f3039c = new a();
        }
        return f3039c;
    }

    public static void a(Context context) {
        f3038b = context;
        if (f3039c == null) {
            f3039c = a();
        }
    }

    public void b() {
        if (f3038b != null) {
            f3037a.d("Check KeepChannelService");
            if (Build.VERSION.SDK_INT >= 21) {
                try {
                    JobScheduler jobScheduler = (JobScheduler) f3038b.getSystemService("jobscheduler");
                    for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                        if (jobInfo.getId() == 900715 && jobInfo.getService().equals(new ComponentName(f3038b.getPackageName(), KeepChannelService.class.getName()))) {
                            f3037a.d("cancel Keep Channel Service");
                            jobScheduler.cancel(jobInfo.getId());
                            return;
                        }
                    }
                } catch (Throwable th) {
                    f3037a.e("start KeepChannelService failed.", th);
                }
            }
        }
    }
}
