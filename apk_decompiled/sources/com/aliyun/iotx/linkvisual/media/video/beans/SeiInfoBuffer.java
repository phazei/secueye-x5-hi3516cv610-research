package com.aliyun.iotx.linkvisual.media.video.beans;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class SeiInfoBuffer {
    public static final String TAG = "linksdk_lv_SeiInfoBuffer";
    public int length;
    public ByteBuffer seiDirectBuffer;
    public long timeStamp;

    public SeiInfoBuffer(int i) {
        this.seiDirectBuffer = ByteBuffer.allocateDirect(i);
    }
}
