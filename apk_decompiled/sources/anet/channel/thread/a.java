package anet.channel.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class a extends ThreadPoolExecutor {
    public a(int i, int i2, long j, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        super(i, i2, j, timeUnit, blockingQueue, threadFactory);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return new C0174a(runnable, t);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new C0174a(callable);
    }

    /* JADX INFO: renamed from: anet.channel.thread.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    class C0174a<V> extends FutureTask<V> implements Comparable<C0174a<V>> {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Object f1934b;

        public C0174a(Callable<V> callable) {
            super(callable);
            this.f1934b = callable;
        }

        public C0174a(Runnable runnable, V v) {
            super(runnable, v);
            this.f1934b = runnable;
        }

        @Override // java.lang.Comparable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(C0174a<V> c0174a) {
            if (this == c0174a) {
                return 0;
            }
            if (c0174a == null) {
                return -1;
            }
            Object obj = this.f1934b;
            if (obj != null && c0174a.f1934b != null && obj.getClass().equals(c0174a.f1934b.getClass())) {
                Object obj2 = this.f1934b;
                if (obj2 instanceof Comparable) {
                    return ((Comparable) obj2).compareTo(c0174a.f1934b);
                }
            }
            return 0;
        }
    }
}
