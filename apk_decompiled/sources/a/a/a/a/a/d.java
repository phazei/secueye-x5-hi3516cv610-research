package a.a.a.a.a;

import androidx.annotation.RequiresApi;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ g f1178a;

    public d(g gVar) {
        this.f1178a = gVar;
    }

    @Override // java.lang.Runnable
    @RequiresApi(api = 21)
    public void run() {
        a.a.a.a.b.m.a.c("AdvertiseManager", "stop alternateAdvertiseTask");
        this.f1178a.h.b();
        this.f1178a.a();
    }
}
