package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalConnectParams;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;

/* JADX INFO: compiled from: PkDnConvertLayer.java */
/* JADX INFO: loaded from: classes2.dex */
public class k extends e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4019a = "[AlcsLPBS]PkDnConvertLayer";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.b.a f4020b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f4021c;

    public k(com.aliyun.alink.linksdk.alcs.lpbs.a.b.a aVar, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar2, e eVar) {
        super(eVar);
        this.f4020b = aVar;
        this.f4021c = aVar2;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe
    public void probeDevice(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener) {
        IPlugin pluginByDevId = PluginMgr.getInstance().getPluginByDevId(palDeviceInfo.getDevId());
        if (pluginByDevId == null) {
            palProbeListener.onComplete(palDeviceInfo, new PalProbeResult(2));
            ALog.e(f4019a, "startConnect error plugin not found");
            return;
        }
        PalDeviceInfo privatePkDn = PluginMgr.getInstance().toPrivatePkDn(palDeviceInfo, pluginByDevId.getPluginId());
        try {
            IPalProbe palProbe = pluginByDevId.getPalBridge().getPalProbe();
            if (palProbe != null) {
                palProbe.probeDevice(privatePkDn, new j(palProbeListener, pluginByDevId.getPluginId()));
            }
        } catch (AbstractMethodError e) {
            ALog.w(f4019a, e.toString());
        } catch (Exception e2) {
            ALog.w(f4019a, e2.toString());
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startNotifyMonitor(PalDiscoveryListener palDiscoveryListener) {
        for (Map.Entry<String, IPlugin> entry : PluginMgr.getInstance().getPluginList().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPalBridge() != null && entry.getValue().getPalBridge().getPalDiscovery() != null) {
                try {
                    entry.getValue().getPalBridge().getPalDiscovery().startNotifyMonitor(new d(entry.getValue().getPluginId(), palDiscoveryListener));
                } catch (Throwable th) {
                    ALog.e(f4019a, "startNotifyMonitor Throwable:" + th.toString());
                }
            }
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void startConnect(PalConnectParams palConnectParams, PalConnectListener palConnectListener) {
        IPlugin pluginByPluginId;
        ALog.d(f4019a, "params:" + palConnectParams + " listener:" + palConnectListener);
        if (palConnectParams == null || palConnectParams.deviceInfo == null || palConnectListener == null) {
            ALog.e(f4019a, "startConnect params null");
            return;
        }
        if (TextUtils.isEmpty(palConnectParams.pluginId)) {
            pluginByPluginId = PluginMgr.getInstance().getPluginByDevId(palConnectParams.getDevId());
        } else {
            pluginByPluginId = PluginMgr.getInstance().getPluginByPluginId(palConnectParams.pluginId);
        }
        if (pluginByPluginId == null) {
            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
            ALog.e(f4019a, "startConnect error plugin not found");
            return;
        }
        PalDeviceInfo privatePkDn = PluginMgr.getInstance().toPrivatePkDn(palConnectParams.deviceInfo, pluginByPluginId.getPluginId());
        PalDeviceInfo palDeviceInfo = palConnectParams.deviceInfo;
        IPalConnect palConnect = pluginByPluginId.getPalBridge().getPalConnect(privatePkDn);
        if (palConnect == null) {
            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
            ALog.e(f4019a, "startConnect error connect not found");
            return;
        }
        if (palDeviceInfo == null || TextUtils.isEmpty(palDeviceInfo.getDevId())) {
            palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
            ALog.e(f4019a, "startConnect error getDevId is null.");
            return;
        }
        this.f4020b.a(palDeviceInfo.getDevId(), palConnect);
        palConnectParams.deviceInfo = PluginMgr.getInstance().toPrivatePkDn(palConnectParams.deviceInfo, pluginByPluginId.getPluginId());
        ALog.d(f4019a, "startConnect params:" + palConnectParams + " devid:" + palConnectParams.deviceInfo.getDevId() + " plugin:" + pluginByPluginId.getPluginId());
        palConnect.startConnect(palConnectParams, new i(new a(palDeviceInfo, palConnectListener, new b(palConnect, privatePkDn), this.f4021c, palConnect), palConnect.getPluginId()));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void stopConnect(PalDeviceInfo palDeviceInfo) {
        if (palDeviceInfo == null) {
            ALog.e(f4019a, "stopConnect deviceInfo null");
            return;
        }
        IPalConnect iPalConnectB = this.f4020b.b(palDeviceInfo.getDevId());
        this.f4020b.a(palDeviceInfo.getDevId());
        if (iPalConnectB != null) {
            iPalConnectB.stopConnect(PluginMgr.getInstance().toPrivatePkDn(palDeviceInfo, iPalConnectB.getPluginId()));
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean asyncSendRequest(PalReqMessage palReqMessage, PalMsgListener palMsgListener) {
        IPalConnect iPalConnectB = this.f4020b.b(palReqMessage.deviceInfo.getDevId());
        PalDeviceInfo palDeviceInfo = palReqMessage.deviceInfo;
        ALog.d(f4019a, "asyncSendRequest connect:" + iPalConnectB + " callback:" + palMsgListener);
        if (iPalConnectB != null) {
            palReqMessage.deviceInfo = PluginMgr.getInstance().toPrivatePkDn(palReqMessage.deviceInfo, iPalConnectB.getPluginId());
            palReqMessage.topic = a(palReqMessage.topic, palDeviceInfo, palReqMessage.deviceInfo);
            return iPalConnectB.asyncSendRequest(palReqMessage, new h(palDeviceInfo, palMsgListener));
        }
        if (palMsgListener == null) {
            return false;
        }
        PalRspMessage palRspMessage = new PalRspMessage();
        palRspMessage.code = 1;
        palMsgListener.onLoad(palRspMessage);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean subscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener, PalMsgListener palMsgListener2) {
        IPalConnect iPalConnectB = this.f4020b.b(palSubMessage.deviceInfo.getDevId());
        if (iPalConnectB == null) {
            if (palMsgListener != null) {
                PalRspMessage palRspMessage = new PalRspMessage();
                palRspMessage.code = 1;
                palMsgListener.onLoad(palRspMessage);
            }
            ALog.e(f4019a, "subscribe connect null");
            return false;
        }
        PalDeviceInfo palDeviceInfo = palSubMessage.deviceInfo;
        palSubMessage.deviceInfo = PluginMgr.getInstance().toPrivatePkDn(palSubMessage.deviceInfo, iPalConnectB.getPluginId());
        palSubMessage.topic = a(palSubMessage.topic, palDeviceInfo, palSubMessage.deviceInfo);
        ALog.d(f4019a, "subscribe topic:" + palSubMessage.topic);
        return iPalConnectB.subscribe(palSubMessage, new h(palDeviceInfo, palMsgListener), new h(palDeviceInfo, palMsgListener2));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unsubscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener) {
        IPalConnect iPalConnectB = this.f4020b.b(palSubMessage.deviceInfo.getDevId());
        if (iPalConnectB == null) {
            if (palMsgListener == null) {
                return false;
            }
            PalRspMessage palRspMessage = new PalRspMessage();
            palRspMessage.code = 1;
            palMsgListener.onLoad(palRspMessage);
            return false;
        }
        PalDeviceInfo palDeviceInfo = palSubMessage.deviceInfo;
        palSubMessage.deviceInfo = PluginMgr.getInstance().toPrivatePkDn(palSubMessage.deviceInfo, iPalConnectB.getPluginId());
        palSubMessage.topic = a(palSubMessage.topic, palDeviceInfo, palSubMessage.deviceInfo);
        ALog.d(f4019a, "unsubscribe topic:" + palSubMessage.topic);
        return iPalConnectB.unsubscribe(palSubMessage, new h(palDeviceInfo, palMsgListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean isDeviceConnected(PalDeviceInfo palDeviceInfo) {
        IPalConnect iPalConnectB = this.f4020b.b(palDeviceInfo.getDevId());
        if (iPalConnectB != null) {
            return iPalConnectB.isDeviceConnected(PluginMgr.getInstance().toPrivatePkDn(palDeviceInfo, iPalConnectB.getPluginId()));
        }
        return false;
    }

    public String a(String str, PalDeviceInfo palDeviceInfo, PalDeviceInfo palDeviceInfo2) {
        return (TextUtils.isEmpty(str) || palDeviceInfo == null || palDeviceInfo2 == null || (palDeviceInfo.productModel.equalsIgnoreCase(palDeviceInfo2.productModel) && palDeviceInfo.deviceId.equalsIgnoreCase(palDeviceInfo2.deviceId))) ? str : str.replace(palDeviceInfo.productModel, palDeviceInfo2.productModel).replace(palDeviceInfo.deviceId, palDeviceInfo2.deviceId);
    }
}
