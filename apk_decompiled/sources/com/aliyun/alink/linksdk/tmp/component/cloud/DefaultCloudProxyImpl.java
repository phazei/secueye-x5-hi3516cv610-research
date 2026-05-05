package com.aliyun.alink.linksdk.tmp.component.cloud;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultCloudProxyImpl implements ICloudProxy {
    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryDevNameFromDevId(String str, ICloudProxyListener iCloudProxyListener) {
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryProductInfo(final String str, final ICloudProxyListener iCloudProxyListener) {
        CloudUtils.queryProductInfo(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl.1
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                if (aResponse == null || aResponse.data == null) {
                    iCloudProxyListener.onFailure(str, null);
                } else {
                    iCloudProxyListener.onResponse(str, aResponse.data);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                iCloudProxyListener.onFailure(str, aError);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryAccessInfo(final String str, final ICloudProxyListener iCloudProxyListener) {
        CloudUtils.queryAccessInfo(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl.2
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                if (aResponse == null || aResponse.data == null) {
                    iCloudProxyListener.onFailure(str, null);
                } else {
                    iCloudProxyListener.onResponse(str, aResponse.data);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                iCloudProxyListener.onFailure(str, aError);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryTslData(final String str, final ICloudProxyListener iCloudProxyListener) {
        CloudUtils.getTsl(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl.3
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                if (aResponse == null || aResponse.data == null) {
                    iCloudProxyListener.onFailure(str, null);
                } else {
                    iCloudProxyListener.onResponse(str, aResponse.data);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                iCloudProxyListener.onFailure(str, aError);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryDeviceDetail(final String str, final ICloudProxyListener iCloudProxyListener) {
        CloudUtils.listBindingByDev(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl.4
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                if (aResponse == null || aResponse.data == null) {
                    iCloudProxyListener.onFailure(str, null);
                } else {
                    iCloudProxyListener.onResponse(str, aResponse.data);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                iCloudProxyListener.onFailure(str, aError);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryProtocolScript(final String str, String str2, String str3, final ICloudProxyListener iCloudProxyListener) {
        CloudUtils.queryProductJsCode(str, str2, str3, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.component.cloud.DefaultCloudProxyImpl.5
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                if (aResponse == null || aResponse.data == null) {
                    iCloudProxyListener.onFailure(str, null);
                } else {
                    iCloudProxyListener.onResponse(str, aResponse.data);
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                iCloudProxyListener.onFailure(str, aError);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxy
    public void queryProdtKeyFromProdtId(String str, ICloudProxyListener iCloudProxyListener) {
        CloudUtils.getProductKey(str, iCloudProxyListener);
    }
}
