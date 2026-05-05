package com.aliyun.alink.linksdk.tmp.service;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.config.ble.BreezeConstants;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceInfo;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.IProvision;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultProvisionConfig;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.CmpNotifyManager;
import com.aliyun.alink.linksdk.tmp.data.SubDevInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.AuthenRegisterRspPayload;
import com.aliyun.alink.linksdk.tmp.data.service.DevTripleInfo;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended.GetDeviceExtendRequest;
import com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended.SetDeviceExtendRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.other.GetDeviceNetTypesSupportedRequest;
import com.aliyun.alink.linksdk.tmp.error.AuthSignIllegalError;
import com.aliyun.alink.linksdk.tmp.error.OnlineError;
import com.aliyun.alink.linksdk.tmp.error.ParamsError;
import com.aliyun.alink.linksdk.tmp.error.UnknownError;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ByteUtils;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.Secure;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.breeze.TLV;
import com.aliyun.iot.breeze.api.ConnectConfig;
import com.aliyun.iot.breeze.biz.BreezeHelper;
import com.aliyun.iot.breeze.mix.ConnectionCallback;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class DevService {
    public static final String BUNDLE_KEY_DEVICENAME = "bundlekey_devicename";
    public static final String BUNDLE_KEY_PRODUCTKEY = "bundlekey_productkey";
    public static final String BUNDLE_KEY_SUBCHANNEL = "bundlekey_subchannel";
    private static final String TAG = "[Tmp]DevService";
    private static Map<String, String> mDevSecList = new ConcurrentHashMap();

    public interface ServiceListener {
        public static final int AUTHSIGN_ILLEGAL = 4106;

        void onComplete(boolean z, @Nullable Object obj);
    }

    public interface ServiceListenerEx {
        void onComplete(boolean z, @Nullable String str);
    }

    public static class OnlyOnceServiceListener implements ServiceListener {
        private ServiceListener mServiceListener;

        public OnlyOnceServiceListener(ServiceListener serviceListener) {
            this.mServiceListener = serviceListener;
        }

        @Override // com.aliyun.alink.linksdk.tmp.service.DevService.ServiceListener
        public void onComplete(boolean z, Object obj) {
            ServiceListener serviceListener = this.mServiceListener;
            if (serviceListener == null) {
                ALog.w(DevService.TAG, "onComplete called twice mServiceListener empty");
            } else {
                serviceListener.onComplete(z, obj);
                this.mServiceListener = null;
            }
        }
    }

    public static void subDeviceAuthenLogin(JSONObject jSONObject, ServiceListener serviceListener) {
        ALog.d(TAG, "subDeviceAuthenLogin params:" + jSONObject + " listener:" + serviceListener);
        if (serviceListener == null) {
            ALog.e(TAG, "subDeviceAuthenLogin listener null");
            return;
        }
        if (jSONObject == null) {
            ALog.e(TAG, "subDeviceAuthenLogin params null");
            serviceListener.onComplete(false, new ParamsError());
            return;
        }
        try {
            SubDevInfo subDevInfo = (SubDevInfo) com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(jSONObject), SubDevInfo.class);
            if (subDevInfo == null) {
                ALog.e(TAG, "subDeviceAuthenLogin subDeviceInfo null");
                serviceListener.onComplete(false, new ParamsError());
            } else {
                subDeviceAuthenLogin(subDevInfo.productKey, subDevInfo.deviceName, true, serviceListener);
            }
        } catch (Exception e) {
            ALog.w(TAG, "subDeviceAuthenLogin error:" + e.toString());
        }
    }

    public static void subDeviceAuthenLogin(String str, String str2, ServiceListener serviceListener) {
        subDeviceAuthenLogin(str, str2, true, serviceListener);
    }

    public static void subDeviceAuthenLogin(String str, String str2, boolean z, ServiceListener serviceListener) {
        ALog.d(TAG, "subDeviceAuthenLogin productKey:" + str + " deviceName:" + str2 + " listener:" + serviceListener);
        SubDeviceInfo subDeviceInfo = new SubDeviceInfo();
        subDeviceInfo.productKey = str;
        subDeviceInfo.deviceName = str2;
        if (serviceListener == null) {
            ALog.e(TAG, "subDeviceAuthenLogin listener empty");
            return;
        }
        DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(TextHelper.combineStr(str, str2));
        if (deviceBasicData != null && "2".equalsIgnoreCase(deviceBasicData.getModelType())) {
            breezeSubDevLogin(str, str2, serviceListener);
            return;
        }
        String clientId = MobileChannel.getInstance().getClientId();
        if (TextUtils.isEmpty(clientId)) {
            ALog.w(TAG, "请检查长连接通道是否初始化成功");
        } else {
            CloudUtils.registerPersistentConnect(null, null, null, null);
            CloudUtils.subPushDeviceInfo(str, str2, new AnonymousClass1(str, str2, serviceListener, clientId, z));
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.service.DevService$1, reason: invalid class name */
    static class AnonymousClass1 implements IConnectRrpcListener {
        final /* synthetic */ String val$clientId;
        final /* synthetic */ String val$deviceName;
        final /* synthetic */ boolean val$isDelayCallback;
        final /* synthetic */ ServiceListener val$listener;
        final /* synthetic */ String val$productKey;

        AnonymousClass1(String str, String str2, ServiceListener serviceListener, String str3, boolean z) {
            this.val$productKey = str;
            this.val$deviceName = str2;
            this.val$listener = serviceListener;
            this.val$clientId = str3;
            this.val$isDelayCallback = z;
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
        public void onSubscribeSuccess(ARequest aRequest) {
            ALog.d(DevService.TAG, "subDeviceAuthenLogin onSubscribe Success");
            DevService.mDevSecList.remove(TextHelper.combineStr(this.val$productKey, this.val$deviceName));
            CloudUtils.subDevAuthenRegister(this.val$productKey, this.val$deviceName, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.1.1
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest2, AResponse aResponse) {
                    ALog.d(DevService.TAG, "subDeviceAuthenLogin onResponse aResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        ALog.e(DevService.TAG, "subDeviceAuthenLogin onResponse aResponse: empty");
                        if (AnonymousClass1.this.val$listener != null) {
                            AnonymousClass1.this.val$listener.onComplete(false, new ParamsError());
                            return;
                        }
                        return;
                    }
                    String str = null;
                    if (aResponse.data instanceof byte[]) {
                        try {
                            str = new String((byte[]) aResponse.data, "UTF-8");
                        } catch (Exception e) {
                            ALog.e(DevService.TAG, e.toString());
                        }
                    } else {
                        str = aResponse.data.toString();
                    }
                    AuthenRegisterRspPayload authenRegisterRspPayload = (AuthenRegisterRspPayload) com.alibaba.fastjson.JSONObject.parseObject(str, AuthenRegisterRspPayload.class);
                    if (authenRegisterRspPayload.code == 6619) {
                        ALog.d(DevService.TAG, "subDeviceAuthenLogin delay login");
                        TmpSdk.mHandler.postDelayed(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                String str2 = (String) DevService.mDevSecList.get(TextHelper.combineStr(AnonymousClass1.this.val$productKey, AnonymousClass1.this.val$deviceName));
                                ALog.d(DevService.TAG, "subDeviceAuthenLogin delay login productKey: " + AnonymousClass1.this.val$productKey + " deviceName:" + AnonymousClass1.this.val$deviceName + " mLastDevSec:" + str2);
                                if (TextUtils.isEmpty(str2)) {
                                    if (AnonymousClass1.this.val$listener != null) {
                                        AnonymousClass1.this.val$listener.onComplete(false, new ParamsError());
                                    }
                                } else {
                                    String signValue = Secure.getSignValue(AnonymousClass1.this.val$clientId, AnonymousClass1.this.val$productKey, AnonymousClass1.this.val$deviceName, str2);
                                    HashMap map = new HashMap();
                                    map.put(TmpConstant.KEY_SIGN_METHOD, TmpConstant.VALUE_SHA256);
                                    map.put("sign", signValue);
                                    DevService.logIn(AnonymousClass1.this.val$productKey, AnonymousClass1.this.val$deviceName, map, AnonymousClass1.this.val$isDelayCallback, AnonymousClass1.this.val$listener);
                                }
                            }
                        }, 12000L);
                        return;
                    }
                    if (authenRegisterRspPayload.data == null || authenRegisterRspPayload.data.isEmpty() || TextUtils.isEmpty(authenRegisterRspPayload.data.get(0).productKey) || TextUtils.isEmpty(authenRegisterRspPayload.data.get(0).deviceName) || TextUtils.isEmpty(authenRegisterRspPayload.data.get(0).deviceSecret)) {
                        ALog.e(DevService.TAG, "subDeviceAuthenLogin authenRegisterRspPayload  empty");
                        if (AnonymousClass1.this.val$listener != null) {
                            AnonymousClass1.this.val$listener.onComplete(false, new ParamsError());
                            return;
                        }
                        return;
                    }
                    String signValue = Secure.getSignValue(AnonymousClass1.this.val$clientId, authenRegisterRspPayload.data.get(0).productKey, authenRegisterRspPayload.data.get(0).deviceName, authenRegisterRspPayload.data.get(0).deviceSecret);
                    HashMap map = new HashMap();
                    map.put(TmpConstant.KEY_SIGN_METHOD, TmpConstant.VALUE_SHA256);
                    map.put("sign", signValue);
                    DevService.logIn(AnonymousClass1.this.val$productKey, AnonymousClass1.this.val$deviceName, map, AnonymousClass1.this.val$isDelayCallback, AnonymousClass1.this.val$listener);
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest2, AError aError) {
                    ALog.e(DevService.TAG, "subDeviceAuthenLogin onFailure aError:" + aError);
                    if (AnonymousClass1.this.val$listener != null) {
                        AnonymousClass1.this.val$listener.onComplete(false, aError);
                    }
                }
            });
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
        public void onSubscribeFailed(ARequest aRequest, AError aError) {
            ALog.d(DevService.TAG, "subDeviceAuthenLogin onSubscribeFailed");
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
        public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
            ALog.d(DevService.TAG, "subDeviceAuthenLogin onReceived");
            if (aRequest == null || !(aRequest instanceof MqttRrpcRequest)) {
                ALog.d(DevService.TAG, "subDeviceAuthenLogin onReceived aRequest null");
                return;
            }
            MqttRrpcRequest mqttRrpcRequest = (MqttRrpcRequest) aRequest;
            String str = null;
            if (mqttRrpcRequest.payloadObj instanceof byte[]) {
                try {
                    str = new String((byte[]) mqttRrpcRequest.payloadObj, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str = String.valueOf(mqttRrpcRequest.payloadObj);
            }
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(str);
            if (object == null) {
                ALog.d(DevService.TAG, "subDeviceAuthenLogin onReceived jsonObjectPayload null");
                return;
            }
            com.alibaba.fastjson.JSONObject jSONObject = object.getJSONObject("params");
            if (jSONObject == null) {
                ALog.d(DevService.TAG, "subDeviceAuthenLogin onReceived jsonObjectParams null");
                return;
            }
            DevTripleInfo devTripleInfo = (DevTripleInfo) JSON.parseObject(jSONObject.toString(), DevTripleInfo.class);
            if (devTripleInfo != null && !TextUtils.isEmpty(devTripleInfo.deviceSecret)) {
                DevService.mDevSecList.put(devTripleInfo.getId(), devTripleInfo.deviceSecret);
            } else {
                ALog.d(DevService.TAG, "subDeviceAuthenLogin onReceived devSec null");
            }
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
        public void onResponseSuccess(ARequest aRequest) {
            ALog.d(DevService.TAG, "subDeviceAuthenLogin onResponseSuccess");
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener
        public void onResponseFailed(ARequest aRequest, AError aError) {
            ALog.d(DevService.TAG, "subDeviceAuthenLogin onResponseFailed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logIn(String str, String str2, Map<String, Object> map, boolean z, ServiceListener serviceListener) {
        SubDeviceInfo subDeviceInfo = new SubDeviceInfo();
        subDeviceInfo.productKey = str;
        subDeviceInfo.deviceName = str2;
        String str3 = map != null ? (String) map.get(TmpConstant.KEY_SIGN_METHOD) : null;
        String str4 = map != null ? (String) map.get("sign") : null;
        ALog.d(TAG, "logIn productKey:" + str + " deviceName:" + str2 + " params:" + map + " listener:" + serviceListener + " signMethod:" + str3 + " signValue:" + str4);
        try {
            GatewayChannel.getInstance().addSubDeviceForILopGlobal(subDeviceInfo, new AnonymousClass2(str3, str4, serviceListener, z, str, str2));
        } catch (Exception e) {
            ALog.e(TAG, "logIn Exception:" + e.toString());
        } catch (Throwable th) {
            ALog.e(TAG, "logIn Throwable:" + th.toString());
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.service.DevService$2, reason: invalid class name */
    static class AnonymousClass2 implements ISubDeviceConnectListener {
        final /* synthetic */ String val$deviceName;
        final /* synthetic */ boolean val$isDelayCallback;
        final /* synthetic */ ServiceListener val$listener;
        final /* synthetic */ String val$productKey;
        final /* synthetic */ String val$signMethod;
        final /* synthetic */ String val$signValue;

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public Map<String, Object> getSignExtraData() {
            return null;
        }

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public void onDataPush(String str, AMessage aMessage) {
        }

        AnonymousClass2(String str, String str2, ServiceListener serviceListener, boolean z, String str3, String str4) {
            this.val$signMethod = str;
            this.val$signValue = str2;
            this.val$listener = serviceListener;
            this.val$isDelayCallback = z;
            this.val$productKey = str3;
            this.val$deviceName = str4;
        }

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public String getSignMethod() {
            return this.val$signMethod;
        }

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public String getSignValue() {
            return this.val$signValue;
        }

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public String getClientId() {
            String clientId = MobileChannel.getInstance().getClientId();
            ALog.d(DevService.TAG, "getClientId:" + clientId);
            return clientId;
        }

        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
        public void onConnectResult(boolean z, final ISubDeviceChannel iSubDeviceChannel, AError aError) {
            ALog.d(DevService.TAG, "onConnectResult b :" + z + " iSubDeviceChannel:" + iSubDeviceChannel + " aeeor:" + aError);
            if (iSubDeviceChannel == null) {
                ServiceListener serviceListener = this.val$listener;
                if (serviceListener != null) {
                    serviceListener.onComplete(false, aError);
                    return;
                }
                return;
            }
            iSubDeviceChannel.online(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.2.1
                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onSuccess() {
                    if (AnonymousClass2.this.val$isDelayCallback) {
                        TmpSdk.mHandler.postDelayed(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.2.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (AnonymousClass2.this.val$listener != null) {
                                    HashMap map = new HashMap();
                                    map.put(DevService.BUNDLE_KEY_PRODUCTKEY, AnonymousClass2.this.val$productKey);
                                    map.put(DevService.BUNDLE_KEY_DEVICENAME, AnonymousClass2.this.val$deviceName);
                                    map.put(DevService.BUNDLE_KEY_SUBCHANNEL, iSubDeviceChannel);
                                    AnonymousClass2.this.val$listener.onComplete(true, map);
                                }
                            }
                        }, 3000L);
                        return;
                    }
                    if (AnonymousClass2.this.val$listener != null) {
                        HashMap map = new HashMap();
                        map.put(DevService.BUNDLE_KEY_PRODUCTKEY, AnonymousClass2.this.val$productKey);
                        map.put(DevService.BUNDLE_KEY_DEVICENAME, AnonymousClass2.this.val$deviceName);
                        map.put(DevService.BUNDLE_KEY_SUBCHANNEL, iSubDeviceChannel);
                        AnonymousClass2.this.val$listener.onComplete(true, map);
                    }
                }

                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onFailed(AError aError2) {
                    if (AnonymousClass2.this.val$listener != null) {
                        AnonymousClass2.this.val$listener.onComplete(false, aError2);
                    }
                }
            });
        }
    }

    public static void logOut(String str, String str2, final ServiceListener serviceListener) {
        try {
            SubDeviceInfo subDeviceInfo = new SubDeviceInfo();
            subDeviceInfo.productKey = str;
            subDeviceInfo.deviceName = str2;
            GatewayChannel.getInstance().removeSubDevice(subDeviceInfo, new ISubDeviceRemoveListener() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.3
                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener
                public void onSuceess() {
                    ALog.d(DevService.TAG, "logOut onSuceess");
                    ServiceListener serviceListener2 = serviceListener;
                    if (serviceListener2 != null) {
                        serviceListener2.onComplete(true, null);
                    }
                }

                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener
                public void onFailed(AError aError) {
                    ALog.d(DevService.TAG, "logOut onFailed aError:" + aError);
                    ServiceListener serviceListener2 = serviceListener;
                    if (serviceListener2 != null) {
                        serviceListener2.onComplete(false, null);
                    }
                }
            });
        } catch (Exception e) {
            ALog.e(TAG, "logOut Exception:" + e.toString());
        } catch (Throwable th) {
            ALog.e(TAG, "logOut Throwable:" + th.toString());
        }
    }

    public static void breezeSubDevLogin(final String str, final String str2, final ServiceListener serviceListener) {
        ALog.d(TAG, "breezeSubDevLogin productKey:" + str + " deviceName:" + str2 + " listener:" + serviceListener);
        TmpSdk.mHandler.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.4
            private AtomicBoolean idDone = new AtomicBoolean(false);

            @Override // java.lang.Runnable
            public void run() {
                if (this.idDone.compareAndSet(false, true)) {
                    DevService.breezeSubDevLoginInner(str, str2, serviceListener);
                }
            }
        });
    }

    protected static void breezeSubDevLoginInner(String str, String str2, ServiceListener serviceListener) {
        ALog.d(TAG, "breezeSubDevLoginInner productKey:" + str + " deviceName:" + str2 + " listener:" + serviceListener);
        breezeSubDevLoginCallback breezesubdevlogincallback = new breezeSubDevLoginCallback(str, str2, new OnlyOnceServiceListener(serviceListener));
        ConnectConfig connectConfig = new ConnectConfig();
        connectConfig.reUseConnect = false;
        MixBleDelegate.getInstance().open(false, str2, breezesubdevlogincallback, connectConfig);
    }

    public static class breezeSubDevLoginCallback implements ConnectionCallback {
        protected String deviceName;
        protected Runnable mRetryRunnable;
        protected ServiceListener onlyOnceServiceListener;
        protected String productKey;
        protected AtomicBoolean isOpening = new AtomicBoolean(false);
        protected AtomicBoolean isDone = new AtomicBoolean(false);
        protected AtomicBoolean isRetry = new AtomicBoolean(true);

        public breezeSubDevLoginCallback(String str, final String str2, ServiceListener serviceListener) {
            this.productKey = str;
            this.deviceName = str2;
            this.onlyOnceServiceListener = serviceListener;
            this.mRetryRunnable = new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.breezeSubDevLoginCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    breezeSubDevLoginCallback.this.isRetry.set(true);
                    breezeSubDevLoginCallback.this.isOpening.set(false);
                    MixBleDelegate.getInstance().open(false, str2, breezeSubDevLoginCallback.this);
                }
            };
        }

        public void setOpening(boolean z) {
            this.isOpening.set(z);
        }

        public boolean isOpening() {
            return this.isOpening.get();
        }

        public void onConnectionStateChange(final MixBleDevice mixBleDevice, int i, int i2) {
            ALog.d(DevService.TAG, "onConnectionStateChange mac:" + this.deviceName + " state:" + i + " isOpening:" + this.isOpening);
            if (i != 2 || !this.isOpening.compareAndSet(false, true)) {
                if (i == 0) {
                    ALog.d(DevService.TAG, "onConnectionStateChange STATE_DISCONNECTED call error isRetry:" + this.isRetry + " isOpening:" + isOpening());
                    if (this.isOpening.compareAndSet(false, true)) {
                        if (!this.isRetry.get()) {
                            TmpSdk.mHandler.removeCallbacks(this.mRetryRunnable);
                            TmpSdk.mHandler.postDelayed(this.mRetryRunnable, 5000L);
                            return;
                        } else {
                            this.onlyOnceServiceListener.onComplete(false, new UnknownError());
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            if (mixBleDevice != null && mixBleDevice.getDescriptor() != null && mixBleDevice.getDescriptor().getBreezeScanRecord() != null && mixBleDevice.getDescriptor().getBreezeScanRecord().getProtocolVersion() >= 6) {
                ArrayList arrayList = new ArrayList();
                AccessInfo comboAccessInfo = TmpStorage.getInstance().getComboAccessInfo(TextHelper.combineStr(this.productKey, TmpStorage.getInstance().getDnByMac(mixBleDevice.getDescriptor().getBreezeScanRecord().getMacWithColon())));
                arrayList.add(new TLV.Element(BreezeConstants.BREEZE_PROVISION_VERSION, new byte[]{0}));
                if (comboAccessInfo == null || TextUtils.isEmpty(comboAccessInfo.mAccessKey) || TextUtils.isEmpty(comboAccessInfo.mAccessToken)) {
                    arrayList.add(new TLV.Element((byte) 1, (byte[]) null));
                    arrayList.add(new TLV.Element((byte) 2, (byte[]) null));
                    arrayList.add(new TLV.Element((byte) 3, (byte[]) null));
                } else {
                    arrayList.add(new TLV.Element((byte) 1, comboAccessInfo.mAccessKey.getBytes()));
                    arrayList.add(new TLV.Element((byte) 2, "0123456789012345".getBytes()));
                    arrayList.add(new TLV.Element((byte) 3, Secure.getHMacSha1Str("0123456789012345", comboAccessInfo.mAccessToken)));
                }
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(new TLV.Element((byte) 12, TLV.toPayload(arrayList)));
                ALog.d(DevService.TAG, "send auth sign newMessage  bytes:" + ByteUtils.byte2hex(TLV.toPayload(arrayList2)));
                mixBleDevice.sendMessage(mixBleDevice.newMessage(false, 13, TLV.toPayload(arrayList2)), new MixBleDevice.ResponseCallback() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.breezeSubDevLoginCallback.2
                    public void onResponse(int i3, byte[] bArr) {
                        List<TLV.Element> list;
                        List<TLV.Element> list2;
                        ALog.d(DevService.TAG, "auth sign ResponseCallback newMessage 0x08 error:" + i3 + " bytes:" + ByteUtils.byte2hex(bArr));
                        if (1 == i3 && (list = TLV.parse(bArr)) != null) {
                            for (TLV.Element element : list) {
                                if (element != null && element.type == 12 && (list2 = TLV.parse(element.value)) != null) {
                                    for (TLV.Element element2 : list2) {
                                        if (element2 != null && element2.type == 1 && element2.value != null && element2.value[0] == 1) {
                                            breezeSubDevLoginCallback.this.breezeOnline(mixBleDevice);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new AuthSignIllegalError());
                    }
                });
                return;
            }
            breezeOnline(mixBleDevice);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void breezeOnline(MixBleDevice mixBleDevice) {
            if (mixBleDevice == null) {
                this.onlyOnceServiceListener.onComplete(false, new ParamsError());
                MixBleDelegate.getInstance().close(this.deviceName, (ConnectionCallback) null);
            } else {
                BreezeHelper.getDeviceInfo(mixBleDevice, new AnonymousClass3(mixBleDevice == null || mixBleDevice.getDescriptor() == null || mixBleDevice.getDescriptor().getBreezeScanRecord() == null || mixBleDevice.getDescriptor().getBreezeScanRecord().getProtocolVersion() < 6));
            }
        }

        /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.service.DevService$breezeSubDevLoginCallback$3, reason: invalid class name */
        class AnonymousClass3 implements BreezeHelper.IDeviceInfoCallback {
            final /* synthetic */ boolean val$fIsNeedCloseBreeze;

            AnonymousClass3(boolean z) {
                this.val$fIsNeedCloseBreeze = z;
            }

            public void onDeviceInfo(final BreezeHelper.DeviceInfo deviceInfo) {
                if (deviceInfo == null) {
                    breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new OnlineError());
                    MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                    return;
                }
                ALog.d(DevService.TAG, "onDeviceInfo sign:" + deviceInfo.sign);
                BreezeHelper.online(deviceInfo, new BreezeHelper.OnlineCallback() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.breezeSubDevLoginCallback.3.1
                    public void onOnlineResult(final ISubDeviceChannel iSubDeviceChannel, int i) {
                        ALog.d(DevService.TAG, "onOnlineResult i:" + i);
                        if (i == 0) {
                            CloudUtils.updateBreezeMac(deviceInfo.productKey, deviceInfo.deviceName, breezeSubDevLoginCallback.this.deviceName, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.breezeSubDevLoginCallback.3.1.1
                                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                                public void onResponse(ARequest aRequest, AResponse aResponse) {
                                    ALog.d(DevService.TAG, "updateBreezeMac onResponse productKey:" + deviceInfo.productKey + " deviceName:" + deviceInfo.deviceName + " mac:" + breezeSubDevLoginCallback.this.deviceName);
                                    if (aResponse == null || aResponse.data == null) {
                                        ALog.e(DevService.TAG, "updateBreezeMac onResponse productKey aResponse or data empty");
                                        breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new OnlineError());
                                        MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                                        return;
                                    }
                                    String responseData = TextHelper.formatResponseData(aResponse.data);
                                    if (TextUtils.isEmpty(responseData)) {
                                        ALog.e(DevService.TAG, "updateBreezeMac onResponse payload empty");
                                        breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new OnlineError());
                                        MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                                        return;
                                    }
                                    CommonResponsePayload commonResponsePayload = (CommonResponsePayload) JSON.parseObject(responseData, CommonResponsePayload.class);
                                    if (commonResponsePayload == null || commonResponsePayload.getCode() != 200) {
                                        ALog.e(DevService.TAG, "updateBreezeMac onResponse commonResponsePayload empty or error");
                                        breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new OnlineError());
                                        MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                                        return;
                                    }
                                    HashMap map = new HashMap();
                                    map.put(DevService.BUNDLE_KEY_PRODUCTKEY, deviceInfo.productKey);
                                    map.put(DevService.BUNDLE_KEY_DEVICENAME, deviceInfo.deviceName);
                                    map.put(DevService.BUNDLE_KEY_SUBCHANNEL, iSubDeviceChannel);
                                    breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(true, map);
                                    if (AnonymousClass3.this.val$fIsNeedCloseBreeze) {
                                        MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                                    }
                                }

                                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                                public void onFailure(ARequest aRequest, AError aError) {
                                    ALog.d(DevService.TAG, "updateBreezeMac onFailure productKey:" + deviceInfo.productKey + " deviceName:" + deviceInfo.deviceName + " mac:" + breezeSubDevLoginCallback.this.deviceName);
                                    ServiceListener serviceListener = breezeSubDevLoginCallback.this.onlyOnceServiceListener;
                                    if (aError == null) {
                                        aError = new OnlineError();
                                    }
                                    serviceListener.onComplete(false, aError);
                                    MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                                }
                            });
                        } else {
                            breezeSubDevLoginCallback.this.onlyOnceServiceListener.onComplete(false, new OnlineError());
                            MixBleDelegate.getInstance().close(breezeSubDevLoginCallback.this.deviceName, (ConnectionCallback) null);
                        }
                    }
                });
            }
        }
    }

    public static void notifySubDeviceBinded(JSONObject jSONObject) {
        StringBuilder sb = new StringBuilder();
        sb.append("notifySubDeviceBinded  params:");
        sb.append(jSONObject);
        ALog.d(TAG, sb.toString() == null ? TmpConstant.GROUP_ROLE_UNKNOWN : jSONObject.toString());
        if (jSONObject == null) {
            ALog.e(TAG, "notifySubDeviceBinded params empty");
        } else {
            notifySubDeviceBinded((SubDevInfo) JSON.parseObject(jSONObject.toString(), SubDevInfo.class));
        }
    }

    public static void notifySubDeviceBinded(SubDevInfo subDevInfo) {
        notifySubDeviceBinded(subDevInfo, null);
    }

    public static void notifySubDeviceBinded(SubDevInfo subDevInfo, ServiceListener serviceListener) {
        if (subDevInfo == null || TextUtils.isEmpty(subDevInfo.productKey) || TextUtils.isEmpty(subDevInfo.deviceName)) {
            ALog.e(TAG, "notifySubDeviceBinded params parse error");
            if (serviceListener != null) {
                serviceListener.onComplete(true, null);
                return;
            }
            return;
        }
        ALog.d(TAG, "notifySubDeviceBinded  subDevInfo pk:" + subDevInfo.productKey + " dn:" + subDevInfo.deviceName);
        DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(TextHelper.combineStr(subDevInfo.productKey, subDevInfo.deviceName));
        if (deviceBasicData == null) {
            ALog.e(TAG, "getDeviceBasicData basicData null");
            if (serviceListener != null) {
                serviceListener.onComplete(true, null);
                return;
            }
            return;
        }
        if ("2".equalsIgnoreCase(deviceBasicData.getModelType())) {
            new WifiBTComboDeviceService().afterBind(subDevInfo, serviceListener);
            return;
        }
        if ("3".equalsIgnoreCase(deviceBasicData.getModelType())) {
            DefaultProvisionConfig defaultProvisionConfig = new DefaultProvisionConfig(deviceBasicData);
            defaultProvisionConfig.setAuthChangeType(DefaultProvisionConfig.AuthChangeType.CloudAuth);
            final IProvision iProvisionCreateProvision = DeviceManager.getInstance().createProvision(defaultProvisionConfig);
            iProvisionCreateProvision.provisionInit(null, new IDevListener() { // from class: com.aliyun.alink.linksdk.tmp.service.DevService.5
                @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                public void onSuccess(Object obj, OutputParams outputParams) {
                    ALog.d(DevService.TAG, "provision init onSuccess returnValue:" + outputParams);
                    iProvisionCreateProvision.unInit();
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                public void onFail(Object obj, ErrorInfo errorInfo) {
                    ALog.d(DevService.TAG, "provision init onFail errorInfo:" + errorInfo);
                    iProvisionCreateProvision.unInit();
                }
            });
            if (serviceListener != null) {
                serviceListener.onComplete(true, null);
            }
        }
    }

    public static void setWifiStatus(String str, TmpEnum.DeviceWifiStatus deviceWifiStatus, ServiceListenerEx serviceListenerEx) {
        setWifiStatus(str, deviceWifiStatus, null, serviceListenerEx);
    }

    public static void setWifiStatus(String str, TmpEnum.DeviceWifiStatus deviceWifiStatus, String str2, ServiceListenerEx serviceListenerEx) {
        if (deviceWifiStatus == null || TextUtils.isEmpty(str)) {
            ALog.e(TAG, "setWifiStatus status emtpy or iotid empty iotId:" + str + " status:" + deviceWifiStatus);
            return;
        }
        DeviceShadowMgr.getInstance().setDeviceWifiStatus(str, deviceWifiStatus);
        CmpNotifyManager.getInstance().onNotify(TmpConstant.LINK_DEFAULT_CONNECT_ID, TmpConstant.THING_WIFI_STATUS_NOTIFY, getWiFiStatusNotifyMessage(str, deviceWifiStatus, str2));
        if (deviceWifiStatus == TmpEnum.DeviceWifiStatus.DeviceWifiStatus_Setting) {
            ALog.d(TAG, "setWifiStatus DeviceWifiStatus_Setting return");
        } else {
            GateWayRequest setDeviceExtendRequest = new SetDeviceExtendRequest(str, TmpConstant.DATA_KEY_DEVICE_WIFI_STATUS, String.valueOf(deviceWifiStatus.getValue()));
            setDeviceExtendRequest.sendRequest(setDeviceExtendRequest, serviceListenerEx);
        }
    }

    private static AMessage getWiFiStatusNotifyMessage(String str, TmpEnum.DeviceWifiStatus deviceWifiStatus, String str2) {
        AMessage aMessage = new AMessage();
        com.alibaba.fastjson.JSONObject jSONObject = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject jSONObject2 = new com.alibaba.fastjson.JSONObject();
        jSONObject2.put("iotId", (Object) str);
        jSONObject2.put("status", (Object) Integer.valueOf(deviceWifiStatus.getValue()));
        if (!TextUtils.isEmpty(str2)) {
            jSONObject2.put(TmpConstant.WIFI_CONNECT_FAIL_CODE, (Object) str2);
        }
        jSONObject.put("params", (Object) jSONObject2);
        aMessage.data = jSONObject.toJSONString().getBytes();
        return aMessage;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static TmpEnum.DeviceWifiStatus getWifiStatus(String str) {
        String deviceWifiStatus = DeviceShadowMgr.getInstance().getDeviceWifiStatus(str);
        TmpEnum.DeviceWifiStatus deviceWifiStatus2 = TmpEnum.DeviceWifiStatus.DeviceWifiStatus_NotSupport;
        try {
            return TmpEnum.DeviceWifiStatus.formatDeviceWifiStatus(Integer.valueOf((String) ((GetDeviceExtendRequest.DeviceExtendGetResponse) JSON.parseObject(deviceWifiStatus, GetDeviceExtendRequest.DeviceExtendGetResponse.class)).data).intValue());
        } catch (Exception e) {
            ALog.e(TAG, "getWifiStatus parseObject e:" + e.toString());
            return deviceWifiStatus2;
        }
    }

    public static int getDeviceNetTypesSupported(String str) {
        GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse;
        try {
            getDeviceNetTypesSupportedResponse = (GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSON.parseObject(DeviceShadowMgr.getInstance().getDeviceSupportedNetTypesByIotId(str), GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class);
        } catch (Exception e) {
            ALog.e(TAG, "getDeviceNetTypesSupported e:" + e.toString());
            getDeviceNetTypesSupportedResponse = null;
        }
        return getDeviceNetTypesSupportedResponse == null ? TmpEnum.DeviceNetType.NET_UNKNOWN.getValue() : TmpEnum.DeviceNetType.formatDeviceNetType((List) getDeviceNetTypesSupportedResponse.data);
    }

    public static boolean isDeviceWifiAndBleCombo(String str) {
        int deviceNetTypesSupported = getDeviceNetTypesSupported(str);
        return (TmpEnum.DeviceNetType.NET_WIFI.getValue() & deviceNetTypesSupported) > 0 && (deviceNetTypesSupported & TmpEnum.DeviceNetType.NET_BT.getValue()) > 0;
    }

    public static boolean isDeviceWifiAndBleCombo(int i) {
        return TmpEnum.DeviceNetType.isWifiBtCombo(i);
    }
}
