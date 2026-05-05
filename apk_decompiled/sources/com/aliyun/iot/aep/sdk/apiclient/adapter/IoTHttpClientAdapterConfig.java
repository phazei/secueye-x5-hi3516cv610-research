package com.aliyun.iot.aep.sdk.apiclient.adapter;

import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import javax.net.SocketFactory;
import okhttp3.EventListener;

/* JADX INFO: loaded from: classes2.dex */
public class IoTHttpClientAdapterConfig {
    public static final long DEFAULT_TIMEOUT = 10000;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f4552a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Env f4553b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f4554c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f4555d;
    public long e;
    public long f;
    public long g;
    public boolean h = false;
    public SocketFactory i;
    public EventListener j;
    public boolean k;

    public static class Builder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f4556a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public Env f4557b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f4558c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public String f4559d;
        public long e;
        public long f;
        public long g;
        public SocketFactory h;
        public EventListener i;
        public boolean j = true;
        public boolean k = false;

        public IoTHttpClientAdapterConfig build() {
            IoTHttpClientAdapterConfig ioTHttpClientAdapterConfig = new IoTHttpClientAdapterConfig();
            ioTHttpClientAdapterConfig.f4553b = this.f4557b;
            ioTHttpClientAdapterConfig.f4552a = this.f4556a;
            ioTHttpClientAdapterConfig.f4554c = this.f4558c;
            ioTHttpClientAdapterConfig.f4555d = this.f4559d;
            if (this.e <= 0) {
                this.e = 10000L;
            }
            if (this.f <= 0) {
                this.f = 10000L;
            }
            if (this.g <= 0) {
                this.g = 10000L;
            }
            ioTHttpClientAdapterConfig.e = this.e;
            ioTHttpClientAdapterConfig.f = this.f;
            ioTHttpClientAdapterConfig.g = this.g;
            if (this.h == null) {
                this.h = SocketFactory.getDefault();
            }
            ioTHttpClientAdapterConfig.i = this.h;
            ioTHttpClientAdapterConfig.j = this.i;
            ioTHttpClientAdapterConfig.k = this.j;
            ioTHttpClientAdapterConfig.h = this.k;
            return ioTHttpClientAdapterConfig;
        }

        public Builder setApiEnv(Env env) {
            this.f4557b = env;
            return this;
        }

        public Builder setAppKey(String str) {
            this.f4558c = str;
            return this;
        }

        public Builder setAuthCode(String str) {
            this.f4559d = str;
            return this;
        }

        public Builder setConnectTimeout(long j) {
            if (j <= 0) {
                j = 10000;
            }
            this.e = j;
            return this;
        }

        public Builder setDebug(boolean z) {
            this.k = z;
            return this;
        }

        public Builder setDefaultHost(String str) {
            this.f4556a = str;
            return this;
        }

        public Builder setEventListener(EventListener eventListener) {
            this.i = eventListener;
            return this;
        }

        public Builder setHttpConnectionRetry(boolean z) {
            this.j = z;
            return this;
        }

        public Builder setReadTimeout(long j) {
            if (j <= 0) {
                j = 10000;
            }
            this.f = j;
            return this;
        }

        public Builder setSocketFactory(SocketFactory socketFactory) {
            this.h = socketFactory;
            return this;
        }

        public Builder setWriteTimeout(long j) {
            if (j <= 0) {
                j = 10000;
            }
            this.g = j;
            return this;
        }
    }

    public Env getApiEnv() {
        return this.f4553b;
    }

    public String getAppKey() {
        return this.f4554c;
    }

    public String getAuthCode() {
        return this.f4555d;
    }

    public long getConnectTimeout() {
        return this.e;
    }

    public String getDefaultHost() {
        return this.f4552a;
    }

    public EventListener getEventListener() {
        return this.j;
    }

    public long getReadTimeout() {
        return this.f;
    }

    public SocketFactory getSocketFactory() {
        return this.i;
    }

    public long getWriteTimeout() {
        return this.g;
    }

    public boolean isDebug() {
        return this.h;
    }

    public boolean isHttpConnectionRetry() {
        return this.k;
    }

    public void setDefaultHost(String str) {
        this.f4552a = str;
    }
}
