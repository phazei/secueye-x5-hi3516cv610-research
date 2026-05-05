package com.aliyun.iotx.linkvisual.media.audio.listener;

import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomException;

/* JADX INFO: loaded from: classes2.dex */
public interface LiveIntercomV2Listener {
    void onError(LiveIntercomException liveIntercomException);

    void onRecordBufferReceived(byte[] bArr, int i, int i2);

    void onRecordEnd();

    void onRecordStart();

    void onTalkReady();
}
