package com.taobao.accs.internal;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UtilityImpl;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
@TargetApi(21)
public class AccsJobService extends JobService {
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        ALog.d("AccsJobService", "onStartJob", new Object[0]);
        if (UtilityImpl.b((Context) this, true)) {
            return false;
        }
        try {
            String packageName = getPackageName();
            Intent intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(Constants.ACTION_COMMAND);
            intent.putExtra("command", 201);
            intent.setClassName(packageName, AdapterUtilityImpl.channelService);
            IntentDispatch.dispatchIntent(getApplicationContext(), intent, AdapterUtilityImpl.channelService);
        } catch (Throwable th) {
            ALog.e("AccsJobService", "onStartJob", th, new Object[0]);
        }
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new c(this, jobParameters), 30000L, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        if (UtilityImpl.b((Context) this, true)) {
            return false;
        }
        try {
            Intent intent = new Intent();
            intent.setPackage(getPackageName());
            intent.setAction(Constants.ACTION_COMMAND);
            intent.putExtra("command", 201);
            intent.setClassName(getPackageName(), AdapterUtilityImpl.channelService);
            IntentDispatch.dispatchIntent(getApplicationContext(), intent, AdapterUtilityImpl.channelService);
        } catch (Throwable th) {
            ALog.e("AccsJobService", "onStopJob", th, new Object[0]);
        }
        return false;
    }
}
