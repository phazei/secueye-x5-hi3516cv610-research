package com.aliyun.alink.business.devicecenter.channel.http;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
public class TransitoryClient {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static IoTAPIClient f3503a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public CopyOnWriteArraySet<String> f3504b;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final TransitoryClient f3505a = new TransitoryClient();
    }

    public static TransitoryClient getInstance() {
        return SingletonHolder.f3505a;
    }

    public RetryTransitoryClient asynRequest(IoTRequest ioTRequest, IoTCallback ioTCallback) {
        ALog.d("TransitoryClient", "asynRequest request=" + requestToStr(ioTRequest) + ", callback=" + ioTCallback);
        if (f3503a == null) {
            f3503a = new IoTAPIClientFactory().getClient();
        }
        if (ioTRequest != null && !TextUtils.isEmpty(ioTRequest.getPath()) && !TextUtils.isEmpty(ioTRequest.getAPIVersion())) {
            RetryTransitoryClient retryTransitoryClient = new RetryTransitoryClient(!this.f3504b.contains(ioTRequest.getPath()));
            retryTransitoryClient.send(f3503a, ioTRequest, ioTCallback);
            return retryTransitoryClient;
        }
        ALog.w("TransitoryClient", "asynRequest request info error. requst=" + requestToStr(ioTRequest));
        if (ioTCallback != null) {
            ioTCallback.onFailure(null, new IllegalArgumentException("RequestParamsError"));
        }
        return null;
    }

    public String getTraceId(IoTResponse ioTResponse) {
        if (ioTResponse != null) {
            return ioTResponse.getId();
        }
        return null;
    }

    public void registerIgnoreRetryApiPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f3504b.add(str);
    }

    public String requestToStr(IoTRequest ioTRequest) {
        if (ioTRequest == null) {
            return null;
        }
        return "[schema=" + ioTRequest.getScheme() + ",host=" + ioTRequest.getHost() + ",path=" + ioTRequest.getPath() + ",apiVersion=" + ioTRequest.getAPIVersion() + ",method=" + ioTRequest.getMethod() + ",authType=" + ioTRequest.getAuthType() + ",params=" + ioTRequest.getParams() + "]";
    }

    public String responseToStr(IoTResponse ioTResponse) {
        if (ioTResponse == null) {
            return null;
        }
        return "[requestId=" + ioTResponse.getCode() + ",code=" + ioTResponse.getCode() + ",data=" + ioTResponse.getData() + ",message=" + ioTResponse.getMessage() + "]";
    }

    public TransitoryClient() {
        this.f3504b = new CopyOnWriteArraySet<>();
    }
}
