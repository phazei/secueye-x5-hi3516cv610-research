package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import com.alibaba.fastjson.JSON;

/* JADX INFO: loaded from: classes2.dex */
public class TransQualityStatisticParams extends BaseParams {
    private float avgBitrate;
    private float avgDecodeTime;
    private float avgFps;
    private float avgRemainFrame;
    private long begin;
    private int decodeErrorFrame;
    private String decodeType;
    private int dropFrame;
    private long end;
    private float fps;
    private int gop;
    private String iotId;
    private int[] jitterSize;
    private int maxIframe;
    private String module;
    private int peakBitrate;
    private int[] pingDelay;
    private float[] recvDeltaSd;
    private long totalFlow;

    public static final class Builder {
        private float avgBitrate;
        private float avgDecodeTime;
        private float avgFps;
        private float avgRemainFrame;
        private long begin;
        private int decodeErrorFrame;
        private String decodeType;
        private int dropFrame;
        private long end;
        private float fps;
        private int gop;
        private String iotId;
        private int[] jitterSize;
        private int maxIframe;
        private String module;
        private int peakBitrate;
        private int[] pingDelay;
        private float[] recvDeltaSd;
        private String sessionId;
        private long totalFlow;

        private Builder() {
        }

        private Builder jitterSize(int[] iArr) {
            this.jitterSize = iArr;
            return this;
        }

        public Builder avgBitrate(int i) {
            this.avgBitrate = i;
            return this;
        }

        public Builder avgDecodeTime(float f) {
            this.avgDecodeTime = f;
            return this;
        }

        public Builder avgFps(float f) {
            this.avgFps = f;
            return this;
        }

        public Builder avgRemainFrame(float f) {
            this.avgRemainFrame = f;
            return this;
        }

        public Builder begin(long j) {
            this.begin = j;
            return this;
        }

        public TransQualityStatisticParams build() {
            return new TransQualityStatisticParams(this);
        }

        public Builder decodeErrorFrame(int i) {
            this.decodeErrorFrame = i;
            return this;
        }

        public Builder decodeType(String str) {
            this.decodeType = str;
            return this;
        }

        public Builder dropFrame(int i) {
            this.dropFrame = i;
            return this;
        }

        public Builder end(long j) {
            this.end = j;
            return this;
        }

        public Builder fps(float f) {
            this.fps = f;
            return this;
        }

        public Builder gop(int i) {
            this.gop = i;
            return this;
        }

        public Builder iotId(String str) {
            this.iotId = str;
            return this;
        }

        public Builder maxIframe(int i) {
            this.maxIframe = i;
            return this;
        }

        public Builder module(String str) {
            this.module = str;
            return this;
        }

        public Builder peakBitrate(int i) {
            this.peakBitrate = i;
            return this;
        }

        public Builder pingDelay(int[] iArr) {
            this.pingDelay = iArr;
            return this;
        }

        public Builder recvDeltaSd(float[] fArr) {
            this.recvDeltaSd = fArr;
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

    public TransQualityStatisticParams() {
    }

    private TransQualityStatisticParams(Builder builder) {
        setSessionId(builder.sessionId);
        setIotId(builder.iotId);
        setModule(builder.module);
        setBegin(builder.begin);
        setEnd(builder.end);
        setRecvDeltaSd(builder.recvDeltaSd);
        setPingDelay(builder.pingDelay);
        setDropFrame(builder.dropFrame);
        setAvgBitrate(builder.avgBitrate);
        setPeakBitrate(builder.peakBitrate);
        setFps(builder.fps);
        setAvgFps(builder.avgFps);
        setGop(builder.gop);
        setAvgRemainFrame(builder.avgRemainFrame);
        setMaxIframe(builder.maxIframe);
        setTotalFlow(builder.totalFlow);
        setDecodeType(builder.decodeType);
        setAvgDecodeTime(builder.avgDecodeTime);
        setDecodeErrorFrame(builder.decodeErrorFrame);
        setJitterSize(builder.jitterSize);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static TransQualityStatisticParams parseFromJSONString(String str) {
        return (TransQualityStatisticParams) JSON.parseObject(str, TransQualityStatisticParams.class);
    }

    public float getAvgBitrate() {
        return this.avgBitrate;
    }

    public float getAvgDecodeTime() {
        return this.avgDecodeTime;
    }

    public float getAvgFps() {
        return this.avgFps;
    }

    public float getAvgRemainFrame() {
        return this.avgRemainFrame;
    }

    public long getBegin() {
        return this.begin;
    }

    public int getDecodeErrorFrame() {
        return this.decodeErrorFrame;
    }

    public String getDecodeType() {
        return this.decodeType;
    }

    public int getDropFrame() {
        return this.dropFrame;
    }

    public long getEnd() {
        return this.end;
    }

    public float getFps() {
        return this.fps;
    }

    public int getGop() {
        return this.gop;
    }

    public String getIotId() {
        return this.iotId;
    }

    public int[] getJitterSize() {
        return this.jitterSize;
    }

    public int getMaxIframe() {
        return this.maxIframe;
    }

    public String getModule() {
        return this.module;
    }

    public int getPeakBitrate() {
        return this.peakBitrate;
    }

    public int[] getPingDelay() {
        return this.pingDelay;
    }

    public float[] getRecvDeltaSd() {
        return this.recvDeltaSd;
    }

    public long getTotalFlow() {
        return this.totalFlow;
    }

    public TransQualityStatisticParams setAvgBitrate(float f) {
        this.avgBitrate = f;
        return this;
    }

    public TransQualityStatisticParams setAvgDecodeTime(float f) {
        this.avgDecodeTime = f;
        return this;
    }

    public TransQualityStatisticParams setAvgFps(float f) {
        this.avgFps = f;
        return this;
    }

    public TransQualityStatisticParams setAvgRemainFrame(float f) {
        this.avgRemainFrame = f;
        return this;
    }

    public TransQualityStatisticParams setBegin(long j) {
        this.begin = j;
        return this;
    }

    public TransQualityStatisticParams setDecodeErrorFrame(int i) {
        this.decodeErrorFrame = i;
        return this;
    }

    public TransQualityStatisticParams setDecodeType(String str) {
        this.decodeType = str;
        return this;
    }

    public TransQualityStatisticParams setDropFrame(int i) {
        this.dropFrame = i;
        return this;
    }

    public TransQualityStatisticParams setEnd(long j) {
        this.end = j;
        return this;
    }

    public TransQualityStatisticParams setFps(float f) {
        this.fps = f;
        return this;
    }

    public TransQualityStatisticParams setGop(int i) {
        this.gop = i;
        return this;
    }

    public TransQualityStatisticParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public TransQualityStatisticParams setJitterSize(int[] iArr) {
        this.jitterSize = iArr;
        return this;
    }

    public TransQualityStatisticParams setMaxIframe(int i) {
        this.maxIframe = i;
        return this;
    }

    public TransQualityStatisticParams setModule(String str) {
        this.module = str;
        return this;
    }

    public TransQualityStatisticParams setPeakBitrate(int i) {
        this.peakBitrate = i;
        return this;
    }

    public TransQualityStatisticParams setPingDelay(int[] iArr) {
        this.pingDelay = iArr;
        return this;
    }

    public TransQualityStatisticParams setRecvDeltaSd(float[] fArr) {
        this.recvDeltaSd = fArr;
        return this;
    }

    public TransQualityStatisticParams setTotalFlow(long j) {
        this.totalFlow = j;
        return this;
    }
}
