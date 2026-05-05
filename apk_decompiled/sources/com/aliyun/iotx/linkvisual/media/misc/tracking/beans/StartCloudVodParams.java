package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import android.os.Build;
import com.alibaba.fastjson.JSON;
import com.aliyun.iotx.linkvisual.media.Version;

/* JADX INFO: loaded from: classes2.dex */
public class StartCloudVodParams extends BaseParams {
    private String audioCodec;
    private String decodeType;
    private String devBrand;
    private boolean encrypt;
    private String fileName;
    private int firstSliceGetTs;
    private int firstSliceSize;
    private int height;
    private int iframeSize;
    private String iotId;
    private int m3u8GetTs;
    private int m3u8Size;
    private String network;
    private String platform;
    private long renderTs;
    private long reqTs;
    private long respTs;
    private String sdkVersion;
    private long seek;
    private String sysModel;
    private String sysVersion;
    private String videoCodec;
    private int width;

    public static final class Builder {
        private String audioCodec;
        private String decodeType;
        private String devBrand;
        private boolean encrypt;
        private String fileName;
        private int firstSliceGetTs;
        private int firstSliceSize;
        private int height;
        private int iframeSize;
        private String iotId;
        private int m3u8GetTs;
        private int m3u8Size;
        private String network;
        private String platform;
        private long renderTs;
        private long reqTs;
        private long respTs;
        private String sdkVersion;
        private long seek;
        private String sessionId;
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

        public StartCloudVodParams build() {
            return new StartCloudVodParams(this);
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

        public Builder fileName(String str) {
            this.fileName = str;
            return this;
        }

        public Builder firstSliceGetTs(int i) {
            this.firstSliceGetTs = i;
            return this;
        }

        public Builder firstSliceSize(int i) {
            this.firstSliceSize = i;
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

        public Builder iotId(String str) {
            this.iotId = str;
            return this;
        }

        public Builder m3u8GetTs(int i) {
            this.m3u8GetTs = i;
            return this;
        }

        public Builder m3u8Size(int i) {
            this.m3u8Size = i;
            return this;
        }

        public Builder network(String str) {
            this.network = str;
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

        public Builder seek(int i) {
            this.seek = i;
            return this;
        }

        public Builder sessionId(String str) {
            this.sessionId = str;
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

    public StartCloudVodParams() {
        setSdkVersion(Version.SDK_VERSION);
        setPlatform("Android");
        setDevBrand(Build.MANUFACTURER);
        setSysModel(Build.MODEL);
        setSysVersion(Build.VERSION.SDK_INT + "");
    }

    private StartCloudVodParams(Builder builder) {
        this();
        setSessionId(builder.sessionId);
        setIotId(builder.iotId);
        setFileName(builder.fileName);
        setReqTs(builder.reqTs);
        setRespTs(builder.respTs);
        setM3u8Size(builder.m3u8Size);
        setM3u8GetTs(builder.m3u8GetTs);
        setFirstSliceSize(builder.firstSliceSize);
        setFirstSliceGetTs(builder.firstSliceGetTs);
        setRenderTs(builder.renderTs);
        setIframeSize(builder.iframeSize);
        setVideoCodec(builder.videoCodec);
        setAudioCodec(builder.audioCodec);
        setEncrypt(builder.encrypt);
        setDecodeType(builder.decodeType);
        setSeek(builder.seek);
        setWidth(builder.width);
        setHeight(builder.height);
        setNetwork(builder.network);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static StartCloudVodParams parseFromJSONString(String str) {
        return (StartCloudVodParams) JSON.parseObject(str, StartCloudVodParams.class);
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

    public String getFileName() {
        return this.fileName;
    }

    public int getFirstSliceGetTs() {
        return this.firstSliceGetTs;
    }

    public int getFirstSliceSize() {
        return this.firstSliceSize;
    }

    public int getHeight() {
        return this.height;
    }

    public int getIframeSize() {
        return this.iframeSize;
    }

    public String getIotId() {
        return this.iotId;
    }

    public int getM3u8GetTs() {
        return this.m3u8GetTs;
    }

    public int getM3u8Size() {
        return this.m3u8Size;
    }

    public String getNetwork() {
        return this.network;
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

    public boolean isEncrypt() {
        return this.encrypt;
    }

    public StartCloudVodParams setAudioCodec(String str) {
        this.audioCodec = str;
        return this;
    }

    public StartCloudVodParams setDecodeType(String str) {
        this.decodeType = str;
        return this;
    }

    public StartCloudVodParams setDevBrand(String str) {
        this.devBrand = str;
        return this;
    }

    public StartCloudVodParams setEncrypt(boolean z) {
        this.encrypt = z;
        return this;
    }

    public StartCloudVodParams setFileName(String str) {
        this.fileName = str;
        return this;
    }

    public StartCloudVodParams setFirstSliceGetTs(int i) {
        this.firstSliceGetTs = i;
        return this;
    }

    public StartCloudVodParams setFirstSliceSize(int i) {
        this.firstSliceSize = i;
        return this;
    }

    public StartCloudVodParams setHeight(int i) {
        this.height = i;
        return this;
    }

    public StartCloudVodParams setIframeSize(int i) {
        this.iframeSize = i;
        return this;
    }

    public StartCloudVodParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public StartCloudVodParams setM3u8GetTs(int i) {
        this.m3u8GetTs = i;
        return this;
    }

    public StartCloudVodParams setM3u8Size(int i) {
        this.m3u8Size = i;
        return this;
    }

    public StartCloudVodParams setNetwork(String str) {
        this.network = str;
        return this;
    }

    public StartCloudVodParams setPlatform(String str) {
        this.platform = str;
        return this;
    }

    public StartCloudVodParams setRenderTs(long j) {
        this.renderTs = j;
        return this;
    }

    public StartCloudVodParams setReqTs(long j) {
        this.reqTs = j;
        return this;
    }

    public StartCloudVodParams setRespTs(long j) {
        this.respTs = j;
        return this;
    }

    public StartCloudVodParams setSdkVersion(String str) {
        this.sdkVersion = str;
        return this;
    }

    public StartCloudVodParams setSeek(long j) {
        this.seek = j;
        return this;
    }

    public StartCloudVodParams setSysModel(String str) {
        this.sysModel = str;
        return this;
    }

    public StartCloudVodParams setSysVersion(String str) {
        this.sysVersion = str;
        return this;
    }

    public StartCloudVodParams setVideoCodec(String str) {
        this.videoCodec = str;
        return this;
    }

    public StartCloudVodParams setWidth(int i) {
        this.width = i;
        return this;
    }
}
