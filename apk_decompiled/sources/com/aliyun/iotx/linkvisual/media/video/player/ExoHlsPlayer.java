package com.aliyun.iotx.linkvisual.media.video.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.TextureView;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnRenderedFirstFrameListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnVideoSizeChangedListener;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import java.util.HashMap;
import lvbyte.lvdo;
import lvbyte.lvif;

/* JADX INFO: loaded from: classes2.dex */
public class ExoHlsPlayer implements Player.EventListener, VideoListener {
    public static final String TAG = "linksdk_lv_ExoHlsPlayer";

    /* JADX INFO: renamed from: lvbreak, reason: collision with root package name */
    private String f5120lvbreak;

    /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
    private OnPlayerStateChangedListener f5121lvbyte;

    /* JADX INFO: renamed from: lvcase, reason: collision with root package name */
    private OnPreparedListener f5122lvcase;

    /* JADX INFO: renamed from: lvcatch, reason: collision with root package name */
    private Handler f5123lvcatch;

    /* JADX INFO: renamed from: lvchar, reason: collision with root package name */
    private OnRenderedFirstFrameListener f5124lvchar;
    private Handler lvclass;

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private Context f5125lvdo;
    private final Runnable lvelse = new AnonymousClass1();

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private SurfaceView f5126lvfor;

    /* JADX INFO: renamed from: lvgoto, reason: collision with root package name */
    private OnErrorListener f5127lvgoto;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private SimpleExoPlayer f5128lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private TextureView f5129lvint;

    /* JADX INFO: renamed from: lvlong, reason: collision with root package name */
    private OnCompletionListener f5130lvlong;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private MediaSource f5131lvnew;

    /* JADX INFO: renamed from: lvthis, reason: collision with root package name */
    private OnVideoSizeChangedListener f5132lvthis;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private DataSource.Factory f5133lvtry;

    /* JADX INFO: renamed from: lvvoid, reason: collision with root package name */
    private String f5134lvvoid;

