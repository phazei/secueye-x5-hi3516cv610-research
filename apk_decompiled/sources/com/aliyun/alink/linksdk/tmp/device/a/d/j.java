package com.aliyun.alink.linksdk.tmp.device.a.d;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.l;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.m;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: UpdateSvrInfoTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class j extends com.aliyun.alink.linksdk.tmp.device.a.d<j> {
    protected static final String n = "[Tmp]UpdateSvrInfoTask";
    protected l o;
    protected m p;

    public j(DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener, l lVar, m mVar) {
        super(null, iDevListener);
        a(deviceBasicData);
        a(deviceConfig);
        this.o = lVar;
        this.p = mVar;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        if (this.m != null && (this.m instanceof DefaultServerConfig)) {
            ALog.d(n, "action mConfig DefaultServerConfig true");
            b();
            c();
        }
        a((Object) null, (Object) null);
        return true;
    }

    protected void b() {
        ALog.d(n, "updatePrefx start");
        CloudUtils.subPrefixUpdateRrpc(this.j.getProductKey(), this.j.getDeviceName(), this.p);
    }

    protected void c() {
        ALog.d(n, "updateBlackList start");
        CloudUtils.subBlacklistUpdateRrpc(this.j.getProductKey(), this.j.getDeviceName(), this.o);
    }
}
