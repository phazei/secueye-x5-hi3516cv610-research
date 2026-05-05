package a.a.a.a.b.i;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;

/* JADX INFO: renamed from: a.a.a.a.b.i.o, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionV2Worker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0349o implements IConnectCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ u f1432a;

    public C0349o(u uVar) {
        this.f1432a = uVar;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onConnected(BluetoothDevice bluetoothDevice) {
        if (this.f1432a.e != null) {
            this.f1432a.e.onConnected(bluetoothDevice);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onFailure(BluetoothDevice bluetoothDevice, int i, String str) {
        if (this.f1432a.e != null) {
            this.f1432a.e.onFailure(bluetoothDevice, i, str);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onReady(BluetoothDevice bluetoothDevice) {
        this.f1432a.e();
        if (this.f1432a.e != null) {
            this.f1432a.e.onReady(bluetoothDevice);
        }
    }
}
