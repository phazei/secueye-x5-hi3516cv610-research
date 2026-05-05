package com.aliyun.alink.linksdk.tmp.connect;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnectOption;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.lpbs.lpbstgmesh.data.TgMeshExtraConnectParams;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultClientConfig;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;

/* JADX INFO: compiled from: ConnectFactory.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4237a = "[Tmp]ConnectFactory";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f4238b = "Alcs_Connect_ID";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected static volatile com.aliyun.alink.linksdk.tmp.connect.entity.cmp.a f4239c;

    public static b a(String str, String str2, String str3) {
        return new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.a(str, str2, str3);
    }

    public static b a(String str, String str2) {
        return new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.a(str, str2);
    }

    public static b a() {
        if (f4239c == null) {
            synchronized (f4238b) {
                if (f4239c == null) {
                    f4239c = new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.a(ConnectSDK.getInstance().getAlcsDiscoveryConnectId(), null, null);
                }
            }
        }
        return f4239c;
    }

    public static String a(DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        if (deviceBasicData.isLocal()) {
            if (deviceConfig.getDeviceType() == DeviceConfig.DeviceType.CLIENT || deviceConfig.getDeviceType() == DeviceConfig.DeviceType.PROVISION) {
                return b(deviceBasicData, deviceConfig, iDevListener);
            }
            if (deviceConfig.getDeviceType() == DeviceConfig.DeviceType.SERVER || deviceConfig.getDeviceType() == DeviceConfig.DeviceType.PROVISION_RECEIVER) {
                return c(deviceBasicData, deviceConfig, iDevListener);
            }
            ALog.e(f4237a, "createConnectId local error");
            return f4238b;
        }
        ALog.d(f4237a, "createConnectId api");
        iDevListener.onSuccess(null, new OutputParams(f4238b, new ValueWrapper.StringValueWrapper(ConnectSDK.getInstance().getApiGatewayConnectId())));
        return ConnectSDK.getInstance().getApiGatewayConnectId();
    }

    /* JADX WARN: Type inference failed for: r2v24, types: [ExtraConnectParams, com.aliyun.alink.linksdk.lpbs.lpbstgmesh.data.TgMeshExtraConnectParams] */
    public static String b(DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        ALog.d(f4237a, "createConnectId local client");
        DefaultClientConfig defaultClientConfig = (DefaultClientConfig) deviceConfig;
        String str = f4238b + deviceConfig.getBasicData().getProductKey() + deviceConfig.getBasicData().getDeviceName();
        com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c cVar = new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c(str, iDevListener);
        if (ConnectSDK.getInstance().isConnectRegisted(str) && (ConnectState.DISCONNECTED == ConnectSDK.getInstance().getConnectState(str) || ConnectState.CONNECTFAIL == ConnectSDK.getInstance().getConnectState(str))) {
            ALog.w(f4237a, "createConnectId isConnectRegisted true but DISCONNECTED local connectId:" + str);
            ConnectSDK.getInstance().unregisterConnect(str);
        }
        if (ConnectSDK.getInstance().isConnectRegisted(str) && ConnectState.CONNECTED == ConnectSDK.getInstance().getConnectState(str)) {
            ALog.w(f4237a, "CONNECTED createConnectId isConnectRegisted true local connectId:" + str);
            cVar.onSuccess();
        } else if (ConnectSDK.getInstance().isConnectRegisted(str) && ConnectState.CONNECTING == ConnectSDK.getInstance().getConnectState(str)) {
            ALog.w(f4237a, "CONNECTING createConnectId isConnectRegisted true local connectId:" + str);
            cVar.onSuccess();
        } else {
            AlcsConnectConfig alcsConnectConfig = new AlcsConnectConfig();
            alcsConnectConfig.setDstAddr(deviceBasicData.getAddr());
            alcsConnectConfig.setDstPort(deviceBasicData.getPort());
            alcsConnectConfig.setProductKey(defaultClientConfig.getBasicData().getProductKey());
            alcsConnectConfig.setDeviceName(defaultClientConfig.getBasicData().getDeviceName());
            alcsConnectConfig.setAccessKey(defaultClientConfig.getAccessKey());
            alcsConnectConfig.setAccessToken(defaultClientConfig.getAccessToken());
            alcsConnectConfig.setIotId(defaultClientConfig.getBasicData().getIotId());
            alcsConnectConfig.setSecurity(true);
            alcsConnectConfig.mDataFormat = defaultClientConfig.mDateFormat;
            alcsConnectConfig.mConnectKeepType = defaultClientConfig.mConnectKeepType;
            if (deviceBasicData.isBleMeshDevice()) {
                try {
                    alcsConnectConfig.mPluginId = AlcsPalConst.LPBS_TGMESH_PLUGIN_ID;
                    ?? tgMeshExtraConnectParams = new TgMeshExtraConnectParams();
                    tgMeshExtraConnectParams.unicastAddresses = new ArrayList();
                    alcsConnectConfig.mExtraConnectParams = tgMeshExtraConnectParams;
                } catch (Exception e) {
                    ALog.e(f4237a, "createClientConnectId mesh error:" + e.toString());
                } catch (Throwable th) {
                    ALog.e(f4237a, "createClientConnectId mesh Throwable:" + th.toString());
                }
            }
            ALog.d(f4237a, "createConnectId local client connectId:" + str + " ip:" + deviceBasicData.getAddr() + " port:" + deviceBasicData.getPort() + " pk:" + defaultClientConfig.getBasicData().getProductKey() + " dn:" + defaultClientConfig.getBasicData().getDeviceName() + " iotid:" + defaultClientConfig.getBasicData().getIotId());
            ConnectSDK.getInstance().registerAlcsConnect(TmpSdk.getContext(), str, alcsConnectConfig, cVar);
        }
        return str;
    }

    public static String c(DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        ALog.d(f4237a, "createConnectId local server");
        DefaultServerConfig defaultServerConfig = (DefaultServerConfig) deviceConfig;
        AlcsServerConnectConfig alcsServerConnectConfig = new AlcsServerConnectConfig();
        alcsServerConnectConfig.setPrefix(defaultServerConfig.getPrefix());
        alcsServerConnectConfig.setSecret(defaultServerConfig.getSecret());
        alcsServerConnectConfig.setProductKey(deviceBasicData.getProductKey());
        alcsServerConnectConfig.setDeviceName(deviceBasicData.getDeviceName());
        alcsServerConnectConfig.setBlackClients(TmpStorage.getInstance().getBlackList(deviceBasicData.getDevId()));
        if (ConnectSDK.getInstance().isConnectRegisted(ConnectSDK.getInstance().getAlcsServerConnectId())) {
            if (!"Xtau@iot".equalsIgnoreCase(alcsServerConnectConfig.getPrefix()) && !"Yx3DdsyetbSezlvc".equalsIgnoreCase(alcsServerConnectConfig.getSecret())) {
                b(defaultServerConfig.getPrefix(), defaultServerConfig.getSecret());
            }
            new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c(ConnectSDK.getInstance().getAlcsServerConnectId(), iDevListener).onSuccess();
        } else {
            ConnectSDK.getInstance().registerAlcsServerConnect(TmpSdk.getContext(), alcsServerConnectConfig, new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c(ConnectSDK.getInstance().getAlcsServerConnectId(), iDevListener));
        }
        return ConnectSDK.getInstance().getAlcsServerConnectId();
    }

    public static void a(String str) {
        ALog.d(f4237a, "destroyConnect local connectId:" + str);
        if (TextUtils.isEmpty(str)) {
            ALog.e(f4237a, "destroyConnect local connectId empty");
        } else {
            ConnectSDK.getInstance().unregisterConnect(str);
        }
    }

    public static void b(String str, String str2) {
        ALog.d(f4237a, "updateAlcsServerConnectOption authcode:" + str + " ConnectId:" + ConnectSDK.getInstance().getAlcsServerConnectId());
        if (ConnectSDK.getInstance().isConnectRegisted(ConnectSDK.getInstance().getAlcsServerConnectId())) {
            AlcsServerConnectOption alcsServerConnectOption = new AlcsServerConnectOption();
            alcsServerConnectOption.setOptionFlag(AlcsServerConnectOption.OptionFlag.ADD_PREFIX_SECRET);
            alcsServerConnectOption.setPrefix(str);
            alcsServerConnectOption.setSecrect(str2);
            ConnectSDK.getInstance().updateConnectOption(ConnectSDK.getInstance().getAlcsServerConnectId(), alcsServerConnectOption);
        }
    }

    public static void b(String str) {
        ALog.d(f4237a, "removeAlcsServerAuthInfo authcode:" + str + " ConnectId:" + ConnectSDK.getInstance().getAlcsServerConnectId());
        if (ConnectSDK.getInstance().isConnectRegisted(ConnectSDK.getInstance().getAlcsServerConnectId())) {
            AlcsServerConnectOption alcsServerConnectOption = new AlcsServerConnectOption();
            alcsServerConnectOption.setOptionFlag(AlcsServerConnectOption.OptionFlag.DELETE_PREFIX);
            alcsServerConnectOption.setPrefix(str);
            ConnectSDK.getInstance().updateConnectOption(ConnectSDK.getInstance().getAlcsServerConnectId(), alcsServerConnectOption);
        }
    }
}
