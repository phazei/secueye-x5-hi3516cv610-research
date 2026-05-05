package com.aliyun.iotx.linkvisual.media.video.player;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.LinkVisualMedia;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.BaseEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.ErrorParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.RealTimeTrackingEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartP2PStreamingEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartP2PStreamingParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartRelayParams;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.StartRelayStreamingEvent;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.TransQualityStatisticParams;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.beans.PreConnectType;
import com.aliyun.iotx.linkvisual.media.video.beans.SeiInfoBuffer;
import com.aliyun.iotx.linkvisual.media.video.listener.OnVideoQualityListener;
import com.aliyun.iotx.linkvisual.media.video.lvdo;
import com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import java.nio.ByteBuffer;
import java.util.HashMap;
import lvbyte.lvif;
import lvnew.lvfor;

/* JADX INFO: loaded from: classes2.dex */
public class LivePlayer extends lvdo {
    public static final String TAG = "linksdk_lv_LivePlayer";
    private String i;
    private int j;
    private boolean k;
    private int l;
    private boolean m;
    private int n;
    private int o;
    private OnVideoQualityListener p;
    private final Runnable q;
    private final Runnable r;

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer$3, reason: invalid class name */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ((lvdo) LivePlayer.this).lvtransient = System.currentTimeMillis();
            LivePlayer.this.lvvoid();
            if (TextUtils.isEmpty(LivePlayer.this.i)) {
                if (TextUtils.isEmpty(((lvdo) LivePlayer.this).f5085lvif)) {
                    LivePlayer.this.lvif(4);
                    LivePlayer.this.lvdo(new PlayerException(6, 1008, "No data source has been set!"));
                    return;
                } else {
                    ((lvdo) LivePlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.3.3
                        @Override // java.lang.Runnable
                        public void run() {
                            synchronized (((lvdo) LivePlayer.this).lvprotected) {
                                if (((lvdo) LivePlayer.this).lvnative != null) {
                                    ((lvdo) LivePlayer.this).lvnative.onPrepared();
                                }
                            }
                        }
                    });
                    ((lvdo) LivePlayer.this).lvimplements = System.currentTimeMillis();
                    return;
                }
            }
            if (LinkVisual.query_connected_channel(LivePlayer.this.i, LivePlayer.this.j, null) > 0) {
                ((lvdo) LivePlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (((lvdo) LivePlayer.this).lvprotected) {
                            if (((lvdo) LivePlayer.this).lvnative != null) {
                                ((lvdo) LivePlayer.this).lvnative.onPrepared();
                            }
                        }
                    }
                });
                return;
            }
            HashMap map = new HashMap();
            map.put("iotId", LivePlayer.this.i);
            map.put("streamType", Integer.valueOf(LivePlayer.this.j));
            map.put("relayEncrypted", Boolean.valueOf(LivePlayer.this.k));
            map.put("relayEncryptType", Integer.valueOf(LivePlayer.this.l));
            map.put("forceIFrame", Boolean.valueOf(LivePlayer.this.m));
            map.put("cacheDuration", Integer.valueOf(LivePlayer.this.n));
            Boolean bool = Boolean.TRUE;
            map.put("needDomainName", bool);
            map.put("enableWebSocket", bool);
            APIHelper.sendIoTRequest(lvbyte.lvdo.LIVE_QUERY, map, LivePlayer.this.i, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.3.2
                @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                public void onFailed(lvif lvifVar) {
                    LivePlayer.this.lvif(4);
                    LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                }

                @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                public void onResponse(lvif lvifVar) {
                    byte[] bArr;
                    byte[] bytes;
                    if (lvifVar.lvdo() != 200) {
                        LivePlayer.this.lvif(4);
                        LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                        return;
                    }
                    JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                    P2PConfig p2PConfig = new P2PConfig();
                    try {
                        String string = object.getString("relayUrl");
                        String string2 = object.getString("signalUrl");
                        String string3 = object.getString("stunUrl");
                        p2PConfig.setSignalUrl(string2);
                        p2PConfig.setStunUrl(string3);
                        if (TextUtils.isEmpty(string)) {
                            LivePlayer.this.lvif(4);
                            LivePlayer.this.lvdo(new PlayerException(6, 1007, "Relay url is null."));
                            return;
                        }
                        if (LivePlayer.this.k && object.containsKey("relayDecryptKey")) {
                            JSONObject jSONObject = object.getJSONObject("relayDecryptKey");
                            if (!jSONObject.containsKey("iv") || !jSONObject.containsKey("key")) {
                                LivePlayer.this.lvif(4);
                                LivePlayer.this.lvdo(new PlayerException(6, 1006, "Not enough decrypt key or iv."));
                                return;
                            } else {
                                byte[] bytes2 = jSONObject.getBytes("iv");
                                bytes = jSONObject.getBytes("key");
                                bArr = bytes2;
                            }
                        } else {
                            bArr = null;
                            bytes = null;
                        }
                        if (LivePlayer.this.getPlayState() == 4 || LivePlayer.this.getPlayState() == 1 || (LivePlayer.this.getPlayState() == 2 && !LivePlayer.this.lvcatch())) {
                            LivePlayer livePlayer = LivePlayer.this;
                            livePlayer.lvdo(string, livePlayer.k, bArr, bytes, p2PConfig);
                        } else {
                            ALog.w(LivePlayer.TAG, "[" + LivePlayer.this.hashCode() + "]ignore setDataSource due to invalid state: " + LivePlayer.this.getPlayState());
                        }
                        ((lvdo) LivePlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.3.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                synchronized (((lvdo) LivePlayer.this).lvprotected) {
                                    if (((lvdo) LivePlayer.this).lvnative != null) {
                                        ((lvdo) LivePlayer.this).lvnative.onPrepared();
                                    }
                                }
                            }
                        });
                        ((lvdo) LivePlayer.this).lvimplements = System.currentTimeMillis();
                    } catch (JSONException e) {
                        LivePlayer.this.lvif(4);
                        LivePlayer.this.lvdo(new PlayerException(6, 1009, e.getLocalizedMessage()));
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f5177lvdo;

        static {
            int[] iArr = new int[lvfor.values().length];
            f5177lvdo = iArr;
            try {
                iArr[lvfor.EVENT_P2P_EVENT_TRACKING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5177lvdo[lvfor.EVENT_P2P_ERROR_TRACKING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5177lvdo[lvfor.EVENT_P2P_STARTSTREAMING_TRACKINFO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5177lvdo[lvfor.EVENT_RELAY_STARTSTREAMING_TRACKINFO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5177lvdo[lvfor.EVENT_P2P_CHANNEL_ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5177lvdo[lvfor.EVENT_REALPLAY_BUFFER_EMPTY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5177lvdo[lvfor.EVENT_BUFFERING_END.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f5177lvdo[lvfor.EVENT_NEED_REOPEN.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private class SetIPCDataSourceRunnable implements Runnable {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private String f5179lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private boolean f5180lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private int f5181lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private int f5182lvint;

        /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
        private boolean f5183lvnew;

        /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
        private int f5184lvtry;

        public SetIPCDataSourceRunnable(String str, int i, boolean z, int i2, boolean z2, int i3) {
            lvfor.lvdo.lvif().lvdo(str);
            this.f5179lvdo = str;
            this.f5181lvif = i;
            this.f5180lvfor = z;
            this.f5182lvint = i2;
            this.f5183lvnew = z2;
            this.f5184lvtry = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            LivePlayer.this.i = this.f5179lvdo;
            LivePlayer.this.j = this.f5181lvif;
            LivePlayer.this.k = this.f5180lvfor;
            LivePlayer.this.l = this.f5182lvint;
            LivePlayer.this.m = this.f5183lvnew;
            LivePlayer.this.n = this.f5184lvtry;
        }
    }

    @Deprecated
    public LivePlayer() {
        super(null);
        this.m = true;
        this.q = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.2
            @Override // java.lang.Runnable
            public void run() {
                if (!TextUtils.isEmpty(LivePlayer.this.i)) {
                    HashMap map = new HashMap();
                    map.put("iotId", LivePlayer.this.i);
                    map.put("streamType", Integer.valueOf(LivePlayer.this.j));
                    map.put("relayEncrypted", Boolean.valueOf(LivePlayer.this.k));
                    map.put("relayEncryptType", Integer.valueOf(LivePlayer.this.l));
                    map.put("forceIFrame", Boolean.valueOf(LivePlayer.this.m));
                    map.put("cacheDuration", Integer.valueOf(LivePlayer.this.n));
                    Boolean bool = Boolean.TRUE;
                    map.put("needDomainName", bool);
                    map.put("enableWebSocket", bool);
                    APIHelper.sendIoTRequest(lvbyte.lvdo.LIVE_QUERY, map, LivePlayer.this.i, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.2.1
                        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                        public void onFailed(lvif lvifVar) {
                            ((lvdo) LivePlayer.this).f.run();
                            LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                        }

                        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                        public void onResponse(lvif lvifVar) {
                            byte[] bArr;
                            byte[] bytes;
                            if (lvifVar.lvdo() != 200) {
                                ((lvdo) LivePlayer.this).f.run();
                                LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                                return;
                            }
                            JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                            P2PConfig p2PConfig = new P2PConfig();
                            try {
                                String string = object.getString("relayUrl");
                                String string2 = object.getString("signalUrl");
                                String string3 = object.getString("stunUrl");
                                p2PConfig.setSignalUrl(string2);
                                p2PConfig.setStunUrl(string3);
                                if (TextUtils.isEmpty(string)) {
                                    ((lvdo) LivePlayer.this).f.run();
                                    LivePlayer.this.lvdo(new PlayerException(6, 1007, "Relay url is null."));
                                    return;
                                }
                                if (LivePlayer.this.k && object.containsKey("relayDecryptKey")) {
                                    JSONObject jSONObject = object.getJSONObject("relayDecryptKey");
                                    if (!jSONObject.containsKey("iv") || !jSONObject.containsKey("key")) {
                                        ((lvdo) LivePlayer.this).f.run();
                                        LivePlayer.this.lvdo(new PlayerException(6, 1006, "Not enough decrypt key or iv."));
                                        return;
                                    } else {
                                        byte[] bytes2 = jSONObject.getBytes("iv");
                                        bytes = jSONObject.getBytes("key");
                                        bArr = bytes2;
                                    }
                                } else {
                                    bArr = null;
                                    bytes = null;
                                }
                                if (LivePlayer.this.getPlayState() == 2 && LivePlayer.this.lvcatch()) {
                                    LivePlayer livePlayer = LivePlayer.this;
                                    livePlayer.lvdo(string, livePlayer.k, bArr, bytes, p2PConfig);
                                    LinkVisual.reopen_p2p_stream(((lvdo) LivePlayer.this).lvfloat, ((lvdo) LivePlayer.this).f5085lvif, LivePlayer.this.j, ((lvdo) LivePlayer.this).f5083lvfor, ((lvdo) LivePlayer.this).f5086lvint, ((lvdo) LivePlayer.this).f5088lvnew, ((lvdo) LivePlayer.this).lvwhile, ((lvdo) LivePlayer.this).f5090lvtry.getSession(), ((lvdo) LivePlayer.this).f5090lvtry.getStunServer(), ((lvdo) LivePlayer.this).f5090lvtry.getStunPort(), ((lvdo) LivePlayer.this).f5090lvtry.getStunKey(), ((lvdo) LivePlayer.this).f5090lvtry.getSignalUrl());
                                } else {
                                    ALog.w(LivePlayer.TAG, "[" + LivePlayer.this.hashCode() + "]ignore setDataSource due to invalid state: " + LivePlayer.this.getPlayState());
                                }
                            } catch (JSONException e) {
                                ((lvdo) LivePlayer.this).f.run();
                                LivePlayer.this.lvdo(new PlayerException(6, 1009, e.getLocalizedMessage()));
                            }
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(((lvdo) LivePlayer.this).f5085lvif)) {
                    ((lvdo) LivePlayer.this).f.run();
                    LivePlayer.this.lvdo(new PlayerException(6, 1008, "No data source has been set!"));
                } else {
                    if (LivePlayer.this.getPlayState() == 2 && LivePlayer.this.lvcatch()) {
                        LinkVisual.reopen_p2p_stream(((lvdo) LivePlayer.this).lvfloat, ((lvdo) LivePlayer.this).f5085lvif, LivePlayer.this.j, ((lvdo) LivePlayer.this).f5083lvfor, ((lvdo) LivePlayer.this).f5086lvint, ((lvdo) LivePlayer.this).f5088lvnew, ((lvdo) LivePlayer.this).lvwhile, null, null, 0, null, null);
                        return;
                    }
                    ALog.w(LivePlayer.TAG, "[" + LivePlayer.this.hashCode() + "]ignore setDataSource due to invalid state: " + LivePlayer.this.getPlayState());
                }
            }
        };
        this.r = new AnonymousClass3();
    }

    public LivePlayer(Context context) {
        super(context);
        this.m = true;
        this.q = new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.2
            @Override // java.lang.Runnable
            public void run() {
                if (!TextUtils.isEmpty(LivePlayer.this.i)) {
                    HashMap map = new HashMap();
                    map.put("iotId", LivePlayer.this.i);
                    map.put("streamType", Integer.valueOf(LivePlayer.this.j));
                    map.put("relayEncrypted", Boolean.valueOf(LivePlayer.this.k));
                    map.put("relayEncryptType", Integer.valueOf(LivePlayer.this.l));
                    map.put("forceIFrame", Boolean.valueOf(LivePlayer.this.m));
                    map.put("cacheDuration", Integer.valueOf(LivePlayer.this.n));
                    Boolean bool = Boolean.TRUE;
                    map.put("needDomainName", bool);
                    map.put("enableWebSocket", bool);
                    APIHelper.sendIoTRequest(lvbyte.lvdo.LIVE_QUERY, map, LivePlayer.this.i, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.2.1
                        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                        public void onFailed(lvif lvifVar) {
                            ((lvdo) LivePlayer.this).f.run();
                            LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                        }

                        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                        public void onResponse(lvif lvifVar) {
                            byte[] bArr;
                            byte[] bytes;
                            if (lvifVar.lvdo() != 200) {
                                ((lvdo) LivePlayer.this).f.run();
                                LivePlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                                return;
                            }
                            JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                            P2PConfig p2PConfig = new P2PConfig();
                            try {
                                String string = object.getString("relayUrl");
                                String string2 = object.getString("signalUrl");
                                String string3 = object.getString("stunUrl");
                                p2PConfig.setSignalUrl(string2);
                                p2PConfig.setStunUrl(string3);
                                if (TextUtils.isEmpty(string)) {
                                    ((lvdo) LivePlayer.this).f.run();
                                    LivePlayer.this.lvdo(new PlayerException(6, 1007, "Relay url is null."));
                                    return;
                                }
                                if (LivePlayer.this.k && object.containsKey("relayDecryptKey")) {
                                    JSONObject jSONObject = object.getJSONObject("relayDecryptKey");
                                    if (!jSONObject.containsKey("iv") || !jSONObject.containsKey("key")) {
                                        ((lvdo) LivePlayer.this).f.run();
                                        LivePlayer.this.lvdo(new PlayerException(6, 1006, "Not enough decrypt key or iv."));
                                        return;
                                    } else {
                                        byte[] bytes2 = jSONObject.getBytes("iv");
                                        bytes = jSONObject.getBytes("key");
                                        bArr = bytes2;
                                    }
                                } else {
                                    bArr = null;
                                    bytes = null;
                                }
                                if (LivePlayer.this.getPlayState() == 2 && LivePlayer.this.lvcatch()) {
                                    LivePlayer livePlayer = LivePlayer.this;
                                    livePlayer.lvdo(string, livePlayer.k, bArr, bytes, p2PConfig);
                                    LinkVisual.reopen_p2p_stream(((lvdo) LivePlayer.this).lvfloat, ((lvdo) LivePlayer.this).f5085lvif, LivePlayer.this.j, ((lvdo) LivePlayer.this).f5083lvfor, ((lvdo) LivePlayer.this).f5086lvint, ((lvdo) LivePlayer.this).f5088lvnew, ((lvdo) LivePlayer.this).lvwhile, ((lvdo) LivePlayer.this).f5090lvtry.getSession(), ((lvdo) LivePlayer.this).f5090lvtry.getStunServer(), ((lvdo) LivePlayer.this).f5090lvtry.getStunPort(), ((lvdo) LivePlayer.this).f5090lvtry.getStunKey(), ((lvdo) LivePlayer.this).f5090lvtry.getSignalUrl());
                                } else {
                                    ALog.w(LivePlayer.TAG, "[" + LivePlayer.this.hashCode() + "]ignore setDataSource due to invalid state: " + LivePlayer.this.getPlayState());
                                }
                            } catch (JSONException e) {
                                ((lvdo) LivePlayer.this).f.run();
                                LivePlayer.this.lvdo(new PlayerException(6, 1009, e.getLocalizedMessage()));
                            }
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(((lvdo) LivePlayer.this).f5085lvif)) {
                    ((lvdo) LivePlayer.this).f.run();
                    LivePlayer.this.lvdo(new PlayerException(6, 1008, "No data source has been set!"));
                } else {
                    if (LivePlayer.this.getPlayState() == 2 && LivePlayer.this.lvcatch()) {
                        LinkVisual.reopen_p2p_stream(((lvdo) LivePlayer.this).lvfloat, ((lvdo) LivePlayer.this).f5085lvif, LivePlayer.this.j, ((lvdo) LivePlayer.this).f5083lvfor, ((lvdo) LivePlayer.this).f5086lvint, ((lvdo) LivePlayer.this).f5088lvnew, ((lvdo) LivePlayer.this).lvwhile, null, null, 0, null, null);
                        return;
                    }
                    ALog.w(LivePlayer.TAG, "[" + LivePlayer.this.hashCode() + "]ignore setDataSource due to invalid state: " + LivePlayer.this.getPlayState());
                }
            }
        };
        this.r = new AnonymousClass3();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean lvcatch() {
        return LinkVisual.query_connected_channel(this.i, this.j, null) > 0 || this.lvfloat > 0;
    }

    public String getIotId() {
        return this.i;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    public lvnew.lvif getPlayerType() {
        return lvnew.lvif.LIVE;
    }

    public int getRelayEncryptedType() {
        return this.l;
    }

    public int getStreamType() {
        return this.j;
    }

    public boolean isForceIFrame() {
        return this.m;
    }

    public boolean isRelayEncrypted() {
        return this.k;
    }

    protected StartP2PStreamingParams lvdo(StartP2PStreamingParams startP2PStreamingParams) {
        startP2PStreamingParams.setIotId(this.i);
        startP2PStreamingParams.setReqTs(this.lvtransient);
        startP2PStreamingParams.setRespTs(this.lvimplements);
        startP2PStreamingParams.setSessionId(this.f5078lvbyte);
        startP2PStreamingParams.setNetwork(lvint.lvdo.lvdo(this.lvfinal));
        return startP2PStreamingParams;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected TransQualityStatisticParams lvdo(TransQualityStatisticParams transQualityStatisticParams) {
        transQualityStatisticParams.setIotId(this.i);
        transQualityStatisticParams.setSessionId(this.f5078lvbyte);
        return transQualityStatisticParams;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(int i) {
        super.lvdo(i);
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvdo(final int i, final lvfor lvforVar, final String str) {
        this.lvboolean.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.4
            /* JADX WARN: Failed to find 'out' block for switch in B:3:0x000c. Please report as an issue. */
            @Override // java.lang.Runnable
            public void run() {
                Runnable runnable;
                BaseEvent baseEvent;
                lvfor.lvdo lvdoVarLvif;
                BaseEvent baseEventBuild;
                int[] iArr = AnonymousClass5.f5177lvdo;
                switch (iArr[lvforVar.ordinal()]) {
                    case 1:
                        RealTimeTrackingEvent fromJSONString = RealTimeTrackingEvent.parseFromJSONString(str);
                        fromJSONString.getParams().setIotId(LivePlayer.this.i);
                        baseEvent = fromJSONString;
                        lvfor.lvdo.lvif().lvdo(baseEvent);
                        break;
                    case 2:
                        ErrorEvent fromJSONString2 = ErrorEvent.parseFromJSONString(str);
                        fromJSONString2.getParams().setIotId(LivePlayer.this.i);
                        baseEvent = fromJSONString2;
                        lvfor.lvdo.lvif().lvdo(baseEvent);
                        break;
                    case 3:
                        StartP2PStreamingParams startP2PStreamingParamsLvdo = LivePlayer.this.lvdo(StartP2PStreamingParams.parseFromJSONString(str));
                        lvdoVarLvif = lvfor.lvdo.lvif();
                        baseEventBuild = StartP2PStreamingEvent.newBuilder().code(200).params(startP2PStreamingParamsLvdo).build();
                        lvdoVarLvif.lvdo(baseEventBuild);
                        break;
                    case 4:
                        StartRelayParams fromJSONString3 = StartRelayParams.parseFromJSONString(str);
                        fromJSONString3.setIotId(LivePlayer.this.i);
                        fromJSONString3.setReqTs(((lvdo) LivePlayer.this).lvtransient);
                        fromJSONString3.setRespTs(((lvdo) LivePlayer.this).lvimplements);
                        fromJSONString3.setCacheGop(LivePlayer.this.n);
                        fromJSONString3.setEncrypt(LivePlayer.this.k);
                        fromJSONString3.setSessionId(((lvdo) LivePlayer.this).f5078lvbyte);
                        fromJSONString3.setFirstFrameDelay(((lvdo) LivePlayer.this).f5074b - ((lvdo) LivePlayer.this).lvtransient > 0 ? ((lvdo) LivePlayer.this).f5074b - ((lvdo) LivePlayer.this).lvtransient : 0L);
                        fromJSONString3.setNetwork(lvint.lvdo.lvdo(((lvdo) LivePlayer.this).lvfinal));
                        lvdoVarLvif = lvfor.lvdo.lvif();
                        baseEventBuild = StartRelayStreamingEvent.newBuilder().code(200).params(fromJSONString3).build();
                        lvdoVarLvif.lvdo(baseEventBuild);
                        break;
                }
                if (((lvdo) LivePlayer.this).lvfloat == i) {
                    switch (iArr[lvforVar.ordinal()]) {
                        case 5:
                            ALog.d(LivePlayer.TAG, "[ " + LivePlayer.this.hashCode() + "] EVENT_P2P_CHANNEL_ERROR");
                            ((lvdo) LivePlayer.this).lvfloat = 0;
                            ((lvdo) LivePlayer.this).lvshort = 0;
                            runnable = LivePlayer.this.r;
                            break;
                        case 6:
                            ((lvdo) LivePlayer.this).lvdefault.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (LivePlayer.this.p != null) {
                                        LivePlayer.this.p.onVideoJitterBufferEmpty();
                                    }
                                }
                            });
                            return;
                        case 7:
                            LivePlayer.this.lvif(3);
                            return;
                        case 8:
                            LivePlayer.this.lvif(2);
                            runnable = LivePlayer.this.q;
                            break;
                        default:
                            return;
                    }
                    runnable.run();
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
        lvif();
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif() {
        super.lvif();
        this.i = null;
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected void lvif(PlayerException playerException) {
        lvfor.lvdo.lvif().lvdo(ErrorEvent.newBuilder().code(playerException.getSubCode()).message(playerException.getMessage()).params(ErrorParams.newBuilder().module("relay").sessionId(this.f5078lvbyte).iotId(this.i).network(lvint.lvdo.lvdo(this.lvfinal)).build()).build());
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected int lvlong() {
        final int iQuery_connected_channel = LinkVisual.query_connected_channel(this.i, this.j, new PreConnectType());
        if (TextUtils.isEmpty(this.f5085lvif) && iQuery_connected_channel == 0) {
            lvdo(new PlayerException(6, 1007, "Url is empty!"));
            return 0;
        }
        LinkVisualMedia.getInstance().playerIotIdTidMap.remove(this.i);
        if (iQuery_connected_channel > 0) {
            String str = this.i;
            int i = this.j;
            ILvStreamCallback iLvStreamCallback = this.e;
            ByteBuffer byteBuffer = this.lvconst;
            int iCapacity = byteBuffer.capacity();
            SeiInfoBuffer seiInfoBuffer = this.lvinterface;
            int iQuery_and_play = LinkVisual.query_and_play(str, i, 1, iLvStreamCallback, byteBuffer, iCapacity, seiInfoBuffer != null ? seiInfoBuffer.seiDirectBuffer : null, seiInfoBuffer != null ? seiInfoBuffer.seiDirectBuffer.capacity() : 0);
            LinkVisualMedia.getInstance().playerIotIdTidMap.put(this.i, Long.valueOf(Thread.currentThread().getId()));
            HashMap map = new HashMap();
            map.put("iotId", this.i);
            map.put("streamType", Integer.valueOf(this.j));
            map.put("relayEncrypted", Boolean.valueOf(this.k));
            map.put("relayEncryptType", 0);
            map.put("forceIFrame", Boolean.valueOf(this.m));
            Boolean bool = Boolean.TRUE;
            map.put("enablePrePlay", bool);
            map.put("needDomainName", bool);
            map.put("enableWebSocket", bool);
            APIHelper.sendIoTRequest(lvbyte.lvdo.LIVE_QUERY, map, this.i, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.LivePlayer.1
                @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                public void onFailed(lvif lvifVar) {
                    ALog.w(LivePlayer.TAG, lvifVar.lvdo() + "\t" + lvifVar.lvfor());
                }

                @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                public void onResponse(lvif lvifVar) {
                    if (lvifVar.lvdo() != 200) {
                        ALog.w(LivePlayer.TAG, "p2p create failed.");
                        return;
                    }
                    JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                    P2PConfig p2PConfig = new P2PConfig();
                    try {
                        String string = object.getString("signalUrl");
                        String string2 = object.getString("stunUrl");
                        boolean booleanValue = object.getBooleanValue("supportP2pPrePlay");
                        p2PConfig.setSignalUrl(string);
                        p2PConfig.setStunUrl(string2);
                        if (booleanValue) {
                            LinkVisual.pre_create_p2p(iQuery_connected_channel, LivePlayer.this.j, p2PConfig.getSession(), p2PConfig.getStunServer(), p2PConfig.getStunPort(), p2PConfig.getStunKey(), p2PConfig.getSignalUrl());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return iQuery_and_play;
        }
        if (!lvnew() || lvint()) {
            String str2 = this.f5085lvif;
            boolean z = this.lvwhile;
            int i2 = this.o;
            boolean z2 = this.f5083lvfor;
            byte[] bArr = this.f5086lvint;
            byte[] bArr2 = this.f5088lvnew;
            ILvStreamCallback iLvStreamCallback2 = this.e;
            ByteBuffer byteBuffer2 = this.lvconst;
            int iCapacity2 = byteBuffer2.capacity();
            SeiInfoBuffer seiInfoBuffer2 = this.lvinterface;
            return LinkVisual.open_rtmp_stream(str2, 1, z, i2, z2, bArr, bArr2, iLvStreamCallback2, byteBuffer2, iCapacity2, seiInfoBuffer2 != null ? seiInfoBuffer2.seiDirectBuffer : null, seiInfoBuffer2 != null ? seiInfoBuffer2.seiDirectBuffer.capacity() : 0);
        }
        String str3 = this.f5085lvif;
        int i3 = this.j;
        boolean z3 = this.f5083lvfor;
        byte[] bArr3 = this.f5086lvint;
        byte[] bArr4 = this.f5088lvnew;
        int i4 = this.o;
        String str4 = this.i;
        String session = this.f5090lvtry.getSession();
        String stunServer = this.f5090lvtry.getStunServer();
        int stunPort = this.f5090lvtry.getStunPort();
        String stunKey = this.f5090lvtry.getStunKey();
        String signalUrl = this.f5090lvtry.getSignalUrl();
        ILvStreamCallback iLvStreamCallback3 = this.e;
        ByteBuffer byteBuffer3 = this.lvconst;
        int iCapacity3 = byteBuffer3.capacity();
        SeiInfoBuffer seiInfoBuffer3 = this.lvinterface;
        return LinkVisual.open_p2p_stream(str3, 1, i3, z3, bArr3, bArr4, i4, str4, session, stunServer, stunPort, stunKey, signalUrl, iLvStreamCallback3, byteBuffer3, iCapacity3, seiInfoBuffer3 != null ? seiInfoBuffer3.seiDirectBuffer : null, seiInfoBuffer3 != null ? seiInfoBuffer3.seiDirectBuffer.capacity() : 0);
    }

    @Override // com.aliyun.iotx.linkvisual.media.video.lvdo
    protected boolean lvthis() {
        Long l = LinkVisualMedia.getInstance().playerIotIdTidMap.get(this.i);
        return l == null || l.longValue() == Thread.currentThread().getId();
    }

    public void prepare() {
        this.lvboolean.post(this.r);
    }

    public void setBufferedFrameCount(int i) {
        LinkVisual.set_display_buffer_size(i);
    }

    public void setDataSource(String str, boolean z, byte[] bArr, byte[] bArr2) throws IllegalArgumentException {
        lvdo(str, z, bArr, bArr2, null);
    }

    public void setForceIFrame(boolean z) {
        this.m = z;
    }

    public void setIPCLiveDataSource(String str, int i) {
        Version.checkSupport();
        this.lvboolean.post(new SetIPCDataSourceRunnable(str, i, true, 0, true, 0));
    }

    public void setIPCLiveDataSource(String str, int i, int i2) {
        Version.checkSupport();
        this.lvboolean.post(new SetIPCDataSourceRunnable(str, i, true, 0, true, i2));
    }

    @Deprecated
    public void setIPCLiveDataSource(String str, int i, boolean z, int i2, boolean z2) {
        Version.checkSupport();
        this.lvboolean.post(new SetIPCDataSourceRunnable(str, i, z, i2, z2, 0));
    }

    public void setIPCLiveDataSource(String str, int i, boolean z, int i2, boolean z2, int i3) {
        Version.checkSupport();
        this.lvboolean.post(new SetIPCDataSourceRunnable(str, i, z, i2, z2, i3));
    }

    public void setIotId(String str) {
        this.i = str;
    }

    public void setMaxJitterBufferSizeInMs(int i) {
        LinkVisual.set_max_jitter_buffer_size_in_ms(i);
    }

    public void setOnVideoQualityListener(OnVideoQualityListener onVideoQualityListener) {
        this.p = onVideoQualityListener;
    }

    public void setReconnectCount(int i) {
        this.o = i;
    }

    public void setRelayEncrypted(boolean z) {
        this.k = z;
    }

    public void setRelayEncryptedType(int i) {
        this.l = i;
    }

    public void setStreamType(int i) {
        this.j = i;
    }
}
