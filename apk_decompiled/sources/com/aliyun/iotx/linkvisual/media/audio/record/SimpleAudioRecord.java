package com.aliyun.iotx.linkvisual.media.audio.record;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.HandlerThread;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.utils.AudioUtils;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class SimpleAudioRecord {
    public static final int DEFAULT_INTERVAL_IN_MS = 40;
    public static final int ERR_INIT_RECORD = -1;
    public static final int ERR_READ_BUFFER = -3;
    public static final int ERR_START_RECORD = -2;
    public static final String TAG = "linksdk_lv_SimpleAudioRecord";
    private AudioParams audioParams;
    private AudioRecord audioRecord;
    private AudioRecordListener audioRecordListener;
    private int audioSource;
    private byte[] callbackBuffer;
    private Handler handler;
    private HandlerThread handlerThread;
    private byte[] readBuffer;
    private int readBufferSize;
    private ByteBuffer recordByteBuffer;
    private AudioRecordState currentState = AudioRecordState.IDLE;
    private int intervalInMs = 40;
    private Runnable startRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord.2
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (SimpleAudioRecord.this.currentState != AudioRecordState.IDLE) {
                    return;
                }
                if (SimpleAudioRecord.this.audioRecord == null) {
                    ALog.d(SimpleAudioRecord.TAG, "sampleRate: " + SimpleAudioRecord.this.audioParams.getSampleRate() + ", channelConf:" + AudioUtils.getChannelInConfig(SimpleAudioRecord.this.audioParams.getChannelCount()) + ", audioEncoding:" + SimpleAudioRecord.this.audioParams.getAudioEncoding() + ", readBufferSize:" + SimpleAudioRecord.this.readBufferSize);
                    SimpleAudioRecord.this.audioRecord = new AudioRecord(SimpleAudioRecord.this.audioSource, SimpleAudioRecord.this.audioParams.getSampleRate(), AudioUtils.getChannelInConfig(SimpleAudioRecord.this.audioParams.getChannelCount()), SimpleAudioRecord.this.audioParams.getAudioEncoding(), SimpleAudioRecord.this.readBufferSize);
                }
                SimpleAudioRecord.this.audioRecord.startRecording();
                if (SimpleAudioRecord.this.audioRecord.getRecordingState() == 3) {
                    SimpleAudioRecord.this.transferState(AudioRecordState.STARTED);
                    SimpleAudioRecord.this.recordByteBuffer.clear();
                    SimpleAudioRecord.this.handler.post(SimpleAudioRecord.this.readRunnable);
                } else {
                    if (SimpleAudioRecord.this.audioRecordListener != null) {
                        SimpleAudioRecord.this.audioRecordListener.onError(-2, "Recorder start failed.");
                    }
                    SimpleAudioRecord.this.audioRecord.release();
                    SimpleAudioRecord.this.audioRecord = null;
                }
            } catch (IllegalArgumentException e) {
                if (SimpleAudioRecord.this.audioRecordListener != null) {
                    SimpleAudioRecord.this.audioRecordListener.onError(-1, e.getMessage());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (SimpleAudioRecord.this.audioRecordListener != null) {
                    SimpleAudioRecord.this.audioRecordListener.onError(-2, "Recorder start failed.");
                }
            }
        }
    };
    private Runnable readRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord.3
        @Override // java.lang.Runnable
        public void run() {
            if (SimpleAudioRecord.this.currentState != AudioRecordState.STARTED) {
                return;
            }
            int i = SimpleAudioRecord.this.audioRecord.read(SimpleAudioRecord.this.readBuffer, 0, SimpleAudioRecord.this.readBufferSize);
            if (i < 0) {
                if (SimpleAudioRecord.this.audioRecordListener != null) {
                    SimpleAudioRecord.this.audioRecordListener.onError(-3, "Read buffer return " + i);
                    return;
                }
                return;
            }
            if (i < SimpleAudioRecord.this.recordByteBuffer.remaining()) {
                SimpleAudioRecord.this.recordByteBuffer.put(SimpleAudioRecord.this.readBuffer, 0, i);
                SimpleAudioRecord.this.recordByteBuffer.flip();
            }
            while (SimpleAudioRecord.this.recordByteBuffer.remaining() / SimpleAudioRecord.this.callbackBuffer.length > 0) {
                SimpleAudioRecord.this.recordByteBuffer.get(SimpleAudioRecord.this.callbackBuffer);
                if (SimpleAudioRecord.this.audioRecordListener != null) {
                    SimpleAudioRecord.this.audioRecordListener.onBufferReceived(SimpleAudioRecord.this.callbackBuffer, 0, SimpleAudioRecord.this.callbackBuffer.length);
                }
            }
            SimpleAudioRecord.this.recordByteBuffer.compact();
            SimpleAudioRecord.this.handler.post(SimpleAudioRecord.this.readRunnable);
        }
    };
    private Runnable stopRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord.4
        @Override // java.lang.Runnable
        public void run() {
            if (SimpleAudioRecord.this.currentState != AudioRecordState.STARTED) {
                return;
            }
            SimpleAudioRecord.this.handler.removeCallbacks(SimpleAudioRecord.this.readRunnable);
            if (SimpleAudioRecord.this.audioRecord != null) {
                SimpleAudioRecord.this.audioRecord.stop();
            }
            SimpleAudioRecord.this.transferState(AudioRecordState.IDLE);
        }
    };
    private Runnable releaseRunnable = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord.5
        @Override // java.lang.Runnable
        public void run() {
            if (SimpleAudioRecord.this.audioRecord != null) {
                SimpleAudioRecord.this.audioRecord.release();
                SimpleAudioRecord.this.audioRecord = null;
            }
            SimpleAudioRecord.this.transferState(AudioRecordState.IDLE);
        }
    };

    public SimpleAudioRecord(int i, AudioParams audioParams) {
        this.audioParams = audioParams;
        this.audioSource = i;
        HandlerThread handlerThread = new HandlerThread("MyAudioRecordThread", -19);
        this.handlerThread = handlerThread;
        handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper()) { // from class: com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord.1
        };
        int bufferSize = getBufferSize();
        this.readBufferSize = bufferSize;
        this.readBuffer = new byte[bufferSize];
        this.recordByteBuffer = ByteBuffer.allocate(Math.min(10240, bufferSize * 2));
        this.callbackBuffer = new byte[((getFrameLength(audioParams) * 2) * this.intervalInMs) / 10];
    }

    private int getBufferSize() {
        int minBufferSize = AudioRecord.getMinBufferSize(this.audioParams.getSampleRate(), AudioUtils.getChannelInConfig(this.audioParams.getChannelCount()), this.audioParams.getAudioEncoding());
        return minBufferSize % (getFrameLength(this.audioParams) * 2) == 0 ? minBufferSize : getFrameLength(this.audioParams) * 4 * 2;
    }

    private static int getFrameLength(AudioParams audioParams) {
        return audioParams.getSampleRate() / 100;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transferState(AudioRecordState audioRecordState) {
        AudioRecordListener audioRecordListener;
        AudioRecordListener audioRecordListener2;
        AudioRecordState audioRecordState2 = this.currentState;
        if (audioRecordState2 == audioRecordState) {
            return;
        }
        AudioRecordState audioRecordState3 = AudioRecordState.IDLE;
        if (audioRecordState2 == audioRecordState3 && audioRecordState == AudioRecordState.STARTED && (audioRecordListener2 = this.audioRecordListener) != null) {
            audioRecordListener2.onRecordStart();
        }
        if (this.currentState == AudioRecordState.STARTED && audioRecordState == audioRecordState3 && (audioRecordListener = this.audioRecordListener) != null) {
            audioRecordListener.onRecordEnd();
        }
        this.currentState = audioRecordState;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.handler.removeCallbacks(this.readRunnable);
        this.handlerThread.quit();
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.release();
            this.audioRecord = null;
        }
    }

    public int getAudioSessionId() {
        ALog.d(TAG, "AudioSessionId = " + this.audioRecord.getAudioSessionId());
        return this.audioRecord.getAudioSessionId();
    }

    public int getIntervalInMs() {
        return this.intervalInMs;
    }

    public void release() {
        this.handler.post(this.stopRunnable);
        this.handler.post(this.releaseRunnable);
    }

    public void setAudioRecordListener(AudioRecordListener audioRecordListener) {
        this.audioRecordListener = audioRecordListener;
    }

    public void setIntervalInMs(int i) {
        this.intervalInMs = i;
    }

    public void start() {
        this.handler.post(this.startRunnable);
    }

    public void stop() {
        this.handler.post(this.stopRunnable);
    }
}
