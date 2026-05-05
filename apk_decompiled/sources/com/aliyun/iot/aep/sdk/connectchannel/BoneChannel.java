package com.aliyun.iot.aep.sdk.connectchannel;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorCode;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.sdk.jsbridge.BoneCallback;
import com.aliyun.alink.sdk.jsbridge.IJSBridge;
import com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin;
import com.aliyun.alink.sdk.jsbridge.methodexport.MethodExported;
import com.aliyun.iot.aep.sdk.connectchannel.log.ALog;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class BoneChannel extends BaseBonePlugin {
    public static final String API_NAME = "BoneChannel";
    private static final String TAG = "BoneChannel";
    private List<String> topicList = new ArrayList();
    private IMobileDownstreamListener downstreamListener = new IMobileDownstreamListener() { // from class: com.aliyun.iot.aep.sdk.connectchannel.BoneChannel.1
        @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
        public void onCommand(String str, String str2) {
            ALog.i("BoneChannel", "downstream success topic=" + str);
            ALog.i("BoneChannel", "downstream success msg=" + str2);
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("topic", str);
                jSONObject.put("payload", str2);
            } catch (JSONException unused) {
            }
            if (BoneChannel.this.jsBridge != null) {
                BoneChannel.this.jsBridge.emit("BoneDownstream", jSONObject);
            }
        }

        @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
        public boolean shouldHandle(String str) {
            ALog.i("BoneChannel", "downstream filter=" + str);
            return BoneChannel.this.topicList != null && BoneChannel.this.topicList.size() > 0 && BoneChannel.this.topicList.contains(str);
        }
    };
    private IMobileConnectListener connectListener = new IMobileConnectListener() { // from class: com.aliyun.iot.aep.sdk.connectchannel.BoneChannel.2
        @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener
        public void onConnectStateChange(MobileConnectState mobileConnectState) {
            ALog.i("BoneChannel", "connectListener onConnectStateChange mobileConnectState=" + mobileConnectState);
            JSONObject jSONObjectJsonStatus = BoneChannel.this.jsonStatus(mobileConnectState);
            if (BoneChannel.this.jsBridge != null) {
                BoneChannel.this.jsBridge.emit("BoneChannelConnectStatusChange", jSONObjectJsonStatus);
            }
        }
    };

    @Override // com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin, com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onDestroy() {
        if (this.connectListener != null) {
            MobileChannel.getInstance().unRegisterConnectListener(this.connectListener);
        }
        if (this.downstreamListener != null) {
            MobileChannel.getInstance().unRegisterDownstreamListener(this.downstreamListener);
        }
        List<String> list = this.topicList;
        if (list != null) {
            list.clear();
        }
        super.onDestroy();
    }

    @Override // com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin, com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onInitialize(Context context, IJSBridge iJSBridge) {
        super.onInitialize(context, iJSBridge);
        ALog.setLevel((byte) 2);
        MobileChannel.getInstance().registerConnectListener(true, this.connectListener);
        MobileChannel.getInstance().registerDownstreamListener(true, this.downstreamListener);
    }

    @MethodExported
    public void getConnectStatus(BoneCallback boneCallback) {
        JSONObject jSONObjectJsonStatus = jsonStatus(MobileChannel.getInstance().getMobileConnectState());
        if (jSONObjectJsonStatus == null) {
            boneCallback.failed("status", "no state");
        }
        boneCallback.success(jSONObjectJsonStatus);
    }

    @MethodExported
    public void subscribe(String str, BoneCallback boneCallback) {
        if (TextUtils.isEmpty(str)) {
            boneCallback.failed("419", "topic不能为空");
            return;
        }
        if (!str.startsWith("/") || str.contains(MqttTopic.MULTI_LEVEL_WILDCARD) || str.contains(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
            boneCallback.failed("418", "topic无效");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", ErrorCode.UNKNOWN_SUCCESS_CODE);
            jSONObject.put("msg", "subscribe success");
        } catch (JSONException unused) {
        }
        boneCallback.success(jSONObject);
        ALog.i("BoneChannel", "subscribe success");
        this.topicList.add(str);
    }

    @MethodExported
    public void unsubscribe(String str, BoneCallback boneCallback) {
        if (TextUtils.isEmpty(str)) {
            boneCallback.failed("419", "topic不能为空");
            return;
        }
        if (!str.startsWith("/") || str.contains(MqttTopic.MULTI_LEVEL_WILDCARD) || str.contains(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
            boneCallback.failed("418", "topic无效");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", ErrorCode.UNKNOWN_SUCCESS_CODE);
            jSONObject.put("msg", "unsubscribe success");
        } catch (JSONException unused) {
        }
        boneCallback.success(jSONObject);
        ALog.i("BoneChannel", "unsubscribe success");
        this.topicList.remove(str);
    }

    @MethodExported
    public void request(String str, JSONObject jSONObject, final BoneCallback boneCallback) {
        MobileChannel.getInstance().asyncSendRequest(str, null, jSONObject, new IMobileRequestListener() { // from class: com.aliyun.iot.aep.sdk.connectchannel.BoneChannel.3
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onSuccess(String str2) {
                ALog.i("BoneChannel", "request success=" + str2);
                if (TextUtils.isEmpty(str2)) {
                    boneCallback.success(new JSONObject());
                    return;
                }
                try {
                    boneCallback.success(new JSONObject(str2));
                } catch (JSONException unused) {
                    boneCallback.success(new JSONObject());
                }
            }

            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onFailure(AError aError) {
                ALog.i("BoneChannel", "request fail=");
                boneCallback.failed("608", aError.getMsg());
            }
        });
    }

    @MethodExported
    public void publish(String str, JSONObject jSONObject, final BoneCallback boneCallback) {
        MobileChannel.getInstance().ayncSendPublishRequest(str, jSONObject, new IMobileRequestListener() { // from class: com.aliyun.iot.aep.sdk.connectchannel.BoneChannel.4
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onSuccess(String str2) {
                ALog.i("BoneChannel", "publish success=" + str2);
                if (TextUtils.isEmpty(str2)) {
                    boneCallback.success(new JSONObject());
                    return;
                }
                try {
                    boneCallback.success(new JSONObject(str2));
                } catch (JSONException unused) {
                    boneCallback.success(new JSONObject());
                }
            }

            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onFailure(AError aError) {
                ALog.i("BoneChannel", "publish fail=");
                boneCallback.failed("608", aError.getMsg());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject jsonStatus(MobileConnectState mobileConnectState) {
        try {
            JSONObject jSONObject = new JSONObject();
            try {
                switch (mobileConnectState) {
                    case CONNECTED:
                        jSONObject.put("status", "Connected");
                        return jSONObject;
                    case CONNECTING:
                        jSONObject.put("status", "Connecting");
                        return jSONObject;
                    case DISCONNECTED:
                    case CONNECTFAIL:
                        jSONObject.put("status", "Disconnected");
                        return jSONObject;
                    default:
                        jSONObject.put("status", "Disconnected");
                        return jSONObject;
                }
            } catch (JSONException unused) {
                return jSONObject;
            }
        } catch (JSONException unused2) {
            return null;
        }
    }
}
