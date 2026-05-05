package com.aliyun.alink.linksdk.alcs.lpbs.bridge;

import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalConnectParams;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IPalConnect {
    public static final int CONNECT_TYPE_BREEZE = 4;
    public static final int CONNECT_TYPE_ICA = 1;
    public static final int CONNECT_TYPE_TGMESH = 5;
    public static final int CONNECT_TYPE_UNKNOW = 0;

    boolean asyncSendRequest(PalReqMessage palReqMessage, PalMsgListener palMsgListener);

    int getConnectType(PalDeviceInfo palDeviceInfo);

    String getPluginId();

    boolean isDeviceConnected(PalDeviceInfo palDeviceInfo);

    void onCloudChannelCreate(IThingCloudChannel iThingCloudChannel);

    boolean regDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener);

    void startConnect(PalConnectParams palConnectParams, PalConnectListener palConnectListener);

    void stopConnect(PalDeviceInfo palDeviceInfo);

    boolean subscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener, PalMsgListener palMsgListener2);

    boolean unregDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener);

    boolean unsubscribe(PalSubMessage palSubMessage, PalMsgListener palMsgListener);
}
