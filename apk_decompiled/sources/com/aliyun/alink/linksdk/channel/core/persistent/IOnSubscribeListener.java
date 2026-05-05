package com.aliyun.alink.linksdk.channel.core.persistent;

import com.aliyun.alink.linksdk.channel.core.base.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IOnSubscribeListener {
    boolean needUISafety();

    void onFailed(String str, AError aError);

    void onSuccess(String str);
}
