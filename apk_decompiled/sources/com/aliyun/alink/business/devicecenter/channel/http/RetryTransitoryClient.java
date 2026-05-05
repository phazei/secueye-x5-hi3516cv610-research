package com.aliyun.alink.business.devicecenter.channel.http;

import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class RetryTransitoryClient {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AtomicInteger f3497c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AtomicInteger f3498d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f3495a = 3;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f3496b = 10;
    public AtomicBoolean e = new AtomicBoolean(false);
    public AtomicBoolean f = new AtomicBoolean(false);

    public RetryTransitoryClient(boolean z) {
        this.f3497c = null;
        this.f3498d = null;
        this.f.set(z);
        this.f3497c = new AtomicInteger(0);
        this.f3498d = new AtomicInteger(0);
    }

    public void cancelRequest() {
        this.e.set(true);
    }

    public void send(final IoTAPIClient ioTAPIClient, final IoTRequest ioTRequest, final IoTCallback ioTCallback) {
        ALog.d("RetryTransitoryClient", "send() called with: client = [" + ioTAPIClient + "], request = [" + ioTRequest + "], callback = [" + ioTCallback + "], needRetrySend = [" + this.f.get() + "]");
        if (ioTAPIClient == null) {
            if (ioTCallback != null) {
                ioTCallback.onFailure(ioTRequest, new IllegalArgumentException("IoTApiClientClientNull"));
                return;
            }
            return;
        }
        try {
            ioTAPIClient.send(ioTRequest, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.http.RetryTransitoryClient.1
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                    ALog.w("RetryTransitoryClient", "onFailure() called with: ioTRequest = [" + TransitoryClient.getInstance().requestToStr(ioTRequest2) + "], e = [" + exc + "]");
                    if (RetryTransitoryClient.this.a(exc)) {
                        RetryTransitoryClient.this.f3498d.incrementAndGet();
                    } else {
                        RetryTransitoryClient.this.f3497c.incrementAndGet();
                    }
                    RetryTransitoryClient.this.a(ioTAPIClient, ioTRequest, ioTCallback, exc, null);
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse) {
                    ALog.d("RetryTransitoryClient", "onResponse() called with: ioTRequest = [" + TransitoryClient.getInstance().requestToStr(ioTRequest2) + "], ioTResponse = [" + TransitoryClient.getInstance().responseToStr(ioTResponse) + "]");
                    if (ioTResponse == null || ioTResponse.getCode() != 200) {
                        RetryTransitoryClient.this.f3497c.incrementAndGet();
                        RetryTransitoryClient.this.a(ioTAPIClient, ioTRequest, ioTCallback, null, ioTResponse);
                        return;
                    }
                    IoTCallback ioTCallback2 = ioTCallback;
                    if (ioTCallback2 != null) {
                        ioTCallback2.onResponse(ioTRequest2, ioTResponse);
                    } else {
                        ALog.w("RetryTransitoryClient", "callback is null.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (ioTCallback != null) {
                ioTCallback.onFailure(ioTRequest, e);
            }
        }
    }

    public final void a(IoTAPIClient ioTAPIClient, IoTRequest ioTRequest, IoTCallback ioTCallback, Exception exc, IoTResponse ioTResponse) {
        ALog.d("RetryTransitoryClient", "retryCount=" + this.f3497c.get() + ", retryDnsCount" + this.f3498d.get() + ", needRetrySend" + this.f.get() + ", stopRetry=" + this.e.get());
        if (this.e.get()) {
            ALog.w("RetryTransitoryClient", "stopRetry called.");
            return;
        }
        if (this.f.get() && this.f3497c.get() < 3 && this.f3498d.get() < 10) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException unused) {
            }
            if (this.e.get()) {
                ALog.w("RetryTransitoryClient", "stopRetry called. sleep");
                return;
            } else {
                send(ioTAPIClient, ioTRequest, ioTCallback);
                return;
            }
        }
        ALog.d("RetryTransitoryClient", "callback return, current retryCount=" + this.f3497c.get() + ", retryDnsCount" + this.f3498d.get());
        if (ioTCallback != null && exc != null) {
            ioTCallback.onFailure(ioTRequest, exc);
        } else {
            if (ioTCallback == null || ioTResponse == null) {
                return;
            }
            ioTCallback.onResponse(ioTRequest, ioTResponse);
        }
    }

    public final boolean a(Exception exc) {
        return (exc == null || exc.toString() == null || !exc.toString().contains("UnknownHostException")) ? false : true;
    }
}
