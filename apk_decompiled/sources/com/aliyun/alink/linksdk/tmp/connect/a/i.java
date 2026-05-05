package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryRequest;

/* JADX INFO: compiled from: TmpReDiscoveryRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class i extends j<i, Object> {
    public static i d() {
        return new i();
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        DiscoveryRequest discoveryRequest = new DiscoveryRequest();
        discoveryRequest.productKey = f();
        discoveryRequest.deviceName = g();
        com.aliyun.alink.linksdk.tmp.connect.d dVar = new com.aliyun.alink.linksdk.tmp.connect.d(discoveryRequest);
        dVar.a(f());
        dVar.b(g());
        return dVar;
    }
}
