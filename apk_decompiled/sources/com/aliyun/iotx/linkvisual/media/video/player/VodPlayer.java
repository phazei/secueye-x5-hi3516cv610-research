package com.aliyun.iotx.linkvisual.media.video.player;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartRelayParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartRelayVodEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.TransQualityStatisticParams;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.beans.SeiInfoBuffer;
import com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener;
import com.aliyun.iotx.linkvisual.media.video.lvdo;
import com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig;
import java.nio.ByteBuffer;
import lvnew.lvfor;
import lvnew.lvif;

/* JADX INFO: loaded from: classes2.dex */
public class VodPlayer extends lvdo {
    public static final String TAG = "linksdk_lv_VodPlayer";
    private String i;
    private String j;
    private int k;
    private int l;
    private boolean m;
    private int n;
    private long o;
    private int p;
    private OnCompletionListener q;
    private final Runnable r;
    private final Runnable s;

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer$3, reason: invalid class name */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x00e1  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x00e4  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instruction units count: 307
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.AnonymousClass3.run():void");
        }
    }

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f5199lvdo;

        static {
            int[] iArr = new int[lvfor.values().length];
            f5199lvdo = iArr;
            try {
                iArr[lvfor.EVENT_SEEK_COMPLETE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5199lvdo[lvfor.EVENT_VOD_COMPLETE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5199lvdo[lvfor.EVENT_BUFFER_EMPTY_FOR_MOMENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5199lvdo[lvfor.EVENT_BUFFERING_END.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5199lvdo[lvfor.EVENT_RELAY_STARTSTREAMING_TRACKINFO.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private class SetDataSourceByIPCRecordFileNameRunnable implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private String f5200lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private boolean f5201lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private String f5202lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private int f5203lvint;

        /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
        private long f5204lvnew;

        public SetDataSourceByIPCRecordFileNameRunnable(String str, String str2, boolean z, int i, long j) {
            this.f5200lvdo = str;
            this.f5202lvif = str2;
            this.f5201lvfor = z;
            this.f5203lvint = i;
            this.f5204lvnew = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            VodPlayer.this.i = this.f5200lvdo;
            VodPlayer.this.j = this.f5202lvif;
            VodPlayer.this.m = this.f5201lvfor;
            VodPlayer.this.n = this.f5203lvint;
            VodPlayer.this.o = this.f5204lvnew;
        }
    }

    private class SetDataSourceByIPCRecordTimeRunnable implements Runnable {

        /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
        private int f5206lvbyte;

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private String f5208lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private int f5209lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private int f5210lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private boolean f5211lvint;

        /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
        private int f5212lvnew;

        /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
        private long f5213lvtry;

        public SetDataSourceByIPCRecordTimeRunnable(String str, int i, int i2, boolean z, int i3, long j, int i4) {
            lvfor.lvdo.lvif().lvdo(str);
            this.f5208lvdo = str;
            this.f5210lvif = i;
            this.f5209lvfor = i2;
            this.f5211lvint = z;
            this.f5212lvnew = i3;
            this.f5213lvtry = j;
            this.f5206lvbyte = i4;
        }

        @Override // java.lang.Runnable
        public void run() {
            VodPlayer.this.i = this.f5208lvdo;
            VodPlayer.this.k = this.f5210lvif;
            VodPlayer.this.l = this.f5209lvfor;
            VodPlayer.this.m = this.f5211lvint;
            VodPlayer.this.n = this.f5212lvnew;
            VodPlayer.this.o = this.f5213lvtry;
            VodPlayer.this.p = this.f5206lvbyte;
        }
    }

    @Deprecated
    public VodPlayer() {
        super(null);
        this.r = new AnonymousClass3();
        this.s = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.4
            @Override // java.lang.Runnable
            public void run() {
                if (!VodPlayer.this.isInvalidHandle()) {
                    LinkVisual.pause_stream(((lvdo) VodPlayer.this).lvfloat, true);
                }
                if (((lvdo) VodPlayer.this).f5077lvbreak != null) {
                    ((lvdo) VodPlayer.this).f5077lvbreak.pause();
                }
            }
        };
    }

    public VodPlayer(Context context) {
        super(context);
        this.r = new AnonymousClass3();
        this.s = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.4
            @Override // java.lang.Runnable
            public void run() {
                if (!VodPlayer.this.isInvalidHandle()) {
                    LinkVisual.pause_stream(((lvdo) VodPlayer.this).lvfloat, true);
                }
                if (((lvdo) VodPlayer.this).f5077lvbreak != null) {
                    ((lvdo) VodPlayer.this).f5077lvbreak.pause();
                }
            }
        };
    }

    public int getBeginTime() {
        return this.k;
    }

    public long getCurrentPosition() {
        if (isInvalidHandle()) {
            return 0L;
        }
        return LinkVisual.get_current_duration(this.lvfloat);
    }

    public long getDuration() {
        if (isInvalidHandle()) {
            return 0L;
        }
        return LinkVisual.get_duration(this.lvfloat);
    }

    public int getEncryptType() {
        return this.n;
    }

    public int getEndTime() {
        return this.l;
    }

    public String getFileName() {
        return this.j;
    }

    public String getIotId() {
        return this.i;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    public lvif getPlayerType() {
        return lvif.FILE;
    }

    public int getRecordType() {
        return this.p;
    }

    public long getSeekToPositionInMs() {
        return this.o;
    }

    public boolean isEncrypted() {
        return this.m;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected TransQualityStatisticParams lvdo(TransQualityStatisticParams transQualityStatisticParams) {
        transQualityStatisticParams.setIotId(this.i);
        transQualityStatisticParams.setSessionId(this.f5078lvbyte);
        return transQualityStatisticParams;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(final int i, final lvfor lvforVar, final String str) {
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.2
            @Override // java.lang.Runnable
            public void run() {
                if (i == ((lvdo) VodPlayer.this).lvfloat) {
                    switch (AnonymousClass6.f5199lvdo[lvforVar.ordinal()]) {
                        case 1:
                            ALog.d(VodPlayer.TAG, "[ " + VodPlayer.this.hashCode() + "] EVENT_SEEK_COMPLETE");
                            VodPlayer.this.lvif(3);
                            if (((lvdo) VodPlayer.this).f5077lvbreak != null) {
                                ((lvdo) VodPlayer.this).f5077lvbreak.stop();
                                ((lvdo) VodPlayer.this).f5077lvbreak.start();
                            }
                            break;
                        case 2:
                            VodPlayer.this.lvif(3);
                            ALog.d(VodPlayer.TAG, "[ " + VodPlayer.this.hashCode() + "] EVENT_VOD_COMPLETE");
                            ((lvdo) VodPlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (VodPlayer.this.q != null) {
                                        VodPlayer.this.q.onCompletion();
                                    }
                                }
                            });
                            break;
                        case 3:
                            ALog.d(VodPlayer.TAG, "[ " + VodPlayer.this.hashCode() + "] EVENT_BUFFER_EMPTY_FOR_MOMENT");
                            if (VodPlayer.this.getPlayState() != 4) {
                                VodPlayer.this.lvif(2);
                            }
                            break;
                        case 4:
                            ALog.d(VodPlayer.TAG, "[ " + VodPlayer.this.hashCode() + "] EVENT_BUFFERING_END");
                            VodPlayer.this.lvif(3);
                            break;
                        case 5:
                            StartRelayParams fromJSONString = StartRelayParams.parseFromJSONString(str);
                            fromJSONString.setIotId(VodPlayer.this.i);
                            fromJSONString.setReqTs(((lvdo) VodPlayer.this).lvtransient);
                            fromJSONString.setSeek(VodPlayer.this.o);
                            fromJSONString.setEncrypt(VodPlayer.this.m);
                            fromJSONString.setRespTs(((lvdo) VodPlayer.this).lvimplements);
                            fromJSONString.setSessionId(((lvdo) VodPlayer.this).f5078lvbyte);
                            fromJSONString.setFirstFrameDelay(((lvdo) VodPlayer.this).f5074b - ((lvdo) VodPlayer.this).lvtransient > 0 ? ((lvdo) VodPlayer.this).f5074b - ((lvdo) VodPlayer.this).lvtransient : 0L);
                            fromJSONString.setNetwork(lvint.lvdo.lvdo(((lvdo) VodPlayer.this).lvfinal));
                            lvfor.lvdo.lvif().lvdo(StartRelayVodEvent.newBuilder().code(200).params(fromJSONString).build());
                            break;
                    }
                }
            }
        });
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(String str, boolean z, byte[] bArr, byte[] bArr2, P2PConfig p2PConfig) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(hashCode());
        sb.append("] Url= \n");
        sb.append(str);
        sb.append("\nIsEncrypted= ");
        sb.append(z);
        sb.append("\nDecryptIv= ");
        sb.append(bArr);
        sb.append("\nDecryptKey= ");
        sb.append(bArr2);
        sb.append("\nP2pConfig= ");
        sb.append(p2PConfig == null ? "" : p2PConfig.toString());
        ALog.d(TAG, sb.toString());
        if (TextUtils.isEmpty(str) || !str.toLowerCase().startsWith("rtmp://")) {
            throw new IllegalArgumentException("Rtmp Url is invalid, should start with \"rtmp://\"");
        }
        if (z && (bArr == null || bArr2 == null || bArr.length != 16 || bArr2.length != 16)) {
            throw new IllegalArgumentException("mDecryptIv or mDecryptKey is illegal when using encrypted source. Should be 16 byte.");
        }
        this.lvboolean.post(new lvdo.lvlong(str, z, bArr, bArr2, p2PConfig));
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvgoto() {
        super.lvgoto();
        this.i = null;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif() {
        super.lvif();
        this.i = null;
        this.j = null;
        this.k = 0;
        this.l = 0;
        this.o = 0L;
        this.p = 0;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif(PlayerException playerException) {
        lvfor.lvdo.lvif().lvdo(ErrorEvent.newBuilder().code(playerException.getSubCode()).message(playerException.getMessage()).params(ErrorParams.newBuilder().module("localstorage").sessionId(this.f5078lvbyte).iotId(this.i).network(lvint.lvdo.lvdo(this.lvfinal)).build()).build());
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected int lvlong() {
        if (TextUtils.isEmpty(this.f5085lvif)) {
            lvdo(new PlayerException(6, 1007, "Url is empty!"));
            return 0;
        }
        if (!lvnew() || lvint()) {
            String str = this.f5085lvif;
            boolean z = this.lvwhile;
            boolean z2 = this.f5083lvfor;
            byte[] bArr = this.f5086lvint;
            byte[] bArr2 = this.f5088lvnew;
            ILvStreamCallback iLvStreamCallback = this.e;
            ByteBuffer byteBuffer = this.lvconst;
            int iCapacity = byteBuffer.capacity();
            SeiInfoBuffer seiInfoBuffer = this.lvinterface;
            return LinkVisual.open_rtmp_stream(str, 0, z, 0, z2, bArr, bArr2, iLvStreamCallback, byteBuffer, iCapacity, seiInfoBuffer != null ? seiInfoBuffer.seiDirectBuffer : null, seiInfoBuffer != null ? seiInfoBuffer.seiDirectBuffer.capacity() : 0);
        }
        String str2 = this.f5085lvif;
        boolean z3 = this.f5083lvfor;
        byte[] bArr3 = this.f5086lvint;
        byte[] bArr4 = this.f5088lvnew;
        String str3 = this.i;
        String session = this.f5090lvtry.getSession();
        String stunServer = this.f5090lvtry.getStunServer();
        int stunPort = this.f5090lvtry.getStunPort();
        String stunKey = this.f5090lvtry.getStunKey();
        String signalUrl = this.f5090lvtry.getSignalUrl();
        ILvStreamCallback iLvStreamCallback2 = this.e;
        ByteBuffer byteBuffer2 = this.lvconst;
        int iCapacity2 = byteBuffer2.capacity();
        SeiInfoBuffer seiInfoBuffer2 = this.lvinterface;
        return LinkVisual.open_p2p_stream(str2, 0, 0, z3, bArr3, bArr4, 0, str3, session, stunServer, stunPort, stunKey, signalUrl, iLvStreamCallback2, byteBuffer2, iCapacity2, seiInfoBuffer2 != null ? seiInfoBuffer2.seiDirectBuffer : null, seiInfoBuffer2 != null ? seiInfoBuffer2.seiDirectBuffer.capacity() : 0);
    }

    public void pause() {
        this.lvboolean.post(this.s);
    }

    public boolean playFrameByFrame() {
        return LinkVisual.frame_by_frame_stream(this.lvfloat);
    }

    public void prepare() {
        this.lvboolean.post(this.r);
    }

    public void seekTo(final long j) {
        if (j < 0) {
            throw new IllegalArgumentException("seekTo position must larger than 0");
        }
        final int playState = getPlayState();
        lvif(2);
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.1
            @Override // java.lang.Runnable
            public void run() {
                if (VodPlayer.this.isInvalidHandle()) {
                    return;
                }
                if (!LinkVisual.seek_stream(((lvdo) VodPlayer.this).lvfloat, (int) j)) {
                    VodPlayer.this.lvif(playState);
                }
                if (((lvdo) VodPlayer.this).f5077lvbreak != null) {
                    ((lvdo) VodPlayer.this).f5077lvbreak.stop();
                    ((lvdo) VodPlayer.this).f5077lvbreak.start();
                }
            }
        });
    }

    public void setBeginTime(int i) {
        this.k = i;
    }

    public void setDataSource(String str, boolean z, byte[] bArr, byte[] bArr2) throws IllegalArgumentException {
        lvdo(str, z, bArr, bArr2, null);
    }

    public void setDataSourceByIPCRecordFileName(String str, String str2) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordFileNameRunnable(str, str2, true, 0, 0L));
    }

    public void setDataSourceByIPCRecordFileName(String str, String str2, long j) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordFileNameRunnable(str, str2, true, 0, j));
    }

    @Deprecated
    public void setDataSourceByIPCRecordFileName(String str, String str2, boolean z, int i) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordFileNameRunnable(str, str2, z, i, 0L));
    }

    public void setDataSourceByIPCRecordTime(String str, int i, int i2, long j) {
        Version.checkSupport();
        setDataSourceByIPCRecordTime(str, i, i2, true, 0, j);
    }

    public void setDataSourceByIPCRecordTime(String str, int i, int i2, long j, int i3) {
        Version.checkSupport();
        setDataSourceByIPCRecordTime(str, i, i2, true, 0, j, i3);
    }

    @Deprecated
    public void setDataSourceByIPCRecordTime(String str, int i, int i2, boolean z, int i3, long j) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordTimeRunnable(str, i, i2, z, i3, j, Integer.MIN_VALUE));
    }

    @Deprecated
    public void setDataSourceByIPCRecordTime(String str, int i, int i2, boolean z, int i3, long j, int i4) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordTimeRunnable(str, i, i2, z, i3, j, i4));
    }

    public void setEncryptType(int i) {
        this.n = i;
    }

    public void setEncrypted(boolean z) {
        this.m = z;
    }

    public void setEndTime(int i) {
        this.l = i;
    }

    public void setFileName(String str) {
        this.j = str;
    }

    public void setIotId(String str) {
        this.i = str;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.q = onCompletionListener;
    }

    public void setPlaybackSpeed(float f) {
        int i;
        if (0.0f < f && f <= 0.0625f) {
            i = 0;
        } else if (0.0625f < f && f <= 0.125f) {
            i = 1;
        } else if (0.125f < f && f <= 0.25f) {
            i = 2;
        } else if (0.25f < f && f <= 0.5f) {
            i = 3;
        } else if (0.5f < f && f <= 1.0f) {
            i = 4;
        } else if (1.0f < f && f <= 2.0f) {
            i = 5;
        } else if (2.0f < f && f <= 4.0f) {
            i = 6;
        } else if (4.0f < f && f <= 8.0f) {
            i = 7;
        } else if (8.0f >= f || f > 16.0f) {
            if (16.0f < f || f <= 0.0f) {
                throw new IllegalArgumentException("speed must greater than 0 and less than 16f.");
            }
            i = 4;
        } else {
            i = 8;
        }
        this.lvthrow = i != 4;
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.VodPlayer.5
            @Override // java.lang.Runnable
            public void run() {
                if (((lvdo) VodPlayer.this).f5077lvbreak != null) {
                    ((lvdo) VodPlayer.this).f5077lvbreak.stop();
                    ((lvdo) VodPlayer.this).f5077lvbreak.start();
                }
            }
        });
        LinkVisual.set_speed_rate(this.lvfloat, i);
    }

    public VodPlayer setRecordType(int i) {
        this.p = i;
        return this;
    }

    public void setSeekToPositionInMs(long j) {
        this.o = j;
    }
}
