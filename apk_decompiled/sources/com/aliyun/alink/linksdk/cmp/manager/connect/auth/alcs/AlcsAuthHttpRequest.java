package com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AConnect;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsAuthHttpRequest {
    private static final String PATH_CLIENT = "/alcs/device/accessInfo/get";
    private static final String TAG = "AlcsAuthHttpRequest";
    private static final String TOPIC_SERVER = "/thing/lan/prefix/get";

    public interface IAlcsAuthCallback {
        void onFailed(AError aError);

        void onSuccess(Object obj);
    }

    public static void requestClientInfo(String str, final IAlcsAuthCallback iAlcsAuthCallback) {
        ALog.d(TAG, "requestClientInfo");
        if (iAlcsAuthCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            if (iAlcsAuthCallback != null) {
                iAlcsAuthCallback.onFailed(CmpError.PARAMS_ERROR());
                return;
            }
            return;
        }
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            AConnect apiGatewayConnect = ConnectManager.getInstance().getApiGatewayConnect();
            ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build(PATH_CLIENT, "1.0.0", null);
            apiGatewayRequestBuild.addParams("iotIdList", arrayList);
            apiGatewayConnect.send(apiGatewayRequestBuild, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.1
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("requestClientInfo, onResponse(),rsp = ");
                    sb.append((aResponse == null || aResponse.data == null) ? "" : (String) aResponse.data);
                    ALog.d(AlcsAuthHttpRequest.TAG, sb.toString());
                    try {
                        JSONObject object = JSONObject.parseObject((String) aResponse.data);
                        String string = object.getString("msg");
                        if (object.getIntValue("code") != 200) {
                            AlcsAuthHttpRequest.clientFailCallback(string, iAlcsAuthCallback);
                        } else {
                            AlcsClientAuthValue alcsClientAuthValue = (AlcsClientAuthValue) object.getJSONObject("data").getJSONArray("alcsDeviceDTOList").getObject(0, AlcsClientAuthValue.class);
                            if (alcsClientAuthValue != null && alcsClientAuthValue.checkValid()) {
                                iAlcsAuthCallback.onSuccess(alcsClientAuthValue);
                            }
                        }
                    } catch (Exception e) {
                        ALog.e(AlcsAuthHttpRequest.TAG, "requestClientInfo,onResponse(), error = " + e.toString());
                        AlcsAuthHttpRequest.clientFailCallback(e.toString(), iAlcsAuthCallback);
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    ALog.e(AlcsAuthHttpRequest.TAG, "requestClientInfo,onErrorResponse(), error = " + aError.getMsg());
                    AlcsAuthHttpRequest.clientFailCallback(aError.getMsg(), iAlcsAuthCallback);
                }
            });
        } catch (Exception e) {
            ALog.d(TAG, "requestClientInfo, request error, e = " + e.toString());
            e.printStackTrace();
            clientFailCallback(e.toString(), iAlcsAuthCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void clientFailCallback(String str, IAlcsAuthCallback iAlcsAuthCallback) {
        CmpError cmpErrorALCS_REQUEST_CLIENT_AUTH_FAIL = CmpError.ALCS_REQUEST_CLIENT_AUTH_FAIL();
        cmpErrorALCS_REQUEST_CLIENT_AUTH_FAIL.setSubMsg(str);
        iAlcsAuthCallback.onFailed(cmpErrorALCS_REQUEST_CLIENT_AUTH_FAIL);
    }

    public static void requestServerInfo(final IAlcsAuthCallback iAlcsAuthCallback) {
        ALog.d(TAG, "requestServerInfo");
        if (iAlcsAuthCallback == null) {
            ALog.e(TAG, "requestServerInfo callback null");
            return;
        }
        try {
            AConnect persistentConnect = ConnectManager.getInstance().getPersistentConnect();
            if (persistentConnect == null) {
                serverFailCallback("connect not found", iAlcsAuthCallback);
                return;
            }
            MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
            mqttPublishRequest.isRPC = true;
            mqttPublishRequest.topic = TOPIC_SERVER;
            persistentConnect.send(mqttPublishRequest, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.2
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("requestServerInfo, onResponse(),rsp = ");
                    sb.append((aResponse == null || aResponse.data == null) ? "" : (String) aResponse.data);
                    ALog.d(AlcsAuthHttpRequest.TAG, sb.toString());
                    try {
                        JSONObject object = JSONObject.parseObject((String) aResponse.data);
                        String string = object.getString("msg");
                        if (object.getIntValue("code") != 200) {
                            AlcsAuthHttpRequest.serverFailCallback(string, iAlcsAuthCallback);
                        } else {
                            AlcsServerAuthValue alcsServerAuthValue = (AlcsServerAuthValue) object.getObject("data", AlcsServerAuthValue.class);
                            if (alcsServerAuthValue != null && alcsServerAuthValue.checkValid()) {
                                iAlcsAuthCallback.onSuccess(alcsServerAuthValue);
                            }
                        }
                    } catch (Exception e) {
                        ALog.e(AlcsAuthHttpRequest.TAG, "requestServerInfo,onResponse(), error = " + e.toString());
                        AlcsAuthHttpRequest.serverFailCallback(e.toString(), iAlcsAuthCallback);
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    ALog.e(AlcsAuthHttpRequest.TAG, "requestServerInfo,onErrorResponse(), error = " + aError.getMsg());
                    AlcsAuthHttpRequest.serverFailCallback(aError.getMsg(), iAlcsAuthCallback);
                }
            });
        } catch (Exception e) {
            ALog.d(TAG, "requestServerInfo, request error, e = " + e.toString());
            e.printStackTrace();
            serverFailCallback(e.toString(), iAlcsAuthCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serverFailCallback(String str, IAlcsAuthCallback iAlcsAuthCallback) {
        CmpError cmpErrorALCS_REQUEST_SERVER_AUTH_FAIL = CmpError.ALCS_REQUEST_SERVER_AUTH_FAIL();
        cmpErrorALCS_REQUEST_SERVER_AUTH_FAIL.setSubMsg(str);
        iAlcsAuthCallback.onFailed(cmpErrorALCS_REQUEST_SERVER_AUTH_FAIL);
    }
}
