package lvdo;

import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomException;

/* JADX INFO: loaded from: classes4.dex */
public interface lvdo {

    /* JADX INFO: renamed from: lvdo.lvdo$lvdo, reason: collision with other inner class name */
    public interface InterfaceC0305lvdo {
        void onConnected();

        void onData(byte[] bArr, int i);

        void onError(LiveIntercomException liveIntercomException);

        void onHeaders(AudioParams audioParams);

        void onTalkReady();
    }

    void lvdo();

    void lvdo(String str, byte[] bArr, byte[] bArr2, AudioParams audioParams, InterfaceC0305lvdo interfaceC0305lvdo);

    void lvdo(byte[] bArr, int i, int i2);
}
