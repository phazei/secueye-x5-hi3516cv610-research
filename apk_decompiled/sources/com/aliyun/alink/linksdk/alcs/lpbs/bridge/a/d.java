package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.api.ICADisconnectListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAConnectParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProviderListener;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalConnectParams;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAAlcsConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements IPalConnect {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f4054d = "[AlcsLPBS]ICAAlcsConnect";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected PalConnectParams f4055a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected c f4056b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected PalDeviceInfo f4057c;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public int getConnectType(PalDeviceInfo palDeviceInfo) {
        return 1;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public String getPluginId() {
        return "iot_ica";
    }

    public d(c cVar, PalDeviceInfo palDeviceInfo) {
        this.f4056b = cVar;
        this.f4057c = palDeviceInfo;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void startConnect(final PalConnectParams palConnectParams, final PalConnectListener palConnectListener) {
        if (palConnectListener == null) {
            ALog.e(f4054d, "startConnect listener null");
            return;
        }
        if (palConnectParams == null) {
            ALog.e(f4054d, "startConnect params null");
            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
            return;
        }
        ICADiscoveryDeviceInfo iCADiscoveryDeviceInfoA = this.f4056b.a(palConnectParams.getDevId());
        this.f4055a = palConnectParams;
        if (iCADiscoveryDeviceInfoA == null) {
            ALog.e(f4054d, "startConnect discoveryDeviceInfo null params:" + palConnectParams.toString());
            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
            return;
        }
        final String str = iCADiscoveryDeviceInfoA.addr;
        final int i = iCADiscoveryDeviceInfoA.port;
        final String str2 = iCADiscoveryDeviceInfoA.pal;
        if (this.f4055a.authInfo == null) {
            ALog.d(f4054d, "authInfo null");
            if (this.f4056b.getPalAuthRegister().getProvider() == null) {
                palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
                return;
            } else {
                this.f4056b.getPalAuthRegister().getProvider().queryAuthInfo(palConnectParams.deviceInfo, null, iCADiscoveryDeviceInfoA, new IAuthProviderListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.d.1
                    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProviderListener
                    public void onComplete(PalDeviceInfo palDeviceInfo, Object obj) {
                        if (obj == null) {
                            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
                            return;
                        }
                        ICAConnectParams iCAConnectParams = new ICAConnectParams(new ICADeviceInfo(palConnectParams.deviceInfo.productModel, palConnectParams.deviceInfo.deviceId), str2, (ICAAuthParams) obj);
                        h hVar = new h(palConnectListener);
                        if (iCAConnectParams.authInfo == null || TextUtils.isEmpty(iCAConnectParams.authInfo.accessKey) || TextUtils.isEmpty(iCAConnectParams.authInfo.accessToken)) {
                            ALog.d(d.f4054d, "startConnect params empty");
                            hVar.onLoad(503, "invalid params", iCAConnectParams.deviceInfo);
                            return;
                        }
                        ALog.d(d.f4054d, "startConnect params:" + palConnectParams + " listener:" + palConnectListener);
                        ICAAlcsNative.connectDevice(str, i, iCAConnectParams, hVar);
                    }
                });
                return;
            }
        }
        ICAConnectParams iCAConnectParams = new ICAConnectParams(new ICADeviceInfo(palConnectParams.deviceInfo.productModel, palConnectParams.deviceInfo.deviceId), iCADiscoveryDeviceInfoA.pal, (ICAAuthParams) palConnectParams.authInfo);
        h hVar = new h(palConnectListener);
        if (iCAConnectParams.authInfo == null || TextUtils.isEmpty(iCAConnectParams.authInfo.accessKey) || TextUtils.isEmpty(iCAConnectParams.authInfo.accessToken)) {
            ALog.d(f4054d, "startConnect params empty");
            hVar.onLoad(503, "invalid params", iCAConnectParams.deviceInfo);
            return;
        }
        ALog.d(f4054d, "startConnect params:" + palConnectParams + " listener:" + palConnectListener);
        ICAAlcsNative.connectDevice(str, i, iCAConnectParams, hVar);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void stopConnect(PalDeviceInfo palDeviceInfo) {
        ALog.d(f4054d, "stopConnect deviceInfo:" + palDeviceInfo);
        ICAAlcsNative.disConnectDevice(m.a(palDeviceInfo));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean asyncSendRequest(PalReqMessage palReqMessage, PalMsgListener palMsgListener) {
        ALog.d(f4054d, "asyncSendRequest reqMessageInfo:" + palReqMessage + " callback:" + palMsgListener);
        if (palReqMessage == null) {
            ALog.d(f4054d, "asyncSendRequest error:");
            return false;
        }
        ICAReqMessage iCAReqMessageA = m.a(palReqMessage);
        if (iCAReqMessageA.topic != null && iCAReqMessageA.topic.contains("/thing/model/down_raw")) {
            iCAReqMessageA.code = 3;
        }
        return ICAAlcsNative.sendRequest(iCAReqMessageA, new k(palMsgListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean subscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener, PalMsgListener palMsgListener2) {
        ALog.d(f4054d, "subscribe  subMessage:" + palSubMessage + " PalMsgListener:" + palMsgListener + " eventListener:" + palMsgListener2);
        return ICAAlcsNative.subcribe(m.a(palSubMessage), new k(palMsgListener), new k(palMsgListener2));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unsubscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener) {
        ALog.d(f4054d, "unsubcribe reqMessageInfo:" + palSubMessage + " callback:" + palMsgListener);
        return ICAAlcsNative.unsubcribe(m.a(palSubMessage), new k(palMsgListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean isDeviceConnected(PalDeviceInfo palDeviceInfo) {
        boolean zIsDeviceOnline = ICAAlcsNative.isDeviceOnline(new ICADeviceInfo(palDeviceInfo.productModel, palDeviceInfo.deviceId));
        ALog.d(f4054d, "isDeviceConnected deviceInfo:" + zIsDeviceOnline);
        return zIsDeviceOnline;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean regDeviceStateListener(final PalDeviceInfo palDeviceInfo, final PalDeviceStateListener palDeviceStateListener) {
        ICAAlcsNative.setDeviceDisconnectListener(new ICADeviceInfo(palDeviceInfo.productModel, palDeviceInfo.deviceId), new ICADisconnectListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.d.2
            @Override // com.aliyun.alink.linksdk.alcs.api.ICADisconnectListener
            public void onDisConnect(ICADeviceInfo iCADeviceInfo) {
                StringBuilder sb = new StringBuilder();
                sb.append("DeviceStatechange icaDeviceInfo:");
                sb.append(iCADeviceInfo == null ? TmpConstant.GROUP_ROLE_UNKNOWN : iCADeviceInfo.toString());
                sb.append(" STATE_DISCONNECTED");
                sb.append(" listener:");
                sb.append(palDeviceStateListener);
                ALog.d(d.f4054d, sb.toString());
                if (palDeviceStateListener != null || iCADeviceInfo == null) {
                    palDeviceStateListener.onDeviceStateChange(palDeviceInfo, 0);
                }
            }
        });
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unregDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        ICAAlcsNative.removeDeviceDisconnectListener(new ICADeviceInfo(palDeviceInfo.productModel, palDeviceInfo.deviceId));
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void onCloudChannelCreate(IThingCloudChannel iThingCloudChannel) {
        ALog.d(f4054d, "onCloudChannelCreate cloudChannel:" + iThingCloudChannel);
    }
}
