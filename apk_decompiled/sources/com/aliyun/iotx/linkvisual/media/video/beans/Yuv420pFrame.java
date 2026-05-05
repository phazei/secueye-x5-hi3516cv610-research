package com.aliyun.iotx.linkvisual.media.video.beans;

import com.aliyun.alink.linksdk.tools.ALog;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class Yuv420pFrame {
    public static final String TAG = "linksdk_lv_Yuv420pFrame";
    public int height;
    public int width;

    @Deprecated
    public ByteBuffer yuv420pFrameDirectBuffer;

    public Yuv420pFrame(int i, int i2) {
        ALog.d(TAG, "Yuv420pFrame: width=" + i + ", height=" + i2);
        this.width = i;
        this.height = i2;
        this.yuv420pFrameDirectBuffer = ByteBuffer.allocateDirect(((i * i2) * 3) / 2);
    }

    public synchronized ByteBuffer getDirectBuffer() {
        return this.yuv420pFrameDirectBuffer;
    }

    public synchronized void relocateDirectBuffer(int i, int i2) {
        int i3 = i * i2;
        if (this.width * this.height < i3) {
            ALog.d(TAG, "relocate Yuv420pFrame: width=" + i + ", height=" + i2);
            this.yuv420pFrameDirectBuffer = ByteBuffer.allocateDirect((i3 * 3) / 2);
        }
        this.width = i;
        this.height = i2;
    }
}
