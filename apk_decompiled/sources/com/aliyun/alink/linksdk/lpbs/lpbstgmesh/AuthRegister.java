package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class AuthRegister implements IPalAuthRegister {
    private static final String TAG = Utils.TAG + "AuthRegister";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister
    public boolean setAuthProvider(IAuthProvider iAuthProvider) {
        ALog.d(TAG, "setAuthProvider iAuthProvider:" + iAuthProvider);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister
    public IAuthProvider getProvider() {
        ALog.d(TAG, "getProvider empty");
        return null;
    }
}
