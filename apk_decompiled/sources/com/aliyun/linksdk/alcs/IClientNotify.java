package com.aliyun.linksdk.alcs;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;

/* JADX INFO: loaded from: classes2.dex */
public interface IClientNotify {
    void onNotify(String str, PalRspMessage palRspMessage);

    void onServerStateChange(boolean z);
}
