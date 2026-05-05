package com.aliyun.alink.linksdk.tmp.utils;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRegisterRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectInfo;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.manager.connect.IRegisterConnectListener;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener;
import com.aliyun.alink.linksdk.tmp.connect.mix.MTopAndApiGMixRequest;
import com.aliyun.alink.linksdk.tmp.connect.mix.a;
import com.aliyun.alink.linksdk.tmp.connect.mix.c;
import com.aliyun.alink.linksdk.tmp.data.auth.AuthenRegisterReqPayload;
import com.aliyun.alink.linksdk.tmp.data.script.ScriptGetRequestPayload;
import com.aliyun.alink.linksdk.tmp.data.script.ScriptRequestItem;
import com.aliyun.alink.linksdk.tmp.data.service.UpdateDevInfoParam;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.facebook.share.internal.ShareConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/* JADX INFO: loaded from: classes2.dex */
public class CloudUtils {
    private static OkHttpClient OKHttpClientInstance = new OkHttpClient();
    protected static final String TAG = "[Tmp]CloudUtils";

    public static void queryAccessInfo(String str, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "queryAccessInfo iotId:" + str);
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        map.put("iotIdList", arrayList);
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build("/alcs/device/accessInfo/get", "1.0.0", map);
        apiGatewayRequestBuild.scheme = Scheme.HTTPS;
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(iConnectSendListener));
    }

    public static void queryPrefixSecret(String str, String str2, String str3, String str4, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "queryPrefx start product:" + str3 + " deviceName:" + str4 + " gateWayProductKey:" + str + " gateWayDeviceName:" + str2);
        HashMap map = new HashMap();
        map.put("productKey", str3);
        map.put("deviceName", str4);
        HashMap map2 = new HashMap();
        map2.put("id", 1);
        map2.put("version", "1.0");
        map2.put("method", "thing.lan.prefix.get");
        map2.put("params", map);
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.payloadObj = GsonUtils.toJson(map2);
        mqttPublishRequest.topic = "/sys/" + str + "/" + str2 + "/thing/lan/prefix/get";
        StringBuilder sb = new StringBuilder();
        sb.append(mqttPublishRequest.topic);
        sb.append(TmpConstant.URI_TOPIC_REPLY_POST);
        mqttPublishRequest.replyTopic = sb.toString();
        mqttPublishRequest.isRPC = true;
        ConnectSDK.getInstance().send(ConnectSDK.getInstance().getPersistentConnectId(), mqttPublishRequest, iConnectSendListener);
    }

    public static void queryPrefixSecret(String str, String str2, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "queryPrefx start product:" + str + " deviceName:" + str2);
        HashMap map = new HashMap();
        map.put("id", 1);
        map.put("version", "1.0");
        map.put("method", "thing.lan.prefix.get");
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.payloadObj = GsonUtils.toJson(map);
        mqttPublishRequest.topic = "/sys/" + str + "/" + str2 + "/thing/lan/prefix/get";
        StringBuilder sb = new StringBuilder();
        sb.append(mqttPublishRequest.topic);
        sb.append(TmpConstant.URI_TOPIC_REPLY_POST);
        mqttPublishRequest.replyTopic = sb.toString();
        mqttPublishRequest.isRPC = true;
        ConnectSDK.getInstance().send(ConnectSDK.getInstance().getPersistentConnectId(), mqttPublishRequest, iConnectSendListener);
    }

    public static void queryTimerDownlinkTask(String str, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/timer/downlink/task/query", "1.0.0", map)), new a(iConnectSendListener));
    }

    public static void getProperties(String str, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/properties/get", "1.0.0", map)), new a(iConnectSendListener));
    }

    public static void setProperties(Map<String, Object> map, IConnectSendListener iConnectSendListener) {
        boolean zBooleanValue;
        if (map.containsKey("comboFlag")) {
            zBooleanValue = ((Boolean) map.get("comboFlag")).booleanValue();
            map.remove("comboFlag");
        } else {
            zBooleanValue = false;
        }
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build(zBooleanValue ? "/living/combomesh/thing/properties/set" : "/living/thing/properties/set", "1.0.0", map)), new a(iConnectSendListener));
    }

    public static void executeScene(String str, Map<String, Object> map, IConnectSendListener iConnectSendListener) {
        Set<String> setKeySet = map.keySet();
        if (setKeySet != null) {
            HashMap map2 = new HashMap();
            map2.put("iotId", str);
            for (String str2 : setKeySet) {
                if (map.get(str2) != null && str2.contains("scene")) {
                    map2.put("sceneNumber", map.get(str2).toString());
                    c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/living/scene/device/control", "1.0.0", map2)), new a(iConnectSendListener));
                    return;
                }
            }
        }
    }

    public static void setGroupProperties(Map<String, Object> map, IConnectSendListener iConnectSendListener) {
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build("/living/controlgroup/properties/set", "1.0.0", map);
        apiGatewayRequestBuild.scheme = Scheme.HTTPS;
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(iConnectSendListener));
    }

    public static void getEvents(String str, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/events/get", "1.0.0", map)), new a(iConnectSendListener));
    }

    public static void invokeService(Map<String, Object> map, IConnectSendListener iConnectSendListener) {
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/service/invoke", "1.0.5", map)), new a(iConnectSendListener));
    }

    public static void getStatus(String str, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/status/get", "1.0.5", map)), new a(iConnectSendListener));
    }

    public static void getTsl(String str, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/tsl/get", "1.0.4", map)), new a(iConnectSendListener));
    }

    public static void subPrefixUpdateRrpc(String str, String str2, IConnectRrpcListener iConnectRrpcListener) {
        StringBuilder sb = new StringBuilder("/");
        sb.append("sys");
        sb.append("/");
        sb.append(str);
        sb.append("/");
        sb.append(str2);
        sb.append(TmpConstant.URI_PREFIX_UPDATE_POST);
        String string = sb.toString();
        sb.append(TmpConstant.URI_PREFIX_UPDATE_REPLY_POST);
        sb.toString();
        subMqttTopicRrpc(string, iConnectRrpcListener);
    }

    public static void subBlacklistUpdateRrpc(String str, String str2, IConnectRrpcListener iConnectRrpcListener) {
        StringBuilder sb = new StringBuilder("/");
        sb.append("sys");
        sb.append("/");
        sb.append(str);
        sb.append("/");
        sb.append(str2);
        sb.append(TmpConstant.URI_BLACKLIST_UPDATE_POST);
        String string = sb.toString();
        sb.append(TmpConstant.URI_BLACKLIST_UPDATE_REPLY_POST);
        sb.toString();
        subMqttTopicRrpc(string, iConnectRrpcListener);
    }

    public static void subMqttTopicRrpc(String str, IConnectRrpcListener iConnectRrpcListener) {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.topic = str;
        ConnectSDK.getInstance().subscribeRrpc(ConnectSDK.getInstance().getPersistentConnectId(), commonRequest, iConnectRrpcListener);
    }

    public static void registerPersistentConnect(String str, String str2, String str3, IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerPersistentConnect iotProductKey:" + str + " iotDevName:" + str2);
        PersistentConnectConfig persistentConnectConfig = new PersistentConnectConfig();
        persistentConnectConfig.productKey = str;
        persistentConnectConfig.deviceName = str2;
        persistentConnectConfig.deviceSecret = str3;
        TmpInitConfig config2 = TmpSdk.getConfig();
        if (config2 != null) {
            persistentConnectConfig.channelHost = config2.mMqttChannelHost;
            persistentConnectConfig.isCheckChannelRootCrt = config2.mIsCheckChannelRootCrt;
        }
        ConnectSDK.getInstance().registerPersistentConnect(TmpSdk.getContext(), persistentConnectConfig, iRegisterConnectListener);
    }

    public static void queryProductInfo(String str, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "queryProductInfo iotid:" + str);
        HashMap map = new HashMap();
        map.put("iotId", str);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/detailInfo/queryProductInfo", "1.1.3", map)), new a(iConnectSendListener));
    }

    public static void listBindingByDev(String str, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "listBindingByDev iotId:" + str);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build("/uc/listBindingByDev", "1.0.2", map);
        apiGatewayRequestBuild.scheme = Scheme.HTTPS;
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(iConnectSendListener));
    }

    public static void queryProductJsCode(String str, String str2, String str3, String str4, String str5, IConnectSendListener iConnectSendListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScriptRequestItem(str3, str4, str5));
        ScriptGetRequestPayload scriptGetRequestPayload = new ScriptGetRequestPayload(arrayList);
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.payloadObj = GsonUtils.toJson(scriptGetRequestPayload);
        mqttPublishRequest.topic = "/sys/" + str + "/" + str2 + "/thing/script/get";
        StringBuilder sb = new StringBuilder();
        sb.append(mqttPublishRequest.topic);
        sb.append(TmpConstant.URI_TOPIC_REPLY_POST);
        mqttPublishRequest.replyTopic = sb.toString();
        mqttPublishRequest.isRPC = true;
        ALog.d(TAG, "queryProductJsCode productKey:" + str3 + " topicDeviceName：" + str2 + " topic：" + mqttPublishRequest.topic + " payload：" + mqttPublishRequest.payloadObj);
        ConnectSDK.getInstance().send(ConnectSDK.getInstance().getPersistentConnectId(), mqttPublishRequest, iConnectSendListener);
    }

    public static void queryProductJsCode(String str, String str2, String str3, IConnectSendListener iConnectSendListener) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        HashMap map2 = new HashMap();
        map2.put("iotId", str);
        map2.put("digest", str2);
        map2.put("digestMethod", str3);
        arrayList.add(map2);
        map.put("scriptQuery", arrayList);
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build("/thing/script/get", "1.0.4", map);
        ALog.d(TAG, "queryProductJsCode iotId:" + str + " digest：" + str2 + " digestMethod：" + str3);
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(iConnectSendListener));
    }

    public static void queryHttpUrl(String str, Callback callback) {
        OKHttpClientInstance.newCall(new Request.Builder().url(str).get().build()).enqueue(callback);
    }

    public static void getProductKey(final String str, final ICloudProxyListener iCloudProxyListener) {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_PRODUCT_ID, "" + str);
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build(AlinkConstants.PROVISION_DEVICE_PIDTOPK, AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION, map);
        apiGatewayRequestBuild.authType = AlinkConstants.KEY_IOT_AUTH;
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.utils.CloudUtils.1
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.w(CloudUtils.TAG, "onResponse" + aResponse + " cb:" + iCloudProxyListener);
                ICloudProxyListener iCloudProxyListener2 = iCloudProxyListener;
                if (iCloudProxyListener2 != null) {
                    if (aResponse != null) {
                        iCloudProxyListener2.onResponse(str, aResponse.data);
                    } else {
                        onFailure(aRequest, null);
                    }
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.w(CloudUtils.TAG, "onFailure" + aError + " cb:" + iCloudProxyListener);
                ICloudProxyListener iCloudProxyListener2 = iCloudProxyListener;
                if (iCloudProxyListener2 != null) {
                    iCloudProxyListener2.onFailure(str, aError);
                }
            }
        }));
    }

    public static void setDeviceExtendProperty(String str, String str2, String str3, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "setDeviceExtendProperty iotId:" + str + " dataKey:" + str2 + " dataValue:" + str3);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("dataKey", str2);
        map.put("dataValue", str3);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/extended/property/set", "1.0.2", map)), new a(iConnectSendListener));
    }

    public static void getDeviceExtendProperty(String str, String str2, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "getDeviceExtendProperty iotId:" + str + " dataKey:" + str2);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("dataKey", str2);
        c.a(new MTopAndApiGMixRequest(ApiGatewayRequest.build("/thing/extended/property/get", "1.0.2", map)), new a(iConnectSendListener));
    }

    public static void subPushDeviceInfo(String str, String str2, IConnectRrpcListener iConnectRrpcListener) {
        PersistentConnectInfo persistentConnectInfo = (PersistentConnectInfo) ConnectSDK.getInstance().getConnectInfo(ConnectSDK.getInstance().getPersistentConnectId());
        MqttRrpcRegisterRequest mqttRrpcRegisterRequest = new MqttRrpcRegisterRequest();
        if (persistentConnectInfo == null) {
            iConnectRrpcListener.onSubscribeFailed(mqttRrpcRegisterRequest, new AError());
            return;
        }
        mqttRrpcRegisterRequest.topic = "/sys/" + persistentConnectInfo.productKey + "/" + persistentConnectInfo.deviceName + "/thing/push/device/info";
        StringBuilder sb = new StringBuilder();
        sb.append(mqttRrpcRegisterRequest.topic);
        sb.append(TmpConstant.URI_TOPIC_REPLY_POST);
        mqttRrpcRegisterRequest.replyTopic = sb.toString();
        ConnectSDK.getInstance().subscribeRrpc(ConnectSDK.getInstance().getPersistentConnectId(), mqttRrpcRegisterRequest, iConnectRrpcListener);
    }

    public static void subDevAuthenRegister(String str, String str2, IConnectSendListener iConnectSendListener) {
        PersistentConnectInfo persistentConnectInfo = (PersistentConnectInfo) ConnectSDK.getInstance().getConnectInfo(ConnectSDK.getInstance().getPersistentConnectId());
        String replaceTopic = TextHelper.formatReplaceTopic(TmpConstant.URI_AUTHEN_REGISTER, persistentConnectInfo.productKey, persistentConnectInfo.deviceName);
        String replaceTopic2 = TextHelper.formatReplaceTopic(TmpConstant.URI_AUTHEN_REGISTER_REPLY, persistentConnectInfo.productKey, persistentConnectInfo.deviceName);
        AuthenRegisterReqPayload authenRegisterReqPayload = new AuthenRegisterReqPayload();
        authenRegisterReqPayload.id = 123;
        authenRegisterReqPayload.method = "thing.authen.sub.register";
        authenRegisterReqPayload.version = "1.0";
        authenRegisterReqPayload.params = new ArrayList();
        authenRegisterReqPayload.params.add(new AuthenRegisterReqPayload.AuthenRegisterParams(str, str2));
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.payloadObj = GsonUtils.toJson(authenRegisterReqPayload);
        mqttPublishRequest.topic = replaceTopic;
        mqttPublishRequest.replyTopic = replaceTopic2;
        mqttPublishRequest.isRPC = true;
        ConnectSDK.getInstance().send(ConnectSDK.getInstance().getPersistentConnectId(), mqttPublishRequest, iConnectSendListener);
    }

    public static void updateBreezeMac(String str, String str2, String str3, IConnectSendListener iConnectSendListener) {
        registerPersistentConnect(null, null, null, null);
        String replaceTopic = TextHelper.formatReplaceTopic(TmpConstant.URI_UPDATE_DEVICE_INFO, str, str2);
        String replaceTopic2 = TextHelper.formatReplaceTopic(TmpConstant.URI_UPDATE_DEVICE_INFO_REPLY, str, str2);
        UpdateDevInfoParam updateDevInfoParam = new UpdateDevInfoParam();
        updateDevInfoParam.attrKey = TmpConstant.DATA_KEY_DEVICENAME;
        updateDevInfoParam.attrValue = str3;
        ArrayList arrayList = new ArrayList();
        arrayList.add(updateDevInfoParam);
        CommonRequestPayload commonRequestPayload = new CommonRequestPayload();
        commonRequestPayload.setId("123");
        commonRequestPayload.setMethod("thing.deviceinfo.update");
        commonRequestPayload.setVersion("1.0");
        commonRequestPayload.setParams(arrayList);
        MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
        mqttPublishRequest.payloadObj = GsonUtils.toJson(commonRequestPayload);
        mqttPublishRequest.topic = replaceTopic;
        mqttPublishRequest.replyTopic = replaceTopic2;
        mqttPublishRequest.isRPC = true;
        ConnectSDK.getInstance().send(ConnectSDK.getInstance().getPersistentConnectId(), mqttPublishRequest, iConnectSendListener);
    }

    public static void getLcaDeviceList(String str, String str2, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "getLcaDeviceList gatewayIotId:" + str2 + " groupId:" + str + " listener:" + iConnectSendListener);
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 1000);
        map.put(DeviceCommonConstants.KEY_MESSAGE_GROUP, str);
        map.put("gatewayIotId", str2);
        HashMap map2 = new HashMap();
        map2.put(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, map);
        ApiGatewayRequest apiGatewayRequestBuild = ApiGatewayRequest.build("/awss/enrollee/lca/product/list", "1.0.5", map2);
        apiGatewayRequestBuild.scheme = Scheme.HTTPS;
        c.a(new MTopAndApiGMixRequest(apiGatewayRequestBuild), new a(iConnectSendListener));
    }
}
