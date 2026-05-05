package com.aliyun.iotx.linkvisual.media.video.beans;

/* JADX INFO: loaded from: classes2.dex */
public enum StreamConnectType {
    LOCAL(0),
    SRFLX(1),
    RELAY(2);

    private final int value;

    StreamConnectType(int i) {
        this.value = i;
    }

    public static StreamConnectType parseInt(int i) {
        return i != 0 ? i != 1 ? RELAY : SRFLX : LOCAL;
    }

    public int getValue() {
        return this.value;
    }
}
