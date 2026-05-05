package b;

import b.K;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class D implements K.a.InterfaceC0175a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ExtendedBluetoothDevice f2094a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ K f2095b;

    public D(K k, ExtendedBluetoothDevice extendedBluetoothDevice) {
        this.f2095b = k;
        this.f2094a = extendedBluetoothDevice;
    }

    @Override // b.K.a.InterfaceC0175a
    public void a(Object obj) {
        a.a.a.a.b.m.a.a(K.f2104a, "connect to device: " + this.f2094a.getAddress() + ", all connection size: " + this.f2095b.y.size());
        this.f2095b.c((ExtendedBluetoothDevice) obj);
    }
}
