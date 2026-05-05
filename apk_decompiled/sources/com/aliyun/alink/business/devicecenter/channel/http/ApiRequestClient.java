package com.aliyun.alink.business.devicecenter.channel.http;

import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ApiRequestClient implements IApiClient {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IApiClient f3472a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AtomicInteger f3475d;
    public AtomicInteger e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f3473b = 3;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f3474c = 10;
    public AtomicBoolean f = new AtomicBoolean(false);
    public AtomicBoolean g = new AtomicBoolean(true);
    public IRequestCallback h = null;
    public IRequestCallback i = null;

    public ApiRequestClient(boolean z) {
        this.f3472a = null;
        this.f3475d = null;
        this.e = null;
        this.g.set(z);
        this.f3475d = new AtomicInteger(0);
        this.e = new AtomicInteger(0);
        if (DCEnvHelper.isTgEnv()) {
            this.f3472a = new MtopApiClientImpl();
        } else if (DCEnvHelper.hasApiClient()) {
            this.f3472a = new AliyunApiClientImpl();
        } else {
            ALog.w("ApiRequestClient", "Not exist mtop and aliyun api");
        }
    }

    public void cancelRequest() {
        this.f.set(true);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.http.IApiClient
    public void send(final IRequest iRequest, final Class<?> cls, IRequestCallback iRequestCallback) {
        ALog.d("ApiRequestClient", "send request: " + iRequest + ", callback: " + iRequestCallback);
        if (this.f3472a == null) {
            ALog.d("ApiRequestClient", "mApiClient is null");
            if (iRequestCallback != null) {
                iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_UNKNOWN_ERROR), "request api client is empty."), null);
                return;
            }
            return;
        }
        this.i = iRequestCallback;
        if (this.h == null) {
            this.h = new IRequestCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient.1
                @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
                public void onFail(DCError dCError, Object obj) {
                    ALog.i("ApiRequestClient", "onFail() called with: dcError = [" + dCError + "], response = [" + obj + "]");
                    if (ApiRequestClient.this.a(dCError)) {
                        ApiRequestClient.this.e.incrementAndGet();
                    } else {
                        ApiRequestClient.this.f3475d.incrementAndGet();
                    }
                    ApiRequestClient apiRequestClient = ApiRequestClient.this;
                    apiRequestClient.a(iRequest, cls, apiRequestClient.i, dCError, obj);
                }

                @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
                public void onSuccess(final Object obj) {
                    ALog.i("ApiRequestClient", "onSuccess() called with: data = [" + obj + "]");
                    ThreadTools.runOnUiThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ApiRequestClient.this.i != null) {
                                ApiRequestClient.this.i.onSuccess(obj);
                            }
                        }
                    });
                }
            };
        }
        this.f3472a.send(iRequest, cls, this.h);
    }

    public final boolean a(DCError dCError) {
        Throwable th;
        return (dCError == null || (th = dCError.throwable) == null || th.toString() == null || !dCError.throwable.toString().contains("UnknownHostException")) ? false : true;
    }

    public final void a(final IRequest iRequest, final Class<?> cls, final IRequestCallback iRequestCallback, final DCError dCError, final Object obj) {
        ALog.d("ApiRequestClient", "retryCount=" + this.f3475d.get() + ", retryDnsCount" + this.e.get() + ", needRetrySend" + this.g.get() + ", stopRetry=" + this.f.get());
        if (this.f.get()) {
            ALog.w("ApiRequestClient", "stopRetry called.");
            return;
        }
        if (this.g.get() && this.f3475d.get() < 3 && this.e.get() < 10) {
            if (this.f.get()) {
                ALog.w("ApiRequestClient", "stopRetry called. sleep");
                return;
            } else {
                ThreadTools.submitTask(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ApiRequestClient.this.send(iRequest, cls, iRequestCallback);
                    }
                }, false, 1000);
                return;
            }
        }
        ALog.d("ApiRequestClient", "callback return, current retryCount=" + this.f3475d.get() + ", retryDnsCount" + this.e.get());
        ThreadTools.runOnUiThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient.3
            @Override // java.lang.Runnable
            public void run() {
                IRequestCallback iRequestCallback2 = iRequestCallback;
                if (iRequestCallback2 != null) {
                    iRequestCallback2.onFail(dCError, obj);
                }
            }
        });
    }
}
