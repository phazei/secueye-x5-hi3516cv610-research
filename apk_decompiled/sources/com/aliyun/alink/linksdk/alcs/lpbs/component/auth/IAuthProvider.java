package com.aliyun.alink.linksdk.alcs.lpbs.component.auth;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface IAuthProvider {
    public static final String AUTH_PATH_PRODUCT_IDTOKEY = "productIdToProductKey";

    void queryAuthInfo(PalDeviceInfo palDeviceInfo, String str, Object obj, IAuthProviderListener iAuthProviderListener);
}
