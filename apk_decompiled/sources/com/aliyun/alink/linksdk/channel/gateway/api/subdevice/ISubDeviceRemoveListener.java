package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface ISubDeviceRemoveListener {
    void onFailed(AError aError);

    void onSuceess();
}
