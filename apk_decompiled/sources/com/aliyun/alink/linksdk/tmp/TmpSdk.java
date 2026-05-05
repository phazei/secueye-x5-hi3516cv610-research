package com.aliyun.alink.linksdk.tmp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.api.AlcsCoAPSdk;
import com.aliyun.alink.linksdk.alcs.jsengine.RhinoJsEngine;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalSdk;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgrConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy;
import com.aliyun.alink.linksdk.tmp.component.cloud.LpbsCloudProxyImpl;
import com.aliyun.alink.linksdk.tmp.component.pkdnconvert.DefaultDevInfoTrans;
import com.aliyun.alink.linksdk.tmp.connect.f;
import com.aliyun.alink.linksdk.tmp.device.c.b;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager;
import com.aliyun.alink.linksdk.tmp.network.BluetoothStateMgr;
import com.aliyun.alink.linksdk.tmp.network.NetConnected;
import com.aliyun.alink.linksdk.tmp.service.CloudChannelFactoryImpl;
import com.aliyun.alink.linksdk.tmp.service.auth.CommonAuthProvider;
import com.aliyun.alink.linksdk.tmp.service.wifiprovision.ComboReProvision;
import com.aliyun.alink.linksdk.tmp.storage.JSQueryApiGateProvider;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.breeze.api.Config;
import com.aliyun.iot.breeze.api.Factory;
import com.aliyun.iot.breeze.api.IBreeze;
import com.aliyun.iot.breeze.lpbs.Plugin;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class TmpSdk {
    public static final String KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS = "KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS";
    private static final String TAG = "[Tmp]TmpSdk";
    private static ICloudProxy mCloudProxy;
    private static TmpInitConfig mConfig;
    private static Context mContext;
    public static Handler mHandler;
    private static Map<String, Object> tmpAbilityConfigMap = new HashMap();

    public static void init(Context context, TmpInitConfig tmpInitConfig) {
        mContext = context;
        mConfig = tmpInitConfig;
        TmpInitConfig tmpInitConfig2 = mConfig;
        if (tmpInitConfig2 == null || tmpInitConfig2.mCloudProxy == null) {
            mCloudProxy = new DefaultCloudProxyImpl();
        } else {
            mCloudProxy = tmpInitConfig.mCloudProxy;
        }
        AlcsCoAPSdk.init();
        initLpbs(tmpInitConfig);
        ConnectSDK.getInstance().init(mContext);
        TmpStorage.getInstance().init(context);
        f.a();
        mHandler = new Handler(Looper.getMainLooper());
        b.a(null);
        com.aliyun.alink.linksdk.tmp.device.c.a.a().b();
        NetConnected.init(context);
        BluetoothStateMgr.init(context);
        new com.aliyun.alink.linksdk.tmp.device.b.b().a();
        new ComboReProvision().start();
        ALog.d(TAG, "init context: " + context + " mConfig:" + mConfig + " mCloudProxy:" + mCloudProxy);
        try {
            DeviceManager.getInstance().startNotifyMonitor();
            MeshManager.getInstance().initMeshManager();
        } catch (Throwable unused) {
            ALog.e(TAG, "");
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static TmpInitConfig getConfig() {
        return mConfig;
    }

    public static DeviceManager getDeviceManager() {
        return DeviceManager.getInstance();
    }

    public static ICloudProxy getCloudProxy() {
        return mCloudProxy;
    }

    public static void initLpbs(TmpInitConfig tmpInitConfig) {
        try {
            PluginMgrConfig pluginMgrConfig = new PluginMgrConfig();
            pluginMgrConfig.context = mContext;
            pluginMgrConfig.devInfoTrans = DefaultDevInfoTrans.getInstance();
            pluginMgrConfig.jsProvider = new JSQueryApiGateProvider();
            pluginMgrConfig.jsEngine = RhinoJsEngine.getInstance();
            pluginMgrConfig.cloudChannelFactory = new CloudChannelFactoryImpl();
            pluginMgrConfig.initData = new PalInitData(3);
            pluginMgrConfig.lpbsCloudProxy = new LpbsCloudProxyImpl();
            pluginMgrConfig.authProvider = new CommonAuthProvider();
            if (tmpInitConfig != null) {
                pluginMgrConfig.initData.deviceInfo = tmpInitConfig.mIcaPalDeviceInfo;
            }
            List<IPlugin> arrayList = tmpInitConfig == null ? null : tmpInitConfig.mLpbsPluginList;
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            try {
                try {
                    try {
                        Class<?> cls = Class.forName("com.aliyun.iot.breeze.lpbs.Plugin");
                        if (cls != null) {
                            Plugin plugin = (Plugin) cls.newInstance();
                            if (plugin != null) {
                                arrayList.add(plugin);
                                Config configBuild = new Config.Builder().debug(true).log(true).logLevel(2).build();
                                IBreeze iBreezeCreateBreeze = Factory.createBreeze(mContext);
                                iBreezeCreateBreeze.configure(configBuild);
                                MixBleDelegate.getInstance().init(mContext, iBreezeCreateBreeze);
                            } else {
                                ALog.w(TAG, "(Plugin)pluginClass.newInstance() null");
                            }
                        }
                    } catch (NoClassDefFoundError e) {
                        ALog.w(TAG, "breezePlugin noClassDefFoundError:" + e.toString());
                    }
                } catch (Throwable th) {
                    ALog.w(TAG, "breezePlugin throwable:" + th.toString());
                }
            } catch (ClassNotFoundException e2) {
                ALog.w(TAG, "breezePlugin ClassNotFoundException:" + e2.toString());
            } catch (Exception e3) {
                ALog.w(TAG, "breezePlugin Exception:" + e3.toString());
            }
            try {
                try {
                    Class<?> cls2 = Class.forName("com.aliyun.alink.linksdk.lpbs.lpbstgmesh.Plugin");
                    if (cls2 != null) {
                        com.aliyun.alink.linksdk.lpbs.lpbstgmesh.Plugin plugin2 = (com.aliyun.alink.linksdk.lpbs.lpbstgmesh.Plugin) cls2.newInstance();
                        if (plugin2 != null) {
                            arrayList.add(plugin2);
                        } else {
                            ALog.w(TAG, "(Plugin)pluginClass.newInstance() null");
                        }
                    }
                } catch (Throwable th2) {
                    ALog.w(TAG, "tgMeshPlugin throwable:" + th2.toString());
                }
            } catch (Exception e4) {
                ALog.w(TAG, "tgMeshPlugin Exception:" + e4.toString());
            }
            AlcsPalSdk.init(mContext, pluginMgrConfig, arrayList);
        } catch (Throwable th3) {
            ALog.w(TAG, "throwable2:" + th3.toString());
        }
    }

    public static Object getTmpAbilityConfigValue(String str) {
        return tmpAbilityConfigMap.get(str);
    }

    public static void setTmpAbilityConfigMap(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        tmpAbilityConfigMap.put(str, obj);
    }
}
