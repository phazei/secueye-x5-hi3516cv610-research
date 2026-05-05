package com.aliyun.iotx.linkvisual.media.video.beans;

/* JADX INFO: loaded from: classes2.dex */
public class PreConnectType {
    public static final int NONE = 0;
    public static final int P2P = 1;
    public static final int RTMP = 2;
    public int type;

    public PreConnectType() {
        this.type = 0;
    }

    public PreConnectType(int i) {
        this.type = 0;
        this.type = i;
    }

    public int getType() {
        return this.type;
    }

    public PreConnectType setType(int i) {
        this.type = i;
        return this;
    }
}
