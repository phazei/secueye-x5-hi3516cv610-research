package com.aliyun.alink.linksdk.tmp.connect;

import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;

/* JADX INFO: compiled from: ConnectWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class b implements IConnect {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected TmpEnum.ConnectType f4240a = TmpEnum.ConnectType.CONNECT_TYPE_UNKNOWN;

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, int i, Object obj) {
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public TmpEnum.ConnectType a() {
        return this.f4240a;
    }
}
