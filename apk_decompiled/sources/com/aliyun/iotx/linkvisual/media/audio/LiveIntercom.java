package com.aliyun.iotx.linkvisual.media.audio;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioBufferReceiveListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnAudioParamsChangeListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.audio.listener.OnTalkReadyListener;
import com.aliyun.iotx.linkvisual.media.audio.utils.WavFileWriter;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import com.huawei.agconnect.exception.AGCServerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import lvdo.lvdo;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class LiveIntercom implements ILiveIntercom, lvdo.InterfaceC0305lvdo {
    private static final boolean DEBUG = lvbyte.lvint.lvdo();
    public static final String TAG = "linksdk_lv_LiveIntercom";
    private static final long WAIT_TALK_READY_TIMEOUT = 5000;
    private Queue<byte[]> audioBuffers;
    private String audioSavePath;
    private lvvoid connectState;
    private lvif.lvnew decodeCodec;
    private WavFileWriter downOriginalWavFileWriter;
    private WavFileWriter downWavFileWriter;
    private lvif.lvnew encodeCodec;
    private Handler eventHandler;
    private Handler handler;
    private HandlerThread handlerThread;
    private boolean internalUse;
    private OnAudioBufferReceiveListener onAudioBufferReceiveListener;
    private OnAudioParamsChangeListener onAudioParamsChangeListener;
    private OnErrorListener onErrorListener;
    private OnTalkReadyListener onTalkReadyListener;
    private final Runnable postRunnable;
    private Runnable stopRunnable;
    private WavFileWriter upWavFileWriter;
    private lvdo.lvdo voiceChannel;
    private Runnable waitTalkReadyTimeoutTask;

    class lvbyte implements Runnable {
        lvbyte() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LiveIntercom.this.transferState(lvvoid.STATE_DISCONNECTED);
        }
    }

    class lvcase implements Runnable {
        lvcase() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (LiveIntercom.this.audioBuffers.peek() != null) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                byte[] bArr = (byte[]) LiveIntercom.this.audioBuffers.poll();
                LiveIntercom.this.voiceChannel.lvdo(bArr, 0, bArr.length);
                ALog.d(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] post data len=" + bArr.length + "  remain=" + LiveIntercom.this.audioBuffers.size() + "  cost(ms): " + (System.currentTimeMillis() - jCurrentTimeMillis));
            }
        }
    }

    class lvchar implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ AudioParams f4987lvdo;

        lvchar(AudioParams audioParams) {
            this.f4987lvdo = audioParams;
        }

        @Override // java.lang.Runnable
        public void run() {
            ALog.d(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] headers:\n" + this.f4987lvdo.toString());
            LiveIntercom liveIntercom = LiveIntercom.this;
            liveIntercom.decodeCodec = liveIntercom.getDecodeCodec(this.f4987lvdo);
            if (LiveIntercom.DEBUG) {
                try {
                    if (this.f4987lvdo.getAudioType() != 2) {
                        LiveIntercom.this.downOriginalWavFileWriter = WavFileWriter.create(LiveIntercom.this.audioSavePath + "-down.original.wav", this.f4987lvdo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.f4987lvdo.setAudioType(0);
            if (LiveIntercom.this.onAudioParamsChangeListener != null) {
                LiveIntercom.this.onAudioParamsChangeListener.onAudioParamsChange(this.f4987lvdo);
            }
            if (LiveIntercom.DEBUG) {
                try {
                    LiveIntercom.this.downWavFileWriter = WavFileWriter.create(LiveIntercom.this.audioSavePath + "-down.wav", this.f4987lvdo);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    class lvdo implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ AudioParams f4989lvdo;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ String f4991lvif;

        /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.audio.LiveIntercom$lvdo$lvdo, reason: collision with other inner class name */
        class C0249lvdo implements IAPIHelperListener {

            /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.audio.LiveIntercom$lvdo$lvdo$lvdo, reason: collision with other inner class name */
            class RunnableC0250lvdo implements Runnable {

                /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
                final /* synthetic */ String f4993lvdo;

                /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
                final /* synthetic */ byte[] f4994lvfor;

                /* JADX INFO: renamed from: lvif, reason: collision with root package name */
                final /* synthetic */ byte[] f4995lvif;

                RunnableC0250lvdo(String str, byte[] bArr, byte[] bArr2) {
                    this.f4993lvdo = str;
                    this.f4995lvif = bArr;
                    this.f4994lvfor = bArr2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (LiveIntercom.this.connectState != lvvoid.STATE_CONNECTING) {
                        ALog.w(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "]ignore establish voice channel due to invalid state:" + LiveIntercom.this.connectState);
                        return;
                    }
                    LiveIntercom.this.startWaitTalkReadyTimer();
                    lvdo.lvdo lvdoVar = LiveIntercom.this.voiceChannel;
                    String str = this.f4993lvdo;
                    byte[] bArr = this.f4995lvif;
                    byte[] bArr2 = this.f4994lvfor;
                    lvdo lvdoVar2 = lvdo.this;
                    lvdoVar.lvdo(str, bArr, bArr2, lvdoVar2.f4989lvdo, LiveIntercom.this);
                }
            }

            C0249lvdo() {
            }

            @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
            public void onFailed(lvbyte.lvif lvifVar) {
                LiveIntercom.this.callbackError(new LiveIntercomException(2, lvifVar.lvdo(), lvifVar.lvfor()));
            }

            @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
            public void onResponse(lvbyte.lvif lvifVar) {
                try {
                    JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                    String string = object.getString("url");
                    JSONObject jSONObject = object.getJSONObject("cryptoKey");
                    if (!jSONObject.containsKey("iv") || !jSONObject.containsKey("key")) {
                        LiveIntercom.this.callbackError(new LiveIntercomException(2, AGCServerException.AUTHENTICATION_INVALID, "Not enough decrypt key or iv."));
                        return;
                    }
                    byte[] bytes = jSONObject.getBytes("iv");
                    LiveIntercom.this.handler.post(new RunnableC0250lvdo(string, jSONObject.getBytes("key"), bytes));
                } catch (JSONException e) {
                    e.printStackTrace();
                    LiveIntercom.this.callbackError(new LiveIntercomException(2, AGCServerException.AUTHENTICATION_INVALID, e));
                }
            }
        }

        lvdo(AudioParams audioParams, String str) {
            this.f4989lvdo = audioParams;
            this.f4991lvif = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.connectState != lvvoid.STATE_DISCONNECTED) {
                ALog.w(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] LiveIntercom has connected or connecting.");
                return;
            }
            if (!this.f4989lvdo.checkSupport()) {
                LiveIntercom.this.callbackError(new LiveIntercomException(1, "Audio params not support."));
                return;
            }
            LiveIntercom.this.transferState(lvvoid.STATE_CONNECTING);
            LiveIntercom liveIntercom = LiveIntercom.this;
            liveIntercom.encodeCodec = liveIntercom.getEncodeCodec(this.f4989lvdo);
            HashMap map = new HashMap();
            map.put("iotId", this.f4991lvif);
            map.put("encryptType", 0);
            APIHelper.sendIoTRequest(lvbyte.lvdo.VOICE_INTERCOM_START, map, this.f4991lvif, new C0249lvdo());
            if (LiveIntercom.DEBUG) {
                try {
                    LiveIntercom.this.audioSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LinkVisual_dump/audio/" + System.currentTimeMillis();
                    ALog.d(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] " + LiveIntercom.this.audioSavePath);
                    LiveIntercom liveIntercom2 = LiveIntercom.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append(LiveIntercom.this.audioSavePath);
                    sb.append("-up.wav");
                    liveIntercom2.upWavFileWriter = WavFileWriter.create(sb.toString(), this.f4989lvdo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class lvfor implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ LiveIntercomException f4997lvdo;

        lvfor(LiveIntercomException liveIntercomException) {
            this.f4997lvdo = liveIntercomException;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.onErrorListener != null) {
                LiveIntercom.this.onErrorListener.onError(this.f4997lvdo);
            }
        }
    }

    class lvgoto implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ byte[] f4999lvdo;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ int f5001lvif;

        lvgoto(byte[] bArr, int i) {
            this.f4999lvdo = bArr;
            this.f5001lvif = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.onAudioBufferReceiveListener != null) {
                LiveIntercom.this.onAudioBufferReceiveListener.onAudioBufferRecevie(this.f4999lvdo, this.f5001lvif);
            }
        }
    }

    class lvif implements Runnable {
        lvif() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.onTalkReadyListener != null) {
                LiveIntercom.this.onTalkReadyListener.onTalkReady();
            }
        }
    }

    class lvint implements Runnable {
        lvint() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.connectState != lvvoid.STATE_DISCONNECTED) {
                LiveIntercom.this.stopRunnable.run();
                LiveIntercom.this.callbackError(new LiveIntercomException(3, "wait talk ready timeout!"));
            }
        }
    }

    class lvlong implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ LiveIntercomException f5004lvdo;

        lvlong(LiveIntercomException liveIntercomException) {
            this.f5004lvdo = liveIntercomException;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.this.connectState != lvvoid.STATE_DISCONNECTED) {
                LiveIntercom.this.voiceChannel.lvdo();
            }
            LiveIntercom.this.callbackError(this.f5004lvdo);
        }
    }

    class lvnew implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ AudioParams f5006lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        final /* synthetic */ byte[] f5007lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ String f5008lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        final /* synthetic */ byte[] f5009lvint;

        lvnew(AudioParams audioParams, String str, byte[] bArr, byte[] bArr2) {
            this.f5006lvdo = audioParams;
            this.f5008lvif = str;
            this.f5007lvfor = bArr;
            this.f5009lvint = bArr2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.f5006lvdo.checkSupport()) {
                LiveIntercom.this.callbackError(new LiveIntercomException(1, "Audio params not support."));
                return;
            }
            if (LiveIntercom.this.connectState != lvvoid.STATE_DISCONNECTED) {
                ALog.w(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] LiveIntercom has connected or connecting.");
                return;
            }
            LiveIntercom.this.transferState(lvvoid.STATE_CONNECTING);
            LiveIntercom liveIntercom = LiveIntercom.this;
            liveIntercom.encodeCodec = liveIntercom.getEncodeCodec(this.f5006lvdo);
            LiveIntercom.this.startWaitTalkReadyTimer();
            LiveIntercom.this.voiceChannel.lvdo(this.f5008lvif, this.f5007lvfor, this.f5009lvint, this.f5006lvdo, LiveIntercom.this);
            if (LiveIntercom.DEBUG) {
                try {
                    LiveIntercom.this.audioSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LinkVisual_dump/audio/" + System.currentTimeMillis();
                    ALog.d(LiveIntercom.TAG, "[" + LiveIntercom.this.hashCode() + "] " + LiveIntercom.this.audioSavePath);
                    LiveIntercom liveIntercom2 = LiveIntercom.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append(LiveIntercom.this.audioSavePath);
                    sb.append("-up.wav");
                    liveIntercom2.upWavFileWriter = WavFileWriter.create(sb.toString(), this.f5006lvdo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class lvthis implements Runnable {
        lvthis() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LiveIntercom.this.transferState(lvvoid.STATE_CONNECTED);
        }
    }

    class lvtry implements Runnable {
        lvtry() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LiveIntercom.DEBUG) {
                try {
                    if (LiveIntercom.this.upWavFileWriter != null) {
                        LiveIntercom.this.upWavFileWriter.close();
                        LiveIntercom.this.upWavFileWriter = null;
                    }
                    if (LiveIntercom.this.downWavFileWriter != null) {
                        LiveIntercom.this.downWavFileWriter.close();
                        LiveIntercom.this.downWavFileWriter = null;
                    }
                    if (LiveIntercom.this.downOriginalWavFileWriter != null) {
                        LiveIntercom.this.downOriginalWavFileWriter.close();
                        LiveIntercom.this.downOriginalWavFileWriter = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LiveIntercom.this.stopWaitTalkReadyTimer();
            LiveIntercom.this.audioBuffers.clear();
            LiveIntercom.this.voiceChannel.lvdo();
            if (LiveIntercom.this.encodeCodec != null) {
                LiveIntercom.this.encodeCodec.lvfor();
                LiveIntercom.this.encodeCodec = null;
            }
            if (LiveIntercom.this.decodeCodec != null) {
                LiveIntercom.this.decodeCodec.lvfor();
                LiveIntercom.this.decodeCodec = null;
            }
        }
    }

    private enum lvvoid {
        STATE_DISCONNECTED,
        STATE_CONNECTING,
        STATE_CONNECTED
    }

    public LiveIntercom() {
        this(false);
    }

    @Deprecated
    public LiveIntercom(Context context, String str, String str2) {
        this();
    }

    protected LiveIntercom(boolean z) {
        this.connectState = lvvoid.STATE_DISCONNECTED;
        this.internalUse = false;
        this.audioBuffers = new LinkedList();
        this.stopRunnable = new lvtry();
        this.postRunnable = new lvcase();
        this.waitTalkReadyTimeoutTask = new lvint();
        this.internalUse = z;
        Version.printVersionInfo();
        LinkVisual.set_log_level(ALog.getLevel());
        initHandler();
        this.voiceChannel = new lvdo.lvif();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackError(LiveIntercomException liveIntercomException) {
        if (transferState(lvvoid.STATE_DISCONNECTED)) {
            this.stopRunnable.run();
            this.eventHandler.post(new lvfor(liveIntercomException));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public lvif.lvnew getDecodeCodec(AudioParams audioParams) {
        int audioType = audioParams.getAudioType();
        if (audioType == 1) {
            return lvif.lvfor.lvint();
        }
        if (audioType == 2) {
            return lvif.lvdo.lvdo(audioParams);
        }
        if (audioType != 3) {
            return null;
        }
        return lvif.lvint.lvint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public lvif.lvnew getEncodeCodec(AudioParams audioParams) {
        int audioType = audioParams.getAudioType();
        if (audioType == 1) {
            return lvif.lvfor.lvint();
        }
        if (audioType == 2) {
            return lvif.lvif.lvdo(audioParams);
        }
        if (audioType != 3) {
            return null;
        }
        return lvif.lvint.lvint();
    }

    private void initHandler() {
        HandlerThread handlerThread = new HandlerThread("LiveIntercomWorker");
        this.handlerThread = handlerThread;
        handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
        this.eventHandler = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWaitTalkReadyTimer() {
        ALog.d(TAG, "[" + hashCode() + "] startWaitTalkReadyTimer");
        this.handler.removeCallbacks(this.waitTalkReadyTimeoutTask);
        this.handler.postDelayed(this.waitTalkReadyTimeoutTask, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopWaitTalkReadyTimer() {
        ALog.d(TAG, "[" + hashCode() + "] stopWaitTalkReadyTimer");
        this.handler.removeCallbacks(this.waitTalkReadyTimeoutTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean transferState(lvvoid lvvoidVar) {
        if (this.connectState == lvvoidVar) {
            return false;
        }
        this.connectState = lvvoidVar;
        ALog.i(TAG, "[" + hashCode() + "] Transfer state to " + lvvoidVar.name());
        return true;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.handlerThread.quitSafely();
    }

    protected Handler getEventHandler() {
        return this.eventHandler;
    }

    @Override // lvdo.lvdo.InterfaceC0305lvdo
    public void onConnected() {
    }

    @Override // lvdo.lvdo.InterfaceC0305lvdo
    public void onData(byte[] bArr, int i) {
        ALog.d(TAG, "[" + hashCode() + "] onData size:" + i + " data.length:" + bArr.length);
        if (DEBUG) {
            try {
                WavFileWriter wavFileWriter = this.downOriginalWavFileWriter;
                if (wavFileWriter != null) {
                    wavFileWriter.write(bArr, 0, i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lvif.lvnew lvnewVar = this.decodeCodec;
        if (lvnewVar != null) {
            bArr = lvnewVar.lvif(bArr, 0, i);
            if (bArr == null) {
                return;
            } else {
                i = bArr.length;
            }
        }
        if (this.internalUse) {
            OnAudioBufferReceiveListener onAudioBufferReceiveListener = this.onAudioBufferReceiveListener;
            if (onAudioBufferReceiveListener != null) {
                onAudioBufferReceiveListener.onAudioBufferRecevie(bArr, i);
            }
        } else {
            this.eventHandler.post(new lvgoto(bArr, i));
        }
        if (DEBUG) {
            try {
                WavFileWriter wavFileWriter2 = this.downWavFileWriter;
                if (wavFileWriter2 != null) {
                    wavFileWriter2.write(bArr, 0, i);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // lvdo.lvdo.InterfaceC0305lvdo
    public void onError(LiveIntercomException liveIntercomException) {
        this.handler.postAtFrontOfQueue(new lvlong(liveIntercomException));
    }

    @Override // lvdo.lvdo.InterfaceC0305lvdo
    public void onHeaders(AudioParams audioParams) {
        this.handler.post(new lvchar(audioParams));
    }

    @Override // lvdo.lvdo.InterfaceC0305lvdo
    public void onTalkReady() {
        this.handler.post(new lvthis());
        stopWaitTalkReadyTimer();
        this.eventHandler.post(new lvif());
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void sendAudioBuffer(byte[] bArr, int i, int i2) {
        if (this.connectState != lvvoid.STATE_CONNECTED) {
            ALog.w(TAG, "[" + hashCode() + "] ignore send audio due to not connected.");
            return;
        }
        lvif.lvnew lvnewVar = this.encodeCodec;
        try {
            if (lvnewVar != null) {
                byte[] bArrLvdo = lvnewVar.lvdo(bArr, i, i2);
                if (bArrLvdo != null) {
                    this.audioBuffers.add(bArrLvdo);
                    if (DEBUG) {
                        WavFileWriter wavFileWriter = this.upWavFileWriter;
                        if (wavFileWriter != null) {
                            wavFileWriter.write(bArrLvdo, 0, bArrLvdo.length);
                        }
                    }
                }
            } else {
                if (i == 0 && bArr.length == i2) {
                    this.audioBuffers.add(bArr);
                } else {
                    byte[] bArr2 = new byte[i2];
                    System.arraycopy(bArr, i, bArr2, 0, i2);
                    this.audioBuffers.add(bArr2);
                }
                if (DEBUG) {
                    WavFileWriter wavFileWriter2 = this.upWavFileWriter;
                    if (wavFileWriter2 != null) {
                        wavFileWriter2.write(bArr, i, bArr.length);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler.post(this.postRunnable);
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void setOnAudioBufferReceiveListener(OnAudioBufferReceiveListener onAudioBufferReceiveListener) {
        this.onAudioBufferReceiveListener = onAudioBufferReceiveListener;
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void setOnAudioParamsChangeListener(OnAudioParamsChangeListener onAudioParamsChangeListener) {
        this.onAudioParamsChangeListener = onAudioParamsChangeListener;
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void setOnTalkReadyListener(OnTalkReadyListener onTalkReadyListener) {
        this.onTalkReadyListener = onTalkReadyListener;
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void start(String str, AudioParams audioParams) {
        lvfor.lvdo.lvif().lvdo(str);
        this.handler.post(new lvdo(audioParams, str));
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void start(String str, byte[] bArr, byte[] bArr2, AudioParams audioParams) {
        this.handler.post(new lvnew(audioParams, str, bArr, bArr2));
    }

    @Override // com.aliyun.iotx.linkvisual.media.audio.ILiveIntercom
    public void stop() {
        this.handler.post(this.stopRunnable);
        this.handler.post(new lvbyte());
    }
}
