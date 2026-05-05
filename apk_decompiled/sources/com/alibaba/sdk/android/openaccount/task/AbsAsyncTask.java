package com.alibaba.sdk.android.openaccount.task;

import android.os.AsyncTask;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @Autowired
    protected ExecutorService executorService;

    protected abstract Result asyncExecute(Params... paramsArr);

    protected abstract void doFinally();

    protected abstract void doWhenException(Throwable th);

    public AbsAsyncTask() {
        Pluto.DEFAULT_INSTANCE.inject(this);
    }

    @Override // android.os.AsyncTask
    protected Result doInBackground(Params... paramsArr) {
        try {
            try {
                return asyncExecute(paramsArr);
            } catch (Throwable th) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, th.getMessage(), th);
                doWhenException(th);
                doFinally();
                return null;
            }
        } finally {
            doFinally();
        }
    }
}
