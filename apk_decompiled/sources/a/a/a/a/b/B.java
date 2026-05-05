package a.a.a.a.b;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;

/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class B implements IConnectCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1197a;

    public B(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1197a = deviceProvisioningWorker;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onConnected(BluetoothDevice bluetoothDevice) {
        this.f1197a.o();
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onFailure(BluetoothDevice bluetoothDevice, int i, String str) {
        this.f1197a.o();
        a.a.a.a.b.m.a.a(this.f1197a.f2789b, "init connect onFailure:" + i + ";msg:" + str);
        this.f1197a.a(MeshUtConst$MeshErrorEnum.CREATE_TINYMESH_CHANNEL_ERROR, "init channel onFailure:" + i + ";msg:" + str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onReady(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f1197a.f2789b, "init transport layer success");
        this.f1197a.k = true;
        this.f1197a.i();
    }
}
