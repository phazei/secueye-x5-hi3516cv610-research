package com.aliyun.alink.linksdk.tmp.listener;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;

/* JADX INFO: loaded from: classes2.dex */
public interface IEventListener extends IDevListener {
    void onMessage(String str, String str2, OutputParams outputParams);
}
