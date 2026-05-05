package lvdo;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomException;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import java.nio.ByteBuffer;
import lvdo.lvdo;
import lvnew.lvfor;

/* JADX INFO: loaded from: classes4.dex */
public class lvif implements lvdo.lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private AudioParams f7993lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private int f7994lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private lvdo.InterfaceC0305lvdo f7995lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private String f7996lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    protected ByteBuffer f7997lvnew = ByteBuffer.allocateDirect(8192);

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private final ILvStreamCallback f7998lvtry = new lvdo();

    class lvdo implements ILvStreamCallback {
        lvdo() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioDataReceived(int i, int i2) {
            if (lvif.this.f7994lvfor != i || lvif.this.f7995lvif == null) {
                return;
            }
            byte[] bArr = new byte[i2];
            System.arraycopy(lvif.this.f7997lvnew.array(), lvif.this.f7997lvnew.arrayOffset(), bArr, 0, i2);
            lvif.this.f7995lvif.onData(bArr, i2);
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioParamsReceived(int i, int i2, int i3, int i4, int i5) {
            if (lvif.this.f7994lvfor != i || lvif.this.f7995lvif == null) {
                return;
            }
            AudioParams audioParams = new AudioParams(i2, i3, i5);
            audioParams.setBitsPerSample(i4);
            ALog.d("linksdk_lv_RtmpVoiceChannel", "[" + lvif.this.hashCode() + "] onAudioParamsReceived: playHandle=" + i + "\t audioParams=" + audioParams.toString());
            if (audioParams.checkSupport()) {
                lvif.this.f7995lvif.onHeaders(audioParams);
            } else {
                lvif.this.lvdo(new LiveIntercomException(1, "receive invalid audio params from devices."));
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onEvent(int i, int i2, String str) {
            lvfor lvforVarLvdo;
            lvif lvifVar;
            LiveIntercomException liveIntercomException;
            if (lvif.this.f7994lvfor != i || lvif.this.f7995lvif == null || (lvforVarLvdo = lvfor.lvdo(i2)) == null) {
                return;
            }
            ALog.d("linksdk_lv_RtmpVoiceChannel", "[" + lvif.this.hashCode() + "] onEvent: " + lvforVarLvdo.name());
            switch (C0306lvif.f8000lvdo[lvforVarLvdo.ordinal()]) {
                case 1:
                    ALog.d("linksdk_lv_RtmpVoiceChannel", "[" + lvif.this.hashCode() + "] EVENT_DISCONNECT reset state to END.");
                    lvifVar = lvif.this;
                    liveIntercomException = new LiveIntercomException(3, "Stream connect failed!");
                    break;
                case 2:
                    ALog.d("linksdk_lv_RtmpVoiceChannel", "[" + lvif.this.hashCode() + "] EVENT_STREAM_ERROR reset state to END.");
                    lvifVar = lvif.this;
                    liveIntercomException = new LiveIntercomException(3, "Stream closed unexpectedly!");
                    break;
                case 3:
                    lvif.this.f7995lvif.onConnected();
                    return;
                case 4:
                    ALog.d("linksdk_lv_RtmpVoiceChannel", "EVENT_TALK_READY:" + lvif.this.f7993lvdo.toString());
                    if (LinkVisual.set_talk_format(lvif.this.f7994lvfor, lvif.this.f7993lvdo.getAudioType(), lvif.this.f7993lvdo.getBitsPerSample(), lvif.this.f7993lvdo.getSampleRate(), lvif.this.f7993lvdo.getChannelCount())) {
                        lvif.this.f7995lvif.onTalkReady();
                        return;
                    } else {
                        lvif.this.lvdo(new LiveIntercomException(5, "Audio params send failed!"));
                        return;
                    }
                default:
                    return;
            }
            lvifVar.lvdo(liveIntercomException);
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onSeiInfoUpdate(int i, int i2, long j) {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onVideoFrameUpdate(int i, int i2, int i3) {
        }
    }

    /* JADX INFO: renamed from: lvdo.lvif$lvif, reason: collision with other inner class name */
    static /* synthetic */ class C0306lvif {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f8000lvdo;

        static {
            int[] iArr = new int[lvfor.values().length];
            f8000lvdo = iArr;
            try {
                iArr[lvfor.EVENT_DISCONNECT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8000lvdo[lvfor.EVENT_STREAM_ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8000lvdo[lvfor.EVENT_RTMP_CONNECT_OK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8000lvdo[lvfor.EVENT_TALK_READY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo(LiveIntercomException liveIntercomException) {
        this.f7995lvif.onError(liveIntercomException);
    }

    private boolean lvif() {
        return this.f7994lvfor <= 0;
    }

    @Override // lvdo.lvdo
    public void lvdo() {
        LinkVisual.close_stream(this.f7994lvfor);
        ALog.i("linksdk_lv_RtmpVoiceChannel", "[" + hashCode() + "] stop: " + this.f7994lvfor + "  Url:" + this.f7996lvint);
        this.f7994lvfor = 0;
    }

    @Override // lvdo.lvdo
    public void lvdo(String str, byte[] bArr, byte[] bArr2, AudioParams audioParams, lvdo.InterfaceC0305lvdo interfaceC0305lvdo) {
        if (TextUtils.isEmpty(str)) {
            lvdo(new LiveIntercomException(2, "stream url is empty!"));
            return;
        }
        this.f7993lvdo = audioParams;
        this.f7995lvif = interfaceC0305lvdo;
        this.f7996lvint = str;
        ILvStreamCallback iLvStreamCallback = this.f7998lvtry;
        ByteBuffer byteBuffer = this.f7997lvnew;
        this.f7994lvfor = LinkVisual.open_rtmp_stream(str, 2, false, 0, true, bArr2, bArr, iLvStreamCallback, byteBuffer, byteBuffer.capacity(), null, 0);
        if (lvif()) {
            lvdo(new LiveIntercomException(3, "Stream open failed!"));
        }
        ALog.i("linksdk_lv_RtmpVoiceChannel", "[" + hashCode() + "] openstream: " + this.f7994lvfor);
    }

    @Override // lvdo.lvdo
    public void lvdo(byte[] bArr, int i, int i2) {
        if (lvif() || LinkVisual.send_talk_data(this.f7994lvfor, bArr, i, i2, System.currentTimeMillis())) {
            return;
        }
        lvdo(new LiveIntercomException(5, "Send audio data failed."));
    }
}
