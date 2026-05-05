package com.aliyun.iotx.linkvisual.media.audio.record;

/* JADX INFO: loaded from: classes2.dex */
public interface AudioRecordListener {
    void onBufferReceived(byte[] bArr, int i, int i2);

    void onError(int i, String str);

    void onRecordEnd();

    void onRecordStart();
}
