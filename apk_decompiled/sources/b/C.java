package b;

import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import java.util.Iterator;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class C implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K f2093a;

    public C(K k) {
        this.f2093a = k;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2093a.b();
        Iterator it = this.f2093a.z.iterator();
        while (it.hasNext()) {
            this.f2093a.a((ExtendedBluetoothDevice) it.next());
        }
        this.f2093a.z.clear();
    }
}
