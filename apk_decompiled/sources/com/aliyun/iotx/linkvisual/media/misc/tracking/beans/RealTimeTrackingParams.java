package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import com.aliyun.iotx.linkvisual.media.Version;

/* JADX INFO: loaded from: classes2.dex */
public class RealTimeTrackingParams extends BaseParams {
    private long avgBitrate;
    private String content;
    private String iotId;
    private String natType;
    private String p2pType;
    private String platform;
    private long playTime;
    private boolean preConnect;
    private String sdkVersion;
    private long totalFlow;

    public static final class Builder {
        private long avgBitrate;
        private String content;
        private String iotId;
        private String natType;
        private String p2pType;
        private String platform;
        private long playTime;
        private boolean preConnect;
        private String sdkVersion;
        private String sessionId;
        private long totalFlow;

        private Builder() {
        }

        public Builder avgBitrate(long j) {
            this.avgBitrate = j;
            return this;
        }

        public RealTimeTrackingParams build() {
            return new RealTimeTrackingParams(this);
        }

        public Builder content(String str) {
            this.content = str;
            return this;
        }

        public Builder iotId(String str) {
            this.iotId = str;
            return this;
        }

        public Builder natType(String str) {
            this.natType = str;
            return this;
        }

        public Builder p2pType(String str) {
            this.p2pType = str;
            return this;
        }

        public Builder platform(String str) {
            this.platform = str;
            return this;
        }

        public Builder playTime(long j) {
            this.playTime = j;
            return this;
        }

        public Builder preConnect(boolean z) {
            this.preConnect = z;
            return this;
        }

        public Builder sdkVersion(String str) {
            this.sdkVersion = str;
            return this;
        }

        public Builder sessionId(String str) {
            this.sessionId = str;
            return this;
        }

        public Builder totalFlow(long j) {
            this.totalFlow = j;
            return this;
        }
    }

    public RealTimeTrackingParams() {
        setSdkVersion(Version.SDK_VERSION);
        setPlatform("Android");
    }

    private RealTimeTrackingParams(Builder builder) {
        setSessionId(builder.sessionId);
        this.content = builder.content;
        this.iotId = builder.iotId;
        this.preConnect = builder.preConnect;
        this.sdkVersion = builder.sdkVersion;
        this.platform = builder.platform;
        this.p2pType = builder.p2pType;
        this.natType = builder.natType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getAvgBitrate() {
        return this.avgBitrate;
    }

    public String getContent() {
        return this.content;
    }

    public String getIotId() {
        return this.iotId;
    }

    public String getNatType() {
        return this.natType;
    }

    public String getP2pType() {
        return this.p2pType;
    }

    public String getPlatform() {
        return this.platform;
    }

    public long getPlayTime() {
        return this.playTime;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public long getTotalFlow() {
        return this.totalFlow;
    }

    public boolean isPreConnect() {
        return this.preConnect;
    }

    public RealTimeTrackingParams setAvgBitrate(long j) {
        this.avgBitrate = j;
        return this;
    }

    public RealTimeTrackingParams setContent(String str) {
        this.content = str;
        return this;
    }

    public RealTimeTrackingParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public RealTimeTrackingParams setNatType(String str) {
        this.natType = str;
        return this;
    }

    public RealTimeTrackingParams setP2pType(String str) {
        this.p2pType = str;
        return this;
    }

    public RealTimeTrackingParams setPlatform(String str) {
        this.platform = str;
        return this;
    }

    public RealTimeTrackingParams setPlayTime(long j) {
        this.playTime = j;
        return this;
    }

    public RealTimeTrackingParams setPreConnect(boolean z) {
        this.preConnect = z;
        return this;
    }

    public RealTimeTrackingParams setSdkVersion(String str) {
        this.sdkVersion = str;
        return this;
    }

    public RealTimeTrackingParams setTotalFlow(long j) {
        this.totalFlow = j;
        return this;
    }
}
