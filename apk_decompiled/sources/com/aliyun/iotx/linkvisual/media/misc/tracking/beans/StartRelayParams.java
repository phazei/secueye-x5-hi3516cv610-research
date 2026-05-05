package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import android.os.Build;
import com.alibaba.fastjson.JSON;
import com.aliyun.iotx.linkvisual.media.Version;

/* JADX INFO: loaded from: classes2.dex */
public class StartRelayParams extends BaseParams {
    private String audioCodec;
    private int cacheGop;
    private String decodeType;
    private String devBrand;
    private boolean encrypt;
    private long firstFrameDelay;
    private long handshakeTs;
    private int height;
    private int iframeSize;
    private long iframeTs;
    private String iotId;
    private int maxJitterSize;
    private String network;
    private long pktTs;
    private String platform;
    private long renderTs;
    private long reqTs;
    private long respTs;
    private String sdkVersion;
    private long seek;
    private long startRtmpTs;
    private String sysModel;
    private String sysVersion;
    private String videoCodec;
    private int width;

    public static final class Builder {
        private String audioCodec;
        private int cacheGop;
        private String decodeType;
        private String devBrand;
        private boolean encrypt;
        private long firstFrameDelay;
        private long handshakeTs;
        private int height;
        private int iframeSize;
        private long iframeTs;
        private String iotId;
        private int maxJitterSize;
        private String network;
        private long pktTs;
        private String platform;
        private long renderTs;
        private long reqTs;
        private long respTs;
        private String sdkVersion;
        private long seek;
        private String sessionId;
        private long startRtmpTs;
        private String sysModel;
        private String sysVersion;
        private String videoCodec;
        private int width;

        private Builder() {
        }

        public Builder audioCodec(String str) {
            this.audioCodec = str;
            return this;
        }

        public StartRelayParams build() {
            return new StartRelayParams(this);
        }

        public Builder cacheGop(int i) {
            this.cacheGop = i;
            return this;
        }

        public Builder decodeType(String str) {
            this.decodeType = str;
            return this;
        }

        public Builder devBrand(String str) {
            this.devBrand = str;
            return this;
        }

        public Builder encrypt(boolean z) {
            this.encrypt = z;
            return this;
        }

        public Builder firstFrameDelay(long j) {
            this.firstFrameDelay = j;
            return this;
        }

        public Builder handshakeTs(long j) {
            this.handshakeTs = j;
            return this;
        }

        public Builder height(int i) {
            this.height = i;
            return this;
        }

        public Builder iframeSize(int i) {
            this.iframeSize = i;
            return this;
        }

        public Builder iframeTs(long j) {
            this.iframeTs = j;
            return this;
        }

        public Builder iotId(String str) {
            this.iotId = str;
            return this;
        }

        public Builder maxJitterSize(int i) {
            this.maxJitterSize = i;
            return this;
        }

        public Builder network(String str) {
            this.network = str;
            return this;
        }

        public Builder pktTs(long j) {
            this.pktTs = j;
            return this;
        }

        public Builder platform(String str) {
            this.platform = str;
            return this;
        }

        public Builder renderTs(long j) {
            this.renderTs = j;
            return this;
        }

        public Builder reqTs(long j) {
            this.reqTs = j;
            return this;
        }

        public Builder respTs(long j) {
            this.respTs = j;
            return this;
        }

        public Builder sdkVersion(String str) {
            this.sdkVersion = str;
            return this;
        }

        public Builder seek(long j) {
            this.seek = j;
            return this;
        }

        public Builder sessionId(String str) {
            this.sessionId = str;
            return this;
        }

        public Builder startRtmpTs(long j) {
            this.startRtmpTs = j;
            return this;
        }

        public Builder sysModel(String str) {
            this.sysModel = str;
            return this;
        }

        public Builder sysVersion(String str) {
            this.sysVersion = str;
            return this;
        }

        public Builder videoCodec(String str) {
            this.videoCodec = str;
            return this;
        }

        public Builder width(int i) {
            this.width = i;
            return this;
        }
    }

    public StartRelayParams() {
        setSdkVersion(Version.SDK_VERSION);
        setPlatform("Android");
        setDevBrand(Build.MANUFACTURER);
        setSysModel(Build.MODEL);
        setSysVersion(Build.VERSION.SDK_INT + "");
    }

