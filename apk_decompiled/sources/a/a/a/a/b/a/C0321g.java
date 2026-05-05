package a.a.a.a.b.a;

import b.C0378l;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/* JADX INFO: renamed from: a.a.a.a.b.a.g, reason: case insensitive filesystem */
/* JADX INFO: compiled from: RequestQueue.java */
/* JADX INFO: loaded from: classes.dex */
public class C0321g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f1259a = "" + C0321g.class.getSimpleName();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public K f1262d;
    public C0318d e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Set<SIGMeshBizRequest> f1260b = new HashSet();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Deque<SIGMeshBizRequest> f1261c = new LinkedList();
    public long f = 0;

    public C0321g(C0378l c0378l) {
        this.e = new C0318d(c0378l);
        this.f1262d = new K(c0378l, this.f1261c);
    }

    public void d() {
        this.f1262d.d();
    }

    public List<SIGMeshBizRequest> b() {
        return (List) this.f1261c;
    }

    public void c() {
        this.f1262d.c();
    }

    public void a(List<SIGMeshBizRequest> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        synchronized (this.f1260b) {
            this.f1260b.addAll(list);
        }
        b(list);
    }

    public final void b(List<SIGMeshBizRequest> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.get(0).e) {
            this.f1261c.addAll(list);
            if (this.f1262d.a()) {
                return;
            }
            a.a.a.a.b.m.a.c(f1259a, "Start dispatcher, delay: " + this.f + ", pending request size: " + this.f1261c.size());
            new Thread(new RunnableC0319e(this)).start();
            return;
        }
        a.a.a.a.b.m.l.a().a(new C0320f(this, list));
    }

    public C0321g(C0378l c0378l, String str) {
        f1259a += str;
        this.e = new C0318d(c0378l);
        this.f1262d = new K(c0378l, this.f1261c, str);
    }

    public void a(boolean z) {
        a.a.a.a.b.m.a.c(f1259a, "Cancel RequestQueue, all? " + z);
        if (z) {
            synchronized (this.f1261c) {
                this.f1261c.clear();
            }
        }
    }

    public void a(long j) {
        this.f = j;
    }

    public void a(int i) {
        this.f1262d.b((int) (400.0d / ((double) i)));
    }
}
