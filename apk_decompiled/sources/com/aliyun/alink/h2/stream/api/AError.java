package com.aliyun.alink.h2.stream.api;

/* JADX INFO: loaded from: classes2.dex */
public class AError {
    public int code;
    public Object extra;
    public String message;
    public int subCode;

    private AError(Builder builder) {
        this.code = 0;
        this.subCode = 0;
        this.message = null;
        this.extra = null;
        this.code = builder.code;
        this.subCode = builder.subCode;
        this.message = builder.message;
        this.extra = builder.extra;
    }

    public static final class Builder {
        private int code;
        private Object extra;
        private String message;
        private int subCode;

        public Builder code(int i) {
            this.code = i;
            return this;
        }

        public Builder subCode(int i) {
            this.subCode = i;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder extra(Object obj) {
            this.extra = obj;
            return this;
        }

        public AError build() {
            return new AError(this);
        }
    }
}
