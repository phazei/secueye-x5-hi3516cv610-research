package com.aliyun.alink.linksdk.tools.log;

/* JADX INFO: loaded from: classes2.dex */
public class Request<T> {
    public String id;
    public String method;
    public T params;
    public String version;

    private Request(Builder<T> builder) {
        this.id = null;
        this.version = null;
        this.method = null;
        this.params = null;
        this.id = String.valueOf(IDGenerater.generateId());
        this.version = ((Builder) builder).f4452a;
        this.method = ((Builder) builder).f4453b;
        this.params = (T) ((Builder) builder).f4454c;
    }

    public static final class Builder<K> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f4452a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f4453b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private K f4454c;

        public Builder version(String str) {
            this.f4452a = str;
            return this;
        }

        public Builder method(String str) {
            this.f4453b = str;
            return this;
        }

        public Builder params(K k) {
            this.f4454c = k;
            return this;
        }

        public Request<K> build() {
            return new Request<>(this);
        }
    }

    public String toString() {
        return "Request{id='" + this.id + "', version='" + this.version + "', method='" + this.method + "', params=" + this.params + '}';
    }
}
