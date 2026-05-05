package com.aliyun.alink.linksdk.channel.mobile.api;

import android.content.Context;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IMobileChannel {
    void asyncSendRequest(String str, Map<String, Object> map, Object obj, IMobileRequestListener iMobileRequestListener);

    void ayncSendPublishRequest(String str, Object obj, IMobileRequestListener iMobileRequestListener);

    void bindAccount(String str, IMobileRequestListener iMobileRequestListener);

    @Deprecated
    void endConnect();

    void endConnect(long j, IMqttActionListener iMqttActionListener);

    String getClientId();

    MobileConnectState getMobileConnectState();

    void registerConnectListener(boolean z, IMobileConnectListener iMobileConnectListener);

    void registerDownstreamListener(boolean z, IMobileDownstreamListener iMobileDownstreamListener);

    void startConnect(Context context, MobileConnectConfig mobileConnectConfig, IMobileConnectListener iMobileConnectListener);

    void subscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener);

    void unBindAccount(IMobileRequestListener iMobileRequestListener);

    void unRegisterConnectListener(IMobileConnectListener iMobileConnectListener);

    void unRegisterDownstreamListener(IMobileDownstreamListener iMobileDownstreamListener);

    void unSubscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener);
}
