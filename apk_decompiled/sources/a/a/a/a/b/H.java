package a.a.a.a.b;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class H implements IConnectCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1206a;

    public H(MeshService meshService) {
        this.f1206a = meshService;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onConnected(BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onFailure(BluetoothDevice bluetoothDevice, int i, String str) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "init connect onFailure:" + i + ";msg:" + str);
        this.f1206a.handleProvisionFailed(MeshUtConst$MeshErrorEnum.CREATE_TINYMESH_CHANNEL_ERROR, "init channel onFailure:" + i + ";msg:" + str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IConnectCallback
    public void onReady(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "init transport layer success");
        this.f1206a.mDeviceIsReadyInProvisioningStep = true;
        this.f1206a.handleDeviceReadyInProvisioningStep();
    }
}
