package com.aliyun.alink.linksdk.tmp.device;

import com.aliyun.alink.linksdk.tmp.config.GroupConfig;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a.c;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;

/* JADX INFO: compiled from: GroupImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4376a = "[Tmp]GroupImpl";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private GroupConfig f4377b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private com.aliyun.alink.linksdk.tmp.connect.b.b f4378c;

    public b(GroupConfig groupConfig) {
        this.f4377b = groupConfig;
    }

    public GroupConfig a() {
        return this.f4377b;
    }

    public void a(GroupConfig groupConfig) {
        this.f4377b = this.f4377b;
    }

    public com.aliyun.alink.linksdk.tmp.connect.b.b b() {
        return this.f4378c;
    }

    public void a(com.aliyun.alink.linksdk.tmp.connect.b.b bVar) {
        this.f4378c = bVar;
    }

    public boolean a(String str, List<KeyValuePair> list, ExtraData extraData, IDevListener iDevListener) {
        ALog.d(f4376a, "invokeService serviceId:" + str + " args:" + list + " handler:" + iDevListener);
        com.aliyun.alink.linksdk.tmp.device.a.c.a aVar = new com.aliyun.alink.linksdk.tmp.device.a.c.a(this, iDevListener);
        return new c().b(aVar).b(new com.aliyun.alink.linksdk.tmp.device.a.c.b(this, iDevListener)).b(new com.aliyun.alink.linksdk.tmp.device.a.c.c(this, iDevListener).a(str).a(list).a(extraData)).a();
    }

    public boolean a(IDevListener iDevListener) {
        ALog.d(f4376a, "GetLocalGroupInfo handler:" + iDevListener);
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.c.b(this, iDevListener)).a();
    }
}
