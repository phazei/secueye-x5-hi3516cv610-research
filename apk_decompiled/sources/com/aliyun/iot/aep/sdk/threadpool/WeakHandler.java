package com.aliyun.iot.aep.sdk.threadpool;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes2.dex */
public class WeakHandler {
    private final Handler.Callback mCallback;
    private final b mExec;
    private Lock mLock;

    @VisibleForTesting
    final a mRunnables;

    public WeakHandler() {
        this.mLock = new ReentrantLock();
        this.mRunnables = new a(this.mLock, null);
        this.mCallback = null;
        this.mExec = new b();
    }

    public WeakHandler(@Nullable Handler.Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new a(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new b((WeakReference<Handler.Callback>) new WeakReference(callback));
    }

    public WeakHandler(@NonNull Looper looper) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new a(this.mLock, null);
        this.mCallback = null;
        this.mExec = new b(looper);
    }

    public WeakHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new a(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new b(looper, new WeakReference(callback));
    }

    public final boolean post(@NonNull Runnable runnable) {
        return this.mExec.post(wrapRunnable(runnable));
    }

    public final boolean postAtTime(@NonNull Runnable runnable, long j) {
        return this.mExec.postAtTime(wrapRunnable(runnable), j);
    }

    public final boolean postAtTime(Runnable runnable, Object obj, long j) {
        return this.mExec.postAtTime(wrapRunnable(runnable), obj, j);
    }

    public final boolean postDelayed(Runnable runnable, long j) {
        return this.mExec.postDelayed(wrapRunnable(runnable), j);
    }

    public final boolean postAtFrontOfQueue(Runnable runnable) {
        return this.mExec.postAtFrontOfQueue(wrapRunnable(runnable));
    }

    public final void removeCallbacks(Runnable runnable) {
        c cVarA = this.mRunnables.a(runnable);
        if (cVarA != null) {
            this.mExec.removeCallbacks(cVarA);
        }
    }

    public final void removeCallbacks(Runnable runnable, Object obj) {
        c cVarA = this.mRunnables.a(runnable);
        if (cVarA != null) {
            this.mExec.removeCallbacks(cVarA, obj);
        }
    }

    public final boolean sendMessage(Message message) {
        return this.mExec.sendMessage(message);
    }

    public final boolean sendEmptyMessage(int i) {
        return this.mExec.sendEmptyMessage(i);
    }

    public final boolean sendEmptyMessageDelayed(int i, long j) {
        return this.mExec.sendEmptyMessageDelayed(i, j);
    }

    public final boolean sendEmptyMessageAtTime(int i, long j) {
        return this.mExec.sendEmptyMessageAtTime(i, j);
    }

    public final boolean sendMessageDelayed(Message message, long j) {
        return this.mExec.sendMessageDelayed(message, j);
    }

    public boolean sendMessageAtTime(Message message, long j) {
        return this.mExec.sendMessageAtTime(message, j);
    }

    public final boolean sendMessageAtFrontOfQueue(Message message) {
        return this.mExec.sendMessageAtFrontOfQueue(message);
    }

    public final void removeMessages(int i) {
        this.mExec.removeMessages(i);
    }

    public final void removeMessages(int i, Object obj) {
        this.mExec.removeMessages(i, obj);
    }

    public final void removeCallbacksAndMessages(Object obj) {
        this.mExec.removeCallbacksAndMessages(obj);
    }

    public final boolean hasMessages(int i) {
        return this.mExec.hasMessages(i);
    }

    public final boolean hasMessages(int i, Object obj) {
        return this.mExec.hasMessages(i, obj);
    }

    public final Looper getLooper() {
        return this.mExec.getLooper();
    }

    private c wrapRunnable(@NonNull Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        a aVar = new a(this.mLock, runnable);
        this.mRunnables.a(aVar);
        return aVar.f4900d;
    }

    static class b extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final WeakReference<Handler.Callback> f4901a;

        b() {
            this.f4901a = null;
        }

        b(WeakReference<Handler.Callback> weakReference) {
            this.f4901a = weakReference;
        }

        b(Looper looper) {
            super(looper);
            this.f4901a = null;
        }

        b(Looper looper, WeakReference<Handler.Callback> weakReference) {
            super(looper);
            this.f4901a = weakReference;
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            Handler.Callback callback;
            WeakReference<Handler.Callback> weakReference = this.f4901a;
            if (weakReference == null || (callback = weakReference.get()) == null) {
                return;
            }
            callback.handleMessage(message);
        }
    }

    static class c implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final WeakReference<Runnable> f4902a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final WeakReference<a> f4903b;

        c(WeakReference<Runnable> weakReference, WeakReference<a> weakReference2) {
            this.f4902a = weakReference;
            this.f4903b = weakReference2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Runnable runnable = this.f4902a.get();
            a aVar = this.f4903b.get();
            if (aVar != null) {
                aVar.a();
            }
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @Nullable
        a f4897a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @Nullable
        a f4898b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @NonNull
        final Runnable f4899c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @NonNull
        final c f4900d;

        @NonNull
        Lock e;

        public a(@NonNull Lock lock, @NonNull Runnable runnable) {
            this.f4899c = runnable;
            this.e = lock;
            this.f4900d = new c(new WeakReference(runnable), new WeakReference(this));
        }

        public c a() {
            this.e.lock();
            try {
                if (this.f4898b != null) {
                    this.f4898b.f4897a = this.f4897a;
                }
                if (this.f4897a != null) {
                    this.f4897a.f4898b = this.f4898b;
                }
                this.f4898b = null;
                this.f4897a = null;
                this.e.unlock();
                return this.f4900d;
            } catch (Throwable th) {
                this.e.unlock();
                throw th;
            }
        }

        public void a(@NonNull a aVar) {
            this.e.lock();
            try {
                if (this.f4897a != null) {
                    this.f4897a.f4898b = aVar;
                }
                aVar.f4897a = this.f4897a;
                this.f4897a = aVar;
                aVar.f4898b = this;
            } finally {
                this.e.unlock();
            }
        }

        @Nullable
        public c a(Runnable runnable) {
            this.e.lock();
            try {
                for (a aVar = this.f4897a; aVar != null; aVar = aVar.f4897a) {
                    if (aVar.f4899c == runnable) {
                        return aVar.a();
                    }
                }
                this.e.unlock();
                return null;
            } finally {
                this.e.unlock();
            }
        }
    }
}
