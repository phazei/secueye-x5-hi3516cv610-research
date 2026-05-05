package com.aliyun.alink.linksdk.tmp.device.a.a;

import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: compiled from: GetSupportedNetTypeTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends com.aliyun.alink.linksdk.tmp.device.a.d<b> {
    protected String n;

    public b(String str, IDevListener iDevListener) {
        super(null, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        DeviceShadowMgr.getInstance().updateDeviceNetTypesSupportedByPk(this.n, true, new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.b.1
            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onSuccess(Object obj) {
                b.this.a((Object) null, (Object) null);
            }

            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onFail(ErrorInfo errorInfo) {
                b.this.a((Object) null, (Object) null);
            }
        });
        return true;
    }
}
