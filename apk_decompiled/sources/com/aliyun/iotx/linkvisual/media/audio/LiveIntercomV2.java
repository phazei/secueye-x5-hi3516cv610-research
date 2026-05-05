package com.aliyun.iotx.linkvisual.media.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Environment;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack;
import com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioBufferReceiveListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioParamsChangeListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnTalkReadyListener;
import com.aliyun.iotx.linkvisual.media.audio.processing.AudioProcesser;
import com.aliyun.iotx.linkvisual.media.audio.processing.IVoiceChange;
import com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener;
import com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord;
import com.aliyun.iotx.linkvisual.media.audio.utils.AecUtils;
import com.aliyun.iotx.linkvisual.media.audio.utils.AudioUtils;
import com.google.android.exoplayer2.util.MimeTypes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class LiveIntercomV2 {
    public static final int GAIN_LEVEL_AGRESSIVE = 3;
    public static final int GAIN_LEVEL_HIGH = 2;
    public static final int GAIN_LEVEL_LOW = 0;
    public static final int GAIN_LEVEL_MAX = 5;
    public static final int GAIN_LEVEL_MIDDLE = 1;
    public static final int GAIN_LEVEL_NONE = -1;
    public static final int GAIN_LEVEL_VERY_AGRESSIVE = 4;
    public static final String TAG = "linksdk_lv_LiveIntercomV2";

    /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
    private NoiseSuppressor f5018lvbyte;

    /* JADX INFO: renamed from: lvcase, reason: collision with root package name */
    private LiveIntercomV2Listener f5019lvcase;

    /* JADX INFO: renamed from: lvcatch, reason: collision with root package name */
    private LiveIntercomRequest f5020lvcatch;

    /* JADX INFO: renamed from: lvchar, reason: collision with root package name */
    private ByteBuffer f5021lvchar;
    private IVoiceChange lvclass;
    private AudioManager lvfinal;
    private Context lvfloat;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private LiveIntercom f5023lvfor;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private SimpleAudioRecord f5026lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private SimpleStreamAudioTrack f5028lvnew;
    private AudioParams lvshort;

    /* JADX INFO: renamed from: lvthis, reason: collision with root package name */
    private int f5029lvthis;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private AcousticEchoCanceler f5030lvtry;
    public static final byte[] SILENCE_8K = {0, 0, 0, 0, -1, -1, 1, 0, 1, 0, -3, -1, 4, 0, -5, -1, 5, 0, -4, -1, 2, 0, 0, 0, -2, -1, 3, 0, -2, -1, 0, 0, 1, 0, -2, -1, 3, 0, -3, -1, 2, 0, -2, -1, 2, 0, -1, -1, 1, 0, -1, -1, -1, -1, 2, 0, -1, -1, 1, 0, 0, 0, -2, -1, 2, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, -2, -1, 3, 0, -3, -1, 3, 0, -3, -1, 2, 0, -2, -1, 2, 0, -1, -1, 0, 0, 1, 0, -2, -1, 2, 0, -2, -1, 2, 0, -2, -1, 2, 0, -1, -1, -1, -1, 2, 0, -3, -1, 3, 0, -1, -1, -1, -1, 2, 0, -3, -1, 3, 0, -2, -1, 1, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, -1, -1, 1, 0, 0, 0};
    public static final byte[] SILENCE_16K = {-1, -1, 1, 0, 0, 0, -1, -1, 2, 0, -3, -1, 2, 0, 0, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, -1, -1, 1, 0, -1, -1, 1, 0, -1, -1, 1, 0, -2, -1, 4, 0, -5, -1, 4, 0, -3, -1, 2, 0, -1, -1, 1, 0, -1, -1, 0, 0, 1, 0, -2, -1, 2, 0, -1, -1, 0, 0, 0, 0, 0, 0, 1, 0, -2, -1, 2, 0, -2, -1, 2, 0, -1, -1, 1, 0, -2, -1, 2, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -2, -1, 2, 0, -2, -1, 1, 0, 1, 0, -2, -1, 2, 0, -1, -1, -1, -1, 3, 0, -4, -1, 4, 0, -4, -1, 3, 0, -1, -1, -1, -1, 2, 0, -2, -1, 1, 0, 1, 0, -2, -1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, -2, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 2, 0, -2, -1, 2, 0, -2, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 2, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, -1, 0, 0, 0, 0, 1, 0, -2, -1, 3, 0, -4, -1, 4, 0, -3, -1, 2, 0, -1, -1, 0, 0, 1, 0, -1, -1, 1, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, 0, 0, -1, -1, 1, 0, -1, -1, 1, 0, 0, 0, -2, -1, 2, 0, -1, -1, 1, 0, -1, -1, 1, 0, -2, -1, 2, 0, -1, -1, 1, 0, -1, -1, 1, 0, -2, -1, 3, 0, -3, -1, 2, 0, 0, 0, -2, -1, 3, 0, -2, -1, 0, 0, 2, 0, -3, -1, 3, 0, -2, -1, 1, 0, 0, 0, 0, 0, -1, -1, 2, 0, -3, -1, 3, 0, -2, -1};

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private boolean f5022lvdo = lvbyte.lvint.lvdo();

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private LiveIntercomVoiceType f5025lvif = LiveIntercomVoiceType.Original;

    /* JADX INFO: renamed from: lvgoto, reason: collision with root package name */
    private ConcurrentLinkedQueue<byte[]> f5024lvgoto = new ConcurrentLinkedQueue<>();

    /* JADX INFO: renamed from: lvlong, reason: collision with root package name */
    private BlockingQueue<byte[]> f5027lvlong = new LinkedBlockingQueue();

    /* JADX INFO: renamed from: lvvoid, reason: collision with root package name */
    private boolean f5031lvvoid = false;

    /* JADX INFO: renamed from: lvbreak, reason: collision with root package name */
    private boolean f5017lvbreak = true;
    private int lvelse = 1;
    private boolean lvconst = !AecUtils.isForbiddenHeadset();
    private AtomicBoolean lvsuper = new AtomicBoolean(false);
    private final byte[] lvthrow = new byte[1];
    private BroadcastReceiver lvwhile = new lvbyte();

    public enum LiveIntercomMode {
        SingleTalk,
        DoubleTalk
    }

    class lvbyte extends BroadcastReceiver {
        lvbyte() {
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x00a9  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00bb  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r5, android.content.Intent r6) {
            /*
                Method dump skipped, instruction units count: 214
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.audio.LiveIntercomV2.lvbyte.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    static /* synthetic */ class lvcase {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f5033lvdo;

        static {
            int[] iArr = new int[LiveIntercomMode.values().length];
            f5033lvdo = iArr;
            try {
                iArr[LiveIntercomMode.DoubleTalk.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5033lvdo[LiveIntercomMode.SingleTalk.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    class lvdo implements OnAudioParamsChangeListener {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ LiveIntercomMode f5034lvdo;

        lvdo(LiveIntercomMode liveIntercomMode) {
            this.f5034lvdo = liveIntercomMode;
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioParamsChangeListener
        public void onAudioParamsChange(AudioParams audioParams) {
            if (this.f5034lvdo == LiveIntercomMode.SingleTalk) {
                return;
            }
            LiveIntercomV2.this.f5021lvchar.clear();
            LiveIntercomV2.this.f5024lvgoto.clear();
            synchronized (LiveIntercomV2.this.lvthrow) {
                if (LiveIntercomV2.this.f5026lvint == null) {
                    return;
                }
                if (LiveIntercomV2.this.f5028lvnew != null) {
                    LiveIntercomV2.this.f5028lvnew.release();
                    LiveIntercomV2.this.f5028lvnew = null;
                    LiveIntercomV2.this.f5027lvlong.clear();
                }
                try {
                    LiveIntercomV2 liveIntercomV2 = LiveIntercomV2.this;
                    liveIntercomV2.f5028lvnew = new SimpleStreamAudioTrack(audioParams, Version.isCC ? 3 : 0, liveIntercomV2.f5027lvlong, LiveIntercomV2.this.f5026lvint.getAudioSessionId());
                    LiveIntercomV2.this.f5028lvnew.start();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    LiveIntercomV2.this.lvdo(16, "Audio Player init failed.");
                }
            }
        }
    }

    class lvfor implements OnErrorListener {
        lvfor() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.listener.OnErrorListener
        public void onError(LiveIntercomException liveIntercomException) {
            LiveIntercomV2.this.lvdo(liveIntercomException.getCode(), liveIntercomException.getSubCode(), liveIntercomException.getMessage());
        }
    }

    class lvif implements OnAudioBufferReceiveListener {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ LiveIntercomMode f5037lvdo;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ byte[] f5039lvif;

        lvif(LiveIntercomMode liveIntercomMode, byte[] bArr) {
            this.f5037lvdo = liveIntercomMode;
            this.f5039lvif = bArr;
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioBufferReceiveListener
        public void onAudioBufferRecevie(byte[] bArr, int i) {
            ALog.d(LiveIntercomV2.TAG, "onFarEndAudioBufferRecevied size=" + i);
            if (this.f5037lvdo == LiveIntercomMode.SingleTalk || LiveIntercomV2.this.f5031lvvoid) {
                return;
            }
            if (i <= LiveIntercomV2.this.f5021lvchar.capacity()) {
                LiveIntercomV2.this.f5021lvchar.put(bArr);
                LiveIntercomV2.this.f5021lvchar.flip();
                while (LiveIntercomV2.this.f5021lvchar.remaining() / (LiveIntercomV2.this.f5029lvthis * 2) > 0) {
                    LiveIntercomV2.this.f5021lvchar.get(this.f5039lvif);
                    LiveIntercomV2.this.lvdo(this.f5039lvif);
                }
                LiveIntercomV2.this.f5021lvchar.compact();
                return;
            }
            LiveIntercomV2.this.lvdo(6, "The audio data received from the far end is too large. " + i + " > " + LiveIntercomV2.this.f5021lvchar.capacity());
        }
    }

    class lvint implements OnTalkReadyListener {
        lvint() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.listener.OnTalkReadyListener
        public void onTalkReady() {
            if (LiveIntercomV2.this.f5019lvcase != null) {
                LiveIntercomV2.this.f5019lvcase.onTalkReady();
            }
        }
    }

    class lvnew implements AudioRecordListener {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ String f5041lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        final /* synthetic */ LiveIntercomMode f5042lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ AudioParams f5043lvif;

        class lvdo implements Runnable {
            lvdo() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (LiveIntercomV2.this.f5019lvcase != null) {
                    LiveIntercomV2.this.f5019lvcase.onRecordStart();
                }
            }
        }

        class lvfor implements Runnable {

            /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
            final /* synthetic */ byte[] f5046lvdo;

            /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
            final /* synthetic */ int f5047lvfor;

            /* JADX INFO: renamed from: lvif, reason: collision with root package name */
            final /* synthetic */ int f5048lvif;

            lvfor(byte[] bArr, int i, int i2) {
                this.f5046lvdo = bArr;
                this.f5048lvif = i;
                this.f5047lvfor = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (LiveIntercomV2.this.f5019lvcase != null) {
                    LiveIntercomV2.this.f5019lvcase.onRecordBufferReceived(this.f5046lvdo, this.f5048lvif, this.f5047lvfor);
                }
            }
        }

        class lvif implements Runnable {
            lvif() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (LiveIntercomV2.this.f5019lvcase != null) {
                    LiveIntercomV2.this.f5019lvcase.onRecordEnd();
                }
            }
        }

        lvnew(String str, AudioParams audioParams, LiveIntercomMode liveIntercomMode) {
            this.f5041lvdo = str;
            this.f5043lvif = audioParams;
            this.f5042lvfor = liveIntercomMode;
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onBufferReceived(byte[] bArr, int i, int i2) {
            ALog.d(LiveIntercomV2.TAG, "onRecordBufferReceived size=" + i2);
            byte[] bArr2 = new byte[i2];
            System.arraycopy(bArr, 0, bArr2, 0, i2);
            LiveIntercomV2.this.f5023lvfor.getEventHandler().post(new lvfor(bArr2, i, i2));
            byte[] bArr3 = new byte[i2];
            short[] sArr = new short[LiveIntercomV2.this.f5029lvthis];
            short[] sArr2 = this.f5042lvfor == LiveIntercomMode.DoubleTalk ? new short[LiveIntercomV2.this.f5029lvthis] : null;
            short[] sArr3 = new short[LiveIntercomV2.this.f5029lvthis];
            int i3 = i2 / 2;
            boolean z = LiveIntercomV2.this.f5024lvgoto.size() < i3 / LiveIntercomV2.this.f5029lvthis;
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i4 * 2;
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2, i5, LiveIntercomV2.this.f5029lvthis * 2);
                ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
                byteBufferWrap.order(byteOrder).asShortBuffer().get(sArr);
                if (this.f5042lvfor == LiveIntercomMode.DoubleTalk) {
                    byte[] bArr4 = !z ? (byte[]) LiveIntercomV2.this.f5024lvgoto.poll() : null;
                    if (bArr4 == null) {
                        bArr4 = this.f5043lvif.getSampleRate() == 8000 ? LiveIntercomV2.SILENCE_8K : LiveIntercomV2.SILENCE_16K;
                    }
                    synchronized (LiveIntercomV2.this.lvthrow) {
                        if (LiveIntercomV2.this.f5028lvnew != null && LiveIntercomV2.this.f5028lvnew.getPlayState() == 3) {
                            LiveIntercomV2.this.f5027lvlong.add(bArr4);
                        }
                    }
                    ByteBuffer.wrap(bArr4, 0, LiveIntercomV2.this.f5029lvthis * 2).order(byteOrder).asShortBuffer().get(sArr2);
                }
                int iProcess = AudioProcesser.process(sArr2, (LiveIntercomV2.this.lvclass != null ? LiveIntercomVoiceType.Original : LiveIntercomV2.this.f5025lvif).getValue(), sArr, sArr3, LiveIntercomV2.this.f5029lvthis);
                if (iProcess < 0) {
                    ALog.e(LiveIntercomV2.TAG, "AudioProcesser process failed = " + iProcess);
                    return;
                }
                ByteBuffer.wrap(bArr3, i5, LiveIntercomV2.this.f5029lvthis * 2).order(byteOrder).asShortBuffer().put(sArr3);
                i4 += LiveIntercomV2.this.f5029lvthis;
            }
            if (LiveIntercomV2.this.lvclass != null && !LiveIntercomV2.this.lvclass.onChangeVoice(bArr3, 0, i2)) {
                ALog.w(LiveIntercomV2.TAG, "external voice change process failed.");
            }
            AudioParams audioParams = this.f5043lvif;
            if (audioParams.mAudioType != 2 || audioParams.getSampleRate() <= 16000) {
                LiveIntercomV2.this.f5023lvfor.sendAudioBuffer(bArr3, 0, i2);
                return;
            }
            int i6 = i2 / 4;
            for (int i7 = 0; i7 < 4; i7++) {
                LiveIntercomV2.this.f5023lvfor.sendAudioBuffer(bArr3, i7 * i6, i6);
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onError(int i, String str) {
            LiveIntercomV2 liveIntercomV2;
            int i2;
            switch (i) {
                case -3:
                    liveIntercomV2 = LiveIntercomV2.this;
                    i2 = 9;
                    break;
                case -2:
                    liveIntercomV2 = LiveIntercomV2.this;
                    i2 = 8;
                    break;
                case -1:
                    liveIntercomV2 = LiveIntercomV2.this;
                    i2 = 7;
                    break;
                default:
                    return;
            }
            liveIntercomV2.lvdo(i2, str);
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onRecordEnd() {
            ALog.d(LiveIntercomV2.TAG, "onRecordEnd");
            AudioProcesser.destroy();
            LiveIntercomV2.this.f5023lvfor.stop();
            LiveIntercomV2.this.f5023lvfor.getEventHandler().post(new lvif());
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener
        public void onRecordStart() {
            String str;
            ALog.d(LiveIntercomV2.TAG, "onRecordStart");
            if (LiveIntercomV2.this.f5017lvbreak) {
                LiveIntercomV2.this.f5023lvfor.start(this.f5041lvdo, this.f5043lvif);
            } else {
                LiveIntercomV2.this.f5023lvfor.start(LiveIntercomV2.this.f5020lvcatch.url, LiveIntercomV2.this.f5020lvcatch.key, LiveIntercomV2.this.f5020lvcatch.iv, this.f5043lvif);
            }
            int sampleRate = this.f5043lvif.getSampleRate();
            int i = LiveIntercomV2.this.lvelse;
            if (LiveIntercomV2.this.f5022lvdo) {
                str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LinkVisual_dump/audio";
            } else {
                str = null;
            }
            AudioProcesser.init(sampleRate, 3, -1, i, str);
            LiveIntercomV2.this.lvdo();
            LiveIntercomV2.this.f5023lvfor.getEventHandler().post(new lvdo());
        }
    }

    class lvtry implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ int f5051lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        final /* synthetic */ String f5052lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ int f5053lvif;

        lvtry(int i, int i2, String str) {
            this.f5051lvdo = i;
            this.f5053lvif = i2;
            this.f5052lvfor = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercomV2.this.f5019lvcase != null) {
                LiveIntercomV2.this.f5019lvcase.onError(new LiveIntercomException(this.f5051lvdo, this.f5053lvif, this.f5052lvfor));
            }
        }
    }

    public LiveIntercomV2(Context context, String str, LiveIntercomMode liveIntercomMode, AudioParams audioParams) {
        lvdo(audioParams, liveIntercomMode);
        this.lvshort = audioParams;
        this.lvfloat = context;
        this.lvfinal = (AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        this.f5029lvthis = lvdo(audioParams);
        this.f5021lvchar = ByteBuffer.allocate(audioParams.getSampleRate() * 2);
        LiveIntercom liveIntercom = new LiveIntercom(true);
        this.f5023lvfor = liveIntercom;
        liveIntercom.setOnAudioParamsChangeListener(new lvdo(liveIntercomMode));
        this.f5023lvfor.setOnAudioBufferReceiveListener(new lvif(liveIntercomMode, new byte[this.f5029lvthis * 2]));
        this.f5023lvfor.setOnErrorListener(new lvfor());
        this.f5023lvfor.setOnTalkReadyListener(new lvint());
        SimpleAudioRecord simpleAudioRecord = new SimpleAudioRecord(Version.isCC ? 1 : 7, audioParams);
        this.f5026lvint = simpleAudioRecord;
        simpleAudioRecord.setAudioRecordListener(new lvnew(str, audioParams, liveIntercomMode));
        context.registerReceiver(this.lvwhile, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"), "android.permission.BLUETOOTH", null);
    }

    private static int lvdo(AudioParams audioParams) {
        return audioParams.getSampleRate() != 8000 ? 160 : 80;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo() {
        if (this.f5018lvbyte == null && NoiseSuppressor.isAvailable()) {
            NoiseSuppressor noiseSuppressorCreate = NoiseSuppressor.create(this.f5026lvint.getAudioSessionId());
            this.f5018lvbyte = noiseSuppressorCreate;
            if (noiseSuppressorCreate != null) {
                ALog.i(TAG, "NoiseSuppressor setEnable ret=" + noiseSuppressorCreate.setEnabled(true));
            }
        }
        if (this.f5030lvtry == null && AcousticEchoCanceler.isAvailable()) {
            AcousticEchoCanceler acousticEchoCancelerCreate = AcousticEchoCanceler.create(this.f5026lvint.getAudioSessionId());
            this.f5030lvtry = acousticEchoCancelerCreate;
            if (acousticEchoCancelerCreate != null) {
                try {
                    ALog.i(TAG, "AcousticEchoCanceler setEnable ret=" + acousticEchoCancelerCreate.setEnabled(true));
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo(int i, int i2, String str) {
        AudioProcesser.destroy();
        SimpleAudioRecord simpleAudioRecord = this.f5026lvint;
        if (simpleAudioRecord != null) {
            simpleAudioRecord.stop();
        }
        this.f5023lvfor.stop();
        this.f5023lvfor.getEventHandler().post(new lvtry(i, i2, str));
        SimpleStreamAudioTrack simpleStreamAudioTrack = this.f5028lvnew;
        if (simpleStreamAudioTrack != null) {
            simpleStreamAudioTrack.stop();
        }
        lvfor();
        lvint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo(int i, String str) {
        lvdo(i, 0, str);
    }

    private static void lvdo(AudioParams audioParams, LiveIntercomMode liveIntercomMode) {
        int i = lvcase.f5033lvdo[liveIntercomMode.ordinal()];
        if ((i == 1 || i == 2) && !audioParams.checkSupport()) {
            throw new IllegalArgumentException("Not support this AudioParams: " + audioParams.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo(byte[] bArr) {
        int i = this.f5029lvthis;
        short[] sArr = new short[i];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, i * 2);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        byteBufferWrap.order(byteOrder).asShortBuffer().get(sArr);
        AudioProcesser.process_farend(sArr, this.f5029lvthis);
        byte[] bArr2 = new byte[this.f5029lvthis * 2];
        ByteBuffer.wrap(bArr2).order(byteOrder).asShortBuffer().put(sArr);
        this.f5024lvgoto.add(bArr2);
        ALog.d(TAG, "far end buffer size =" + this.f5024lvgoto.size());
    }

    private void lvfor() {
        if (!AecUtils.isModeNone()) {
            try {
                this.lvfinal.setMode(0);
            } catch (Exception unused) {
            }
        }
        this.lvfinal.abandonAudioFocus(null);
        if (AudioUtils.isBlueToothHeadsetConnected()) {
            this.lvfinal.stopBluetoothSco();
        }
    }

    private boolean lvif() {
        String str;
        String str2;
        if (!AecUtils.isModeNone()) {
            try {
                if (AecUtils.isModeInCallAEC()) {
                    this.lvfinal.setMode(2);
                    str = TAG;
                    str2 = "MODE_IN_CALL";
                } else {
                    this.lvfinal.setMode(3);
                    str = TAG;
                    str2 = "MODE_IN_COMMUNICATION";
                }
                ALog.i(str, str2);
            } catch (Exception unused) {
            }
        }
        this.lvfinal.requestAudioFocus(null, 3, 4);
        if (this.lvconst) {
            ALog.d(TAG, "isBluetoothHeadsetConnected() : " + AudioUtils.isBlueToothHeadsetConnected());
            ALog.d(TAG, "isBtHeadsetScoOn() : " + AudioUtils.isBtHeadsetScoOn(this.lvfinal));
            ALog.d(TAG, "isWiredHeadsetOn() : " + AudioUtils.isWiredHeadsetOn(this.lvfinal));
            ALog.d(TAG, "isBluetoothScoAvailableOffCall() :" + this.lvfinal.isBluetoothScoAvailableOffCall());
            if (this.lvfinal.isBluetoothScoAvailableOffCall() && AudioUtils.isBlueToothHeadsetConnected()) {
                ALog.i(TAG, "using sco");
                this.lvfinal.startBluetoothSco();
                this.lvfinal.setSpeakerphoneOn(false);
                this.lvsuper.set(true);
                return false;
            }
            if (AudioUtils.isWiredHeadsetOn(this.lvfinal)) {
                ALog.i(TAG, "using wired headset");
                AudioUtils.useWiredHeadset(this.lvfinal);
                return true;
            }
        }
        ALog.i(TAG, "using phone speaker");
        AudioUtils.useSpeaker(this.lvfinal);
        return true;
    }

    private void lvint() {
        AcousticEchoCanceler acousticEchoCanceler = this.f5030lvtry;
        if (acousticEchoCanceler != null) {
            acousticEchoCanceler.release();
            this.f5030lvtry = null;
        }
        NoiseSuppressor noiseSuppressor = this.f5018lvbyte;
        if (noiseSuppressor != null) {
            noiseSuppressor.release();
            this.f5018lvbyte = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        synchronized (this.lvthrow) {
            AcousticEchoCanceler acousticEchoCanceler = this.f5030lvtry;
            if (acousticEchoCanceler != null) {
                acousticEchoCanceler.release();
                this.f5030lvtry = null;
            }
            NoiseSuppressor noiseSuppressor = this.f5018lvbyte;
            if (noiseSuppressor != null) {
                noiseSuppressor.release();
                this.f5018lvbyte = null;
            }
            SimpleAudioRecord simpleAudioRecord = this.f5026lvint;
            if (simpleAudioRecord != null) {
                simpleAudioRecord.release();
                this.f5026lvint = null;
            }
            SimpleStreamAudioTrack simpleStreamAudioTrack = this.f5028lvnew;
            if (simpleStreamAudioTrack != null) {
                simpleStreamAudioTrack.release();
                this.f5028lvnew = null;
            }
        }
    }

    public int getGainLevel() {
        return this.lvelse;
    }

    public LiveIntercomVoiceType getLiveIntercomVoiceType() {
        if (this.lvclass == null) {
            return this.f5025lvif;
        }
        throw new UnsupportedOperationException("setVoiceChangeType is forbidden when external voiceChangeImpl has been set.");
    }

    public boolean isMute() {
        return this.f5031lvvoid;
    }

    public boolean isSupportExternalHeadset() {
        return this.lvconst;
    }

    public void release() {
        synchronized (this.lvthrow) {
            SimpleAudioRecord simpleAudioRecord = this.f5026lvint;
            if (simpleAudioRecord != null) {
                simpleAudioRecord.release();
                this.f5026lvint = null;
            }
            SimpleStreamAudioTrack simpleStreamAudioTrack = this.f5028lvnew;
            if (simpleStreamAudioTrack != null) {
                simpleStreamAudioTrack.release();
                this.f5028lvnew = null;
            }
        }
        try {
            this.lvfloat.unregisterReceiver(this.lvwhile);
        } catch (Exception unused) {
        }
        this.f5019lvcase = null;
    }

    public void setExternalVoiceChangeImpl(IVoiceChange iVoiceChange) {
        this.lvclass = iVoiceChange;
    }

    public void setGainLevel(int i) {
        this.lvelse = i;
    }

    public void setLiveIntercomV2Listener(LiveIntercomV2Listener liveIntercomV2Listener) {
        this.f5019lvcase = liveIntercomV2Listener;
    }

    public void setMute(boolean z) {
        this.f5031lvvoid = z;
    }

    public void setSupportExternalHeadset(boolean z) {
        if (AecUtils.isForbiddenHeadset()) {
            ALog.e(TAG, "This phone is not support external headset!");
        } else {
            this.lvconst = z;
        }
    }

    public void setVoiceChangeType(LiveIntercomVoiceType liveIntercomVoiceType) {
        if (this.lvclass != null) {
            throw new UnsupportedOperationException("setVoiceChangeType is forbidden when external voiceChangeImpl has been set.");
        }
        this.f5025lvif = liveIntercomVoiceType;
    }

    public void start() {
        this.f5017lvbreak = true;
        if (lvif()) {
            this.f5026lvint.start();
        }
    }

    public void startWithExternalRequest(LiveIntercomRequest liveIntercomRequest) {
        this.f5017lvbreak = false;
        this.f5020lvcatch = liveIntercomRequest;
        if (lvif()) {
            this.f5026lvint.start();
        }
    }

    public void stop() {
        this.f5026lvint.stop();
        SimpleStreamAudioTrack simpleStreamAudioTrack = this.f5028lvnew;
        if (simpleStreamAudioTrack != null) {
            simpleStreamAudioTrack.stop();
        }
        this.f5023lvfor.stop();
        lvfor();
        lvint();
    }
}
