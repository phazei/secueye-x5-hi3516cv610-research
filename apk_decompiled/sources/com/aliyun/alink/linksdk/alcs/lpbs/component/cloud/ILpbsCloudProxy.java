package com.aliyun.alink.linksdk.alcs.lpbs.component.cloud;

/* JADX INFO: loaded from: classes2.dex */
public interface ILpbsCloudProxy {
    public static final String TOPIC_CONVERT_TGMESH_PARAMS = "/living/device/bt/protocol/convert";

    void requestCloud(String str, Object obj, ILpbsCloudProxyListener iLpbsCloudProxyListener);
}
