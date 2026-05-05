package com.aliyun.iotx.linkvisual.media.audio;

import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioBufferReceiveListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioParamsChangeListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnTalkReadyListener;

/* JADX INFO: loaded from: classes2.dex */
public interface ILiveIntercom {
    void sendAudioBuffer(byte[] bArr, int i, int i2);

    void setOnAudioBufferReceiveListener(OnAudioBufferReceiveListener onAudioBufferReceiveListener);

    void setOnAudioParamsChangeListener(OnAudioParamsChangeListener onAudioParamsChangeListener);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnTalkReadyListener(OnTalkReadyListener onTalkReadyListener);

    void start(String str, AudioParams audioParams);

    void start(String str, byte[] bArr, byte[] bArr2, AudioParams audioParams);

    void stop();
}
