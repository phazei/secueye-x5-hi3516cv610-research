package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

/* JADX INFO: loaded from: classes2.dex */
public class StartP2PStreamingEvent extends BaseEvent {
    private StartP2PStreamingParams params;

    public static final class Builder {
        private int code;
        private String message;
        private StartP2PStreamingParams params;

        private Builder() {
        }

        public StartP2PStreamingEvent build() {
            return new StartP2PStreamingEvent(this);
        }

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder params(StartP2PStreamingParams startP2PStreamingParams) {
            this.params = startP2PStreamingParams;
            return this;
        }
    }

    private StartP2PStreamingEvent(Builder builder) {
        super("1.2", "startP2PStreaming");
        setCode(builder.code);
        setMessage(builder.message);
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public StartP2PStreamingParams getParams() {
        return this.params;
    }

    public StartP2PStreamingEvent setParams(StartP2PStreamingParams startP2PStreamingParams) {
        this.params = startP2PStreamingParams;
        return this;
    }
}
