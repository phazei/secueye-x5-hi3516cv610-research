package com.aliyun.alink.apiclient.biz;

import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.CommonResponse;
import com.aliyun.alink.apiclient.IoTCallback;
import com.aliyun.alink.apiclient.LocalData;
import com.aliyun.alink.apiclient.model.DeviceAuthInfo;
import com.aliyun.alink.apiclient.model.DeviceResponse;
import com.aliyun.alink.apiclient.utils.GsonUtils;
import com.aliyun.alink.apiclient.utils.HmacSignUtils;
import com.aliyun.alink.apiclient.utils.ParameterHelper;
import com.aliyun.alink.apiclient.utils.StringUtils;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.gson.reflect.TypeToken;
import com.http.helper.HttpCallback;
import com.http.helper.HttpFailCode;
import com.http.okhttp.OkHttpManager;
import com.http.utils.LogUtils;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
public class IoTRequestHandler implements IHandler {
    private static final String TAG = "IoTRequestHandler";

    @Override // com.aliyun.alink.apiclient.biz.IHandler
    public void handle(CommonRequest commonRequest, IoTCallback ioTCallback) {
        if (ioTCallback == null) {
            LogUtils.error(TAG, "IoTRequestHandler handle failed, callback=null.");
            return;
        }
        String baseUrl = RequestHelper.getBaseUrl(commonRequest, null);
        synchronized (this) {
            if (StringUtils.isEmptyString(LocalData.getInstance().getAuthToken())) {
                getDeviceAuthToken(commonRequest, ioTCallback);
            } else {
                if ("/auth".equals(commonRequest.getPath())) {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setData(LocalData.getInstance().getAuthToken());
                    ioTCallback.onResponse(commonRequest, commonResponse);
                    return;
                }
                sendIoIRequest(baseUrl, commonRequest, ioTCallback);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendIoIRequest(String str, final CommonRequest commonRequest, final IoTCallback ioTCallback) {
        if (commonRequest != null) {
            try {
                if (commonRequest.getQueryParams() != null) {
                    final CommonResponse commonResponse = new CommonResponse();
                    HashMap map = new HashMap();
                    map.put("Content-Type", "application/octet-stream");
                    map.put("password", LocalData.getInstance().getAuthToken());
                    OkHttpManager.getInstance().postAsync(str, map, GsonUtils.toJsonString(commonRequest.getQueryParams()), new HttpCallback<IOException, String>() { // from class: com.aliyun.alink.apiclient.biz.IoTRequestHandler.1
                        @Override // com.http.helper.HttpCallback
                        public void onFail(String str2, IOException iOException) {
                            LogUtils.error(IoTRequestHandler.TAG, "sendIotRequest onFail url=" + str2 + ", e=" + iOException);
                            IoTCallback ioTCallback2 = ioTCallback;
                            if (ioTCallback2 != null) {
                                ioTCallback2.onFailure(commonRequest, iOException);
                            }
                        }

                        @Override // com.http.helper.HttpCallback
                        public void onSuccess(String str2, String str3) {
                            LogUtils.print(IoTRequestHandler.TAG, "sendIoTReques onSuccess url=" + str2 + ", result=" + str3);
                            if (ioTCallback != null) {
                                commonResponse.setData(str3);
                                ioTCallback.onResponse(commonRequest, commonResponse);
                            }
                        }
                    });
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.error(TAG, "sendSync error, e=" + e);
                return;
            }
        }
        if (ioTCallback != null) {
            ioTCallback.onFailure(commonRequest, new IllegalArgumentException("queryParamsNull"));
        }
    }

    private void getDeviceAuthToken(final CommonRequest commonRequest, final IoTCallback ioTCallback) {
        LogUtils.print(TAG, "getDeviceSecret call()");
        final String baseUrl = RequestHelper.getBaseUrl(commonRequest, "/auth");
        try {
            HashMap map = new HashMap();
            map.put("Content-Type", HttpHeaders.Values.APPLICATION_JSON);
            DeviceAuthInfo deviceData = LocalData.getInstance().getDeviceData();
            TreeMap treeMap = new TreeMap();
            treeMap.put("productKey", deviceData.productKey);
            treeMap.put("deviceName", deviceData.deviceName);
            treeMap.put(TmpConstant.KEY_CLIENT_ID, ParameterHelper.getUniqueNonce());
            String hmacSign = HmacSignUtils.getHmacSign(treeMap, deviceData.deviceSecret);
            treeMap.put("signmethod", MqttConfigure.SIGN_METHOD);
            treeMap.put("sign", hmacSign);
            if (ioTCallback == null) {
                LogUtils.error(TAG, "getDeviceAuthToken failed, callback=null.");
            } else {
                OkHttpManager.getInstance().postAsync(baseUrl, map, GsonUtils.toJsonString(treeMap), new HttpCallback<IOException, String>() { // from class: com.aliyun.alink.apiclient.biz.IoTRequestHandler.2
                    @Override // com.http.helper.HttpCallback
                    public void onFail(String str, IOException iOException) {
                        LogUtils.error(IoTRequestHandler.TAG, "getDeviceAuthToken onFail url=" + str + ", e=" + iOException);
                        ioTCallback.onFailure(commonRequest, iOException);
                    }

                    @Override // com.http.helper.HttpCallback
                    public void onSuccess(String str, String str2) {
                        LogUtils.print(IoTRequestHandler.TAG, "getDeviceAuthToken onSuccess url=" + str + ", result=" + str2);
                        if (StringUtils.isEmptyString(str2)) {
                            ioTCallback.onFailure(commonRequest, new IOException("onSuccessTokenNull", new HttpFailCode(0)));
                            return;
                        }
                        DeviceResponse deviceResponse = (DeviceResponse) GsonUtils.parseJson(str2, new TypeToken<DeviceResponse<Map<String, String>>>() { // from class: com.aliyun.alink.apiclient.biz.IoTRequestHandler.2.1
                        }.getType());
                        if (deviceResponse != null && deviceResponse.getInfo() != null && ((Map) deviceResponse.getInfo()).containsKey("token")) {
                            String str3 = (String) ((Map) deviceResponse.getInfo()).get("token");
                            LocalData.getInstance().setAuthToken(str3);
                            if ("/auth".equals(commonRequest.getPath())) {
                                CommonResponse commonResponse = new CommonResponse();
                                commonResponse.setData(str3);
                                ioTCallback.onResponse(commonRequest, commonResponse);
                                return;
                            } else {
                                if (StringUtils.isEmptyString(str3)) {
                                    ioTCallback.onFailure(commonRequest, new IOException("getAuthTokenFailed", new HttpFailCode(0)));
                                    return;
                                }
                                HashMap map2 = new HashMap();
                                map2.put("Content-Type", "application/octet-stream");
                                map2.put("password", str3);
                                IoTRequestHandler.this.sendIoIRequest(baseUrl, commonRequest, ioTCallback);
                                return;
                            }
                        }
                        LogUtils.error(IoTRequestHandler.TAG, "getDeviceAuthToken failed.");
                        ioTCallback.onFailure(commonRequest, new IOException("onSuccessTokenNull", new HttpFailCode(0)));
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "getDeviceSecret failed. e=" + e);
            ioTCallback.onFailure(commonRequest, e);
        }
    }
}