    private StartRelayParams(Builder builder) {
        this();
        setSessionId(builder.sessionId);
        setIotId(builder.iotId);
        setReqTs(builder.reqTs);
        setRespTs(builder.respTs);
        setStartRtmpTs(builder.startRtmpTs);
        setHandshakeTs(builder.handshakeTs);
        setPktTs(builder.pktTs);
        setIframeTs(builder.iframeTs);
        setRenderTs(builder.renderTs);
        setIframeSize(builder.iframeSize);
        setMaxJitterSize(builder.maxJitterSize);
        setVideoCodec(builder.videoCodec);
        setAudioCodec(builder.audioCodec);
        setEncrypt(builder.encrypt);
        setDecodeType(builder.decodeType);
        setSeek(builder.seek);
        setCacheGop(builder.cacheGop);
        setWidth(builder.width);
        setHeight(builder.height);
        setNetwork(builder.network);
        setFirstFrameDelay(builder.firstFrameDelay);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static StartRelayParams parseFromJSONString(String str) {
        return (StartRelayParams) JSON.parseObject(str, StartRelayParams.class);
    }

    public String getAudioCodec() {
        return this.audioCodec;
    }

    public int getCacheGop() {
        return this.cacheGop;
    }

    public String getDecodeType() {
        return this.decodeType;
    }

    public String getDevBrand() {
        return this.devBrand;
    }

    public long getFirstFrameDelay() {
        return this.firstFrameDelay;
    }

    public long getHandshakeTs() {
        return this.handshakeTs;
    }

    public int getHeight() {
        return this.height;
    }

    public int getIframeSize() {
        return this.iframeSize;
    }

    public long getIframeTs() {
        return this.iframeTs;
    }

    public String getIotId() {
        return this.iotId;
    }

    public int getMaxJitterSize() {
        return this.maxJitterSize;
    }

    public String getNetwork() {
        return this.network;
    }

    public long getPktTs() {
        return this.pktTs;
    }

    public String getPlatform() {
        return this.platform;
    }

    public long getRenderTs() {
        return this.renderTs;
    }

    public long getReqTs() {
        return this.reqTs;
    }

    public long getRespTs() {
        return this.respTs;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public long getSeek() {
        return this.seek;
    }

    public long getStartRtmpTs() {
        return this.startRtmpTs;
    }

    public String getSysModel() {
        return this.sysModel;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public String getVideoCodec() {
        return this.videoCodec;
    }

    public int getWidth() {
        return this.width;
    }

    public int isCacheGop() {
        return this.cacheGop;
    }

    public boolean isEncrypt() {
        return this.encrypt;
    }

    public StartRelayParams setAudioCodec(String str) {
        this.audioCodec = str;
        return this;
    }

    public StartRelayParams setCacheGop(int i) {
        this.cacheGop = i;
        return this;
    }

    public StartRelayParams setDecodeType(String str) {
        this.decodeType = str;
        return this;
    }

    public StartRelayParams setDevBrand(String str) {
        this.devBrand = str;
        return this;
    }

    public StartRelayParams setEncrypt(boolean z) {
        this.encrypt = z;
        return this;
    }

    public StartRelayParams setFirstFrameDelay(long j) {
        this.firstFrameDelay = j;
        return this;
    }

    public StartRelayParams setHandshakeTs(long j) {
        this.handshakeTs = j;
        return this;
    }

    public StartRelayParams setHeight(int i) {
        this.height = i;
        return this;
    }

    public StartRelayParams setIframeSize(int i) {
        this.iframeSize = i;
        return this;
    }

    public StartRelayParams setIframeTs(long j) {
        this.iframeTs = j;
        return this;
    }

    public StartRelayParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public StartRelayParams setMaxJitterSize(int i) {
        this.maxJitterSize = i;
        return this;
    }

    public StartRelayParams setNetwork(String str) {
        this.network = str;
        return this;
    }

    public StartRelayParams setPktTs(long j) {
        this.pktTs = j;
        return this;
    }

    public StartRelayParams setPlatform(String str) {
        this.platform = str;
        return this;
    }

    public StartRelayParams setRenderTs(long j) {
        this.renderTs = j;
        return this;
    }

    public StartRelayParams setReqTs(long j) {
        this.reqTs = j;
        return this;
    }

    public StartRelayParams setRespTs(long j) {
        this.respTs = j;
        return this;
    }

    public StartRelayParams setSdkVersion(String str) {
        this.sdkVersion = str;
        return this;
    }

    public StartRelayParams setSeek(long j) {
        this.seek = j;
        return this;
    }

    public StartRelayParams setStartRtmpTs(long j) {
        this.startRtmpTs = j;
        return this;
    }

    public StartRelayParams setSysModel(String str) {
        this.sysModel = str;
        return this;
    }

    public StartRelayParams setSysVersion(String str) {
        this.sysVersion = str;
        return this;
    }

    public StartRelayParams setVideoCodec(String str) {
        this.videoCodec = str;
        return this;
    }

    public StartRelayParams setWidth(int i) {
        this.width = i;
        return this;
    }
}
