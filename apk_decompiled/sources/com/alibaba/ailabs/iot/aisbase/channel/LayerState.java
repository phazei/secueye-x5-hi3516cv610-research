package com.alibaba.ailabs.iot.aisbase.channel;

/* JADX INFO: loaded from: classes.dex */
public enum LayerState {
    NONE,
    BT_OPEN,
    BT_CLOSED,
    UNBIND,
    BINDING,
    BOUND,
    CONNECTING,
    CONNECTED,
    AUTH_FAILED,
    AUTH_SUCCESSFUL,
    DISCONNECTING,
    DISCONNECTED,
    A2DP_CONNECTING,
    A2DP_CONNECTED,
    A2DP_DISCONNECTING,
    A2DP_DISCONNECTED;

    public static LayerState parserFromIntValue(int i) {
        switch (i) {
            case 0:
                return DISCONNECTED;
            case 1:
                return CONNECTING;
            case 2:
                return CONNECTED;
            case 3:
                return DISCONNECTING;
            default:
                return NONE;
        }
    }
}
