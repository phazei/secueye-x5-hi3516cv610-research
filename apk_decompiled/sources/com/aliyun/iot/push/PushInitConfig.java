package com.aliyun.iot.push;

/* JADX INFO: loaded from: classes2.dex */
public class PushInitConfig {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4907a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f4908b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4909c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private PushChannelType f4910d = null;
    private PushInitCallback e = null;
    private String f = null;
    private String g = null;
    private String h = null;

    public String getAuthCode() {
        return this.f4907a;
    }

    public String getDomain() {
        return this.f4908b;
    }

    public PushChannelType getPushChannelType() {
        return this.f4910d;
    }

    public PushInitCallback getPushInitCallback() {
        return this.e;
    }

    public String getServiceName() {
        return this.f;
    }

    public String getPushClientTag() {
        return this.g;
    }

    public String getDomainBindIpPort() {
        return this.h;
    }

    public String getDomainPort() {
        return this.f4909c;
    }

    public String toString() {
        return "{\"authCode\":\"" + this.f4907a + "\",\"domain\":\"" + this.f4908b + "\",\"domainPort\":\"" + this.f4909c + "\",\"serviceName\":\"" + this.f + "\",\"pushClientTag\":\"" + this.g + "\",\"domainBindIpPort\":\"" + this.h + "\",\"pushChannelType\":\"" + this.f4910d + "\",\"pushInitCallback\":\"" + this.e + "\"}";
    }

    public static final class Builder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f4911a = null;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f4912b = null;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f4913c = null;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f4914d = null;
        private String e = null;
        private String f = null;
        private PushChannelType g = null;
        private PushInitCallback h = null;

        public Builder authCode(String str) {
            this.f4911a = str;
            return this;
        }

        public Builder domain(String str) {
            this.f4912b = str;
            return this;
        }

        public Builder serviceName(String str) {
            this.f4914d = str;
            return this;
        }

        public Builder pushClientTag(String str) {
            this.e = str;
            return this;
        }

        public Builder pushChannelType(PushChannelType pushChannelType) {
            this.g = pushChannelType;
            return this;
        }

        public Builder pushInitCallback(PushInitCallback pushInitCallback) {
            this.h = pushInitCallback;
            return this;
        }

        public Builder domainBindIpPort(String str) {
            this.f = str;
            return this;
        }

        public Builder domainPort(String str) {
            this.f4913c = str;
            return this;
        }

        public PushInitConfig build() {
            PushInitConfig pushInitConfig = new PushInitConfig();
            pushInitConfig.e = this.h;
            pushInitConfig.f4910d = this.g;
            pushInitConfig.f4907a = this.f4911a;
            pushInitConfig.f4908b = this.f4912b;
            pushInitConfig.f4909c = this.f4913c;
            pushInitConfig.f = this.f4914d;
            pushInitConfig.g = this.e;
            pushInitConfig.h = this.f;
            return pushInitConfig;
        }
    }
}
