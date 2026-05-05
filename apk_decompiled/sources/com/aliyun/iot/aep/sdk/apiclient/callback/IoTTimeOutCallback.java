package com.aliyun.iot.aep.sdk.apiclient.callback;

import android.os.Handler;
import android.os.Message;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes2.dex */
public class IoTTimeOutCallback implements IoTCallback, Handler.Callback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IoTCallback f4572a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Handler f4573b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f4574c = false;

    public IoTTimeOutCallback(IoTCallback ioTCallback, long j) {
        j = j < 1000 ? 1000L : j;
        this.f4572a = ioTCallback;
        Handler handler = new Handler(this);
        this.f4573b = handler;
        handler.sendEmptyMessageDelayed(1717986918, j);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        this.f4574c = true;
        this.f4572a.onFailure(null, new TimeoutException("request timeout"));
        this.f4573b.removeCallbacksAndMessages(null);
        return this.f4574c;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        if (this.f4574c) {
            return;
        }
        this.f4572a.onFailure(ioTRequest, exc);
        this.f4573b.removeCallbacksAndMessages(null);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (this.f4574c) {
            return;
        }
        this.f4572a.onResponse(ioTRequest, ioTResponse);
        this.f4573b.removeCallbacksAndMessages(null);
    }
}
