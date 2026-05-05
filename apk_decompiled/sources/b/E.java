package b;

import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class E implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ExtendedBluetoothDevice f2096a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ K f2097b;

    public E(K k, ExtendedBluetoothDevice extendedBluetoothDevice) {
        this.f2097b = k;
        this.f2096a = extendedBluetoothDevice;
    }

    @Override // java.lang.Runnable
    public void run() {
        BleMeshManager bleMeshManagerA = this.f2097b.a(this.f2096a, false);
        if (bleMeshManagerA != null && bleMeshManagerA.isConnected()) {
            a.a.a.a.b.m.a.d(K.f2104a, String.format("Status error, timeout occurred in the connection(%s) state", this.f2096a.getAddress()));
            return;
        }
        if (bleMeshManagerA != null) {
            bleMeshManagerA.close();
        }
        this.f2097b.c(this.f2096a.getDevice());
        this.f2097b.y.remove(this.f2096a.getAddress());
    }
}
