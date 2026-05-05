package com.aliyun.alink.linksdk.alcs.lpbs.a.b;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: ConnectMgr.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3961a = "[AlcsLPBS]ConnectMgr";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, IPalConnect> f3962b = new ConcurrentHashMap();

    public Map<String, IPalConnect> a() {
        return this.f3962b;
    }

    public void a(String str, IPalConnect iPalConnect) {
        this.f3962b.put(str, iPalConnect);
    }

    public void a(String str) {
        this.f3962b.remove(str);
    }

    public IPalConnect b(String str) {
        ALog.d(f3961a, "getConnect id: mConnectList:" + this.f3962b);
        return this.f3962b.get(str);
    }
}
