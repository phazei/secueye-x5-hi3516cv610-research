package com.aliyun.alink.linksdk.channel.core.persistent;

import android.content.Context;
import com.aliyun.alink.linksdk.channel.core.base.INet;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.PersisitentNetParams;

/* JADX INFO: loaded from: classes2.dex */
public interface IPersisitentNet extends INet {
    void destroy();

    void destroy(long j, Object obj, Object obj2);

    void dynamicRegister(Context context, PersistentInitParams persistentInitParams, IOnCallListener iOnCallListener);

    PersistentConnectState getConnectState();

    void init(Context context, PersistentInitParams persistentInitParams);

    boolean isDeiniting();

    void openLog(boolean z);

    void reconnect();

    void subscribe(String str, IOnSubscribeListener iOnSubscribeListener);

    void subscribe(String str, PersisitentNetParams persisitentNetParams, IOnSubscribeListener iOnSubscribeListener);

    void subscribeRrpc(String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener);

    void unSubscribe(String str, IOnSubscribeListener iOnSubscribeListener);
}
