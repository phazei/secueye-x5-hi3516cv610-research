package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

/* JADX INFO: loaded from: classes2.dex */
public class StartRelayStreamingEvent extends BaseEvent {
    private StartRelayParams params;

    public static final class Builder {
        private int code;
        private String message;
        private StartRelayParams params;

        private Builder() {
        }

        public StartRelayStreamingEvent build() {
            return new StartRelayStreamingEvent(this);
        }

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder params(StartRelayParams startRelayParams) {
            this.params = startRelayParams;
            return this;
        }
    }

    private StartRelayStreamingEvent(Builder builder) {
        super("1.2", "startRelayStreaming");
        setCode(builder.code);
        setMessage(builder.message);
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public StartRelayParams getParams() {
        return this.params;
    }

    public StartRelayStreamingEvent setParams(StartRelayParams startRelayParams) {
        this.params = startRelayParams;
        return this;
    }
}
