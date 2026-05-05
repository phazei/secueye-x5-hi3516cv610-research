package com.aliyun.alink.linksdk.alcs.lpbs.component.cloud;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IThingCloudChannel {

    public interface IChannelActionListener {
        void onFailed(AError aError);

        void onSuccess();
    }

    void addDownDataListener(IDataDownListener iDataDownListener);

    void removeDownDataListener(IDataDownListener iDataDownListener);

    void reportData(String str, Object obj, IChannelActionListener iChannelActionListener);

    void reportData(String str, byte[] bArr);
}
