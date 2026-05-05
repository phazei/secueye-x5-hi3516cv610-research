package com.aliyun.iotx.linkvisual.media.video.player;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartCloudVodEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartCloudVodParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.TransQualityStatisticParams;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener;
import com.aliyun.iotx.linkvisual.media.video.lvdo;
import com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import java.nio.ByteBuffer;
import java.util.HashMap;
import lvbyte.lvif;
import lvnew.lvfor;

/* JADX INFO: loaded from: classes2.dex */
public class HlsPlayer extends lvdo {
    public static final String TAG = "linksdk_lv_HlsPlayer";
    private String i;
    private String j;
    private long k;
    private OnCompletionListener l;
    private final Runnable m;
    private final Runnable n;

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer$3, reason: invalid class name */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (TextUtils.isEmpty(HlsPlayer.this.i)) {
                if (((lvdo) HlsPlayer.this).f5085lvif != null) {
                    ((lvdo) HlsPlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            synchronized (((lvdo) HlsPlayer.this).lvprotected) {
                                if (((lvdo) HlsPlayer.this).lvnative != null) {
                                    ((lvdo) HlsPlayer.this).lvnative.onPrepared();
                                }
                            }
                        }
                    });
                    return;
                } else {
                    HlsPlayer.this.lvdo(new PlayerException(6, 1008, "No data source has been set!"));
                    return;
                }
            }
            HashMap map = new HashMap(2);
            map.put("iotId", HlsPlayer.this.i);
            if (TextUtils.isEmpty(HlsPlayer.this.j)) {
                HlsPlayer.this.lvdo(new PlayerException(6, 1008, "fileName required."));
            } else {
                map.put("fileName", HlsPlayer.this.j);
                APIHelper.sendIoTRequest(lvbyte.lvdo.CLOUD_VOD_BY_FILENAME, map, HlsPlayer.this.i, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.3.1
                    @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                    public void onFailed(lvif lvifVar) {
                        HlsPlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                    }

                    @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                    public void onResponse(lvif lvifVar) {
                        if (lvifVar.lvdo() != 200) {
                            HlsPlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                            return;
                        }
                        JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                        if (object != null && !TextUtils.isEmpty(object.getString("vodUrl"))) {
                            HlsPlayer.this.setDataSource(object.getString("vodUrl"));
                            ((lvdo) HlsPlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.3.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    synchronized (((lvdo) HlsPlayer.this).lvprotected) {
                                        if (((lvdo) HlsPlayer.this).lvnative != null) {
                                            ((lvdo) HlsPlayer.this).lvnative.onPrepared();
                                        }
                                    }
                                }
                            });
                            ((lvdo) HlsPlayer.this).lvimplements = System.currentTimeMillis();
                            return;
                        }
                        HlsPlayer.this.lvdo(new PlayerException(6, PlayerException.SUB_CODE_SOURCE_INVALID_HLS_URL, "url is null."));
                        ALog.w(HlsPlayer.TAG, "[" + HlsPlayer.this.hashCode() + "] url is null");
                    }
                });
            }
        }
    }

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f5158lvdo;

        static {
            int[] iArr = new int[lvfor.values().length];
            f5158lvdo = iArr;
            try {
                iArr[lvfor.EVENT_SEEK_COMPLETE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5158lvdo[lvfor.EVENT_VOD_COMPLETE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5158lvdo[lvfor.EVENT_BUFFER_EMPTY_FOR_MOMENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5158lvdo[lvfor.EVENT_BUFFERING_END.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5158lvdo[lvfor.EVENT_HLS_SLICE_DOWNLOAD_ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5158lvdo[lvfor.EVENT_HLS_M3U8_DOWNLOAD_ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5158lvdo[lvfor.EVENT_HLS_STARTSTREAMING_TRACKINFO.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private class SetDataSourceByIPCRecordFileNameRunnable implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private String f5159lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private long f5160lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private String f5161lvif;

        public SetDataSourceByIPCRecordFileNameRunnable(String str, String str2, long j) {
            lvfor.lvdo.lvif().lvdo(str);
            this.f5159lvdo = str;
            this.f5161lvif = str2;
            this.f5160lvfor = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            HlsPlayer.this.i = this.f5159lvdo;
            HlsPlayer.this.j = this.f5161lvif;
            HlsPlayer.this.k = this.f5160lvfor;
        }
    }

    @Deprecated
    public HlsPlayer() {
        super(null);
        this.m = new AnonymousClass3();
        this.n = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.4
            @Override // java.lang.Runnable
            public void run() {
                if (!HlsPlayer.this.isInvalidHandle()) {
                    LinkVisual.pause_stream(((lvdo) HlsPlayer.this).lvfloat, true);
                }
                if (((lvdo) HlsPlayer.this).f5077lvbreak != null) {
                    ((lvdo) HlsPlayer.this).f5077lvbreak.pause();
                }
            }
        };
    }

    public HlsPlayer(Context context) {
        super(context);
        this.m = new AnonymousClass3();
        this.n = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.4
            @Override // java.lang.Runnable
            public void run() {
                if (!HlsPlayer.this.isInvalidHandle()) {
                    LinkVisual.pause_stream(((lvdo) HlsPlayer.this).lvfloat, true);
                }
                if (((lvdo) HlsPlayer.this).f5077lvbreak != null) {
                    ((lvdo) HlsPlayer.this).f5077lvbreak.pause();
                }
            }
        };
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

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    public lvnew.lvif getPlayerType() {
        return lvnew.lvif.HLS;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected TransQualityStatisticParams lvdo(TransQualityStatisticParams transQualityStatisticParams) {
        transQualityStatisticParams.setIotId(this.i);
        transQualityStatisticParams.setSessionId(this.f5078lvbyte);
        return transQualityStatisticParams;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(final int i, final lvfor lvforVar, final String str) {
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.2
            @Override // java.lang.Runnable
            public void run() {
                if (i == ((lvdo) HlsPlayer.this).lvfloat) {
                    switch (AnonymousClass6.f5158lvdo[lvforVar.ordinal()]) {
                        case 1:
                            ALog.d(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_SEEK_COMPLETE");
                            HlsPlayer.this.lvif(3);
                            if (((lvdo) HlsPlayer.this).f5077lvbreak != null) {
                                ((lvdo) HlsPlayer.this).f5077lvbreak.stop();
                                ((lvdo) HlsPlayer.this).f5077lvbreak.start();
                            }
                            break;
                        case 2:
                            ALog.d(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_VOD_COMPLETE");
                            ((lvdo) HlsPlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (HlsPlayer.this.l != null) {
                                        HlsPlayer.this.l.onCompletion();
                                    }
                                }
                            });
                            ((lvdo) HlsPlayer.this).f.run();
                            break;
                        case 3:
                            ALog.d(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_BUFFER_EMPTY_FOR_MOMENT");
                            if (HlsPlayer.this.getPlayState() != 4) {
                                HlsPlayer.this.lvif(2);
                            }
                            break;
                        case 4:
                            ALog.d(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_BUFFERING_END");
                            HlsPlayer.this.lvif(3);
                            break;
                        case 5:
                            ALog.e(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_HLS_SLICE_DOWNLOAD_ERROR");
                            ((lvdo) HlsPlayer.this).f.run();
                            HlsPlayer.this.lvdo(new PlayerException(8, PlayerException.SUB_CODE_UNEXPECTED_PULL_STREAM_ERROR, "Hls download slice error."));
                            break;
                        case 6:
                            ALog.e(HlsPlayer.TAG, "[ " + HlsPlayer.this.hashCode() + "] EVENT_HLS_M3U8_DOWNLOAD_ERROR");
                            ((lvdo) HlsPlayer.this).f.run();
                            HlsPlayer.this.lvdo(new PlayerException(6, 1005, "Hls download m3u8 error."));
                            break;
                        case 7:
                            StartCloudVodParams fromJSONString = StartCloudVodParams.parseFromJSONString(str);
                            fromJSONString.setIotId(HlsPlayer.this.i);
                            fromJSONString.setReqTs(((lvdo) HlsPlayer.this).lvtransient);
                            fromJSONString.setFileName(HlsPlayer.this.j);
                            fromJSONString.setSeek(HlsPlayer.this.k);
                            fromJSONString.setRespTs(((lvdo) HlsPlayer.this).lvimplements);
                            fromJSONString.setSessionId(((lvdo) HlsPlayer.this).f5078lvbyte);
                            fromJSONString.setNetwork(lvint.lvdo.lvdo(((lvdo) HlsPlayer.this).lvfinal));
                            lvfor.lvdo.lvif().lvdo(StartCloudVodEvent.newBuilder().code(200).params(fromJSONString).build());
                            break;
                    }
                }
            }
        });
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(String str, boolean z, byte[] bArr, byte[] bArr2, P2PConfig p2PConfig) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        sb.append("Url= \n");
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
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Url is invalid, should start with http:// or https://");
        }
        this.lvboolean.post(new lvdo.lvlong(str, false, null, null, null));
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif() {
        super.lvif();
        this.i = null;
        this.j = null;
        this.k = 0L;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif(PlayerException playerException) {
        lvfor.lvdo.lvif().lvdo(ErrorEvent.newBuilder().code(playerException.getSubCode()).message(playerException.getMessage()).params(ErrorParams.newBuilder().module("cloudvod").sessionId(this.f5078lvbyte).iotId(this.i).network(lvint.lvdo.lvdo(this.lvfinal)).build()).build());
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected int lvlong() {
        if (TextUtils.isEmpty(this.f5085lvif)) {
            lvdo(new PlayerException(6, PlayerException.SUB_CODE_SOURCE_INVALID_HLS_URL, "Url is empty!"));
            return 0;
        }
        String str = this.f5085lvif;
        long j = this.k;
        ILvStreamCallback iLvStreamCallback = this.e;
        ByteBuffer byteBuffer = this.lvconst;
        return LinkVisual.open_hls_stream(str, j, iLvStreamCallback, byteBuffer, byteBuffer.capacity());
    }

    public void pause() {
        this.lvboolean.post(this.n);
    }

    public boolean playFrameByFrame() {
        return LinkVisual.frame_by_frame_stream(this.lvfloat);
    }

    public void prepare() {
        this.lvboolean.post(this.m);
    }

    public void seekTo(final long j) {
        if (j < 0) {
            throw new IllegalArgumentException("seekTo position must larger than 0");
        }
        final int playState = getPlayState();
        lvif(2);
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.1
            @Override // java.lang.Runnable
            public void run() {
                if (HlsPlayer.this.isInvalidHandle()) {
                    return;
                }
                if (!LinkVisual.seek_stream(((lvdo) HlsPlayer.this).lvfloat, (int) j)) {
                    HlsPlayer.this.lvif(playState);
                }
                if (((lvdo) HlsPlayer.this).f5077lvbreak != null) {
                    ((lvdo) HlsPlayer.this).f5077lvbreak.stop();
                    ((lvdo) HlsPlayer.this).f5077lvbreak.start();
                }
            }
        });
    }

    public void setDataSourceByIPCRecordFileName(String str, String str2) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordFileNameRunnable(str, str2, 0L));
    }

    public void setDataSourceByIPCRecordFileName(String str, String str2, long j) {
        Version.checkSupport();
        this.lvboolean.post(new SetDataSourceByIPCRecordFileNameRunnable(str, str2, j));
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.l = onCompletionListener;
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
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer.5
            @Override // java.lang.Runnable
            public void run() {
                if (((lvdo) HlsPlayer.this).f5077lvbreak != null) {
                    ((lvdo) HlsPlayer.this).f5077lvbreak.stop();
                    ((lvdo) HlsPlayer.this).f5077lvbreak.start();
                }
            }
        });
        LinkVisual.set_speed_rate(this.lvfloat, i);
    }
}
