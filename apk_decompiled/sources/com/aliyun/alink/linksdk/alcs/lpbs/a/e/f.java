package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.TopicUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;

/* JADX INFO: compiled from: MainDataConvertLayer.java */
/* JADX INFO: loaded from: classes2.dex */
public class f extends e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3995a = "[AlcsLPBS]MainDataConvertLayer";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.b.a f3996b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f3997c;

    public f(com.aliyun.alink.linksdk.alcs.lpbs.a.b.a aVar, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar2, e eVar) {
        super(eVar);
        this.f3996b = aVar;
        this.f3997c = aVar2;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void initAlcs(PalInitData palInitData) {
        ALog.d(f3995a, "initAlcs initData:" + palInitData);
        for (Map.Entry<String, IPlugin> entry : PluginMgr.getInstance().getPluginList().entrySet()) {
            if (entry.getValue().getPalBridge() != null) {
                entry.getValue().getPalBridge().initBridge(palInitData);
            }
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void deInitAlcs() {
        ALog.d(f3995a, "deInitAlcs initData");
        for (Map.Entry<String, IPlugin> entry : PluginMgr.getInstance().getPluginList().entrySet()) {
            entry.getValue().getPalBridge().deInitBridge();
            entry.getValue().stopPlugin(entry.getValue().getPluginId());
        }
        PluginMgr.getInstance().getPluginList().clear();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public void stopConnect(PalDeviceInfo palDeviceInfo) {
        if (palDeviceInfo == null) {
            ALog.e(f3995a, "stopConnect deviceInfo null error");
            return;
        }
        ALog.d(f3995a, "stopConnect deviceInfo:" + palDeviceInfo.getDevId());
        if (this.f3996b.b(palDeviceInfo.getDevId()) != null) {
            this.f3997c.a(palDeviceInfo);
        }
        super.stopConnect(palDeviceInfo);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean asyncSendRequest(final PalReqMessage palReqMessage, final PalMsgListener palMsgListener) {
        ALog.d(f3995a, "asyncSendRequest reqMessageInfo:" + palReqMessage + " callback:" + palMsgListener);
        if (PluginMgr.getInstance().isDataNeedConvert(palReqMessage.deviceInfo) && PluginMgr.getInstance().getJsProvider() != null && PluginMgr.getInstance().getJsEngine() != null) {
            PluginMgr.getInstance().getJsProvider().queryJsCode(palReqMessage.deviceInfo.productModel, palReqMessage.deviceInfo.deviceId, new IJsQeuryCallback() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.f.1
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback
                public void onLoad(String str, String str2) {
                    PalReqMessage palReqMessage2 = palReqMessage;
                    palReqMessage2.topic = TopicUtils.topicToRawDownTopic(palReqMessage2.topic, palReqMessage.deviceInfo.productModel, palReqMessage.deviceInfo.deviceId);
                    try {
                        if (!TextUtils.isEmpty(str2)) {
                            byte[] bArrProtocolToRawData = PluginMgr.getInstance().getJsEngine().protocolToRawData(str2, new String(palReqMessage.payload, "UTF-8"));
                            if (bArrProtocolToRawData != null) {
                                palReqMessage.payload = bArrProtocolToRawData;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    f.super.asyncSendRequest(palReqMessage, new com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b(palMsgListener, str2, PluginMgr.getInstance().getJsEngine()));
                }
            });
            return true;
        }
        super.asyncSendRequest(palReqMessage, palMsgListener);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean subscribe(final PalSubMessage palSubMessage, final PalMsgListener palMsgListener, final PalMsgListener palMsgListener2) {
        ALog.d(f3995a, "subscribe getJsProvider:" + PluginMgr.getInstance().getJsProvider() + " getJsEngine:" + PluginMgr.getInstance().getJsEngine());
        final IThingCloudChannel iThingCloudChannelB = this.f3997c.b(palSubMessage.deviceInfo);
        if (PluginMgr.getInstance().isDataNeedConvert(palSubMessage.deviceInfo) && PluginMgr.getInstance().getJsProvider() != null && PluginMgr.getInstance().getJsEngine() != null) {
            PluginMgr.getInstance().getJsProvider().queryJsCode(palSubMessage.deviceInfo.productModel, palSubMessage.deviceInfo.deviceId, new IJsQeuryCallback() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.f.2
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback
                public void onLoad(String str, String str2) {
                    String str3 = palSubMessage.topic;
                    PalSubMessage palSubMessage2 = palSubMessage;
                    palSubMessage2.topic = TopicUtils.topicToRawUpTopic(palSubMessage2.deviceInfo.productModel, palSubMessage.deviceInfo.deviceId);
                    try {
                        if (!TextUtils.isEmpty(str2)) {
                            byte[] bArrProtocolToRawData = PluginMgr.getInstance().getJsEngine().protocolToRawData(str2, new String(palSubMessage.payload, "UTF-8"));
                            if (bArrProtocolToRawData != null) {
                                palSubMessage.payload = bArrProtocolToRawData;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    f.super.subscribe(palSubMessage, new com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b(palMsgListener, str2, PluginMgr.getInstance().getJsEngine()), new com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.a(new m(palSubMessage.deviceInfo, iThingCloudChannelB, str3, palMsgListener2), str2, PluginMgr.getInstance().getJsEngine()));
                }
            });
            return true;
        }
        return super.subscribe(palSubMessage, palMsgListener, new m(palSubMessage.deviceInfo, iThingCloudChannelB, palSubMessage.topic, palMsgListener2));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unsubscribe(final PalSubMessage palSubMessage, final PalMsgListener palMsgListener) {
        if (PluginMgr.getInstance().isDataNeedConvert(palSubMessage.deviceInfo) && PluginMgr.getInstance().getJsProvider() != null && PluginMgr.getInstance().getJsEngine() != null) {
            PluginMgr.getInstance().getJsProvider().queryJsCode(palSubMessage.deviceInfo.productModel, palSubMessage.deviceInfo.deviceId, new IJsQeuryCallback() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.f.3
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback
                public void onLoad(String str, String str2) {
                    PalSubMessage palSubMessage2 = palSubMessage;
                    palSubMessage2.topic = TopicUtils.topicToRawUpTopic(palSubMessage2.deviceInfo.productModel, palSubMessage.deviceInfo.deviceId);
                    try {
                        if (!TextUtils.isEmpty(str2)) {
                            byte[] bArrProtocolToRawData = PluginMgr.getInstance().getJsEngine().protocolToRawData(str2, new String(palSubMessage.payload, "UTF-8"));
                            if (bArrProtocolToRawData != null) {
                                palSubMessage.payload = bArrProtocolToRawData;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    f.super.unsubscribe(palSubMessage, palMsgListener);
                }
            });
            return true;
        }
        return super.unsubscribe(palSubMessage, palMsgListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryListener palDiscoveryListener) {
        ALog.d(f3995a, "startDiscovery timeOut:" + i + " listener:" + palDiscoveryListener);
        Map<String, IPlugin> pluginList = PluginMgr.getInstance().getPluginList();
        if (pluginList == null) {
            ALog.e(f3995a, "startDiscovery pluginMap empty");
            return false;
        }
        for (Map.Entry<String, IPlugin> entry : pluginList.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPalBridge() != null && entry.getValue().getPalBridge().getPalDiscovery() != null) {
                entry.getValue().getPalBridge().getPalDiscovery().startDiscovery(i, new d(entry.getValue().getPluginId(), palDiscoveryListener));
            }
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryConfig palDiscoveryConfig, PalDiscoveryListener palDiscoveryListener) {
        Map<String, IPlugin> pluginList = PluginMgr.getInstance().getPluginList();
        if (pluginList == null) {
            ALog.e(f3995a, "startDiscovery pluginMap empty");
            return false;
        }
        if (palDiscoveryConfig == null) {
            return startDiscovery(i, palDiscoveryListener);
        }
        if (palDiscoveryConfig.mPluginIdList == null || palDiscoveryConfig.mPluginIdList.isEmpty()) {
            for (Map.Entry<String, IPlugin> entry : pluginList.entrySet()) {
                if (entry.getValue() != null && entry.getValue().getPalBridge() != null && entry.getValue().getPalBridge().getPalDiscovery() != null) {
                    try {
                        entry.getValue().getPalBridge().getPalDiscovery().startDiscovery(i, palDiscoveryConfig, new d(entry.getValue().getPluginId(), palDiscoveryListener));
                    } catch (Throwable th) {
                        ALog.e(f3995a, "startDiscovery error:" + th.toString());
                    }
                }
            }
            return true;
        }
        for (String str : palDiscoveryConfig.mPluginIdList) {
            IPlugin iPlugin = pluginList.get(str);
            if (iPlugin == null) {
                ALog.w(f3995a, "pluginMap not find pluginId:" + str);
            } else {
                IPalBridge palBridge = iPlugin.getPalBridge();
                if (palBridge == null) {
                    ALog.w(f3995a, "not find palBridge");
                } else {
                    IPalDiscovery palDiscovery = palBridge.getPalDiscovery();
                    if (palDiscovery == null) {
                        ALog.w(f3995a, "not find palDiscovery");
                    } else {
                        try {
                            ALog.d(f3995a, "startDiscovery pluginId:" + str + " discoveryConfig:" + palDiscoveryConfig + " timeOut:" + i);
                            palDiscovery.startDiscovery(i, palDiscoveryConfig, palDiscoveryListener);
                        } catch (Throwable th2) {
                            ALog.e(f3995a, "startDiscovery error:" + th2.toString());
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopDiscovery() {
        for (Map.Entry<String, IPlugin> entry : PluginMgr.getInstance().getPluginList().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPalBridge() != null && entry.getValue().getPalBridge().getPalDiscovery() != null) {
                entry.getValue().getPalBridge().getPalDiscovery().stopDiscovery();
            }
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopNotifyMonitor() {
        for (Map.Entry<String, IPlugin> entry : PluginMgr.getInstance().getPluginList().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPalBridge() != null && entry.getValue().getPalBridge().getPalDiscovery() != null) {
                try {
                    entry.getValue().getPalBridge().getPalDiscovery().stopNotifyMonitor();
                } catch (Throwable th) {
                    ALog.e(f3995a, "stopNotifyMonitor throwable:" + th.toString());
                }
            }
        }
        return true;
    }
}
