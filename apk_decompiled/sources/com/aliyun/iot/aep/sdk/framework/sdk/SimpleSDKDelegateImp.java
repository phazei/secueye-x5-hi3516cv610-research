package com.aliyun.iot.aep.sdk.framework.sdk;

import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SimpleSDKDelegateImp implements ISDKDelegate {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public boolean canBeenInitialized(Map<String, String> map) {
        return true;
    }
}
