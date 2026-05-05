package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import android.os.Build;
import com.alibaba.fastjson.JSON;
import com.aliyun.iotx.linkvisual.media.Version;

/* JADX INFO: loaded from: classes2.dex */
public class StartP2PStreamingParams extends BaseParams {
    private long answerSdpTs;
    private String audioCodec;
    private String decodeType;
    private String devBrand;
    private int height;
    private int iframeSize;
    private long iframeTs;
    private String iotId;
    private int maxJitterSize;
    private String natType;
    private String network;
    private long offerSdpTs;
    private String p2pType;
    private long pktTs;
    private String platform;
    private boolean preConnect;
    private long renderTs;
    private long reqTs;
    private long respTs;
    private String sdkVersion;
    private long stunReqTs;
    private long stunRespTs;
    private String sysModel;
    private String sysVersion;
    private String videoCodec;
    private int width;

    public static final class Builder {
        private long answerSdpTs;
        private String audioCodec;
        private String decodeType;
        private int hight;
        private int iframeSize;
        private long iframeTs;
        private String iotId;
        private int maxJitterSize;
        private String natType;
        private String network;
        private long offerSdpTs;
        private String p2pType;
        private long pktTs;
        private boolean preConnect;
        private long renderTs;
        private long reqTs;
        private long respTs;
        private String sessionId;
        private long stunReqTs;
        private long stunRespTs;
        private String videoCodec;
        private int width;

        private Builder() {
        }

        public Builder answerSdpTs(long j) {
            this.answerSdpTs = j;
            return this;
        }

        public Builder audioCodec(String str) {
            this.audioCodec = str;
            return this;
        }

        public StartP2PStreamingParams build() {
            return new StartP2PStreamingParams(this);
        }

        public Builder decodeType(String str) {
            this.decodeType = str;
            return this;
        }

        public Builder hight(int i) {
            this.hight = i;
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

        public Builder natType(String str) {
            this.natType = str;
            return this;
        }

        public Builder network(String str) {
            this.network = str;
            return this;
        }

        public Builder offerSdpTs(long j) {
            this.offerSdpTs = j;
            return this;
        }

        public Builder p2pType(String str) {
            this.p2pType = str;
            return this;
        }

        public Builder pktTs(long j) {
            this.pktTs = j;
            return this;
        }

        public Builder preConnect(boolean z) {
            this.preConnect = z;
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

        public Builder sessionId(String str) {
            this.sessionId = str;
            return this;
        }

        public Builder stunReqTs(long j) {
            this.stunReqTs = j;
            return this;
        }

        public Builder stunRespTs(long j) {
            this.stunRespTs = j;
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

    public StartP2PStreamingParams() {
        setSdkVersion(Version.SDK_VERSION);
        setPlatform("Android");
        setDevBrand(Build.MANUFACTURER);
        setSysModel(Build.MODEL);
        setSysVersion(Build.VERSION.SDK_INT + "");
    }

    private StartP2PStreamingParams(Builder builder) {
        this();
        setSessionId(builder.sessionId);
        setIotId(builder.iotId);
        setPreConnect(builder.preConnect);
        setReqTs(builder.reqTs);
        setRespTs(builder.respTs);
        setStunReqTs(builder.stunReqTs);
        setStunRespTs(builder.stunRespTs);
        setOfferSdpTs(builder.offerSdpTs);
        setAnswerSdpTs(builder.answerSdpTs);
        setPktTs(builder.pktTs);
        setMaxJitterSize(builder.maxJitterSize);
        setIframeTs(builder.iframeTs);
        setRenderTs(builder.renderTs);
        setIframeSize(builder.iframeSize);
        setVideoCodec(builder.videoCodec);
        setAudioCodec(builder.audioCodec);
        setDecodeType(builder.decodeType);
        setWidth(builder.width);
        setHeight(builder.hight);
        setNetwork(builder.network);
        setP2pType(builder.p2pType);
        setNatType(builder.natType);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static StartP2PStreamingParams parseFromJSONString(String str) {
        return (StartP2PStreamingParams) JSON.parseObject(str, StartP2PStreamingParams.class);
    }

    public long getAnswerSdpTs() {
        return this.answerSdpTs;
    }

    public String getAudioCodec() {
        return this.audioCodec;
    }

    public String getDecodeType() {
        return this.decodeType;
    }

    public String getDevBrand() {
        return this.devBrand;
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

    public String getNatType() {
        return this.natType;
    }

    public String getNetwork() {
        return this.network;
    }

    public long getOfferSdpTs() {
        return this.offerSdpTs;
    }

    public String getP2pType() {
        return this.p2pType;
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

    public long getStunReqTs() {
        return this.stunReqTs;
    }

    public long getStunRespTs() {
        return this.stunRespTs;
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

    public boolean isPreConnect() {
        return this.preConnect;
    }

    public StartP2PStreamingParams setAnswerSdpTs(long j) {
        this.answerSdpTs = j;
        return this;
    }

    public StartP2PStreamingParams setAudioCodec(String str) {
        this.audioCodec = str;
        return this;
    }

    public StartP2PStreamingParams setDecodeType(String str) {
        this.decodeType = str;
        return this;
    }

    public StartP2PStreamingParams setDevBrand(String str) {
        this.devBrand = str;
        return this;
    }

    public StartP2PStreamingParams setHeight(int i) {
        this.height = i;
        return this;
    }

    public StartP2PStreamingParams setIframeSize(int i) {
        this.iframeSize = i;
        return this;
    }

    public StartP2PStreamingParams setIframeTs(long j) {
        this.iframeTs = j;
        return this;
    }

    public StartP2PStreamingParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public StartP2PStreamingParams setMaxJitterSize(int i) {
        this.maxJitterSize = i;
        return this;
    }

    public StartP2PStreamingParams setNatType(String str) {
        this.natType = str;
        return this;
    }

    public StartP2PStreamingParams setNetwork(String str) {
        this.network = str;
        return this;
    }

    public StartP2PStreamingParams setOfferSdpTs(long j) {
        this.offerSdpTs = j;
        return this;
    }

    public StartP2PStreamingParams setP2pType(String str) {
        this.p2pType = str;
        return this;
    }

    public StartP2PStreamingParams setPktTs(long j) {
        this.pktTs = j;
        return this;
    }

    public StartP2PStreamingParams setPlatform(String str) {
        this.platform = str;
        return this;
    }

    public StartP2PStreamingParams setPreConnect(boolean z) {
        this.preConnect = z;
        return this;
    }

    public StartP2PStreamingParams setRenderTs(long j) {
        this.renderTs = j;
        return this;
    }

    public StartP2PStreamingParams setReqTs(long j) {
        this.reqTs = j;
        return this;
    }

    public StartP2PStreamingParams setRespTs(long j) {
        this.respTs = j;
        return this;
    }

    public StartP2PStreamingParams setSdkVersion(String str) {
        this.sdkVersion = str;
        return this;
    }

    public StartP2PStreamingParams setStunReqTs(long j) {
        this.stunReqTs = j;
        return this;
    }

    public StartP2PStreamingParams setStunRespTs(long j) {
        this.stunRespTs = j;
        return this;
    }

    public StartP2PStreamingParams setSysModel(String str) {
        this.sysModel = str;
        return this;
    }

    public StartP2PStreamingParams setSysVersion(String str) {
        this.sysVersion = str;
        return this;
    }

    public StartP2PStreamingParams setVideoCodec(String str) {
        this.videoCodec = str;
        return this;
    }

    public StartP2PStreamingParams setWidth(int i) {
        this.width = i;
        return this;
    }
}
