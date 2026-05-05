package a.a.a.a.b.j;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.task.bean.MeshControlDevice;

/* JADX INFO: compiled from: LowPowerMeshRunnable.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1460a = b.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public MeshService.b f1461b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public MeshControlDevice f1462c;

    public b(MeshService.b bVar, MeshControlDevice meshControlDevice) {
        this.f1461b = bVar;
        this.f1462c = meshControlDevice;
    }

    public final void a(String str) {
        a.a.a.a.b.m.a.a(this.f1460a, str);
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1461b != null) {
            a("LowPowerMeshRunnable send msg:" + this.f1462c.f());
            this.f1461b.a(this.f1462c.b(), this.f1462c.c(), this.f1462c.f(), this.f1462c.d(), this.f1462c.e(), this.f1462c.a());
        }
    }
}
