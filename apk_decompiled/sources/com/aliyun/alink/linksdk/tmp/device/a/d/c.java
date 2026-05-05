package com.aliyun.alink.linksdk.tmp.device.a.d;

import com.aliyun.alink.linksdk.cmp.manager.connect.IRegisterConnectListener;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CreateMqttConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends com.aliyun.alink.linksdk.tmp.device.a.d<c> {
    protected static final String n = "[Tmp]CreateMqttConnectTask";

    public c(DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(null, iDevListener);
        a(deviceConfig);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        if (this.m != null && (this.m instanceof DefaultServerConfig)) {
            ALog.d(n, "action mConfig");
            DefaultServerConfig defaultServerConfig = (DefaultServerConfig) this.m;
            CloudUtils.registerPersistentConnect(defaultServerConfig.mIotProductKey, defaultServerConfig.mIotDeviceName, defaultServerConfig.mIotSecret, new IRegisterConnectListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.c.1
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                public void onSuccess() {
                    ALog.d(c.n, "action onSuccess");
                    c.this.a((Object) null, (Object) null);
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                public void onFailure(AError aError) {
                    ALog.e(c.n, "action onFailure aError:" + aError);
                    c.this.a((Object) null, (Object) null);
                }
            });
            return true;
        }
        ALog.d(n, "action mConfig null or not server return ");
        a((Object) null, (Object) null);
        return true;
    }
}
