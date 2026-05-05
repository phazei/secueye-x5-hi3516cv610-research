package a.a.a.a.a;

import androidx.annotation.RequiresApi;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ g f1108a;

    public a(g gVar) {
        this.f1108a = gVar;
    }

    @Override // java.lang.Runnable
    @RequiresApi(api = 21)
    public void run() {
        a.a.a.a.b.m.a.c("AdvertiseManager", "------timeout");
        this.f1108a.d();
    }
}
