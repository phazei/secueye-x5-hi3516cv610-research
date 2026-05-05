package com.aliyun.iot.aep.sdk.bridge.validator;

/* JADX INFO: loaded from: classes2.dex */
public interface BoneValidateListener {
    void onAuthorized();

    void onPermissionDie(String str, String str2, String str3);
}
