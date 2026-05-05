package com.aliyun.iotx.linkvisual.media.audio.processing;

/* JADX INFO: loaded from: classes2.dex */
public class AudioProcesser {
    static {
        System.loadLibrary("linkvisual");
    }

    public static native void destroy();

    public static native int init(int i, int i2, int i3, int i4, String str);

    public static native int process(short[] sArr, int i, short[] sArr2, short[] sArr3, int i2);

    public static native int process_farend(short[] sArr, int i);

    public static native int process_vad(short[] sArr, int i);

    public static native int reset();

    public static native int version();
}
