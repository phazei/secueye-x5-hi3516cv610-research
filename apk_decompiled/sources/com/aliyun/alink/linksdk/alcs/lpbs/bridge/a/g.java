package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;

/* JADX INFO: compiled from: ICABridgeAuthRegister.java */
/* JADX INFO: loaded from: classes2.dex */
public class g implements IPalAuthRegister {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private IAuthProvider f4069a;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister
    public boolean setAuthProvider(IAuthProvider iAuthProvider) {
        this.f4069a = iAuthProvider;
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister
    public IAuthProvider getProvider() {
        return this.f4069a;
    }
}
