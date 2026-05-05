package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import android.os.Build;
import com.alibaba.fastjson.JSON;
import com.aliyun.iotx.linkvisual.media.Version;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorParams extends BaseParams {
    private String devBrand;
    private String iotId;
    private String module;
    private String network;
    private String platform;
    private String sdkVersion;
    private String sysModel;
    private String sysVersion;
    private int timeConsumed;

    public static final class Builder {
        private String iotId;
        private String module;
        private String network;
        private String sessionId;
        private int timeConsumed;

        private Builder() {
        }

        public ErrorParams build() {
            return new ErrorParams(this);
        }

        public Builder iotId(String str) {
            this.iotId = str;
            return this;
        }

        public Builder module(String str) {
            this.module = str;
            return this;
        }

        public Builder network(String str) {
            this.network = str;
            return this;
        }

        public Builder sessionId(String str) {
            this.sessionId = str;
            return this;
        }

        public Builder timeConsumed(int i) {
            this.timeConsumed = i;
            return this;
        }
    }

    public ErrorParams() {
        setSdkVersion(Version.SDK_VERSION);
        setPlatform("Android");
        setDevBrand(Build.MANUFACTURER);
        setSysModel(Build.MODEL);
        setSysVersion(Build.VERSION.SDK_INT + "");
    }

    private ErrorParams(Builder builder) {
        this();
        setSessionId(builder.sessionId);
        setModule(builder.module);
        setIotId(builder.iotId);
        setNetwork(builder.network);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static ErrorParams parseFromJSONString(String str) {
        return (ErrorParams) JSON.parseObject(str, ErrorParams.class);
    }

    public String getDevBrand() {
        return this.devBrand;
    }

    public String getIotId() {
        return this.iotId;
    }

    public String getModule() {
        return this.module;
    }

    public String getNetwork() {
        return this.network;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public String getSysModel() {
        return this.sysModel;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public int getTimeConsumed() {
        return this.timeConsumed;
    }

    public ErrorParams setDevBrand(String str) {
        this.devBrand = str;
        return this;
    }

    public ErrorParams setIotId(String str) {
        this.iotId = str;
        return this;
    }

    public ErrorParams setModule(String str) {
        this.module = str;
        return this;
    }

    public ErrorParams setNetwork(String str) {
        this.network = str;
        return this;
    }

    public ErrorParams setPlatform(String str) {
        this.platform = str;
        return this;
    }

    public ErrorParams setSdkVersion(String str) {
        this.sdkVersion = str;
        return this;
    }

    public ErrorParams setSysModel(String str) {
        this.sysModel = str;
        return this;
    }

    public ErrorParams setSysVersion(String str) {
        this.sysVersion = str;
        return this;
    }

    public ErrorParams setTimeConsumed(int i) {
        this.timeConsumed = i;
        return this;
    }
}
