package com.aliyun.iotx.linkvisual.media.video;

/* JADX INFO: loaded from: classes2.dex */
public interface ILvStreamCallback {
    void onAudioDataReceived(int i, int i2);

    void onAudioParamsReceived(int i, int i2, int i3, int i4, int i5);

    void onEvent(int i, int i2, String str);

    void onSeiInfoUpdate(int i, int i2, long j);

    void onVideoFrameUpdate(int i, int i2, int i3);
}
