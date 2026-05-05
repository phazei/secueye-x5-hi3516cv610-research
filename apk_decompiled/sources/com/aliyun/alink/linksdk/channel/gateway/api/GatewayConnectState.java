package com.aliyun.alink.linksdk.channel.gateway.api;

import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;

/* JADX INFO: loaded from: classes2.dex */
public enum GatewayConnectState {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    CONNECTFAIL;

    public static GatewayConnectState toGatewayConnectState(ConnectState connectState) {
        if (connectState == ConnectState.CONNECTED) {
            return CONNECTED;
        }
        if (connectState == ConnectState.DISCONNECTED) {
            return DISCONNECTED;
        }
        if (connectState == ConnectState.CONNECTING) {
            return CONNECTING;
        }
        return CONNECTFAIL;
    }
}
