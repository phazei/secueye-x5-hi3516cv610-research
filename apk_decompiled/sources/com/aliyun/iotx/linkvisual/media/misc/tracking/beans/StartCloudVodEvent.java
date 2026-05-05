package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

/* JADX INFO: loaded from: classes2.dex */
public class StartCloudVodEvent extends BaseEvent {
    private StartCloudVodParams params;

    public static final class Builder {
        private int code;
        private String message;
        private StartCloudVodParams params;

        private Builder() {
        }

        public StartCloudVodEvent build() {
            return new StartCloudVodEvent(this);
        }

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder params(StartCloudVodParams startCloudVodParams) {
            this.params = startCloudVodParams;
            return this;
        }
    }

    private StartCloudVodEvent(Builder builder) {
        super("1.2", "startCloudVod");
        setCode(builder.code);
        setMessage(builder.message);
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public StartCloudVodParams getParams() {
        return this.params;
    }

    public StartCloudVodEvent setParams(StartCloudVodParams startCloudVodParams) {
        this.params = startCloudVodParams;
        return this;
    }
}
