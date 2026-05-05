package com.taobao.accs.internal;

import android.app.job.JobParameters;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ JobParameters f6338a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ AccsJobService f6339b;

    c(AccsJobService accsJobService, JobParameters jobParameters) {
        this.f6339b = accsJobService;
        this.f6338a = jobParameters;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6339b.jobFinished(this.f6338a, false);
    }
}
