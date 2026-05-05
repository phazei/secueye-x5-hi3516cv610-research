package com.aliyun.alink.linksdk.alcs.lpbs.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.a;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.j;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.ICAPlugin;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsPalSdk {
    public static Context mContext;
    public static Handler mHandler;

    public static void init(Context context, PluginMgrConfig pluginMgrConfig, List<IPlugin> list) {
        mHandler = new Handler(Looper.getMainLooper());
        mContext = context;
        PluginMgr.getInstance().initPluginMgr(pluginMgrConfig);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                PluginMgr.getInstance().addPlugin(list.get(i));
                PluginMgr.getInstance().startPlugin(list.get(i));
            }
        }
        if (PluginMgr.getInstance().getPluginByPluginId("iot_ica") == null) {
            ICAPlugin iCAPlugin = new ICAPlugin(pluginMgrConfig);
            iCAPlugin.getPalBridge().getPalAuthRegister().setAuthProvider(new j(new a(mContext)));
            PluginMgr.getInstance().addPlugin(iCAPlugin);
            PluginMgr.getInstance().startPlugin(iCAPlugin);
        }
        if (pluginMgrConfig == null) {
            PluginMgr.getInstance().initAlcs(null);
        } else {
            PluginMgr.getInstance().initAlcs(pluginMgrConfig.initData);
        }
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }
}
