package com.taobao.accs.net;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import com.taobao.accs.internal.AccsJobService;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class t extends f {
    public static final int DEAMON_JOB_ID = 2051;
    public static final int HB_JOB_ID = 2050;

    protected t(Context context) {
        super(context);
    }

    @Override // com.taobao.accs.net.f
    protected void a(int i) {
        long j = i * 1000;
        ((JobScheduler) this.f6376a.getSystemService("jobscheduler")).schedule(new JobInfo.Builder(2050, new ComponentName(this.f6376a.getPackageName(), AccsJobService.class.getName())).setMinimumLatency(j).setOverrideDeadline(j).setRequiredNetworkType(1).build());
    }
}
