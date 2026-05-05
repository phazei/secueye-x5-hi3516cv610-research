package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

/* JADX INFO: loaded from: classes2.dex */
public class TransQualityStatisticEvent extends BaseEvent {
    private TransQualityStatisticParams params;

    public static final class Builder {
        private int code;
        private String message;
        private TransQualityStatisticParams params;

        private Builder() {
        }

        public TransQualityStatisticEvent build() {
            return new TransQualityStatisticEvent(this);
        }

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder params(TransQualityStatisticParams transQualityStatisticParams) {
            this.params = transQualityStatisticParams;
            return this;
        }
    }

    private TransQualityStatisticEvent(Builder builder) {
        super("1.2", "transQualityStatistic");
        setCode(builder.code);
        setMessage(builder.message);
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public TransQualityStatisticParams getParams() {
        return this.params;
    }

    public TransQualityStatisticEvent setParams(TransQualityStatisticParams transQualityStatisticParams) {
        this.params = transQualityStatisticParams;
        return this;
    }
}
