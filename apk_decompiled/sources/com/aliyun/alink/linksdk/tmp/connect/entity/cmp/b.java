package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener;
import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryMessage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CmpReDiscoveryHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends com.aliyun.alink.linksdk.tmp.connect.f implements IDiscoveryListener {
    public b(com.aliyun.alink.linksdk.tmp.connect.c cVar, com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        super(cVar, dVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener
    public void onDiscovery(DiscoveryMessage discoveryMessage) {
        AResponse aResponse = new AResponse();
        aResponse.data = discoveryMessage;
        a(this.f4276d, new i(aResponse));
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IDiscoveryListener
    public void onFailure(AError aError) {
        a(this.f4276d, new ErrorInfo(aError.getCode(), aError.getMsg()));
    }
}
