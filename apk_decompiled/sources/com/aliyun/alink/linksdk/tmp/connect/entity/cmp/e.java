package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener;
import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryMessage;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CpDiscoveryHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class e implements IDiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected INotifyHandler f4258a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.d f4259b = new f(null);

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener
    public void onFailure(AError aError) {
    }

    public e(INotifyHandler iNotifyHandler) {
        this.f4258a = iNotifyHandler;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener
    public void onDiscovery(DiscoveryMessage discoveryMessage) {
        AResponse aResponse = new AResponse();
        aResponse.data = discoveryMessage;
        this.f4258a.onMessage(this.f4259b, new i(aResponse));
    }
}
