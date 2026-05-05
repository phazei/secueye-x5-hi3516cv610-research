package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import android.content.Context;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ILpbsCloudProxy;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.PluginConfig;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class Bridge implements IPalBridge {
    private static final String TAG = Utils.TAG + "Bridge";
    private Connect mConnect;
    private Context mContext;
    private ILpbsCloudProxy mLpbsCloudProxy;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public void deInitBridge() {
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalGroupConnect getGroupConnect(PalGroupInfo palGroupInfo) {
        return null;
    }

    public Bridge(PluginConfig pluginConfig) {
        this.mContext = pluginConfig == null ? null : pluginConfig.context;
        this.mLpbsCloudProxy = pluginConfig != null ? pluginConfig.lpbsCloudProxy : null;
        ALog.d(TAG, "Bridge mLpbsCloudProxy:" + this.mLpbsCloudProxy + " mContext:" + this.mContext);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public void initBridge(PalInitData palInitData) {
        ALog.d(TAG, "initBridge palInitData:" + palInitData);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalDiscovery getPalDiscovery() {
        return new Discovery();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalConnect getPalConnect(PalDeviceInfo palDeviceInfo) {
        if (this.mConnect == null) {
            this.mConnect = new Connect(this.mContext, palDeviceInfo, this.mLpbsCloudProxy);
        }
        return this.mConnect;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalAuthRegister getPalAuthRegister() {
        return new AuthRegister();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalProbe getPalProbe() {
        return new Probe();
    }
}
