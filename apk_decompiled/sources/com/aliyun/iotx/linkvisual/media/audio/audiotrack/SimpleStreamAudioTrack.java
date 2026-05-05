package com.aliyun.iotx.linkvisual.media.audio.audiotrack;

import android.media.AudioTrack;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.utils.AudioUtils;
import com.aliyun.iotx.linkvisual.media.audio.utils.WavFileWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lvif.lvdo;
import lvif.lvfor;
import lvif.lvnew;

/* JADX INFO: loaded from: classes2.dex */
public class SimpleStreamAudioTrack {
    private static final boolean DEBUG = false;
    private static final int JITTER_BUFFERING_DELAY_START_TIME_IN_MS = 160;
    private static final int MAX_ACCUMULATION_TIME_IN_MS = 1000;
    private static final int MAX_ALLOW_ACCUMULATION_COUNT = 10;
    public static final int MODE_LOW_DELAY = 1;
    public static final int MODE_NORMAL = 0;
    public static final String TAG = "linksdk_lv_SimpleStreamAudioTrack";
    private BlockingQueue<byte[]> audioBuffer;
    private lvnew audioCodec;
    private AudioParams audioParams;
    private int audioSessionId;
    private int audioStreamType;
    private AudioTrack audioTrack;
    private Thread audioWriteThread;
    private final Runnable audioWriteThreadRunnable;
    private RandomAccessFile codecFile;
    private Thread codecThread;
    private final Runnable codecThreadRunnable;
    private boolean isJitterBuffering;
    private boolean isReleaseAudioTrackPlayerThread;
    private int mode;
    private ConcurrentLinkedQueue<byte[]> pcmBuffer;
    private WavFileWriter pcmFileWriter;

    public SimpleStreamAudioTrack(AudioParams audioParams, int i, BlockingQueue blockingQueue) throws IllegalArgumentException {
        this(audioParams, i, blockingQueue, 0);
    }

    public SimpleStreamAudioTrack(AudioParams audioParams, int i, BlockingQueue blockingQueue, int i2) throws IllegalArgumentException {
        this.mode = 0;
        this.isJitterBuffering = false;
        this.audioWriteThreadRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack.1
            /* JADX WARN: Removed duplicated region for block: B:20:0x0088 A[Catch: Exception -> 0x013d, TryCatch #0 {Exception -> 0x013d, blocks: (B:5:0x0031, B:9:0x004d, B:10:0x0058, B:14:0x006c, B:17:0x0072, B:19:0x007a, B:40:0x0136, B:20:0x0088, B:23:0x00b3, B:27:0x00bb, B:28:0x00ec, B:30:0x00f0, B:31:0x00fc, B:33:0x0104, B:35:0x0110, B:37:0x011d, B:39:0x012b, B:11:0x005d, B:13:0x0065), top: B:46:0x0031 }] */
            /* JADX WARN: Removed duplicated region for block: B:50:0x0072 A[SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void run() {
                /*
                    Method dump skipped, instruction units count: 333
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack.AnonymousClass1.run():void");
            }
        };
        this.codecThreadRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack.2
            @Override // java.lang.Runnable
            public void run() {
                byte[] bArrLvif;
                try {
                    SimpleStreamAudioTrack simpleStreamAudioTrack = SimpleStreamAudioTrack.this;
                    simpleStreamAudioTrack.setupCodec(simpleStreamAudioTrack.audioParams);
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                }
                while (!SimpleStreamAudioTrack.this.isReleaseAudioTrackPlayerThread) {
                    try {
                        bArrLvif = (byte[]) SimpleStreamAudioTrack.this.audioBuffer.take();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (SimpleStreamAudioTrack.this.audioCodec != null) {
                        bArrLvif = SimpleStreamAudioTrack.this.audioCodec.lvif(bArrLvif, 0, bArrLvif.length);
                        if (bArrLvif == null) {
                            ALog.d(SimpleStreamAudioTrack.TAG, "Decode audio data failed. using: " + SimpleStreamAudioTrack.this.audioCodec.lvdo());
                        } else {
                            ALog.d(SimpleStreamAudioTrack.TAG, "Decoded audio size= " + bArrLvif.length);
                        }
                    }
                    if (bArrLvif != null) {
                        SimpleStreamAudioTrack.this.pcmBuffer.add(bArrLvif);
                    }
                }
                if (SimpleStreamAudioTrack.this.audioCodec != null) {
                    SimpleStreamAudioTrack.this.audioCodec.lvfor();
                }
            }
        };
        this.audioBuffer = blockingQueue;
        this.audioSessionId = i2;
        this.audioParams = audioParams;
        this.audioStreamType = i;
        this.pcmBuffer = new ConcurrentLinkedQueue<>();
        createAudioTrack(i);
        startWriteThread();
        startCodecThread();
    }

    private void createAudioTrack(int i) throws IllegalArgumentException {
        this.audioTrack = new AudioTrack(i, this.audioParams.getSampleRate(), AudioUtils.getChannelOutConfig(this.audioParams.getChannelCount()), this.audioParams.getAudioEncoding(), AudioUtils.calculateAudioTrackBuffer(this.audioParams), 1, this.audioSessionId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupCodec(AudioParams audioParams) {
        int audioType = audioParams.getAudioType();
        this.audioCodec = audioType != 1 ? audioType != 2 ? null : lvdo.lvdo(audioParams) : lvfor.lvint();
    }

    private void startCodecThread() {
        Thread thread = new Thread(this.codecThreadRunnable, "AudioTrack-codec");
        this.codecThread = thread;
        thread.start();
    }

    private void startWriteThread() {
        Thread thread = new Thread(this.audioWriteThreadRunnable, "AudioTrack-write");
        this.audioWriteThread = thread;
        thread.start();
    }

    public int getPlayState() {
        return this.audioTrack.getPlayState();
    }

    public void pause() {
        try {
            this.audioTrack.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        this.isReleaseAudioTrackPlayerThread = true;
        this.pcmBuffer.add(new byte[0]);
        this.audioBuffer.add(new byte[0]);
        try {
            this.audioWriteThread.join(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            this.codecThread.join(3000L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        this.audioBuffer.clear();
        this.pcmBuffer.clear();
    }

    public void reloadWithStreamType(int i) {
        if (this.audioStreamType == i) {
            ALog.w(TAG, "[" + hashCode() + "] AudioTrack stream type not changed, ignore new one.");
            return;
        }
        int playState = this.audioTrack.getPlayState();
        this.audioTrack.release();
        createAudioTrack(i);
        if (playState == 3) {
            this.audioTrack.play();
        }
    }

    public void resume() {
        this.audioTrack.play();
    }

    public void setMode(int i) {
        this.mode = i;
    }

    public void setVolume(float f) {
        try {
            this.audioTrack.setStereoVolume(f, f);
        } catch (IllegalStateException unused) {
        }
    }

    public void start() {
        try {
            this.audioBuffer.clear();
            this.pcmBuffer.clear();
            this.audioTrack.flush();
            this.audioTrack.play();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        lvnew lvnewVar = this.audioCodec;
        if (lvnewVar != null) {
            lvnewVar.lvif();
        }
        try {
            this.audioBuffer.clear();
            this.pcmBuffer.clear();
            this.audioTrack.pause();
            this.audioTrack.flush();
        } catch (IllegalStateException unused) {
        }
    }
}
