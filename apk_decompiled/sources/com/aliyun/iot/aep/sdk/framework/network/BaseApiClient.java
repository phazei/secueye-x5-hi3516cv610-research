package com.aliyun.iot.aep.sdk.framework.network;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.rpc.RpcServerBizConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.base.ApiDataCallBack;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class BaseApiClient {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static String f4691a = "BaseApiClient";

    public static void send(String str, String str2, BaseRequest baseRequest, ApiDataCallBack apiDataCallBack) {
        send(new ILopRequest(baseRequest, str, str2), apiDataCallBack);
    }

    public static void send(String str, String str2, String str3, String str4, BaseRequest baseRequest, ApiDataCallBack apiDataCallBack) {
        send(new ILopRequest(baseRequest, str, str2, str3, str4), apiDataCallBack);
    }

    public static void send(String str, String str2, Map<String, Object> map, final ApiDataCallBack apiDataCallBack) {
        try {
            IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion(str2).setPath(str).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
            if (map != null && !map.isEmpty()) {
                authType.setParams(map);
            }
            new IoTAPIClientFactory().getClient().send(authType.build(), new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.1
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    if (apiDataCallBack != null) {
                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                apiDataCallBack.onFail(0, "");
                            }
                        });
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                    if (apiDataCallBack == null) {
                        return;
                    }
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IoTResponse ioTResponse2 = ioTResponse;
                            if (ioTResponse2 == null || ioTResponse2.getCode() != 200) {
                                ApiDataCallBack apiDataCallBack2 = apiDataCallBack;
                                IoTResponse ioTResponse3 = ioTResponse;
                                int code = ioTResponse3 == null ? 0 : ioTResponse3.getCode();
                                IoTResponse ioTResponse4 = ioTResponse;
                                apiDataCallBack2.onFail(code, ioTResponse4 == null ? "" : ioTResponse4.getLocalizedMsg());
                                return;
                            }
                            JSONObject object = null;
                            if (ioTResponse.getData() != null) {
                                try {
                                    object = JSONObject.parseObject(ioTResponse.getData().toString());
                                } catch (Exception e) {
                                    ALog.e(BaseApiClient.f4691a, "Exception: " + e.toString());
                                }
                                apiDataCallBack.onSuccess(object);
                                return;
                            }
                            apiDataCallBack.onSuccess(null);
                        }
                    });
                }
            });
        } catch (Exception e) {
            ALog.e(f4691a, e.getLocalizedMessage());
        }
    }

    public static void send(ILopRequest iLopRequest, final ApiDataCallBack apiDataCallBack) {
        if (iLopRequest == null) {
            return;
        }
        Type type = null;
        final boolean z = false;
        if (apiDataCallBack != null) {
            try {
                type = ((ParameterizedType) apiDataCallBack.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                if (((Class) ((ParameterizedType) type).getRawType()) == List.class) {
                    try {
                        type = ((ParameterizedType) type).getActualTypeArguments()[0];
                        z = true;
                    } catch (Exception unused) {
                        z = true;
                    }
                }
            } catch (Exception unused2) {
            }
        }
        try {
            final Class cls = (Class) type;
            Map<String, Object> params = iLopRequest.getParams();
            IoTRequestBuilder apiVersion = new IoTRequestBuilder().setScheme(iLopRequest.getScheme()).setPath(iLopRequest.getPath()).setAuthType(TextUtils.isEmpty(iLopRequest.getAuthType()) ? AlinkConstants.KEY_IOT_AUTH : iLopRequest.getAuthType()).setApiVersion(iLopRequest.getApiVersion());
            if (params != null && !params.isEmpty()) {
                apiVersion.setParams(params);
            }
            if (!TextUtils.isEmpty(iLopRequest.getHost())) {
                apiVersion.setHost(iLopRequest.getHost());
            }
            new IoTAPIClientFactory().getClient().send(apiVersion.build(), new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.2
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, final Exception exc) {
                    if (apiDataCallBack != null) {
                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                apiDataCallBack.onFail(0, exc.getLocalizedMessage());
                            }
                        });
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                    if (apiDataCallBack == null) {
                        return;
                    }
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.framework.network.BaseApiClient.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IoTResponse ioTResponse2 = ioTResponse;
                            if (ioTResponse2 != null && ioTResponse2.getCode() == 403 && ioTResponse.getMessage() != null && ioTResponse.getMessage().contains("Timestamp is expired")) {
                                apiDataCallBack.onFail(RpcServerBizConstants.PWD_ERROR_TO_TIME_EXPIRED, ioTResponse.getMessage());
                                return;
                            }
                            IoTResponse ioTResponse3 = ioTResponse;
                            if (ioTResponse3 == null || ioTResponse3.getCode() != 200) {
                                ApiDataCallBack apiDataCallBack2 = apiDataCallBack;
                                IoTResponse ioTResponse4 = ioTResponse;
                                int code = ioTResponse4 == null ? 0 : ioTResponse4.getCode();
                                IoTResponse ioTResponse5 = ioTResponse;
                                apiDataCallBack2.onFail(code, ioTResponse5 == null ? "" : ioTResponse5.getLocalizedMsg());
                                return;
                            }
                            if (ioTResponse.getMessage() != null && ioTResponse.getMessage().contains("Chain validation failed")) {
                                apiDataCallBack.onFail(RpcServerBizConstants.PWD_ERROR_TO_TIME_EXPIRED, ioTResponse.getMessage());
                                return;
                            }
                            Object object = null;
                            if (ioTResponse.getData() != null) {
                                try {
                                    String string = ioTResponse.getData().toString();
                                    if (z) {
                                        object = JSON.parseArray(string, cls);
                                    } else {
                                        object = JSON.parseObject(string, (Class<Object>) cls);
                                    }
                                } catch (Exception e) {
                                    ALog.e(BaseApiClient.f4691a, "Exception: " + e.toString());
                                }
                                apiDataCallBack.onSuccess(object);
                                return;
                            }
                            apiDataCallBack.onSuccess(null);
                        }
                    });
                }
            });
        } catch (Exception e) {
            ALog.e(f4691a, e.getLocalizedMessage());
        }
    }
}
