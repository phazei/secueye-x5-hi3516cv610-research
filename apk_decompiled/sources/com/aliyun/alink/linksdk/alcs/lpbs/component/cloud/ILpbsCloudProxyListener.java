package com.aliyun.alink.linksdk.alcs.lpbs.component.cloud;

/* JADX INFO: loaded from: classes2.dex */
public interface ILpbsCloudProxyListener {
    public static final int ERROR_CODE_FAILD_UNKNOWN = 1;
    public static final int ERROR_CODE_SUCCESS = 0;

    void onComplete(int i, Object obj);
}
