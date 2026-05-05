package com.alibaba.sdk.android.push.channel;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(21)
public class KeepChannelService extends JobService {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f3040a = AmsLogger.getLogger("MPS:KeepChannelService");

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        f3040a.d("keepScheduleService start");
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        f3040a.d("keepScheduleService stoped");
        return false;
    }
}
