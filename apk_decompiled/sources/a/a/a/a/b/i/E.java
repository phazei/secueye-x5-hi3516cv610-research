package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class E implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ J f1347a;

    public E(J j) {
        this.f1347a = j;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        a.a.a.a.b.m.a.c(this.f1347a.f1354a, "Advertising network data, begin scan");
        J j = this.f1347a;
        j.a(j.f1355b);
        this.f1347a.v.postDelayed(new D(this), 1500L);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1347a.onProvisionFailed(-1, "failed to advertise network data");
    }
}
