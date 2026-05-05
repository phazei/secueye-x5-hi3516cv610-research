package com.aliyun.alink.linksdk.tmp.device.a.d;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.TDeviceShadow;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: ShadowInitTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class i extends com.aliyun.alink.linksdk.tmp.device.a.d<g> implements IDevListener {
    protected WeakReference<TDeviceShadow> n;

    public i(TDeviceShadow tDeviceShadow, IDevListener iDevListener) {
        super(null, iDevListener);
        this.n = new WeakReference<>(tDeviceShadow);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        TDeviceShadow tDeviceShadow = this.n.get();
        if (tDeviceShadow != null) {
            tDeviceShadow.init(this);
            return true;
        }
        onFail(null, new ErrorInfo(300, "param is invalid"));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        a((Object) null, (Object) null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        b((Object) null, errorInfo);
    }
}
