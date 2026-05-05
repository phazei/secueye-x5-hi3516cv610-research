package a.a.a.a.b.m;

import android.os.Process;

/* JADX INFO: compiled from: PriorityThreadFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class h extends Thread {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ i f1500a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(i iVar, Runnable runnable, String str) {
        super(runnable, str);
        this.f1500a = iVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Process.setThreadPriority(this.f1500a.f1501a);
        super.run();
    }
}
