package com.aliyun.iotx.linkvisual.media.video;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.TextureView;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.LinkVisualMedia;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.TransQualityStatisticEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.TransQualityStatisticParams;
import com.aliyun.iotx.linkvisual.media.video.HardwareDecoderable;
import com.aliyun.iotx.linkvisual.media.video.beans.FrameColor;
import com.aliyun.iotx.linkvisual.media.video.beans.PlayInfo;
import com.aliyun.iotx.linkvisual.media.video.beans.PlayerStoppedDrawingMode;
import com.aliyun.iotx.linkvisual.media.video.beans.SeiInfoBuffer;
import com.aliyun.iotx.linkvisual.media.video.beans.StreamConnectType;
import com.aliyun.iotx.linkvisual.media.video.beans.Yuv420pFrame;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnExternalRenderListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnRenderedFirstFrameListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnSeiInfoListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnVideoSizeChangedListener;
import com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig;
import com.aliyun.iotx.linkvisual.media.video.processing.IVideoFrameProcessor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class lvdo implements HardwareDecoderable {
    public static final String TAG = "linksdk_lv_BasePlayer";
    protected Handler lvboolean;

    /* JADX INFO: renamed from: lvbreak, reason: collision with root package name */
    protected SimpleStreamAudioTrack f5077lvbreak;

    /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
    protected String f5078lvbyte;

    /* JADX INFO: renamed from: lvcase, reason: collision with root package name */
    private lvtry.lvdo f5079lvcase;

    /* JADX INFO: renamed from: lvchar, reason: collision with root package name */
    private GLSurfaceView f5081lvchar;
    private OnExternalRenderListener lvcontinue;
    protected Handler lvdefault;
    protected Context lvfinal;

    /* JADX INFO: renamed from: lvgoto, reason: collision with root package name */
    private TextureView f5084lvgoto;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    protected String f5085lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    protected byte[] f5086lvint;
    protected SeiInfoBuffer lvinterface;
    protected OnPreparedListener lvnative;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    protected byte[] f5088lvnew;
    private IVideoFrameProcessor lvprivate;
    private OnRenderedFirstFrameListener lvpublic;
    private OnPlayerStateChangedListener lvreturn;
    private OnErrorListener lvstatic;
    private Yuv420pFrame lvstrictfp;
    private OnVideoSizeChangedListener lvswitch;

    /* JADX INFO: renamed from: lvthis, reason: collision with root package name */
    private int f5089lvthis;
    private HandlerThread lvthrows;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    protected P2PConfig f5090lvtry;

    /* JADX INFO: renamed from: lvvoid, reason: collision with root package name */
    private int f5091lvvoid;
    private OnSeiInfoListener lvvolatile;

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private HashMap<String, Object> f5082lvdo = new HashMap<>(4);

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    protected boolean f5083lvfor = false;

    /* JADX INFO: renamed from: lvlong, reason: collision with root package name */
    private int f5087lvlong = 2;

    /* JADX INFO: renamed from: lvcatch, reason: collision with root package name */
    private float f5080lvcatch = 1.0f;
    private int lvclass = 3;
    private BlockingQueue<byte[]> lvelse = new LinkedBlockingQueue();
    protected ByteBuffer lvconst = ByteBuffer.allocateDirect(8192);
    protected int lvfloat = 0;
    protected int lvshort = 0;
    private int lvsuper = 1;
    protected boolean lvthrow = false;
    protected boolean lvwhile = false;
    private boolean lvdouble = false;
    private PlayerStoppedDrawingMode lvimport = PlayerStoppedDrawingMode.KEEP_LAST_FRAME_WITHOUT_ERROR;
    private WeakHashMap<SurfaceView, Boolean> lvextends = new WeakHashMap<>();
    private AtomicBoolean lvfinally = new AtomicBoolean(false);
    private boolean lvpackage = false;
    private boolean lvabstract = false;
    protected final byte[] lvprotected = new byte[1];
    protected long lvtransient = 0;
    protected long lvimplements = 0;
    protected long lvinstanceof = 0;
    protected long lvsynchronized = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected long f5073a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected long f5074b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected long f5075c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Runnable f5076d = new lvint();
    protected final ILvStreamCallback e = new lvnew();
    protected final Runnable f = new lvtry();
    private final Runnable g = new lvbyte();
    private final Runnable h = new lvcase();

    class lvbyte implements Runnable {
        lvbyte() {
        }

        @Override // java.lang.Runnable
        public void run() {
            lvdo.this.f.run();
            if (lvdo.this.f5077lvbreak != null) {
                ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] release SimpleStreamAudioTrack ");
                lvdo.this.f5077lvbreak.release();
                lvdo.this.f5077lvbreak = null;
            }
            lvdo.this.clearSurfaceView();
            lvdo.this.clearTextureView();
            lvdo.this.lvchar();
            LinkVisualMedia.getInstance().internalDeinit(lvdo.this.hashCode());
        }
    }

    class lvcase implements Runnable {
        lvcase() {
        }

        @Override // java.lang.Runnable
        public void run() {
            lvdo.this.lvif();
            lvdo.this.lvgoto();
            lvdo.this.lvif(1);
        }
    }

    class lvchar implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ int f5094lvdo;

        lvchar(int i) {
            this.f5094lvdo = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (lvdo.this.lvprotected) {
                if (lvdo.this.lvreturn != null) {
                    lvdo.this.lvreturn.onPlayerStateChange(this.f5094lvdo);
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.lvdo$lvdo, reason: collision with other inner class name */
    static /* synthetic */ class C0252lvdo {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f5096lvdo;

        static {
            int[] iArr = new int[lvnew.lvfor.values().length];
            f5096lvdo = iArr;
            try {
                iArr[lvnew.lvfor.EVENT_TRANSQUALITY_TRACKINFO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_DISCONNECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_STREAM_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_P2P_CONNECT_OK.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_RTMP_CONNECT_OK.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_SWITCH_TO_P2P_STREAM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_DEC_ERROR_FOR_MOMENT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_RECV_FIRST_VIDEO_PACKAET.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_RECEIVE_FIRST_K_FRAME.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f5096lvdo[lvnew.lvfor.EVENT_RESOLUTION_CHANGE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    class lvfor implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ int f5097lvdo;

        lvfor(int i) {
            this.f5097lvdo = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            lvdo lvdoVar = lvdo.this;
            SimpleStreamAudioTrack simpleStreamAudioTrack = lvdoVar.f5077lvbreak;
            if (simpleStreamAudioTrack == null) {
                lvdoVar.lvclass = this.f5097lvdo;
            } else {
                simpleStreamAudioTrack.reloadWithStreamType(this.f5097lvdo);
            }
        }
    }

    class lvgoto implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ PlayerException f5099lvdo;

        lvgoto(PlayerException playerException) {
            this.f5099lvdo = playerException;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (lvdo.this.lvprotected) {
                if (lvdo.this.lvstatic != null) {
                    lvdo.this.lvstatic.onError(this.f5099lvdo);
                }
            }
        }
    }

    class lvif implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ FrameColor f5101lvdo;

        lvif(FrameColor frameColor) {
            this.f5101lvdo = frameColor;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (lvdo.this.isInvalidHandle()) {
                return;
            }
            int i = lvdo.this.lvfloat;
            FrameColor frameColor = this.f5101lvdo;
            LinkVisual.set_stream_color(i, frameColor.brightness, frameColor.contrast, frameColor.saturation, frameColor.hue);
        }
    }

    class lvint implements Runnable {
        lvint() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] start: " + lvdo.this.lvfloat);
            if (lvdo.this.isInvalidHandle()) {
                lvdo.this.lvif(2);
                lvdo lvdoVar = lvdo.this;
                lvdoVar.lvthrow = false;
                lvdoVar.lvfloat = lvdoVar.lvlong();
                if (lvdo.this.isInvalidHandle()) {
                    lvdo.this.lvif(4);
                }
                ALog.i(lvdo.TAG, "[" + lvdo.this.hashCode() + "] openstream: " + lvdo.this.lvfloat);
                return;
            }
            if (lvdo.this.getPlayerType() != lvnew.lvif.FILE && lvdo.this.getPlayerType() != lvnew.lvif.HLS) {
                ALog.w(lvdo.TAG, "[" + lvdo.this.hashCode() + "] ignore start due to LivePlayer not support resume.");
                return;
            }
            LinkVisual.pause_stream(lvdo.this.lvfloat, false);
            SimpleStreamAudioTrack simpleStreamAudioTrack = lvdo.this.f5077lvbreak;
            if (simpleStreamAudioTrack == null || simpleStreamAudioTrack.getPlayState() != 2) {
                return;
            }
            lvdo.this.f5077lvbreak.resume();
        }
    }

    protected class lvlong implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private String f5104lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private byte[] f5105lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private boolean f5106lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private byte[] f5107lvint;

        /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
        private P2PConfig f5108lvnew;

        public lvlong(String str, boolean z, byte[] bArr, byte[] bArr2, P2PConfig p2PConfig) {
            this.f5104lvdo = str;
            this.f5106lvif = z;
            this.f5105lvfor = bArr;
            this.f5107lvint = bArr2;
            this.f5108lvnew = p2PConfig;
        }

        @Override // java.lang.Runnable
        public void run() {
            lvdo lvdoVar = lvdo.this;
            String str = this.f5104lvdo;
            lvdoVar.f5085lvif = str;
            lvdoVar.f5078lvbyte = lvbyte.lvnew.lvdo(str);
            lvdo lvdoVar2 = lvdo.this;
            lvdoVar2.f5083lvfor = this.f5106lvif;
            lvdoVar2.f5086lvint = this.f5105lvfor;
            lvdoVar2.f5088lvnew = this.f5107lvint;
            lvdoVar2.f5090lvtry = this.f5108lvnew;
        }
    }

    class lvnew implements ILvStreamCallback {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private AudioParams f5110lvdo;

        /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.lvdo$lvnew$lvdo, reason: collision with other inner class name */
        class RunnableC0253lvdo implements Runnable {
            RunnableC0253lvdo() {
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (lvdo.this.lvprotected) {
                    if (lvdo.this.lvpublic != null) {
                        lvdo.this.lvpublic.onRenderedFirstFrame();
                    }
                }
            }
        }

        class lvfor implements Runnable {
            lvfor() {
            }

            @Override // java.lang.Runnable
            public void run() {
                ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] EVENT_STREAM_ERROR reset mState to END.");
                lvdo.this.f.run();
                lvdo.this.lvdo(new PlayerException(8, PlayerException.SUB_CODE_UNEXPECTED_PULL_STREAM_ERROR, "Pull stream failed!"));
            }
        }

        class lvif implements Runnable {
            lvif() {
            }

            @Override // java.lang.Runnable
            public void run() {
                ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] EVENT_DISCONNECT reset mState to END.");
                lvdo.this.f.run();
                lvdo.this.lvdo(new PlayerException(6, 1005, "Source connect failed!"));
            }
        }

        class lvint implements Runnable {
            lvint() {
            }

            @Override // java.lang.Runnable
            public void run() {
                ALog.e(lvdo.TAG, "[" + lvdo.this.hashCode() + "] EVENT_DEC_ERROR_FOR_MOMENT.");
                lvdo.this.f.run();
                lvdo.this.lvdo(new PlayerException(7, 1000, "Decode video frame error!"));
            }
        }

        /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.lvdo$lvnew$lvnew, reason: collision with other inner class name */
        class RunnableC0254lvnew implements Runnable {

            /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
            final /* synthetic */ int f5116lvdo;

            /* JADX INFO: renamed from: lvif, reason: collision with root package name */
            final /* synthetic */ int f5118lvif;

            RunnableC0254lvnew(int i, int i2) {
                this.f5116lvdo = i;
                this.f5118lvif = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (lvdo.this.lvprotected) {
                    if (lvdo.this.lvswitch != null) {
                        lvdo.this.lvswitch.onVideoSizeChanged(this.f5116lvdo, this.f5118lvif);
                    }
                }
            }
        }

        lvnew() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioDataReceived(int i, int i2) {
            ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] onAudioDataReceived: playHandle=" + i + "\t size:" + i2);
            lvdo lvdoVar = lvdo.this;
            if (lvdoVar.lvfloat != i || lvdoVar.lvthrow || lvdoVar.f5077lvbreak == null) {
                return;
            }
            if (lvdoVar.lvsuper == 3 || lvdo.this.lvsuper == 2) {
                byte[] bArr = new byte[i2];
                lvdo.this.lvconst.get(bArr);
                lvdo.this.lvelse.add(bArr);
                lvdo.this.lvconst.clear();
                ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] audioBuffer size: " + lvdo.this.lvelse.size());
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onAudioParamsReceived(int i, int i2, int i3, int i4, int i5) {
            AudioParams audioParams = new AudioParams(i2, i3, i5);
            audioParams.setBitsPerSample(i4);
            ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] onAudioParamsReceived: playHandle=" + i + "\t audioParams=" + audioParams.toString());
            if (lvdo.this.lvfloat == i) {
                if (!audioParams.equals(this.f5110lvdo)) {
                    this.f5110lvdo = audioParams;
                    SimpleStreamAudioTrack simpleStreamAudioTrack = lvdo.this.f5077lvbreak;
                    if (simpleStreamAudioTrack != null) {
                        simpleStreamAudioTrack.release();
                    }
                    try {
                        lvdo lvdoVar = lvdo.this;
                        lvdoVar.f5077lvbreak = new SimpleStreamAudioTrack(audioParams, lvdoVar.lvclass, lvdo.this.lvelse);
                        if (lvdo.this.getPlayerType() == lvnew.lvif.LIVE) {
                            lvdo.this.f5077lvbreak.setMode(1);
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        lvdo.this.f5077lvbreak = null;
                    }
                }
                SimpleStreamAudioTrack simpleStreamAudioTrack2 = lvdo.this.f5077lvbreak;
                if (simpleStreamAudioTrack2 != null) {
                    simpleStreamAudioTrack2.start();
                    lvdo lvdoVar2 = lvdo.this;
                    lvdoVar2.f5077lvbreak.setVolume(lvdoVar2.f5080lvcatch);
                }
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onEvent(int i, int i2, String str) {
            Handler handler;
            Runnable lvifVar;
            lvnew.lvfor lvforVarLvdo = lvnew.lvfor.lvdo(i2);
            if (lvforVarLvdo == null) {
                return;
            }
            if (C0252lvdo.f5096lvdo[lvforVarLvdo.ordinal()] == 1) {
                try {
                    lvfor.lvdo.lvif().lvdo(TransQualityStatisticEvent.newBuilder().code(200).params(lvdo.this.lvdo(TransQualityStatisticParams.parseFromJSONString(str))).build());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (lvdo.this.lvfloat == i) {
                switch (C0252lvdo.f5096lvdo[lvforVarLvdo.ordinal()]) {
                    case 2:
                        handler = lvdo.this.lvboolean;
                        lvifVar = new lvif();
                        handler.post(lvifVar);
                        break;
                    case 3:
                        handler = lvdo.this.lvboolean;
                        lvifVar = new lvfor();
                        handler.post(lvifVar);
                        break;
                    case 4:
                        lvdo.this.lvsynchronized = System.currentTimeMillis();
                        break;
                    case 5:
                        lvdo lvdoVar = lvdo.this;
                        if (lvdoVar.f5073a == 0) {
                            lvdoVar.lvinstanceof = System.currentTimeMillis();
                        }
                        break;
                    case 6:
                        lvdo.this.f5073a = System.currentTimeMillis();
                        break;
                    case 7:
                        handler = lvdo.this.lvboolean;
                        lvifVar = new lvint();
                        handler.post(lvifVar);
                        break;
                    case 8:
                        lvdo.this.f5075c = System.currentTimeMillis();
                        break;
                    case 9:
                        if (lvdo.this.lvfinally.get()) {
                            lvdo.this.lvif(3);
                        }
                        break;
                    case 10:
                        if (!TextUtils.isEmpty(str)) {
                            String[] strArrSplit = str.split("x");
                            lvdo.this.lvdefault.post(new RunnableC0254lvnew(Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1])));
                        }
                        break;
                }
            }
            lvdo.this.lvdo(i, lvforVarLvdo, str);
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onSeiInfoUpdate(int i, int i2, long j) {
            lvdo lvdoVar;
            SeiInfoBuffer seiInfoBuffer;
            lvdo lvdoVar2 = lvdo.this;
            if (lvdoVar2.lvfloat != i || lvdoVar2.lvvolatile == null || (seiInfoBuffer = (lvdoVar = lvdo.this).lvinterface) == null) {
                return;
            }
            seiInfoBuffer.length = i2;
            seiInfoBuffer.timeStamp = j;
            lvdoVar.lvvolatile.onSeiInfoUpdate(lvdo.this.lvinterface);
            lvdo.this.lvinterface.seiDirectBuffer.clear();
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback
        public void onVideoFrameUpdate(int i, int i2, int i3) {
            ALog.d(lvdo.TAG, "[" + lvdo.this.hashCode() + "] onVideoFrameUpdate: stream[" + lvdo.this.lvfloat + "] playHandle[" + i + "]");
            if (lvdo.this.f5089lvthis != i2 || lvdo.this.f5091lvvoid != i3) {
                lvdo.this.f5089lvthis = i2;
                lvdo.this.f5091lvvoid = i3;
                if (lvdo.this.lvstrictfp != null && (lvdo.this.useVideoFrameProcessing() || lvdo.this.useExternalRender() || lvdo.this.lvimport == PlayerStoppedDrawingMode.ALWAYS_KEEP_LAST_FRAME)) {
                    lvdo.this.lvstrictfp.relocateDirectBuffer(i2, i3);
                }
            }
            if (lvdo.this.lvfinally.compareAndSet(false, true)) {
                if (lvdo.this.useVideoFrameProcessing() || lvdo.this.useExternalRender()) {
                    lvdo.this.lvstrictfp = new Yuv420pFrame(i2, i3);
                }
                lvdo.this.f5074b = System.currentTimeMillis();
                lvdo.this.lvdefault.post(new RunnableC0253lvdo());
                if (lvdo.this.f5079lvcase != null && !lvdo.this.lvabstract) {
                    lvdo.this.f5079lvcase.lvdo(lvdo.this.f5087lvlong);
                }
                lvdo.this.lvif(3);
                lvdo.this.lvcase();
            }
            if (lvdo.this.f5079lvcase != null && !lvdo.this.lvabstract) {
                lvdo.this.f5079lvcase.lvdo(false);
            } else if (lvdo.this.lvabstract) {
                lvdo.this.lvcontinue.onVideoFrameUpdate(i2, i3, System.currentTimeMillis());
            }
        }
    }

    class lvtry implements Runnable {
        lvtry() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (lvdo.this.isInvalidHandle()) {
                return;
            }
            lvdo.this.lvdo();
            lvdo lvdoVar = lvdo.this;
            int i = lvdoVar.lvfloat;
            if (!lvdoVar.lvthis()) {
                ALog.w(lvdo.TAG, "[" + lvdo.this.hashCode() + "] ignore stop due to onStop() check failed.");
                return;
            }
            lvdo.this.stopRecordingContent();
            lvdo.this.lvfloat = 0;
            LinkVisual.close_stream(i);
            ALog.i(lvdo.TAG, "[" + lvdo.this.hashCode() + "] stop: " + i + " mUrl:" + lvdo.this.f5085lvif);
            SimpleStreamAudioTrack simpleStreamAudioTrack = lvdo.this.f5077lvbreak;
            if (simpleStreamAudioTrack != null) {
                simpleStreamAudioTrack.stop();
            }
            lvdo.this.lvfinally.set(false);
            lvdo.this.lvif(4);
        }
    }

    static {
        String str;
        if (lvbyte.lvint.lvdo()) {
            str = Environment.getExternalStorageDirectory() + "/LinkVisual_dump";
        } else {
            str = null;
        }
        LinkVisual.set_lv_dump_dir(str);
    }

    public lvdo(Context context) {
        ALog.i(TAG, "Using Global Mode= " + LinkVisualMedia.getInstance().isInit());
        LinkVisualMedia.getInstance().internalInit(hashCode());
        if (context != null) {
            if (!(context instanceof Application)) {
                throw new IllegalArgumentException("Context should be Application Context.");
            }
            this.lvfinal = context;
        }
        setDecoderStrategy(HardwareDecoderable.DecoderStrategy.FORCE_SOFTWARE);
        Version.printVersionInfo();
        LinkVisual.set_log_level(ALog.getLevel());
        lvfor();
    }

    private void lvbreak() {
        if (this.lvextends.get(this.f5081lvchar) == null) {
            this.f5079lvcase = new lvtry.lvif(this.f5081lvchar, this);
            this.f5081lvchar.setEGLContextClientVersion(2);
            this.f5081lvchar.setRenderer((lvtry.lvif) this.f5079lvcase);
            this.f5081lvchar.setRenderMode(0);
            this.lvextends.put(this.f5081lvchar, Boolean.TRUE);
        }
    }

    private boolean lvbyte() {
        return this.f5081lvchar == null && this.f5084lvgoto != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvdo() {
        if (this.lvimport == PlayerStoppedDrawingMode.ALWAYS_KEEP_LAST_FRAME) {
            if (this.lvstrictfp == null) {
                this.lvstrictfp = new Yuv420pFrame(this.f5089lvthis, this.f5091lvvoid);
            }
            Yuv420pFrame yuvFrame = getYuvFrame();
            StringBuilder sb = new StringBuilder();
            sb.append("cache last frame [");
            sb.append(this.lvstrictfp.width);
            sb.append(",");
            sb.append(this.lvstrictfp.height);
            sb.append("] ");
            sb.append(yuvFrame != null ? "success" : "failed");
            ALog.d(TAG, sb.toString());
        }
    }

    private void lvfor() {
        HandlerThread handlerThread = new HandlerThread("PlayerWorker");
        this.lvthrows = handlerThread;
        handlerThread.start();
        this.lvboolean = new Handler(this.lvthrows.getLooper());
        this.lvdefault = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
    }

    private boolean lvtry() {
        return this.f5081lvchar != null && this.f5084lvgoto == null;
    }

    public void addDebugConfig(String str, Object obj) {
    }

    public void clearSurfaceView() {
        GLSurfaceView gLSurfaceView = this.f5081lvchar;
        if (gLSurfaceView != null) {
            this.lvextends.remove(gLSurfaceView);
        }
        this.f5081lvchar = null;
    }

    public void clearTextureView() {
        this.f5084lvgoto = null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public PlayInfo getCurrentPlayInfo() {
        PlayInfo playInfo = new PlayInfo();
        LinkVisual.get_current_play_info(this.lvfloat, playInfo);
        return playInfo;
    }

    public long getCurrentRecordingContentDuration() {
        return LinkVisual.get_current_recording_content_duration(this.lvfloat);
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.HardwareDecoderable
    public HardwareDecoderable.DecoderType getDecoderType() {
        if (isInvalidHandle()) {
            return null;
        }
        return HardwareDecoderable.DecoderType.parseInt(LinkVisual.get_decoder_type(this.lvfloat));
    }

    public FrameColor getFrameColor() {
        if (isInvalidHandle()) {
            return null;
        }
        FrameColor frameColor = new FrameColor();
        LinkVisual.get_stream_color(this.lvfloat, frameColor);
        return frameColor;
    }

    public int getHandle() {
        return this.lvfloat;
    }

    public int getPlayState() {
        return this.lvsuper;
    }

    public PlayerStoppedDrawingMode getPlayerStoppedDrawingMode() {
        return this.lvimport;
    }

    protected abstract lvnew.lvif getPlayerType();

    public JSONObject getStatisticsInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("prepare_time_in_ms", this.lvimplements - this.lvtransient);
            long j = this.lvinstanceof - this.lvtransient;
            if (j > 0) {
                jSONObject.put("rtmp_connected_time_in_ms", j);
            }
            long j2 = this.lvsynchronized - this.lvtransient;
            if (j2 > 0) {
                jSONObject.put("p2p_connected_time_in_ms", j2);
            }
            long j3 = this.f5073a - this.lvtransient;
            if (j3 > 0) {
                jSONObject.put("switch_to_p2p_stream_time_in_ms", j3);
            }
            long j4 = this.f5074b - this.lvtransient;
            if (j4 > 0) {
                jSONObject.put("first_iframe_in_ms", j4);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public StreamConnectType getStreamConnectType() {
        return StreamConnectType.parseInt(LinkVisual.get_stream_connect_type(this.lvfloat));
    }

    public TextureView getTextureView() {
        return this.f5084lvgoto;
    }

    public IVideoFrameProcessor getVideoFrameProcessor() {
        return this.lvprivate;
    }

    public int getVideoHeight() {
        return this.f5091lvvoid;
    }

    public int getVideoWidth() {
        return this.f5089lvthis;
    }

    public float getVolume() {
        return this.f5080lvcatch;
    }

    public Yuv420pFrame getYuvFrame() {
        if (this.lvstrictfp == null) {
            return null;
        }
        if (isInvalidHandle()) {
            return this.lvstrictfp;
        }
        Yuv420pFrame yuv420pFrame = this.lvstrictfp;
        if (LinkVisual.get_yuv420p_frame(yuv420pFrame, yuv420pFrame.getDirectBuffer(), this.lvfloat)) {
            return this.lvstrictfp;
        }
        return null;
    }

    public boolean isInvalidHandle() {
        return this.lvfloat <= 0;
    }

    protected void lvcase() {
    }

    protected void lvchar() {
        clearSurfaceView();
        synchronized (this.lvprotected) {
            this.lvnative = null;
            this.lvpublic = null;
            this.lvreturn = null;
            this.lvstatic = null;
        }
    }

    protected TransQualityStatisticParams lvdo(TransQualityStatisticParams transQualityStatisticParams) {
        return transQualityStatisticParams;
    }

    protected void lvdo(int i) {
        if (i == 4) {
            this.lvfinally.set(false);
            if (this.lvimport == PlayerStoppedDrawingMode.ALWAYS_BLACK) {
                this.f5079lvcase.lvdo(true);
            }
        }
    }

    protected void lvdo(int i, lvnew.lvfor lvforVar, String str) {
    }

    protected void lvdo(PlayerException playerException) {
        PlayerStoppedDrawingMode playerStoppedDrawingMode;
        lvtry.lvdo lvdoVar = this.f5079lvcase;
        if (lvdoVar != null && !this.lvabstract && ((playerStoppedDrawingMode = this.lvimport) == PlayerStoppedDrawingMode.KEEP_LAST_FRAME_WITHOUT_ERROR || playerStoppedDrawingMode == PlayerStoppedDrawingMode.ALWAYS_BLACK)) {
            lvdoVar.lvdo(true);
        }
        this.lvdefault.post(new lvgoto(playerException));
        lvif(playerException);
    }

    protected void lvdo(String str, boolean z, byte[] bArr, byte[] bArr2, P2PConfig p2PConfig) throws IllegalArgumentException {
    }

    protected void lvgoto() {
    }

    protected void lvif() {
        this.f5085lvif = null;
        this.f5086lvint = null;
        this.f5088lvnew = null;
        this.f5090lvtry = null;
        this.f5078lvbyte = null;
    }

    protected synchronized void lvif(int i) {
        if (this.lvsuper == i) {
            return;
        }
        ALog.d(TAG, "[" + hashCode() + "] transferState from " + this.lvsuper + " to " + i);
        this.lvsuper = i;
        lvdo(i);
        this.lvdefault.post(new lvchar(i));
    }

    protected void lvif(PlayerException playerException) {
    }

    protected boolean lvint() {
        Object obj = this.f5082lvdo.get("forcerelay");
        return obj != null && ((Boolean) obj).booleanValue();
    }

    protected int lvlong() {
        return 0;
    }

    protected boolean lvnew() {
        P2PConfig p2PConfig = this.f5090lvtry;
        return p2PConfig != null && p2PConfig.isValid() && (Version.isIlop || Version.isTg);
    }

    protected boolean lvthis() {
        return true;
    }

    protected void lvvoid() {
        this.lvimplements = 0L;
        this.lvinstanceof = 0L;
        this.lvsynchronized = 0L;
        this.f5074b = 0L;
        this.f5073a = 0L;
        this.f5075c = 0L;
    }

    public void release() {
        if (this.lvdouble) {
            return;
        }
        this.lvboolean.post(this.g);
        this.lvthrows.quitSafely();
        this.lvdouble = true;
    }

    public void reset() {
        this.lvboolean.post(this.f);
        this.lvboolean.post(this.h);
    }

    public void setAudioStreamType(int i) {
        this.lvboolean.post(new lvfor(i));
    }

    public void setDataSource(String str) throws IllegalArgumentException {
        this.lvwhile = true;
        lvdo(str, false, null, null, null);
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.HardwareDecoderable
    public void setDecoderStrategy(HardwareDecoderable.DecoderStrategy decoderStrategy) {
        LinkVisual.set_decoder_strategy(decoderStrategy.ordinal());
    }

    public void setFrameColor(FrameColor frameColor) {
        this.lvboolean.post(new lvif(frameColor));
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.lvstatic = onErrorListener;
    }

    public void setOnExternalRenderListener(OnExternalRenderListener onExternalRenderListener) {
        this.lvcontinue = onExternalRenderListener;
    }

    public void setOnPlayerStateChangedListener(OnPlayerStateChangedListener onPlayerStateChangedListener) {
        this.lvreturn = onPlayerStateChangedListener;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.lvnative = onPreparedListener;
    }

    public void setOnRenderedFirstFrameListener(OnRenderedFirstFrameListener onRenderedFirstFrameListener) {
        this.lvpublic = onRenderedFirstFrameListener;
    }

    public void setOnSeiInfoListener(SeiInfoBuffer seiInfoBuffer, OnSeiInfoListener onSeiInfoListener) {
        this.lvinterface = seiInfoBuffer;
        this.lvvolatile = onSeiInfoListener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.lvswitch = onVideoSizeChangedListener;
    }

    public void setPlayerStoppedDrawingMode(PlayerStoppedDrawingMode playerStoppedDrawingMode) {
        this.lvimport = playerStoppedDrawingMode;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        if (!(surfaceView instanceof GLSurfaceView)) {
            throw new IllegalArgumentException("surfaceview must be the instance of GLSurfaceView.");
        }
        if (lvbyte()) {
            throw new RuntimeException("TextureView is using, can not change to GLSurfaceView!!!");
        }
        this.f5081lvchar = (GLSurfaceView) surfaceView;
        lvbreak();
    }

    public void setTextureView(TextureView textureView) {
        if (lvtry()) {
            throw new RuntimeException("GLSurfaceView is using, can not change to TextureView!!!");
        }
        this.f5084lvgoto = textureView;
        lvtry.lvfor lvforVar = new lvtry.lvfor(this);
        this.f5079lvcase = lvforVar;
        this.f5084lvgoto.setSurfaceTextureListener(lvforVar);
    }

    public void setUseExternalRender(boolean z) {
        this.lvabstract = z;
    }

    public void setUseVideoFrameProcessing(boolean z) {
        this.lvpackage = z;
    }

    public void setVideoFrameProcessor(IVideoFrameProcessor iVideoFrameProcessor) {
        this.lvprivate = iVideoFrameProcessor;
    }

    public void setVideoScalingMode(int i) {
        this.f5087lvlong = i;
        lvtry.lvdo lvdoVar = this.f5079lvcase;
        if (lvdoVar != null) {
            lvdoVar.lvdo(i);
        }
    }

    public void setVolume(float f) {
        this.f5080lvcatch = f;
        SimpleStreamAudioTrack simpleStreamAudioTrack = this.f5077lvbreak;
        if (simpleStreamAudioTrack != null) {
            simpleStreamAudioTrack.setVolume(f);
        }
    }

    public Bitmap snapShot() {
        StringBuilder sb;
        String str;
        int i;
        int i2;
        int i3;
        IVideoFrameProcessor iVideoFrameProcessor;
        int i4 = this.f5091lvvoid;
        if (i4 == 0 || (i = this.f5089lvthis) == 0) {
            sb = new StringBuilder();
        } else {
            byte[] bArr = new byte[((i4 * i) * 3) / 2];
            if (this.lvstrictfp != null) {
                getYuvFrame();
                if (useVideoFrameProcessing() && (iVideoFrameProcessor = this.lvprivate) != null) {
                    iVideoFrameProcessor.processing(this.lvstrictfp);
                }
                try {
                    int iPosition = this.lvstrictfp.getDirectBuffer().position();
                    this.lvstrictfp.getDirectBuffer().get(bArr);
                    this.lvstrictfp.getDirectBuffer().position(iPosition);
                    Yuv420pFrame yuv420pFrame = this.lvstrictfp;
                    i3 = yuv420pFrame.width;
                    i2 = yuv420pFrame.height;
                } catch (Exception e) {
                    e.printStackTrace();
                    sb = new StringBuilder();
                    sb.append("[");
                    sb.append(hashCode());
                    str = "] failed to snapShot";
                }
            } else if (LinkVisual.get_yuv420p_frame_data(this.lvfloat, bArr, i, i4)) {
                i3 = this.f5089lvthis;
                i2 = this.f5091lvvoid;
            } else {
                i2 = 0;
                i3 = 0;
            }
            if (i2 != 0 && i3 != 0) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                new YuvImage(lvbyte.lvtry.lvdo(bArr, i3, i2), 17, i3, i2, null).compressToJpeg(new Rect(0, 0, i3, i2), 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            sb = new StringBuilder();
        }
        sb.append("[");
        sb.append(hashCode());
        str = "] no content to snapShot";
        sb.append(str);
        ALog.w(TAG, sb.toString());
        return null;
    }

    public boolean snapShotToFile(File file) {
        IVideoFrameProcessor iVideoFrameProcessor;
        if (this.f5091lvvoid == 0 || this.f5089lvthis == 0) {
            ALog.w(TAG, "[" + hashCode() + "] no content to snapShot");
            return false;
        }
        if (this.lvstrictfp != null) {
            getYuvFrame();
            if (useVideoFrameProcessing() && (iVideoFrameProcessor = this.lvprivate) != null) {
                iVideoFrameProcessor.processing(this.lvstrictfp);
            }
        }
        int handle = getHandle();
        String absolutePath = file.getAbsolutePath();
        Yuv420pFrame yuv420pFrame = this.lvstrictfp;
        return LinkVisual.snapshot_yuv_to_jpeg(handle, absolutePath, yuv420pFrame != null ? yuv420pFrame.getDirectBuffer() : null, this.f5089lvthis, this.f5091lvvoid);
    }

    public void start() {
        this.lvboolean.post(this.f5076d);
    }

    public boolean startRecordingContent(File file) throws IOException {
        if (!file.getName().toLowerCase().endsWith(".mp4")) {
            throw new IllegalArgumentException("文件名后缀必须是.mp4");
        }
        if (file.getParentFile().canWrite()) {
            if (this.lvsuper == 3) {
                return LinkVisual.start_convert_mp4(this.lvfloat, file.getAbsolutePath());
            }
            return false;
        }
        throw new IOException(file.getAbsolutePath() + " can not be write.");
    }

    public void stop() {
        this.lvboolean.post(this.f);
    }

    public boolean stopRecordingContent() {
        if (this.lvsuper == 3) {
            return LinkVisual.stop_convert_mp4(this.lvfloat);
        }
        return false;
    }

    public boolean useExternalRender() {
        return this.lvabstract;
    }

    public boolean useVideoFrameProcessing() {
        return this.lvpackage;
    }
}
