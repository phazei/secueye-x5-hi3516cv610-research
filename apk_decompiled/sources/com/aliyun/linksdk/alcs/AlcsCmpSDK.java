package com.aliyun.linksdk.alcs;

import android.content.Context;
import com.aliyun.alink.linksdk.alcs.api.client.AlcsClientConfig;
import com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler;
import com.aliyun.alink.linksdk.alcs.api.server.AlcsServerConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsCmpSDK {
    public static String DISCOVERY_ADDR = "224.0.1.187";
    private static int DISCOVERY_PORT = 5683;
    private static String DISCOVERY_TOPIC = "/dev/core/service/dev";
    private static final String TAG = "AlcsCmpSDK";
    private static IAlcsPal discoveryClient;
    private static volatile IDiscoveryDevicesListener discoveryDevicesListener;
    private static volatile IDiscoveryDevicesListener notifyDevicesListener;
    private static IAlcsServer server;

    public interface IDiscoveryCertainDeviceListener {
        void onFail(PalDeviceInfo palDeviceInfo);

        void onSuccess(PalDeviceInfo palDeviceInfo);

        void onTimeout(PalDeviceInfo palDeviceInfo);
    }

    public interface IDiscoveryDevicesListener {
        void onFound(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo);
    }

    public static void init(Context context) {
        ALog.d(TAG, "init()");
    }

    public static void destroy() {
        ALog.d(TAG, "destroy()");
    }

    public static void startDiscoverDevices(int i, IDiscoveryDevicesListener iDiscoveryDevicesListener) {
        startDiscoverDevices(i, null, iDiscoveryDevicesListener);
    }

    public static void startDiscoverDevices(int i, PalDiscoveryConfig palDiscoveryConfig, IDiscoveryDevicesListener iDiscoveryDevicesListener) {
        ALog.d(TAG, "startDiscoverDevices");
        discoveryDevicesListener = iDiscoveryDevicesListener;
        if (discoveryClient == null) {
            discoveryClient = PluginMgr.getInstance();
        }
        AlcsClientConfig alcsClientConfig = new AlcsClientConfig();
        alcsClientConfig.setDstAddr(DISCOVERY_ADDR);
        alcsClientConfig.setDstPort(DISCOVERY_PORT);
        alcsClientConfig.setIsNeddAuth(false);
        try {
            discoveryClient.startDiscovery(i, palDiscoveryConfig, new PalDiscoveryListener() { // from class: com.aliyun.linksdk.alcs.AlcsCmpSDK.1
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
                public void onDiscoveryFinish() {
                }

                @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
                public void onDiscoveryDevice(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo) {
                    ALog.d(AlcsCmpSDK.TAG, "onReqComplete(), result = " + palDiscoveryDeviceInfo);
                    if (palDiscoveryDeviceInfo == null || AlcsCmpSDK.discoveryDevicesListener == null) {
                        return;
                    }
                    AlcsCmpSDK.discoveryDevicesListener.onFound(palDiscoveryDeviceInfo);
                }
            });
        } catch (Throwable th) {
            ALog.e(TAG, "startDiscoverDevices error t: " + th.toString());
        }
    }

    public static void stopDiscoveryDevices() {
        ALog.d(TAG, "stopDiscoveryDevices()");
        IAlcsPal iAlcsPal = discoveryClient;
        if (iAlcsPal == null) {
            return;
        }
        iAlcsPal.stopDiscovery();
    }

    public static void discoveryCertainDevice(PalDeviceInfo palDeviceInfo, final IDiscoveryCertainDeviceListener iDiscoveryCertainDeviceListener) {
        ALog.d(TAG, "discoveryCertainDevice");
        if (discoveryClient == null) {
            discoveryClient = PluginMgr.getInstance();
        }
        discoveryClient.probeDevice(palDeviceInfo, new PalProbeListener() { // from class: com.aliyun.linksdk.alcs.AlcsCmpSDK.2
            @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener
            public void onComplete(PalDeviceInfo palDeviceInfo2, PalProbeResult palProbeResult) {
                StringBuilder sb = new StringBuilder();
                sb.append("discoveryCertainDevice,onComplete(), code = ");
                sb.append(palProbeResult != null ? Integer.valueOf(palProbeResult.probeResult) : TmpConstant.GROUP_ROLE_UNKNOWN);
                ALog.d(AlcsCmpSDK.TAG, sb.toString());
                if (iDiscoveryCertainDeviceListener == null || palProbeResult == null) {
                    return;
                }
                switch (palProbeResult.probeResult) {
                    case 0:
                        iDiscoveryCertainDeviceListener.onSuccess(palDeviceInfo2);
                        break;
                    case 1:
                        iDiscoveryCertainDeviceListener.onTimeout(palDeviceInfo2);
                        break;
                    case 2:
                        iDiscoveryCertainDeviceListener.onFail(palDeviceInfo2);
                        break;
                }
            }
        });
    }

    public static void startNotifyMonitor(IDiscoveryDevicesListener iDiscoveryDevicesListener) {
        ALog.d(TAG, "startNotifyMonitor");
        notifyDevicesListener = iDiscoveryDevicesListener;
        if (discoveryClient == null) {
            discoveryClient = PluginMgr.getInstance();
        }
        discoveryClient.startNotifyMonitor(new PalDiscoveryListener() { // from class: com.aliyun.linksdk.alcs.AlcsCmpSDK.3
            @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
            public void onDiscoveryFinish() {
            }

            @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
            public void onDiscoveryDevice(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo) {
                ALog.d(AlcsCmpSDK.TAG, "onReqComplete(), result = " + palDiscoveryDeviceInfo);
                if (palDiscoveryDeviceInfo == null || AlcsCmpSDK.notifyDevicesListener == null) {
                    return;
                }
                AlcsCmpSDK.notifyDevicesListener.onFound(palDiscoveryDeviceInfo);
            }
        });
    }

    public static void stopNotifyMonitor() {
        if (discoveryClient == null) {
            discoveryClient = PluginMgr.getInstance();
        }
        notifyDevicesListener = null;
        discoveryClient.stopNotifyMonitor();
    }

    public static IAlcsClient initClientConnect(AlcsClientConfig alcsClientConfig, IDeviceHandler iDeviceHandler) {
        ALog.d(TAG, "initDeviceConnect()");
        AlcsClientWrapper alcsClientWrapper = new AlcsClientWrapper();
        alcsClientWrapper.init(alcsClientConfig, iDeviceHandler);
        return alcsClientWrapper;
    }

    public static IAlcsServer initServer(AlcsServerConfig alcsServerConfig) {
        ALog.d(TAG, "initServer()");
        if (server == null) {
            server = new AlcsServerWrapper(alcsServerConfig);
        }
        return server;
    }

    public static IAlcsServer getServer() {
        ALog.d(TAG, "getServer()");
        return server;
    }
}
