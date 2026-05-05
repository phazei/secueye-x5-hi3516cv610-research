package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import com.alibaba.fastjson.JSON;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorEvent extends BaseEvent {
    private ErrorParams params;

    public static final class Builder {
        private int code;
        private String message;
        private ErrorParams params;

        private Builder() {
        }

        public ErrorEvent build() {
            return new ErrorEvent(this);
        }

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder params(ErrorParams errorParams) {
            this.params = errorParams;
            return this;
        }
    }

    public ErrorEvent() {
        this.params = ErrorParams.newBuilder().build();
    }

    private ErrorEvent(Builder builder) {
        super("1.2", "error");
        setCode(builder.code);
        setMessage(builder.message);
        setParams(builder.params);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static ErrorEvent parseFromJSONString(String str) {
        return (ErrorEvent) JSON.parseObject(str, ErrorEvent.class);
    }

    public ErrorParams getParams() {
        return this.params;
    }

    public void setParams(ErrorParams errorParams) {
        this.params = errorParams;
    }
}
