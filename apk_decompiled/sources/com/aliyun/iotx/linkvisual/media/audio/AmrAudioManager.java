package com.aliyun.iotx.linkvisual.media.audio;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener;
import com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord;
import com.aliyun.iotx.linkvisual.media.audio.utils.AudioUtils;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import java.io.File;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class AmrAudioManager {
    public static final String TAG = "AmrAudioManager";

    /* JADX INFO: renamed from: lvthis, reason: collision with root package name */
    private static AmrAudioManager f4966lvthis;

    /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
    private AmrPlayBackListener f4967lvbyte;

    /* JADX INFO: renamed from: lvcase, reason: collision with root package name */
    private Handler f4968lvcase;

    /* JADX INFO: renamed from: lvchar, reason: collision with root package name */
    private long f4969lvchar;

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private SimpleAudioRecord f4970lvdo;

    /* JADX INFO: renamed from: lvgoto, reason: collision with root package name */
    private int f4972lvgoto;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private AudioTrack f4973lvif;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private ByteBuffer f4971lvfor = ByteBuffer.allocateDirect(8192);

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private int f4974lvint = 0;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private final byte[] f4976lvnew = new byte[0];

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private final byte[] f4977lvtry = new byte[0];

    /* JADX INFO: renamed from: lvlong, reason: collision with root package name */
    protected final ILvStreamCallback f4975lvlong = new lvif();

    public interface AmrPlayBackListener {
        void onCompletion();
    }

    public interface AmrRecordListener {
        void onError(int i, String str);

        void onRecordEnd();

        void onRecordStart();

        void onRecordVolume(int i);
    }

    class lvdo implements AudioRecordListener {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ AudioParams f4978lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        final /* synthetic */ AmrRecordListener f4979lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ File f4980lvif;

        lvdo(AudioParams audioParams, File file, AmrRecordListener amrRecordListener) {
            this.f4978lvdo = audioParams;
            this.f4980lvif = file;
            this.f4979lvfor = amrRecordListener;
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onBufferReceived(byte[] bArr, int i, int i2) {
            LinkVisual.native_write_pcm_to_amr(AmrAudioManager.this.f4969lvchar, bArr, i2);
            AmrRecordListener amrRecordListener = this.f4979lvfor;
            if (amrRecordListener != null) {
                amrRecordListener.onRecordVolume(0);
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onError(int i, String str) {
            LinkVisual.native_stop_writer(AmrAudioManager.this.f4969lvchar);
            AmrAudioManager.this.f4969lvchar = 0L;
            AmrAudioManager.this.stopRecorder();
            AmrRecordListener amrRecordListener = this.f4979lvfor;
            if (amrRecordListener != null) {
                amrRecordListener.onError(i, str);
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onRecordEnd() {
            LinkVisual.native_stop_writer(AmrAudioManager.this.f4969lvchar);
            AmrAudioManager.this.f4969lvchar = 0L;
            AmrAudioManager.this.stopRecorder();
            AmrRecordListener amrRecordListener = this.f4979lvfor;
            if (amrRecordListener != null) {
                amrRecordListener.onRecordEnd();
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onRecordStart() {
            AmrAudioManager.this.f4969lvchar = LinkVisual.native_start_amr_writer(this.f4978lvdo.getSampleRate(), this.f4978lvdo.getChannelCount(), this.f4978lvdo.getBitsPerSample(), this.f4980lvif.getAbsolutePath());
            AmrRecordListener amrRecordListener = this.f4979lvfor;
            if (amrRecordListener != null) {
                amrRecordListener.onRecordStart();
            }
        }
    }

    static /* synthetic */ class lvfor {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f4982lvdo;

        static {
            int[] iArr = new int[lvnew.lvfor.values().length];
            f4982lvdo = iArr;
            try {
                iArr[lvnew.lvfor.EVENT_VOD_COMPLETE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    class lvif implements ILvStreamCallback {

        class lvdo implements Runnable {
            lvdo() {
            }

            @Override // java.lang.Runnable
            public void run() {
                AmrAudioManager.this.stopPlay();
                if (AmrAudioManager.this.f4967lvbyte != null) {
                    AmrAudioManager.this.f4967lvbyte.onCompletion();
                }
            }
        }

        lvif() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioDataReceived(int i, int i2) {
            ALog.d(AmrAudioManager.TAG, " onAudioDataReceived: playHandle=" + i + "\t size:" + i2);
            if (AmrAudioManager.this.f4972lvgoto == i) {
                synchronized (AmrAudioManager.this.f4976lvnew) {
                    if (AmrAudioManager.this.f4973lvif != null) {
                        byte[] bArr = new byte[i2];
                        AmrAudioManager.this.f4971lvfor.get(bArr);
                        AmrAudioManager.this.f4973lvif.write(bArr, 0, i2);
                        AmrAudioManager.this.f4971lvfor.clear();
                    }
                }
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioParamsReceived(int i, int i2, int i3, int i4, int i5) {
            AudioParams audioParams = new AudioParams(i2, i3, i5);
            audioParams.setBitsPerSample(i4);
            ALog.d(AmrAudioManager.TAG, "onAudioParamsReceived: playHandle=" + i + "\t audioParams=" + audioParams.toString());
            if (AmrAudioManager.this.f4972lvgoto == i) {
                synchronized (AmrAudioManager.this.f4976lvnew) {
                    try {
                        AmrAudioManager.this.f4973lvif = new AudioTrack(3, audioParams.getSampleRate(), AudioUtils.getChannelOutConfig(audioParams.getChannelCount()), audioParams.getAudioEncoding(), AudioUtils.calculateAudioTrackBuffer(audioParams), 1);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        AmrAudioManager.this.f4973lvif = null;
                    }
                    if (AmrAudioManager.this.f4973lvif != null) {
                        AmrAudioManager.this.f4973lvif.play();
                    }
                }
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onEvent(int i, int i2, String str) {
            if (lvfor.f4982lvdo[lvnew.lvfor.lvdo(i2).ordinal()] != 1) {
                return;
            }
            ALog.d(AmrAudioManager.TAG, "amr EVENT_VOD_COMPLETE");
            AmrAudioManager.this.f4968lvcase.post(new lvdo());
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onSeiInfoUpdate(int i, int i2, long j) {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onVideoFrameUpdate(int i, int i2, int i3) {
        }
    }

    private AmrAudioManager() {
        this.f4968lvcase = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
    }

    public static synchronized AmrAudioManager getInstance() {
        if (f4966lvthis == null) {
            LinkVisual.set_log_level(ALog.getLevel());
            f4966lvthis = new AmrAudioManager();
        }
        return f4966lvthis;
    }

    public boolean startPlay(File file, AmrPlayBackListener amrPlayBackListener) {
        StringBuilder sb;
        String absolutePath;
        if (file == null) {
            throw new IllegalArgumentException("amrFile is null.");
        }
        if (file.canRead()) {
            synchronized (this.f4977lvtry) {
                if (this.f4974lvint != 0) {
                    ALog.e(TAG, "ignore startPlay due to invalid state= " + this.f4974lvint);
                    return false;
                }
                this.f4974lvint = 2;
                this.f4967lvbyte = amrPlayBackListener;
                String absolutePath2 = file.getAbsolutePath();
                ILvStreamCallback iLvStreamCallback = this.f4975lvlong;
                ByteBuffer byteBuffer = this.f4971lvfor;
                int iNative_open_amr = LinkVisual.native_open_amr(absolutePath2, true, iLvStreamCallback, byteBuffer, byteBuffer.capacity());
                this.f4972lvgoto = iNative_open_amr;
                if (iNative_open_amr > 0) {
                    return true;
                }
                sb = new StringBuilder();
                sb.append("open amr failed. ");
                absolutePath = file.getAbsolutePath();
            }
        } else {
            sb = new StringBuilder();
            sb.append(file.getAbsolutePath());
            absolutePath = " is not readable.";
        }
        sb.append(absolutePath);
        ALog.e(TAG, sb.toString());
        return false;
    }

    public boolean startPlay(String str, AmrPlayBackListener amrPlayBackListener) {
        synchronized (this.f4977lvtry) {
            if (this.f4974lvint != 0) {
                ALog.e(TAG, "ignore startPlay due to invalid state= " + this.f4974lvint);
                return false;
            }
            this.f4974lvint = 2;
            this.f4967lvbyte = amrPlayBackListener;
            ILvStreamCallback iLvStreamCallback = this.f4975lvlong;
            ByteBuffer byteBuffer = this.f4971lvfor;
            int iNative_open_amr = LinkVisual.native_open_amr(str, false, iLvStreamCallback, byteBuffer, byteBuffer.capacity());
            this.f4972lvgoto = iNative_open_amr;
            if (iNative_open_amr > 0) {
                return true;
            }
            ALog.e(TAG, "open amr failed. " + str);
            return false;
        }
    }

    public void startRecorder(AudioParams audioParams, File file, AmrRecordListener amrRecordListener) {
        if (!AudioParams.AUDIOPARAM_MONO_8K_AMRNB.equals(audioParams)) {
            throw new IllegalArgumentException("AudioParams Only support AudioParams.AUDIOPARAM_MONO_8K_AMR");
        }
        if (file == null) {
            throw new IllegalArgumentException("amrFile is null.");
        }
        synchronized (this.f4977lvtry) {
            if (this.f4974lvint != 0) {
                ALog.e(TAG, "ignore startRecord due to invalid state= " + this.f4974lvint);
                return;
            }
            this.f4974lvint = 1;
            SimpleAudioRecord simpleAudioRecord = new SimpleAudioRecord(1, audioParams);
            this.f4970lvdo = simpleAudioRecord;
            simpleAudioRecord.setAudioRecordListener(new lvdo(audioParams, file, amrRecordListener));
            this.f4970lvdo.start();
        }
    }

    public void stopPlay() {
        synchronized (this.f4977lvtry) {
            if (this.f4974lvint == 2) {
                this.f4974lvint = 0;
            }
        }
        LinkVisual.close_stream(this.f4972lvgoto);
        this.f4972lvgoto = -1;
        synchronized (this.f4976lvnew) {
            AudioTrack audioTrack = this.f4973lvif;
            if (audioTrack != null) {
                audioTrack.release();
                this.f4973lvif = null;
            }
        }
    }

    public void stopRecorder() {
        synchronized (this.f4977lvtry) {
            if (this.f4974lvint == 1) {
                this.f4974lvint = 0;
            }
        }
        SimpleAudioRecord simpleAudioRecord = this.f4970lvdo;
        if (simpleAudioRecord != null) {
            simpleAudioRecord.release();
            this.f4970lvdo = null;
        }
    }
}
