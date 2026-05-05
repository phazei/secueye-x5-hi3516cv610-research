package a.a.a.a.b.m;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: ThreadPool.java */
/* JADX INFO: loaded from: classes.dex */
public class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f1504a = new c();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static volatile l f1505b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public d f1506c = new d(2);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public d f1507d = new d(2);
    public final Executor e = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue(), new i("thread-pool", 10));

    /* JADX INFO: compiled from: ThreadPool.java */
    public interface a<T> {
        T a(b bVar);
    }

    /* JADX INFO: compiled from: ThreadPool.java */
    public interface b {
    }

    /* JADX INFO: compiled from: ThreadPool.java */
    private static class c implements b {
        public c() {
        }
    }

    /* JADX INFO: compiled from: ThreadPool.java */
    private static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f1508a;

        public d(int i) {
            this.f1508a = i;
        }
    }

    public static l a() {
        if (f1505b == null) {
            synchronized (l.class) {
                if (f1505b == null) {
                    f1505b = new l();
                }
            }
        }
        return f1505b;
    }

    /* JADX INFO: compiled from: ThreadPool.java */
    private class e<T> implements Runnable, a.a.a.a.b.m.d<T>, b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public a<T> f1509a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public a.a.a.a.b.m.e<T> f1510b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public d f1511c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public volatile boolean f1512d;
        public boolean e;
        public T f;
        public int g;

        public e(a<T> aVar, a.a.a.a.b.m.e<T> eVar) {
            this.f1509a = aVar;
            this.f1510b = eVar;
        }

        public final d a(int i) {
            if (i == 1) {
                return l.this.f1506c;
            }
            if (i == 2) {
                return l.this.f1507d;
            }
            return null;
        }

        public boolean b(int i) {
            d dVarA = a(this.g);
            if (dVarA != null) {
                b(dVarA);
            }
            this.g = 0;
            d dVarA2 = a(i);
            if (dVarA2 == null) {
                return true;
            }
            if (!a(dVarA2)) {
                return false;
            }
            this.g = i;
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            T tA;
            if (b(1)) {
                try {
                    tA = this.f1509a.a(this);
                } catch (Throwable th) {
                    a.a.a.a.b.m.a.d("Worker", "Exception in running a job" + th);
                    tA = null;
                }
            } else {
                tA = null;
            }
            synchronized (this) {
                b(0);
                this.f = tA;
                this.e = true;
                notifyAll();
            }
            a.a.a.a.b.m.e<T> eVar = this.f1510b;
            if (eVar != null) {
                eVar.a(this);
            }
        }

        public final boolean a(d dVar) {
            while (true) {
                synchronized (this) {
                    if (this.f1512d) {
                        this.f1511c = null;
                        return false;
                    }
                    this.f1511c = dVar;
                    synchronized (dVar) {
                        if (dVar.f1508a > 0) {
                            dVar.f1508a--;
                            synchronized (this) {
                                this.f1511c = null;
                            }
                            return true;
                        }
                        try {
                            dVar.wait();
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            }
        }

        public final void b(d dVar) {
            synchronized (dVar) {
                dVar.f1508a++;
                dVar.notifyAll();
            }
        }
    }

    public <T> a.a.a.a.b.m.d<T> a(a<T> aVar, a.a.a.a.b.m.e<T> eVar) {
        e eVar2 = new e(aVar, eVar);
        this.e.execute(eVar2);
        return eVar2;
    }

    public <T> a.a.a.a.b.m.d<T> a(a<T> aVar) {
        return a(aVar, null);
    }
}
