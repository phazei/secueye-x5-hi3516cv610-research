package com.aliyun.alink.linksdk.tmp.api;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TmpInitConfig {
    public static final int DAILY = 0;
    public static final int ONLINE = 2;
    public static final int PRE = 1;
    private static final String TAG = "[Tmp]TmpInitConfig";
    public ICloudProxy mCloudProxy;
    public int mEnv;
    public PalDeviceInfo mIcaPalDeviceInfo;
    public boolean mIsCheckChannelRootCrt;
    public List<IPlugin> mLpbsPluginList;
    public String mMqttChannelHost;

    public TmpInitConfig() {
        this(null, true);
    }

    public TmpInitConfig(String str, boolean z) {
        this.mMqttChannelHost = str;
        this.mIsCheckChannelRootCrt = z;
        ALog.d(TAG, "TmpInitConfig mMqttChannelHost:" + this.mMqttChannelHost);
        ALog.d(TAG, "TmpInitConfig mIsCheckChannelRootCrt:" + this.mIsCheckChannelRootCrt);
    }

    public TmpInitConfig(int i) {
        this.mEnv = i;
        switch (i) {
            case 0:
                this.mMqttChannelHost = "ssl://10.125.0.27:1883";
                this.mIsCheckChannelRootCrt = false;
                break;
            case 1:
                this.mMqttChannelHost = "ssl://100.67.80.75:80";
                this.mIsCheckChannelRootCrt = true;
                break;
            case 2:
                this.mMqttChannelHost = null;
                this.mIsCheckChannelRootCrt = true;
                break;
        }
        ALog.d(TAG, "TmpInitConfig mMqttChannelHost:" + this.mMqttChannelHost);
        ALog.d(TAG, "TmpInitConfig mIsCheckChannelRootCrt:" + this.mIsCheckChannelRootCrt);
    }
}
