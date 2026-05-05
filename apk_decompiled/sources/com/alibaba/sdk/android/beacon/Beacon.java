package com.alibaba.sdk.android.beacon;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class Beacon {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f2842a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final HandlerThread f0a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final c f1a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final List<OnUpdateListener> f2a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List<OnServiceErrListener> f2843b;
    private final boolean isStartPoll;
    private final String mAppKey;
    private final String mAppSecret;
    private final Map<String, String> mExtras;
    private Handler mHandler;
    private final long mLoopInterval;

    private final class BeaconHandler extends Handler {
        static final int MSG_ADD_ERR_LISTENER = 6;
        static final int MSG_ADD_UPDATE_LISTENER = 4;
        static final int MSG_ERR_CALLBACK = 7;
        static final int MSG_REMOVE_UPDATE_LISTENER = 5;
        static final int MSG_START = 0;
        static final int MSG_START_POLLING = 2;
        static final int MSG_STOP_POLLING = 3;
        static final int MSG_UPDATE = 1;

        BeaconHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                switch (message.what) {
                    case 0:
                        Beacon.this.c((Context) message.obj);
                        break;
                    case 1:
                        Beacon.this.d((Context) message.obj);
                        break;
                    case 2:
                        Beacon.this.e((Context) message.obj);
                        break;
                    case 3:
                        Beacon.this.b();
                        break;
                    case 4:
                        Beacon.this.a((OnUpdateListener) message.obj);
                        break;
                    case 5:
                        Beacon.this.b((OnUpdateListener) message.obj);
                        break;
                    case 6:
                        Beacon.this.a((OnServiceErrListener) message.obj);
                        break;
                    case 7:
                        Beacon.this.b((Error) message.obj);
                        break;
                }
            } catch (Exception e) {
                b.a("beacon", e.getMessage(), e);
            }
        }
    }

    public static final class Builder {
        String mAppKey;
        String mAppSecret;
        Map<String, String> mExtras = new HashMap();
        long mLoopInterval = 300000;
        boolean isStartPoll = false;
        boolean isLogEnabled = true;

        public Builder appKey(String str) {
            this.mAppKey = str.trim();
            return this;
        }

        public Builder appSecret(String str) {
            this.mAppSecret = str.trim();
            return this;
        }

        public Beacon build() {
            return new Beacon(this);
        }

        public Builder extras(Map<String, String> map) {
            this.mExtras.putAll(map);
            return this;
        }

        public Builder logEnabled(boolean z) {
            this.isLogEnabled = z;
            return this;
        }

        public Builder loopInterval(long j) {
            if (j < 60000) {
                this.mLoopInterval = 60000L;
            } else {
                this.mLoopInterval = j;
            }
            return this;
        }

        public Builder startPoll(boolean z) {
            this.isStartPoll = z;
            return this;
        }
    }

    public static final class Config {
        public final String key;
        public final String value;

        public Config(String str, String str2) {
            this.key = str;
            this.value = str2;
        }
    }

    public static final class Error {
        public final String errCode;
        public final String errMsg;

        Error(String str, String str2) {
            this.errCode = str;
            this.errMsg = str2;
        }
    }

    public interface OnServiceErrListener {
        void onErr(Error error);
    }

    public interface OnUpdateListener {
        void onUpdate(List<Config> list);
    }

    private Beacon(Builder builder) {
        this.f2a = new ArrayList();
        this.f2843b = new ArrayList();
        this.f2842a = 255;
        this.mAppKey = builder.mAppKey;
        this.mAppSecret = builder.mAppSecret;
        this.mExtras = builder.mExtras;
        this.mLoopInterval = builder.mLoopInterval;
        this.isStartPoll = builder.isStartPoll;
        b.setLogEnabled(builder.isLogEnabled);
        this.f1a = new c(this);
        this.f0a = new HandlerThread("Beacon Daemon");
        this.f0a.start();
        a();
    }

    private void a() {
        this.mHandler = new BeaconHandler(this.f0a.getLooper());
    }

    private void a(Context context) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        messageObtain.obj = context;
        this.mHandler.sendMessage(messageObtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(OnServiceErrListener onServiceErrListener) {
        this.f2843b.add(onServiceErrListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(OnUpdateListener onUpdateListener) {
        this.f2a.add(onUpdateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (Build.VERSION.SDK_INT >= 18) {
            this.mHandler.getLooper().quitSafely();
        } else {
            this.mHandler.getLooper().quit();
        }
        a();
    }

    private void b(Context context) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        messageObtain.obj = context;
        this.mHandler.sendMessage(messageObtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Error error) {
        Iterator<OnServiceErrListener> it = this.f2843b.iterator();
        while (it.hasNext()) {
            it.next().onErr(error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(OnUpdateListener onUpdateListener) {
        this.f2a.remove(onUpdateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Context context) {
        if (this.isStartPoll) {
            b(context);
            this.f2842a = 1;
        } else {
            this.f2842a = 1;
            a(context);
            stop();
            this.f2842a = 255;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Context context) {
        this.f1a.m12a(context, this.mAppKey, this.mAppSecret, this.mExtras);
        List<Config> listA = this.f1a.a();
        Iterator<OnUpdateListener> it = this.f2a.iterator();
        while (it.hasNext()) {
            it.next().onUpdate(listA);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Context context) {
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        a(context);
        this.mHandler.sendEmptyMessageDelayed(2, this.mLoopInterval);
    }

    private boolean isStarted() {
        return this.f2842a == 1;
    }

    public static void setPrepare(boolean z) {
        a.f2844a = z;
    }

    void a(Error error) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 7;
        messageObtain.obj = error;
        this.mHandler.sendMessage(messageObtain);
    }

    public void addServiceErrListener(OnServiceErrListener onServiceErrListener) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 6;
        messageObtain.obj = onServiceErrListener;
        this.mHandler.sendMessage(messageObtain);
    }

    public void addUpdateListener(OnUpdateListener onUpdateListener) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 4;
        messageObtain.obj = onUpdateListener;
        this.mHandler.sendMessage(messageObtain);
    }

    public List<Config> getConfigs() {
        return this.f1a.a();
    }

    public void setLogEnabled(boolean z) {
        b.setLogEnabled(z);
    }

    public void start(Context context) {
        if (isStarted()) {
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = 0;
        messageObtain.obj = context;
        this.mHandler.sendMessage(messageObtain);
    }

    public void stop() {
        if (isStarted()) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 3;
            this.mHandler.sendMessage(messageObtain);
        }
    }
}
