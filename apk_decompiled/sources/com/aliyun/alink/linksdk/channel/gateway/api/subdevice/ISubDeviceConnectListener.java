package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tools.AError;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public interface ISubDeviceConnectListener {
    String getClientId();

    Map<String, Object> getSignExtraData();

    String getSignMethod();

    String getSignValue();

    void onConnectResult(boolean z, ISubDeviceChannel iSubDeviceChannel, AError aError);

    void onDataPush(String str, AMessage aMessage);
}
