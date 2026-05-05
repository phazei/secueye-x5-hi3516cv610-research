package com.aliyun.iot.aep.sdk.framework.log;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponseImpl;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.log.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class APITracker implements Tracker {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final String f4684a = "APITracker";

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onSend(IoTRequest ioTRequest) {
        ALog.i("APITracker", "onSend\r\n");
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onRealSend(IoTRequestWrapper ioTRequestWrapper) {
        ALog.d("APITracker", "onRealSend:\r\n" + a(ioTRequestWrapper));
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onRawFailure(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
        ALog.d("APITracker", "onRawFailure:\r\n" + a(ioTRequestWrapper) + "ERROR-MESSAGE:" + exc.getMessage());
        exc.printStackTrace();
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        ALog.i("APITracker", "onFailure\r\n");
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onRawResponse(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
        ALog.d("APITracker", "onRawResponse:\r\n" + a(ioTRequestWrapper) + a(ioTResponse));
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        ALog.i("APITracker", "onResponse\r\n");
    }

    private static String a(IoTRequestWrapper ioTRequestWrapper) {
        IoTRequest ioTRequest = ioTRequestWrapper.request;
        String apiEnv = GlobalConfig.getInstance().getApiEnv();
        String defaultHost = IoTAPIClientImpl.getInstance().getDefaultHost();
        StringBuilder sb = new StringBuilder("Request:");
        sb.append("\r\n");
        sb.append("id:");
        sb.append(ioTRequestWrapper.payload.getId());
        sb.append("\r\n");
        sb.append("apiEnv:");
        sb.append(apiEnv);
        sb.append("\r\n");
        sb.append("url:");
        sb.append(ioTRequest.getScheme());
        sb.append(HttpConstant.SCHEME_SPLIT);
        if (!TextUtils.isEmpty(ioTRequestWrapper.request.getHost())) {
            defaultHost = ioTRequestWrapper.request.getHost();
        }
        sb.append(defaultHost);
        sb.append(ioTRequest.getPath());
        sb.append("\r\n");
        sb.append("apiVersion:");
        sb.append(ioTRequest.getAPIVersion());
        sb.append("\r\n");
        sb.append("params:");
        sb.append(ioTRequest.getParams() == null ? "" : JSON.toJSONString(ioTRequest.getParams()));
        sb.append("\r\n");
        sb.append("payload:");
        sb.append(JSON.toJSONString(ioTRequestWrapper.payload));
        sb.append("\r\n");
        return sb.toString();
    }

    private static String a(IoTResponse ioTResponse) {
        StringBuilder sb = new StringBuilder("Response:");
        sb.append("\r\n");
        sb.append("id:");
        sb.append(ioTResponse.getId());
        sb.append("\r\n");
        sb.append("code:");
        sb.append(ioTResponse.getCode());
        sb.append("\r\n");
        sb.append("message:");
        sb.append(ioTResponse.getMessage());
        sb.append("\r\n");
        sb.append("localizedMsg:");
        sb.append(ioTResponse.getLocalizedMsg());
        sb.append("\r\n");
        sb.append("data:");
        sb.append(ioTResponse.getData() == null ? "" : ioTResponse.getData().toString());
        sb.append("\r\n");
        if (ioTResponse instanceof IoTResponseImpl) {
            Object extraResponseData = ((IoTResponseImpl) ioTResponse).getExtraResponseData();
            if (extraResponseData instanceof ApiResponse) {
                sb.append(a((ApiResponse) extraResponseData));
            }
        }
        return sb.toString();
    }

    private static String a(ApiResponse apiResponse) {
        return "GatewayResponse:\r\nex:" + apiResponse.getHeaders() + "\r\nheaders:" + apiResponse.getCode() + "\r\n";
    }
}
