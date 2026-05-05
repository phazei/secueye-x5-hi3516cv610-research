package com.aliyun.iot.aep.sdk.apiclient.callback;

import android.os.Handler;
import android.os.Looper;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: loaded from: classes2.dex */
public class IoTUIThreadCallback implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IoTCallback f4575a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Handler f4576b = new Handler(Looper.getMainLooper());

    public class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequest f4577a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ Exception f4578b;

        public a(IoTRequest ioTRequest, Exception exc) {
            this.f4577a = ioTRequest;
            this.f4578b = exc;
        }

        @Override // java.lang.Runnable
        public void run() {
            IoTUIThreadCallback.this.f4575a.onFailure(this.f4577a, this.f4578b);
        }
    }

    public class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequest f4580a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ IoTResponse f4581b;

        public b(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            this.f4580a = ioTRequest;
            this.f4581b = ioTResponse;
        }

        @Override // java.lang.Runnable
        public void run() {
            IoTUIThreadCallback.this.f4575a.onResponse(this.f4580a, this.f4581b);
        }
    }

    public IoTUIThreadCallback(IoTCallback ioTCallback) {
        this.f4575a = ioTCallback;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        if (this.f4575a == null) {
            return;
        }
        this.f4576b.post(new a(ioTRequest, exc));
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (this.f4575a == null) {
            return;
        }
        this.f4576b.post(new b(ioTRequest, ioTResponse));
    }
}
