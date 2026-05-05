package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalConnectParams;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: IAlcsPalLayer.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class e extends IAlcsPal {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3993a = "[AlcsLPBS]IAlcsPalLayer";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private e f3994b;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public String getPluginId() {
        return AlcsPalConst.DEFAULT_PLUGIN_ID;
    }

    public e(e eVar) {
        this.f3994b = eVar;
    }

    public e a() {
        return this.f3994b;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void initAlcs(PalInitData palInitData) {
        if (a() != null) {
            a().initAlcs(palInitData);
        } else {
            ALog.e(f3993a, "initAlcs on error Layer");
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void deInitAlcs() {
        if (a() != null) {
            a().deInitAlcs();
        } else {
            ALog.e(f3993a, "deInitAlcs on error Layer");
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public boolean regAuthProvider(String str, IAuthProvider iAuthProvider) {
        if (a() != null) {
            return a().regAuthProvider(str, iAuthProvider);
        }
        ALog.e(f3993a, "regAuthProvider on error Layer");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void regCloudChannelFactory(ICloudChannelFactory iCloudChannelFactory) {
        if (a() != null) {
            a().regCloudChannelFactory(iCloudChannelFactory);
        } else {
            ALog.e(f3993a, "regCloudChannelFactory on error Layer");
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void startConnect(PalConnectParams palConnectParams, PalConnectListener palConnectListener) {
        if (a() == null) {
            ALog.e(f3993a, "startConnect on error Layer");
            if (palConnectListener != null) {
                palConnectListener.onLoad(1, null, palConnectParams.deviceInfo);
                return;
            }
            return;
        }
        a().startConnect(palConnectParams, palConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void stopConnect(PalDeviceInfo palDeviceInfo) {
        if (a() != null) {
            a().stopConnect(palDeviceInfo);
        } else {
            ALog.e(f3993a, "stopConnect on error Layer");
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean asyncSendRequest(PalReqMessage palReqMessage, PalMsgListener palMsgListener) {
        if (a() == null) {
            ALog.e(f3993a, "asyncSendRequest on error Layer");
            if (palMsgListener == null) {
                return false;
            }
            palMsgListener.onLoad(null);
            return false;
        }
        return a().asyncSendRequest(palReqMessage, palMsgListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean subscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener, PalMsgListener palMsgListener2) {
        if (a() == null) {
            ALog.e(f3993a, "subscribe on error Layer");
            if (palMsgListener == null) {
                return false;
            }
            palMsgListener.onLoad(null);
            return false;
        }
        return a().subscribe(palSubMessage, palMsgListener, palMsgListener2);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unsubscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener) {
        if (a() == null) {
            ALog.e(f3993a, "unsubscribe on error Layer");
            if (palMsgListener == null) {
                return false;
            }
            palMsgListener.onLoad(null);
            return false;
        }
        return a().unsubscribe(palSubMessage, palMsgListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean isDeviceConnected(PalDeviceInfo palDeviceInfo) {
        if (a() == null) {
            ALog.e(f3993a, "isDeviceConnected on error Layer");
            return false;
        }
        return a().isDeviceConnected(palDeviceInfo);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean regDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        if (a() == null) {
            ALog.e(f3993a, "regDeviceStateListener on error Layer");
            return false;
        }
        return a().regDeviceStateListener(palDeviceInfo, palDeviceStateListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unregDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        if (a() == null) {
            ALog.e(f3993a, "unregDeviceStateListener on error Layer");
            return false;
        }
        return a().unregDeviceStateListener(palDeviceInfo, palDeviceStateListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe
    public void probeDevice(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener) {
        if (a() == null) {
            ALog.e(f3993a, "probeDevice on error Layer");
        } else {
            a().probeDevice(palDeviceInfo, palProbeListener);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryListener palDiscoveryListener) {
        if (a() == null) {
            ALog.e(f3993a, "startDiscovery on error Layer");
            return false;
        }
        return a().startDiscovery(i, palDiscoveryListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryConfig palDiscoveryConfig, PalDiscoveryListener palDiscoveryListener) {
        if (a() == null) {
            ALog.e(f3993a, "startDiscovery on error Layer");
            return false;
        }
        return a().startDiscovery(i, palDiscoveryConfig, palDiscoveryListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopDiscovery() {
        if (a() == null) {
            ALog.e(f3993a, "stopDiscovery on error Layer");
            return false;
        }
        return a().stopDiscovery();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startNotifyMonitor(PalDiscoveryListener palDiscoveryListener) {
        if (a() == null) {
            ALog.e(f3993a, "startNotifyMonitor on error Layer");
            return false;
        }
        return a().startNotifyMonitor(palDiscoveryListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopNotifyMonitor() {
        if (a() == null) {
            ALog.e(f3993a, "stopNotifyMonitor on error Layer");
            return false;
        }
        return a().stopNotifyMonitor();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void onCloudChannelCreate(IThingCloudChannel iThingCloudChannel) {
        ALog.e(f3993a, "onCloudChannelCreate empty impl");
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public int getConnectType(PalDeviceInfo palDeviceInfo) {
        ALog.e(f3993a, "getConnectType empty impl");
        return 0;
    }
}
