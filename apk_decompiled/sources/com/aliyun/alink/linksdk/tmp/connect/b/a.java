package com.aliyun.alink.linksdk.tmp.connect.b;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginHelper;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupReqMessage;
import com.aliyun.alink.linksdk.tmp.connect.c;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;

/* JADX INFO: compiled from: GroupConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4241c = "[Tmp]GroupConnect";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected AccessInfo f4242a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4243b;

    public a(String str, AccessInfo accessInfo) {
        this.f4242a = accessInfo;
        this.f4243b = str;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.b.b
    public boolean a(d dVar, c cVar) {
        return PluginHelper.asyncGroupSendRequest((PalGroupReqMessage) dVar.c(), new com.aliyun.alink.linksdk.tmp.connect.entity.a.c(cVar, dVar));
    }
}
