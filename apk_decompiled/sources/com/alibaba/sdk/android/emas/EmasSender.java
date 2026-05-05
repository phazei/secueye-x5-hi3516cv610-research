package com.alibaba.sdk.android.emas;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.alibaba.sdk.android.emas.i;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/* JADX INFO: loaded from: classes.dex */
public class EmasSender {
    private static final int BUILD_REQ_DATA_DONE = 1;
    private static final String TAG = "EmasSender";
    private static Handler sHandler;
    private boolean mBackground;
    private c mCacheManager;
    private e mDiskCacheManager;
    private final ExecutorService mPackDataExecutor;
    private final j mSendManager;
    private final int mSingleLogLimitCapacity;

    private EmasSender(Builder builder) {
        this.mBackground = false;
        this.mPackDataExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.alibaba.sdk.android.emas.EmasSender.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "EmasSenderPackDataThread");
            }
        });
        this.mSingleLogLimitCapacity = builder.singleLogLimitCapacity;
        if (builder.diskCache) {
            this.mDiskCacheManager = new e(builder.context, builder.host, builder.appKey, builder.businessKey);
            this.mDiskCacheManager.a(builder.diskCacheLimitCount, builder.diskCacheLimitCapacity, builder.diskCacheLimitDay);
        }
        this.mSendManager = new j(this, this.mDiskCacheManager);
        this.mSendManager.init(builder.context, builder.appId, builder.appKey, builder.appVersion, builder.channel, builder.userNick);
        this.mSendManager.setHost(builder.host);
        this.mSendManager.a(builder.appSecret);
        this.mSendManager.openHttp(builder.openHttp);
        this.mSendManager.a(builder.sendDiskCacheBackground);
        this.mSendManager.a(builder.maxSendDiskCacheQueueCount);
        this.mSendManager.a(builder.preSendHandler);
        this.mSendManager.e();
        if (builder.cache && builder.cacheLimitCount > 1) {
            this.mCacheManager = new c(this.mSendManager, builder.cacheLimitCount, builder.cacheLimitCapacity);
            i iVar = new i();
            iVar.a(new i.a() { // from class: com.alibaba.sdk.android.emas.EmasSender.2
                @Override // com.alibaba.sdk.android.emas.i.a
                public void c() {
                    EmasSender.this.mBackground = false;
                }

                @Override // com.alibaba.sdk.android.emas.i.a
                public void d() {
                    EmasSender.this.mBackground = true;
                    EmasSender.this.mCacheManager.flush();
                }
            });
            builder.context.registerActivityLifecycleCallbacks(iVar);
        }
        sHandler = new a(Looper.getMainLooper(), this);
    }

    public void changeHost(String str) {
        this.mSendManager.setHost(str);
    }

    public void openHttp(boolean z) {
        this.mSendManager.openHttp(z);
    }

    public void setUserNick(String str) {
        this.mSendManager.setUserNick(str);
    }

    public void send(long j, String str, int i, String str2, String str3, String str4, Map<String, String> map) {
        if (TextUtils.isEmpty(this.mSendManager.m22a().getAppKey()) || TextUtils.isEmpty(this.mSendManager.m22a().getChangeHost())) {
            LogUtil.d("EmasSender send failed. appkey or host is empty.");
        } else {
            this.mPackDataExecutor.submit(new b(j, str, i, str2, str3, str4, map));
        }
    }

    public void flush() {
        c cVar = this.mCacheManager;
        if (cVar != null) {
            cVar.flush();
        }
    }

    boolean isBackground() {
        return this.mBackground;
    }

    private class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with other field name */
        private final Map<String, String> f5a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final long f2874b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f2875d;

        /* JADX INFO: renamed from: d, reason: collision with other field name */
        private final String f6d;
        private final String e;
        private final String f;
        private final String g;

        public b(long j, String str, int i, String str2, String str3, String str4, Map<String, String> map) {
            this.f2874b = j;
            this.f6d = str;
            this.f2875d = i;
            this.e = str2;
            this.f = str3;
            this.g = str4;
            this.f5a = map;
        }

        @Override // java.lang.Runnable
        public void run() {
            String strA = com.alibaba.sdk.android.tbrest.rest.e.a(EmasSender.this.mSendManager.m22a(), EmasSender.this.mSendManager.m22a().getAppKey(), this.f2874b, this.f6d, this.f2875d, this.e, this.f, this.g, this.f5a);
            if (!TextUtils.isEmpty(strA)) {
                int length = strA.getBytes(Charset.forName("UTF-8")).length;
                if (length <= EmasSender.this.mSingleLogLimitCapacity) {
                    EmasSender.sHandler.obtainMessage(1, new g(String.valueOf(this.f2875d), strA, this.f2874b)).sendToTarget();
                    return;
                } else {
                    LogUtil.d("EmasSender send failed. build data is exceed limit. current length: " + length);
                    return;
                }
            }
            LogUtil.d("EmasSender send failed. build data is null.");
        }
    }

    private static class a extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final WeakReference<EmasSender> f2872a;

        public a(Looper looper, EmasSender emasSender) {
            super(looper);
            this.f2872a = new WeakReference<>(emasSender);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                try {
                    g gVar = (g) message.obj;
                    if (gVar == null) {
                        LogUtil.d("EmasSender EmasHandler singleLog is null");
                        return;
                    }
                    EmasSender emasSender = this.f2872a.get();
                    if (emasSender != null) {
                        if (emasSender.mCacheManager != null) {
                            emasSender.mCacheManager.add(gVar);
                            return;
                        } else {
                            emasSender.mSendManager.b(gVar);
                            return;
                        }
                    }
                    LogUtil.d("EmasSender EmasHandler weakRef sender get null");
                    return;
                } catch (Exception unused) {
                    LogUtil.e("EmasSender EmasHandler error:");
                    return;
                }
            }
            LogUtil.e("EmasSender unknown msg");
        }
    }

    public static final class Builder {
        private String appId;
        private String appKey;
        private String appSecret;
        private String appVersion;
        private String channel;
        private Application context;
        private String host;
        private boolean openHttp;
        private PreSendHandler preSendHandler;
        private String userNick;
        private String businessKey = "common";
        private boolean cache = true;
        private int cacheLimitCount = 20;
        private int singleLogLimitCapacity = 204800;
        private int cacheLimitCapacity = 2097152;
        private boolean diskCache = true;
        private int diskCacheLimitCount = 50;
        private int diskCacheLimitCapacity = 104857600;
        private int diskCacheLimitDay = 5;
        private boolean sendDiskCacheBackground = false;
        private int maxSendDiskCacheQueueCount = 0;

        public Builder context(Application application) {
            this.context = application;
            return this;
        }

        public Builder host(String str) {
            this.host = str;
            return this;
        }

        public Builder appKey(String str) {
            this.appKey = str;
            return this;
        }

        public Builder appId(String str) {
            this.appId = str;
            return this;
        }

        public Builder appSecret(String str) {
            this.appSecret = str;
            return this;
        }

        public Builder appVersion(String str) {
            this.appVersion = str;
            return this;
        }

        public Builder channel(String str) {
            this.channel = str;
            return this;
        }

        public Builder openHttp(boolean z) {
            this.openHttp = z;
            return this;
        }

        public Builder userNick(String str) {
            this.userNick = str;
            return this;
        }

        public Builder cache(boolean z) {
            this.cache = z;
            return this;
        }

        public Builder cacheLimitCount(int i) {
            this.cacheLimitCount = i;
            return this;
        }

        public Builder diskCache(boolean z) {
            this.diskCache = z;
            return this;
        }

        public Builder diskCacheLimitCount(int i) {
            this.diskCacheLimitCount = i;
            return this;
        }

        public Builder diskCacheLimitDay(int i) {
            this.diskCacheLimitDay = i;
            return this;
        }

        public Builder preSendHandler(PreSendHandler preSendHandler) {
            this.preSendHandler = preSendHandler;
            return this;
        }

        public Builder businessKey(String str) {
            this.businessKey = str;
            return this;
        }

        public Builder setSendDiskCacheBackground(boolean z) {
            this.sendDiskCacheBackground = z;
            return this;
        }

        public Builder setMaxSendDiskCacheQueueCount(int i) {
            this.maxSendDiskCacheQueueCount = i;
            return this;
        }

        public EmasSender build() {
            return new EmasSender(this);
        }
    }
}
