package a.a.a.a.a;

import androidx.annotation.RequiresApi;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ byte[] f1173a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BleAdvertiseCallback f1174b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ g f1175c;

    public b(g gVar, byte[] bArr, BleAdvertiseCallback bleAdvertiseCallback) {
        this.f1175c = gVar;
        this.f1173a = bArr;
        this.f1174b = bleAdvertiseCallback;
    }

    @Override // java.lang.Runnable
    @RequiresApi(api = 21)
    public void run() {
        this.f1175c.h.b();
        this.f1175c.a(this.f1173a, (BleAdvertiseCallback<Boolean>) this.f1174b);
    }
}