    /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer$1, reason: invalid class name */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (TextUtils.isEmpty(ExoHlsPlayer.this.f5134lvvoid)) {
                if (ExoHlsPlayer.this.f5131lvnew == null) {
                    ExoHlsPlayer.this.lvdo(new PlayerException(6, 1008, "No data source has been set!"));
                    return;
                } else {
                    ExoHlsPlayer.this.f5123lvcatch.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ExoHlsPlayer.this.f5122lvcase != null) {
                                ExoHlsPlayer.this.f5122lvcase.onPrepared();
                            }
                        }
                    });
                    ExoHlsPlayer.this.lvclass.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.1.3
                        @Override // java.lang.Runnable
                        public void run() {
                            ExoHlsPlayer.this.f5128lvif.prepare(ExoHlsPlayer.this.f5131lvnew);
                        }
                    });
                    return;
                }
            }
            HashMap map = new HashMap(2);
            map.put("iotId", ExoHlsPlayer.this.f5134lvvoid);
            if (TextUtils.isEmpty(ExoHlsPlayer.this.f5120lvbreak)) {
                ExoHlsPlayer.this.lvdo(new PlayerException(6, 1008, "fileName required."));
            } else {
                map.put("fileName", ExoHlsPlayer.this.f5120lvbreak);
                APIHelper.sendIoTRequest(lvdo.CLOUD_VOD_BY_FILENAME, map, ExoHlsPlayer.this.f5134lvvoid, new IAPIHelperListener() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.1.1
                    @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                    public void onFailed(lvif lvifVar) {
                        ExoHlsPlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                    }

                    @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
                    public void onResponse(lvif lvifVar) {
                        if (lvifVar.lvdo() != 200) {
                            ExoHlsPlayer.this.lvdo(new PlayerException(6, 1009, lvifVar.lvfor()));
                            return;
                        }
                        final JSONObject object = JSON.parseObject(String.valueOf(lvifVar.lvif()));
                        if (object != null && !TextUtils.isEmpty(object.getString("vodUrl"))) {
                            ExoHlsPlayer.this.lvclass.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.1.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ExoHlsPlayer.this.setDataSource(object.getString("vodUrl"));
                                    ExoHlsPlayer.this.f5128lvif.prepare(ExoHlsPlayer.this.f5131lvnew);
                                }
                            });
                            ExoHlsPlayer.this.f5123lvcatch.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.1.1.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (ExoHlsPlayer.this.f5122lvcase != null) {
                                        ExoHlsPlayer.this.f5122lvcase.onPrepared();
                                    }
                                }
                            });
                            return;
                        }
                        ExoHlsPlayer.this.lvdo(new PlayerException(6, 1007, "url is null."));
                        ALog.w(ExoHlsPlayer.TAG, "[" + ExoHlsPlayer.this.hashCode() + "] url is null");
                    }
                });
            }
        }
    }

    public ExoHlsPlayer(Context context) {
        Version.printVersionInfo();
        this.f5125lvdo = context;
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        this.f5128lvif = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(defaultBandwidthMeter)), new DefaultLoadControl());
        this.f5133lvtry = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "Exo2"), defaultBandwidthMeter);
        this.f5128lvif.addListener(this);
        this.f5128lvif.addVideoListener(this);
        this.f5123lvcatch = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
        this.lvclass = new Handler();
    }

    private void lvdo() {
        this.f5134lvvoid = null;
        this.f5131lvnew = null;
        this.f5120lvbreak = null;
    }

    public void clearSurfaceView() {
        this.f5128lvif.clearVideoSurfaceView(this.f5126lvfor);
    }

    public void clearTextureView() {
        this.f5128lvif.clearVideoTextureView(this.f5129lvint);
        this.f5129lvint = null;
    }

    public int getBufferedPercentage() {
        return this.f5128lvif.getBufferedPercentage();
    }

    public long getCurrentPosition() {
        return this.f5128lvif.getCurrentPosition();
    }

    public long getDuration() {
        return this.f5128lvif.getDuration();
    }

    public ExoPlayer getExoPlayer() {
        return this.f5128lvif;
    }

    public String getFileName() {
        return this.f5120lvbreak;
    }

    public String getIotId() {
        Version.checkSupport();
        return this.f5134lvvoid;
    }

    public int getPlayState() {
        return this.f5128lvif.getPlaybackState();
    }

    public TextureView getTextureView() {
        return this.f5129lvint;
    }

    public float getVolume() {
        return this.f5128lvif.getVolume();
    }

    void lvdo(final PlayerException playerException) {
        if (this.f5127lvgoto != null) {
            this.f5123lvcatch.post(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer.2
                @Override // java.lang.Runnable
                public void run() {
                    ExoHlsPlayer.this.f5127lvgoto.onError(playerException);
                }
            });
        }
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onLoadingChanged(boolean z) {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onPlayerError(ExoPlaybackException exoPlaybackException) {
        if (this.f5127lvgoto != null) {
            int i = exoPlaybackException.type;
            this.f5127lvgoto.onError(i != 0 ? i != 1 ? new PlayerException(8, exoPlaybackException.getUnexpectedException().getMessage()) : new PlayerException(7, exoPlaybackException.getRendererException().getMessage()) : new PlayerException(6, exoPlaybackException.getSourceException().getMessage()));
        }
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onPlayerStateChanged(boolean z, int i) {
        OnCompletionListener onCompletionListener;
        if (i == 4 && (onCompletionListener = this.f5130lvlong) != null) {
            onCompletionListener.onCompletion();
        }
        OnPlayerStateChangedListener onPlayerStateChangedListener = this.f5121lvbyte;
        if (onPlayerStateChangedListener != null) {
            onPlayerStateChangedListener.onPlayerStateChange(i);
        }
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onPositionDiscontinuity(int i) {
    }

    @Override // com.google.android.exoplayer2.video.VideoListener
    public void onRenderedFirstFrame() {
        OnRenderedFirstFrameListener onRenderedFirstFrameListener = this.f5124lvchar;
        if (onRenderedFirstFrameListener != null) {
            onRenderedFirstFrameListener.onRenderedFirstFrame();
        }
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onRepeatModeChanged(int i) {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onSeekProcessed() {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onShuffleModeEnabledChanged(boolean z) {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onTimelineChanged(Timeline timeline, Object obj, int i) {
    }

    @Override // com.google.android.exoplayer2.Player.EventListener
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
    }

    @Override // com.google.android.exoplayer2.video.VideoListener
    public void onVideoSizeChanged(int i, int i2, int i3, float f) {
        OnVideoSizeChangedListener onVideoSizeChangedListener = this.f5132lvthis;
        if (onVideoSizeChangedListener != null) {
            onVideoSizeChangedListener.onVideoSizeChanged(i, i2);
        }
    }

    public void pause() {
        this.f5128lvif.setPlayWhenReady(false);
    }

    public void prepare() {
        this.lvelse.run();
    }

    public void release() {
        this.f5128lvif.release();
        this.f5125lvdo = null;
    }

    public void reset() {
        this.f5128lvif.stop(true);
        lvdo();
    }

    public void seekTo(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("seekTo position must larger than 0");
        }
        this.f5128lvif.seekTo(j);
    }

    public void setAudioStreamType(int i) {
        this.f5128lvif.setAudioStreamType(i);
    }

    public void setCirclePlay(boolean z) {
        this.f5128lvif.setRepeatMode(z ? 1 : 0);
    }

    public void setDataSource(String str) {
        lvdo();
        ALog.d(TAG, "[" + hashCode() + "] url= \n" + str + SdkConstant.CLOUDAPI_LF);
        this.f5131lvnew = new HlsMediaSource.Factory(this.f5133lvtry).createMediaSource(Uri.parse(str));
    }

    public void setDataSourceByIPCRecordFileName(String str, String str2) {
        Version.checkSupport();
        lvfor.lvdo.lvif().lvdo(str);
        lvdo();
        this.f5134lvvoid = str;
        this.f5120lvbreak = str2;
    }

    public void setFileName(String str) {
        this.f5120lvbreak = str;
    }

    public void setIotId(String str) {
        Version.checkSupport();
        this.f5134lvvoid = str;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.f5130lvlong = onCompletionListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.f5127lvgoto = onErrorListener;
    }

    public void setOnPlayerStateChangedListener(OnPlayerStateChangedListener onPlayerStateChangedListener) {
        this.f5121lvbyte = onPlayerStateChangedListener;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.f5122lvcase = onPreparedListener;
    }

    public void setOnRenderedFirstFrameListener(OnRenderedFirstFrameListener onRenderedFirstFrameListener) {
        this.f5124lvchar = onRenderedFirstFrameListener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.f5132lvthis = onVideoSizeChangedListener;
    }

    public void setPlaybackSpeed(float f) {
        this.f5128lvif.setPlaybackParameters(new PlaybackParameters(f));
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.f5126lvfor = surfaceView;
        this.f5128lvif.setVideoSurfaceView(surfaceView);
    }

    public void setTextureView(TextureView textureView) {
        this.f5129lvint = textureView;
        this.f5128lvif.setVideoTextureView(textureView);
    }

    public void setVideoScalingMode(int i) {
        this.f5128lvif.setVideoScalingMode(i);
    }

    public void setVolume(float f) {
        this.f5128lvif.setVolume(f);
    }

    public void start() {
        this.f5128lvif.setPlayWhenReady(true);
    }

    public void stop() {
        this.f5128lvif.stop();
    }
}
