package com.aliyun.alink.linksdk.channel.gateway.a;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorCode;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectConfig;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectState;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayErrorCode;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayDownstreamListener;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayRequestListener;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewaySubscribeListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ErrorResponse;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceInfo;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceInfoWrapper;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceLoginState;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager;
import com.aliyun.alink.linksdk.cmp.manager.connect.IRegisterConnectListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.taobao.agoo.control.data.BaseDO;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: GatewayChannelImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements IGatewayChannel {
    private Context e;
    private IGatewayConnectListener f;
    private PersistentConnectConfig h;
    private Map<String, SubDeviceInfo> i;
    private Map<String, ISubDeviceConnectListener> j;
    private Map<String, ISubDeviceChannel> k;
    private GatewayConnectState g = GatewayConnectState.DISCONNECTED;
    private b l = null;
    private AtomicBoolean m = new AtomicBoolean(false);
    private CopyOnWriteArraySet<String> n = new CopyOnWriteArraySet<>();
    private int o = 64;
    private LruCache<String, SubDeviceInfoWrapper> p = new LruCache<>(this.o);
    private boolean q = false;
    private String r = null;
    private String s = "427";
    private String t = "520";
    private String u = "521";
    private String v = "522";
    private String w = "6401";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    String f4150a = "thing/sub/register";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    String f4151b = "thing.sub.register";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    String f4152c = "thing/sub/unregister";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    String f4153d = "thing.sub.unregister";
    private final String x = "thing/list/found";
    private final String y = "thing.list.found";
    private final String z = "thing/topo/get";
    private final String A = "thing.topo.get";
    private final String B = "thing/topo/add";
    private final String C = "thing.topo.add";
    private final String D = "_thing/service/post";
    private final String E = "_thing.service.post";
    private final String F = "thing/topo/delete";
    private final String G = "thing.topo.delete";
    private IConnectNotifyListener H = new IConnectNotifyListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.7
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public void onNotify(String str, String str2, AMessage aMessage) {
            if (aMessage == null || a.this.h == null || TextUtils.isEmpty(a.this.r) || !a.this.r.equals(str2)) {
                return;
            }
            try {
                if (aMessage.data instanceof byte[]) {
                    String str3 = new String((byte[]) aMessage.data, "UTF-8");
                    ErrorResponse errorResponse = (ErrorResponse) JSONObject.parseObject(str3, new TypeReference<ErrorResponse<SubDeviceInfo>>() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.7.1
                    }.getType(), new Feature[0]);
                    String str4 = errorResponse.code;
                    ALog.w("GatewayChannelImpl", "device error received. " + str3);
                    SubDeviceInfo subDeviceInfo = (SubDeviceInfo) errorResponse.data;
                    if (subDeviceInfo != null && subDeviceInfo.checkValid()) {
                        if (!subDeviceInfo.productKey.equals(a.this.h.productKey) || !subDeviceInfo.deviceName.equals(a.this.h.deviceName)) {
                            if (!a.this.v.equals(str4)) {
                                if (!a.this.u.equals(str4) && !a.this.w.equals(str4)) {
                                    if (!a.this.s.equals(str4)) {
                                        if (a.this.t.equals(str4)) {
                                            ALog.w("GatewayChannelImpl", "device session error. devInfo=" + str3);
                                            a.this.k.remove(subDeviceInfo.getDeviceId());
                                            return;
                                        }
                                        return;
                                    }
                                    ALog.w("GatewayChannelImpl", "device login by other device. device need login again. devInfo=" + str3);
                                    a.this.k.remove(subDeviceInfo.getDeviceId());
                                    return;
                                }
                                ALog.i("GatewayChannelImpl", "remove device topo relation! deviceInfo=" + subDeviceInfo);
                                a.this.k.remove(subDeviceInfo.getDeviceId());
                                a.this.j.remove(subDeviceInfo.getDeviceId());
                                a.this.i.remove(subDeviceInfo.getDeviceId());
                                return;
                            }
                            ALog.i("GatewayChannelImpl", "device was forbidden. deviceInfo=" + subDeviceInfo);
                            return;
                        }
                        ALog.w("GatewayChannelImpl", "gateway device error.");
                    }
                }
            } catch (Exception unused) {
            }
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public boolean shouldHandle(String str, String str2) {
            return (a.this.h == null || TextUtils.isEmpty(a.this.r) || !a.this.r.equals(str2)) ? false : true;
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public void onConnectStateChange(String str, ConnectState connectState) {
            if (ConnectSDK.getInstance().getPersistentConnectId().equals(str)) {
                a.this.g = GatewayConnectState.toGatewayConnectState(connectState);
                if (connectState == ConnectState.CONNECTED) {
                    a.this.q = true;
                    a.this.c();
                }
                if (a.this.f != null) {
                    a.this.f.onConnectStateChange(a.this.g);
                }
            }
        }
    };

    public a() {
        this.i = null;
        this.j = null;
        this.k = null;
        this.i = new ConcurrentHashMap();
        this.j = new ConcurrentHashMap();
        this.k = new ConcurrentHashMap();
    }

    public void a(boolean z) {
        this.m.set(z);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void startConnect(Context context, PersistentConnectConfig persistentConnectConfig, IGatewayConnectListener iGatewayConnectListener) {
        ALog.d("GatewayChannelImpl", "startConnect()");
        if (context == null || !a(persistentConnectConfig)) {
            ALog.e("GatewayChannelImpl", "startConnect(), param error, config is empty");
            return;
        }
        this.e = context;
        this.f = iGatewayConnectListener;
        this.h = persistentConnectConfig;
        this.r = "/ext/error/" + this.h.productKey + "/" + this.h.deviceName;
        if (this.q || this.g == GatewayConnectState.CONNECTING || this.g == GatewayConnectState.CONNECTED) {
            ALog.d("GatewayChannelImpl", "startConnect(), channel is connecting or connected");
        } else {
            a(context, persistentConnectConfig);
        }
    }

    private boolean a(PersistentConnectConfig persistentConnectConfig) {
        return (persistentConnectConfig == null || TextUtils.isEmpty(persistentConnectConfig.productKey) || TextUtils.isEmpty(persistentConnectConfig.deviceName)) ? false : true;
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void startConnectReuseMobileChannel(Context context, IGatewayConnectListener iGatewayConnectListener) {
        ALog.d("GatewayChannelImpl", "startConnectReuseMobileChannel()");
        String clientId = MobileChannel.getInstance().getClientId();
        String str = "";
        String str2 = "";
        if (!TextUtils.isEmpty(clientId)) {
            str = clientId.split("&")[1];
            str2 = clientId.split("&")[0];
        }
        GatewayConnectConfig gatewayConnectConfig = new GatewayConnectConfig(str, str2, "");
        if (!a(gatewayConnectConfig)) {
            ALog.d("GatewayChannelImpl", "startConnectReuseMobileChannel(), get mobile client id error,mark sure MobileConnect connected firstly");
            if (iGatewayConnectListener != null) {
                iGatewayConnectListener.onConnectStateChange(GatewayConnectState.CONNECTFAIL);
                return;
            }
            return;
        }
        startConnect(context, gatewayConnectConfig, iGatewayConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public GatewayConnectState getGatewayConnectState() {
        return this.g;
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDeviceRegister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        ALog.d("GatewayChannelImpl", "subDeviceRegister()");
        if (list == null || list.size() < 1) {
            if (iConnectSendListener != null) {
                AError aError = new AError();
                aError.setCode(1101400);
                aError.setMsg("subDeviceRegister infoList empty");
                iConnectSendListener.onFailure(null, aError);
                return;
            }
            return;
        }
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            SubDeviceInfo subDeviceInfo = list.get(i);
            if (subDeviceInfo != null && !TextUtils.isEmpty(subDeviceInfo.productKey) && !TextUtils.isEmpty(subDeviceInfo.deviceName)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("deviceName", (Object) subDeviceInfo.deviceName);
                jSONObject.put("productKey", (Object) subDeviceInfo.productKey);
                jSONArray.add(jSONObject);
            }
        }
        ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, this.f4150a, this.f4151b, null, jSONArray), iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDeviceRegister(ARequest aRequest, IConnectSendListener iConnectSendListener) {
        ALog.i("GatewayChannelImpl", "subDeviceRegister() called with: requestData = [" + aRequest + "], listener = [" + iConnectSendListener + "]");
        if (!(aRequest instanceof MqttPublishRequest) || iConnectSendListener == null) {
            if (iConnectSendListener != null) {
                AError aError = new AError();
                aError.setCode(1102000);
                aError.setMsg("subDeviceRegister listener is null or requestData not instance of MqttPublishRequest.");
                iConnectSendListener.onFailure(null, aError);
                return;
            }
            return;
        }
        MqttPublishRequest mqttPublishRequest = (MqttPublishRequest) aRequest;
        if (mqttPublishRequest.payloadObj == null) {
            AError aError2 = new AError();
            aError2.setCode(1102000);
            aError2.setMsg("subDeviceRegister request payload is null.");
            iConnectSendListener.onFailure(null, aError2);
            return;
        }
        if (TextUtils.isEmpty(mqttPublishRequest.topic)) {
            mqttPublishRequest.topic = "/sys/" + this.h.productKey + "/" + this.h.deviceName + GatewayChannel.TOPIC_PRESET_SUBDEV_REGITER;
            mqttPublishRequest.isRPC = true;
        }
        try {
            ConnectSDK.getInstance().send(aRequest, iConnectSendListener);
        } catch (Exception e) {
            AError aError3 = new AError();
            aError3.setCode(4201);
            aError3.setMsg("subDeviceRegister send exception=" + e);
            iConnectSendListener.onFailure(aRequest, aError3);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDevicUnregister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        ALog.d("GatewayChannelImpl", "subDevicUnregister() called， interface deprecated!");
        if (iConnectSendListener != null) {
            AError aError = new AError();
            aError.setCode(1102001);
            aError.setMsg("interface deprecated!");
            iConnectSendListener.onFailure(null, aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void addSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener) {
        ALog.d("GatewayChannelImpl", "addSubDevice()");
        if (iSubDeviceConnectListener == null) {
            throw new IllegalArgumentException("addSubDevice listener cannot be null.");
        }
        if (subDeviceInfo == null || !subDeviceInfo.checkValid()) {
            ALog.d("GatewayChannelImpl", "addSubDevice(), params error");
            AError aError = new AError();
            aError.setCode(GatewayErrorCode.ERROR_INVOKE_PARAMS_INVALID);
            aError.setMsg("addSubDeviceForILopGlobal subDeviceInfoList empty.");
            iSubDeviceConnectListener.onConnectResult(false, null, aError);
            return;
        }
        if (this.g != GatewayConnectState.CONNECTED) {
            AError aError2 = new AError();
            aError2.setCode(4101);
            aError2.setMsg("addSubDevice device not connected");
            iSubDeviceConnectListener.onConnectResult(false, null, aError2);
            return;
        }
        this.j.put(subDeviceInfo.getDeviceId(), iSubDeviceConnectListener);
        a(subDeviceInfo, iSubDeviceConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void addSubDeviceForILopGlobal(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener) {
        ALog.d("GatewayChannelImpl", "addSubDeviceForILopGlobal() called with: subDeviceInfo = [" + subDeviceInfo + "], subDeviceConnectListener = [" + iSubDeviceConnectListener + "]");
        if (iSubDeviceConnectListener == null) {
            throw new IllegalArgumentException("addSubDeviceForILopGlobal subDeviceConnectListener cannot be null.");
        }
        if (subDeviceInfo == null || !subDeviceInfo.checkValid()) {
            ALog.e("GatewayChannelImpl", "addSubDeviceForILopGlobal(), params error");
            AError aError = new AError();
            aError.setCode(GatewayErrorCode.ERROR_INVOKE_PARAMS_INVALID);
            aError.setMsg("addSubDeviceForILopGlobal subDeviceInfo invalid.");
            iSubDeviceConnectListener.onConnectResult(false, null, aError);
            return;
        }
        if (this.g != GatewayConnectState.CONNECTED) {
            if (iSubDeviceConnectListener != null) {
                AError aError2 = new AError();
                aError2.setCode(4101);
                aError2.setMsg("addSubDeviceForILopGlobal device not connected");
                iSubDeviceConnectListener.onConnectResult(false, null, aError2);
                return;
            }
            return;
        }
        PersistentConnectConfig persistentConnectConfig = this.h;
        if (persistentConnectConfig != null && !TextUtils.isEmpty(persistentConnectConfig.productKey) && !TextUtils.isEmpty(this.h.deviceName)) {
            this.j.put(subDeviceInfo.getDeviceId(), iSubDeviceConnectListener);
            b(subDeviceInfo, iSubDeviceConnectListener);
        } else if (iSubDeviceConnectListener != null) {
            AError aError3 = new AError();
            aError3.setCode(4202);
            aError3.setMsg("addSubDeviceForILopGlobal gateway connect info not set.");
            iSubDeviceConnectListener.onConnectResult(false, null, aError3);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void removeSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceRemoveListener iSubDeviceRemoveListener) {
        ALog.d("GatewayChannelImpl", "removeSubDevice()");
        if (subDeviceInfo == null || !subDeviceInfo.checkValid()) {
            ALog.d("GatewayChannelImpl", "removeSubDevice(), params error");
            return;
        }
        if (this.g == GatewayConnectState.CONNECTED) {
            a(subDeviceInfo, iSubDeviceRemoveListener);
        } else if (iSubDeviceRemoveListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("removeSubDevice device not connected");
            iSubDeviceRemoveListener.onFailed(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void getSubDevices(IConnectSendListener iConnectSendListener) {
        a(iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void deviceListUpload(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        ALog.d("GatewayChannelImpl", "deviceListUpload()");
        if (list != null && list.size() >= 1) {
            ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, "thing/list/found", "thing.list.found", null, list), iConnectSendListener);
        } else if (iConnectSendListener != null) {
            AError aError = new AError();
            aError.setCode(1101400);
            aError.setMsg("deviceListUpload infoList empty");
            iConnectSendListener.onFailure(null, aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void asyncSendRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener) {
        ALog.d("GatewayChannelImpl", "asyncSendRequest()");
        if (TextUtils.isEmpty(str)) {
            ALog.e("GatewayChannelImpl", "asyncSendRequest(), params error");
            return;
        }
        if (this.g == GatewayConnectState.CONNECTED) {
            ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, str, str2, map, obj), new C0219a(iGatewayRequestListener));
        } else if (iGatewayRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("device not connected");
            iGatewayRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void ayncSendPublishRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener) {
        ALog.d("GatewayChannelImpl", "ayncSendPublishRequest()");
        if (TextUtils.isEmpty(str)) {
            ALog.e("GatewayChannelImpl", "ayncSendPublishRequest(), params error");
            return;
        }
        if (this.g == GatewayConnectState.CONNECTED) {
            ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(false, this.h, str, str2, map, obj), new C0219a(iGatewayRequestListener));
        } else if (iGatewayRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("device not connected");
            iGatewayRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener) {
        PersistentConnectConfig persistentConnectConfig;
        ALog.d("GatewayChannelImpl", "subscribe(), topic = " + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e("GatewayChannelImpl", "subscribe(), topic is empty!");
            return;
        }
        if (this.g != GatewayConnectState.CONNECTED) {
            if (iGatewaySubscribeListener != null) {
                AError aError = new AError();
                aError.setCode(4101);
                aError.setMsg("subscribe device not connected");
                iGatewaySubscribeListener.onFailure(aError);
                return;
            }
            return;
        }
        if (!str.startsWith("/sys/") && !str.startsWith("/ota/") && (persistentConnectConfig = this.h) != null && a(persistentConnectConfig)) {
            str = ("/sys/" + this.h.productKey + "/" + this.h.deviceName + "/" + str).replace("//", "/");
        }
        MqttSubscribeRequest mqttSubscribeRequest = new MqttSubscribeRequest();
        mqttSubscribeRequest.topic = str;
        ConnectSDK.getInstance().subscribe(ConnectSDK.getInstance().getPersistentConnectId(), mqttSubscribeRequest, iGatewaySubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void unSubscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener) {
        ALog.d("GatewayChannelImpl", "unSubscribe()");
        if (TextUtils.isEmpty(str)) {
            ALog.e("GatewayChannelImpl", "unSubscribe(), topic is empty!");
            return;
        }
        if (this.g != GatewayConnectState.CONNECTED) {
            if (iGatewaySubscribeListener != null) {
                AError aError = new AError();
                aError.setCode(4101);
                aError.setMsg("subscribe device not connected");
                iGatewaySubscribeListener.onFailure(aError);
                return;
            }
            return;
        }
        if (!str.startsWith("/sys/") && !str.startsWith("/ota/") && a(this.h)) {
            str = ("/sys/" + this.h.productKey + "/" + this.h.deviceName + "/" + str).replace("//", "/");
        }
        MqttSubscribeRequest mqttSubscribeRequest = new MqttSubscribeRequest();
        mqttSubscribeRequest.topic = str;
        ConnectSDK.getInstance().subscribe(ConnectSDK.getInstance().getPersistentConnectId(), mqttSubscribeRequest, iGatewaySubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void registerDownstreamListener(boolean z, IGatewayDownstreamListener iGatewayDownstreamListener) {
        PersistentEventDispatcher.getInstance().registerOnPushListener(iGatewayDownstreamListener, z);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void unRegisterDownstreamListener(IGatewayDownstreamListener iGatewayDownstreamListener) {
        PersistentEventDispatcher.getInstance().unregisterOnPushListener(iGatewayDownstreamListener);
    }

    private void a(Context context, PersistentConnectConfig persistentConnectConfig) {
        ALog.d("GatewayChannelImpl", "connect()");
        if (this.q || this.g == GatewayConnectState.CONNECTING || this.g == GatewayConnectState.CONNECTED) {
            ALog.d("GatewayChannelImpl", "connect(), channel is connecting or connected now");
            return;
        }
        ConnectSDK.getInstance().registerNofityListener(ConnectSDK.getInstance().getPersistentConnectId(), this.H);
        if (PersistentNet.getInstance().getConnectState() == PersistentConnectState.CONNECTED) {
            ALog.d("GatewayChannelImpl", "connect(), Persistent already Connected!");
            this.g = GatewayConnectState.CONNECTED;
            this.q = true;
            IGatewayConnectListener iGatewayConnectListener = this.f;
            if (iGatewayConnectListener != null) {
                iGatewayConnectListener.onConnectStateChange(GatewayConnectState.CONNECTED);
            }
            a();
            return;
        }
        ALog.d("GatewayChannelImpl", "connect(), connecting...");
        this.g = GatewayConnectState.CONNECTING;
        IGatewayConnectListener iGatewayConnectListener2 = this.f;
        if (iGatewayConnectListener2 != null) {
            iGatewayConnectListener2.onConnectStateChange(GatewayConnectState.CONNECTING);
        }
        ConnectSDK.getInstance().registerPersistentConnect(context, persistentConnectConfig, new IRegisterConnectListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.1
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
            public void onSuccess() {
                a.this.g = GatewayConnectState.CONNECTED;
                a.this.q = true;
                a.this.a();
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
            public void onFailure(AError aError) {
                a.this.g = GatewayConnectState.CONNECTFAIL;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        a((IConnectSendListener) null);
        if (this.l == null) {
            this.l = new b();
            PersistentEventDispatcher.getInstance().registerOnPushListener(this.l, true);
        }
    }

    private void a(final IConnectSendListener iConnectSendListener) {
        ALog.d("GatewayChannelImpl", "topoGetReq()");
        ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, "thing/topo/get", "thing.topo.get", null, null), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.2
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                JSONArray jSONArray;
                StringBuilder sb = new StringBuilder();
                sb.append("topoGetReq(), onSuceess, rsp = ");
                sb.append((aResponse == null || aResponse.data == null) ? "" : aResponse.data);
                ALog.d("GatewayChannelImpl", sb.toString());
                try {
                    JSONObject object = JSONObject.parseObject((String) aResponse.data);
                    if (200 == object.getIntValue("code") && (jSONArray = object.getJSONArray("data")) != null && jSONArray.size() != 0) {
                        for (int i = 0; i < jSONArray.size(); i++) {
                            SubDeviceInfo subDeviceInfo = (SubDeviceInfo) jSONArray.getObject(i, SubDeviceInfo.class);
                            a.this.i.put(subDeviceInfo.getDeviceId(), subDeviceInfo);
                        }
                    }
                } catch (Exception e) {
                    ALog.d("GatewayChannelImpl", "topoGetReq(), onSuccess(), parse error, e" + e.toString());
                    e.printStackTrace();
                }
                IConnectSendListener iConnectSendListener2 = iConnectSendListener;
                if (iConnectSendListener2 != null) {
                    iConnectSendListener2.onResponse(aRequest, aResponse);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d("GatewayChannelImpl", "topoGetReq(), onFailed");
                IConnectSendListener iConnectSendListener2 = iConnectSendListener;
                if (iConnectSendListener2 != null) {
                    iConnectSendListener2.onFailure(aRequest, aError);
                }
            }
        });
    }

    private void a(final SubDeviceInfo subDeviceInfo, final ISubDeviceConnectListener iSubDeviceConnectListener) {
        String clientId;
        ALog.d("GatewayChannelImpl", "topoAdd()");
        if (subDeviceInfo == null || !subDeviceInfo.checkValid()) {
            ALog.e("GatewayChannelImpl", "topoAdd param:suddevice info invalid.");
            return;
        }
        if (iSubDeviceConnectListener == null) {
            ALog.e("GatewayChannelImpl", "topoAdd param:listener cannot be null.");
            return;
        }
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("sign", (Object) iSubDeviceConnectListener.getSignValue());
        jSONObject.put(TmpConstant.KEY_SIGN_METHOD, (Object) iSubDeviceConnectListener.getSignMethod());
        Map<String, Object> signExtraData = iSubDeviceConnectListener.getSignExtraData();
        if (signExtraData != null && !signExtraData.isEmpty()) {
            ALog.d("GatewayChannelImpl", "topoAdd(), get extra data " + signExtraData);
            jSONObject.putAll(signExtraData);
        }
        jSONObject.put("deviceName", (Object) subDeviceInfo.deviceName);
        jSONObject.put("productKey", (Object) subDeviceInfo.productKey);
        if (TextUtils.isEmpty(iSubDeviceConnectListener.getClientId())) {
            clientId = subDeviceInfo.deviceName + "&" + subDeviceInfo.productKey;
        } else {
            clientId = iSubDeviceConnectListener.getClientId();
        }
        jSONObject.put(TmpConstant.KEY_CLIENT_ID, (Object) clientId);
        jSONArray.add(jSONObject);
        ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, "thing/topo/add", "thing.topo.add", null, jSONArray), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.3
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ISubDeviceChannel cVar;
                StringBuilder sb = new StringBuilder();
                sb.append("topoAdd(), onSuceess, rsp = ");
                sb.append((aResponse == null || aResponse.data == null) ? "" : aResponse.data);
                ALog.d("GatewayChannelImpl", sb.toString());
                try {
                    int intValue = JSONObject.parseObject((String) aResponse.data).getIntValue("code");
                    if (intValue == 200) {
                        a.this.i.put(subDeviceInfo.getDeviceId(), subDeviceInfo);
                        if (a.this.k.containsKey(subDeviceInfo.getDeviceId())) {
                            cVar = (ISubDeviceChannel) a.this.k.get(subDeviceInfo.getDeviceId());
                        } else {
                            cVar = new c(subDeviceInfo, iSubDeviceConnectListener);
                            a.this.k.put(subDeviceInfo.getDeviceId(), cVar);
                        }
                        iSubDeviceConnectListener.onConnectResult(true, cVar, null);
                        return;
                    }
                    AError aError = new AError();
                    aError.setCode(intValue);
                    aError.setMsg("topo add failed, server error code =" + intValue);
                    iSubDeviceConnectListener.onConnectResult(false, null, aError);
                } catch (Exception e) {
                    ALog.d("GatewayChannelImpl", "topoAdd(), onSuccess(), parse error, e" + e.toString());
                    e.printStackTrace();
                    AError aError2 = new AError();
                    aError2.setCode(4103);
                    aError2.setMsg("reqSuccess, parse error, e" + e.toString());
                    iSubDeviceConnectListener.onConnectResult(false, null, aError2);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d("GatewayChannelImpl", "topoAdd(), onFailed");
                iSubDeviceConnectListener.onConnectResult(false, null, aError);
            }
        });
    }

    private void b(SubDeviceInfo subDeviceInfo, final ISubDeviceConnectListener iSubDeviceConnectListener) {
        String clientId;
        ALog.d("GatewayChannelImpl", "topoAddForILopGlobal()");
        if (iSubDeviceConnectListener == null) {
            if (iSubDeviceConnectListener != null) {
                AError aError = new AError();
                aError.setCode(GatewayErrorCode.ERROR_INVOKE_PARAMS_INVALID);
                aError.setMsg("topoAddForILopGlobal subDeviceInfoListener empty.");
                iSubDeviceConnectListener.onConnectResult(false, null, aError);
                return;
            }
            return;
        }
        if (TextUtils.isEmpty(iSubDeviceConnectListener.getSignMethod())) {
            AError aError2 = new AError();
            aError2.setCode(GatewayErrorCode.ERROR_INVOKE_PARAMS_INVALID);
            aError2.setMsg("topoAddForILopGlobal sign method empty.");
            iSubDeviceConnectListener.onConnectResult(false, null, aError2);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("identifier", "_LivingLink.activation.subdevice.connect");
        JSONObject jSONObject2 = new JSONObject();
        jSONObject.put("serviceParams", (Object) jSONObject2);
        JSONArray jSONArray = new JSONArray();
        final String string = UUID.randomUUID().toString();
        jSONObject2.put("requestId", (Object) string);
        jSONObject2.put("version", "2.0");
        jSONObject2.put("DeviceList", (Object) jSONArray);
        JSONObject jSONObject3 = new JSONObject();
        if (TextUtils.isEmpty(iSubDeviceConnectListener.getClientId())) {
            clientId = subDeviceInfo.deviceName + "&" + subDeviceInfo.productKey;
        } else {
            clientId = iSubDeviceConnectListener.getClientId();
        }
        jSONObject3.put(TmpConstant.KEY_CLIENT_ID, (Object) clientId);
        Object[] objArr = new Object[4];
        objArr[0] = clientId;
        objArr[1] = subDeviceInfo.deviceName;
        objArr[2] = TextUtils.isEmpty(this.h.deviceSecret) ? MqttConfigure.deviceSecret : this.h.deviceSecret;
        objArr[3] = subDeviceInfo.productKey;
        jSONObject3.put("sign", (Object) com.aliyun.alink.linksdk.channel.gateway.b.a.a(String.format("clientId%sdeviceName%sdeviceSecret%sproductKey%s", objArr), iSubDeviceConnectListener.getSignMethod()));
        jSONObject3.put(TmpConstant.KEY_SIGN_METHOD, (Object) iSubDeviceConnectListener.getSignMethod());
        jSONObject3.put("subSign", (Object) iSubDeviceConnectListener.getSignValue());
        if (!TextUtils.isEmpty(subDeviceInfo.resetFlag)) {
            jSONObject3.put("reset", (Object) subDeviceInfo.resetFlag);
        }
        Map<String, Object> signExtraData = iSubDeviceConnectListener.getSignExtraData();
        if (signExtraData != null && !signExtraData.isEmpty()) {
            ALog.d("GatewayChannelImpl", "topoAddForILopGlobal(), get extra data " + signExtraData);
            jSONObject3.putAll(signExtraData);
        }
        if (!"true".equals(jSONObject3.getString("cleanSession")) && !RequestConstant.FALSE.equals(jSONObject3.getString("cleanSession"))) {
            jSONObject3.put("cleanSession", (Object) "true");
        }
        jSONObject3.put("deviceName", (Object) subDeviceInfo.deviceName);
        jSONObject3.put("productKey", (Object) subDeviceInfo.productKey);
        jSONArray.add(jSONObject3);
        if (!TextUtils.isEmpty(this.h.productKey) && !TextUtils.isEmpty(this.h.deviceName)) {
            String strB = b();
            if (!this.n.contains(strB)) {
                a(string, strB, iSubDeviceConnectListener);
            }
        }
        synchronized (this.p) {
            this.p.put(string, new SubDeviceInfoWrapper(subDeviceInfo, iSubDeviceConnectListener));
        }
        ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, "_thing/service/post", "_thing.service.post", null, jSONObject), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.4
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                StringBuilder sb = new StringBuilder();
                sb.append("topoAddForILopGlobal(), onSuceess, rsp = ");
                sb.append((aResponse == null || aResponse.data == null) ? "" : aResponse.data);
                ALog.d("GatewayChannelImpl", sb.toString());
                try {
                    int intValue = JSONObject.parseObject((String) aResponse.data).getIntValue("code");
                    if (intValue == 200) {
                        ALog.d("GatewayChannelImpl", "topoAddForILopGlobal service/post success，wait for async notify.");
                        return;
                    }
                    AError aError3 = new AError();
                    aError3.setCode(intValue);
                    aError3.setMsg("topoAddForILopGlobal failed, server error code =" + intValue);
                    synchronized (a.this.p) {
                        a.this.p.remove(string);
                    }
                    ALog.w("GatewayChannelImpl", "topoAddForILopGlobal(), onConnectResult onFailed " + a.this.a(aError3));
                    iSubDeviceConnectListener.onConnectResult(false, null, aError3);
                } catch (Exception e) {
                    ALog.d("GatewayChannelImpl", "topoAddForILopGlobal(), onSuccess(), parse error, e" + e.toString());
                    e.printStackTrace();
                    AError aError4 = new AError();
                    aError4.setCode(4103);
                    aError4.setMsg("reqSuccess, parse error, e" + e.toString());
                    synchronized (a.this.p) {
                        a.this.p.remove(string);
                        ALog.w("GatewayChannelImpl", "topoAddForILopGlobal(), onConnectResult onFailed " + a.this.a(aError4) + ", e=" + e);
                        iSubDeviceConnectListener.onConnectResult(false, null, aError4);
                    }
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError3) {
                ALog.w("GatewayChannelImpl", "topoAddForILopGlobal(), onConnectResult onFailed " + a.this.a(aError3));
                synchronized (a.this.p) {
                    a.this.p.remove(string);
                }
                iSubDeviceConnectListener.onConnectResult(false, null, aError3);
            }
        });
    }

    private void a(final String str, final String str2, final ISubDeviceConnectListener iSubDeviceConnectListener) {
        subscribe(str2, new IGatewaySubscribeListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.5
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
            public void onSuccess() {
                a.this.n.add(str2);
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
            public void onFailure(AError aError) {
                ALog.e("GatewayChannelImpl", "subscribeThingEventNotify failed requestId = " + str + "aError=" + a.this.a(aError));
                if (iSubDeviceConnectListener != null) {
                    synchronized (a.this.p) {
                        a.this.p.remove(str);
                    }
                    iSubDeviceConnectListener.onConnectResult(false, null, aError);
                }
            }
        });
    }

    public String a(AError aError) {
        if (aError == null) {
            return null;
        }
        return "code=" + aError.getCode() + ", subCode=" + aError.getSubCode() + ", msg=" + aError.getMsg() + ", subMsg=" + aError.getSubMsg();
    }

    private void a(SubDeviceInfo subDeviceInfo, final ISubDeviceRemoveListener iSubDeviceRemoveListener) {
        ALog.d("GatewayChannelImpl", "topoDelete()");
        if (subDeviceInfo == null || !subDeviceInfo.checkValid()) {
            return;
        }
        this.i.remove(subDeviceInfo.getDeviceId());
        this.j.remove(subDeviceInfo.getDeviceId());
        this.k.remove(subDeviceInfo.getDeviceId());
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("deviceName", (Object) subDeviceInfo.deviceName);
        jSONObject.put("productKey", (Object) subDeviceInfo.productKey);
        jSONArray.add(jSONObject);
        ConnectSDK.getInstance().send(new com.aliyun.alink.linksdk.channel.gateway.a.b(true, this.h, "thing/topo/delete", "thing.topo.delete", null, jSONArray), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.6
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                StringBuilder sb = new StringBuilder();
                sb.append("topoDelete(), onSuceess, rsp = ");
                sb.append((aResponse == null || aResponse.data == null) ? "" : aResponse.data);
                ALog.d("GatewayChannelImpl", sb.toString());
                try {
                    String string = JSONObject.parseObject((String) aResponse.data).getString("code");
                    if (ErrorCode.UNKNOWN_SUCCESS_CODE.equals(string)) {
                        if (iSubDeviceRemoveListener != null) {
                            iSubDeviceRemoveListener.onSuceess();
                            return;
                        }
                        return;
                    }
                    AError aError = new AError();
                    if (!TextUtils.isEmpty(string)) {
                        aError.setCode(Integer.getInteger(string).intValue());
                    }
                    aError.setMsg("code =" + string);
                    iSubDeviceRemoveListener.onFailed(aError);
                } catch (Exception e) {
                    ALog.d("GatewayChannelImpl", "topoDelete(), onSuccess(), parse error, e" + e.toString());
                    e.printStackTrace();
                    AError aError2 = new AError();
                    aError2.setMsg("reqSuccess, parse error, e" + e.toString());
                    iSubDeviceRemoveListener.onFailed(aError2);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d("GatewayChannelImpl", "topoDelete(), onFailed");
                iSubDeviceRemoveListener.onFailed(aError);
            }
        });
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.gateway.a.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: GatewayChannelImpl.java */
    private class C0219a implements IConnectSendListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private IGatewayRequestListener f4173b;

        public C0219a(IGatewayRequestListener iGatewayRequestListener) {
            this.f4173b = iGatewayRequestListener;
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
        public void onResponse(ARequest aRequest, AResponse aResponse) {
            StringBuilder sb = new StringBuilder();
            sb.append("GatewayOnCallListener, onSuccess, rsp = ");
            sb.append((aResponse == null || aResponse.data == null) ? TmpConstant.GROUP_ROLE_UNKNOWN : aResponse.data.toString());
            ALog.d("GatewayChannelImpl", sb.toString());
            this.f4173b.onSuccess((aResponse == null || aResponse.data == null) ? null : aResponse.data.toString());
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
        public void onFailure(ARequest aRequest, AError aError) {
            this.f4173b.onFailure(aError);
        }
    }

    /* JADX INFO: compiled from: GatewayChannelImpl.java */
    private class b implements IOnPushListener {
        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener
        public boolean shouldHandle(String str) {
            return true;
        }

        private b() {
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener
        public void onCommand(String str, byte[] bArr) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String strB = a.this.b();
            if (!TextUtils.isEmpty(strB) && str.equals(strB)) {
                a.this.a(bArr);
            }
            if (a.this.h == null || str.contains(a.this.h.deviceName)) {
                return;
            }
            for (String str2 : a.this.j.keySet()) {
                if (!TextUtils.isEmpty(str2) && str2.contains(GatewayChannel.DID_SEPARATOR)) {
                    try {
                        CharSequence charSequence = str2.split(GatewayChannel.DID_SEPARATOR)[0];
                        CharSequence charSequence2 = str2.split(GatewayChannel.DID_SEPARATOR)[1];
                        if (str.contains(charSequence) && str.contains(charSequence2)) {
                            ISubDeviceConnectListener iSubDeviceConnectListener = (ISubDeviceConnectListener) a.this.j.get(str2);
                            AMessage aMessage = new AMessage();
                            aMessage.data = bArr;
                            iSubDeviceConnectListener.onDataPush(str, aMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(byte[] bArr) {
        JSONObject jSONObject;
        String string;
        ALog.d("GatewayChannelImpl", "handleThingEventNotify() called with: data = [" + bArr + "]");
        try {
            jSONObject = JSONObject.parseObject(new String(bArr)).getJSONObject("params");
            string = jSONObject.getString("identifier");
        } catch (Exception e) {
            ALog.w("GatewayChannelImpl", "topoAddForILopGlobal fail, data error " + e);
            return;
        }
        if (!"_LivingLink.activation.subdevice.connect".equals(string)) {
            ALog.w("GatewayChannelImpl", "_thing/event/notify identifier invalid " + string);
            return;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("value");
        int intValue = jSONObject2.getIntValue("bizCode");
        String string2 = jSONObject2.getString("requestId");
        if (TextUtils.isEmpty(string2)) {
            ALog.d("GatewayChannelImpl", "invalid requestId, return " + string2);
            return;
        }
        SubDeviceInfoWrapper subDeviceInfoWrapper = this.p.get(string2);
        if (subDeviceInfoWrapper != null && subDeviceInfoWrapper.deviceInfo != null && subDeviceInfoWrapper.connectListener != null) {
            if (intValue == 200) {
                JSONArray jSONArray = jSONObject2.getJSONArray("DeviceList");
                if (jSONArray == null || jSONArray.size() < 1) {
                    AError aError = new AError();
                    aError.setCode(4103);
                    aError.setMsg("reqSuccess, deviceList is empty");
                    synchronized (this.p) {
                        this.p.remove(string2);
                    }
                    ALog.w("GatewayChannelImpl", "topoAddForILopGlobal onConnectResult fail, requestId=" + string2 + ", error=" + aError.getMsg());
                    subDeviceInfoWrapper.connectListener.onConnectResult(false, null, aError);
                    return;
                }
                int size = jSONArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                    if (jSONObject3 != null && subDeviceInfoWrapper.deviceInfo != null && !TextUtils.isEmpty(jSONObject3.getString("deviceName")) && !TextUtils.isEmpty("productKey") && jSONObject3.getString("deviceName").equals(subDeviceInfoWrapper.deviceInfo.deviceName) && jSONObject3.getString("productKey").equals(subDeviceInfoWrapper.deviceInfo.productKey)) {
                        String string3 = jSONObject3.getString(BaseDO.JSON_ERRORCODE);
                        if ("0".equals(string3)) {
                            this.i.put(subDeviceInfoWrapper.deviceInfo.getDeviceId(), subDeviceInfoWrapper.deviceInfo);
                            c cVar = new c(subDeviceInfoWrapper.deviceInfo, subDeviceInfoWrapper.connectListener);
                            this.k.put(subDeviceInfoWrapper.deviceInfo.getDeviceId(), cVar);
                            synchronized (this.p) {
                                this.p.remove(string2);
                            }
                            ALog.i("GatewayChannelImpl", "topoAddForILopGlobal onConnectResult success, requestId=" + string2);
                            subDeviceInfoWrapper.connectListener.onConnectResult(true, cVar, null);
                            return;
                        }
                        AError aError2 = new AError();
                        aError2.setCode(a(string3));
                        aError2.setMsg("topoAddForILopGlobal failed, server error resultCode(1:bindAlready-2:signError-3:addTopoError-4:devNotFound-5:TBD) =" + string3);
                        synchronized (this.p) {
                            this.p.remove(string2);
                        }
                        ALog.w("GatewayChannelImpl", "topoAddForILopGlobal onConnectResult fail, requestId=" + string2 + ", error=" + aError2.getMsg());
                        subDeviceInfoWrapper.connectListener.onConnectResult(false, null, aError2);
                        return;
                    }
                }
                ALog.w("GatewayChannelImpl", "topoAddForILopGlobal fail, data error " + e);
                return;
            }
            AError aError3 = new AError();
            aError3.setCode(intValue);
            aError3.setMsg("topoAddForILopGlobal failed, server error code =" + intValue);
            synchronized (this.p) {
                this.p.remove(string2);
            }
            ALog.w("GatewayChannelImpl", "topoAddForILopGlobal onConnectResult fail, requestId=" + string2 + ", error=" + aError3.getMsg());
            subDeviceInfoWrapper.connectListener.onConnectResult(false, null, aError3);
            return;
        }
        ALog.w("GatewayChannelImpl", "requestId with no listener.");
    }

    private int a(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b() {
        PersistentConnectConfig persistentConnectConfig = this.h;
        if (persistentConnectConfig == null || TextUtils.isEmpty(persistentConnectConfig.productKey) || TextUtils.isEmpty(this.h.deviceName)) {
            return null;
        }
        return "/sys/" + this.h.productKey + "/" + this.h.deviceName + "/_thing/event/notify";
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public ISubDeviceChannel getSubDeviceChannel(String str) {
        Map<String, ISubDeviceChannel> map = this.k;
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        return this.k.get(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        ALog.d("GatewayChannelImpl", "autoLogin() called");
        if (this.m.get()) {
            for (Map.Entry<String, ISubDeviceChannel> entry : this.k.entrySet()) {
                if (entry != null) {
                    ISubDeviceChannel value = entry.getValue();
                    if (value.getSubDeviceInfo() != null && value.getSubDeviceInfo().checkValid()) {
                        if (ConnectState.CONNECTED != ConnectSDK.getInstance().getConnectState(ConnectSDK.getInstance().getPersistentConnectId())) {
                            return;
                        }
                        if (value.getSubDeviceInfo().loginState == SubDeviceLoginState.ONLINE) {
                            ALog.d("GatewayChannelImpl", "autoLogin onLine & enabled. entry=" + entry);
                            value.online(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.channel.gateway.a.a.8
                                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                                public void onSuccess() {
                                    ALog.d("GatewayChannelImpl", "autoLogin onSuccess");
                                }

                                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                                public void onFailed(AError aError) {
                                    String str;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("autoLogin aError=");
                                    if (aError == null) {
                                        str = "";
                                    } else {
                                        str = aError.getCode() + aError.getMsg();
                                    }
                                    sb.append(str);
                                    ALog.d("GatewayChannelImpl", sb.toString());
                                }
                            });
                        } else {
                            ALog.w("GatewayChannelImpl", "autoLogin offline or disabled. entry=" + entry);
                        }
                    }
                }
            }
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void destroyConnect() {
        try {
            this.q = false;
            this.g = GatewayConnectState.DISCONNECTED;
            if (this.k != null) {
                this.k.clear();
            }
            if (this.j != null) {
                this.j.clear();
            }
            if (this.i != null) {
                this.i.clear();
            }
            ConnectManager.getInstance().unregisterConnect(ConnectSDK.getInstance().getPersistentConnectId());
        } catch (Exception e) {
            ALog.w("GatewayChannelImpl", "destroyConnect exception=" + e);
        }
        try {
            ConnectSDK.getInstance().unregisterNofityListener(this.H);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public PersistentConnectConfig getPersistentConnectConfig() {
        return this.h;
    }
}
