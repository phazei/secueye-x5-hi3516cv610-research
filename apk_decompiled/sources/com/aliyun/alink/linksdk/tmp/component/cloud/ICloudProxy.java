package com.aliyun.alink.linksdk.tmp.component.cloud;

/* JADX INFO: loaded from: classes2.dex */
public interface ICloudProxy {
    void queryAccessInfo(String str, ICloudProxyListener iCloudProxyListener);

    void queryDevNameFromDevId(String str, ICloudProxyListener iCloudProxyListener);

    void queryDeviceDetail(String str, ICloudProxyListener iCloudProxyListener);

    void queryProdtKeyFromProdtId(String str, ICloudProxyListener iCloudProxyListener);

    void queryProductInfo(String str, ICloudProxyListener iCloudProxyListener);

    void queryProtocolScript(String str, String str2, String str3, ICloudProxyListener iCloudProxyListener);

    void queryTslData(String str, ICloudProxyListener iCloudProxyListener);
}
