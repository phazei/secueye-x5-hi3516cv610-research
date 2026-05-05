package a.a.a.a.b.i.c;

import aisble.data.Data;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.provision.bean.FastProvisionDevice;

/* JADX INFO: compiled from: TinyMeshAdvTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class d implements ILeScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ g f1387a;

    public d(g gVar) {
        this.f1387a = gVar;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        a.a.a.a.b.m.a.c(this.f1387a.f1391a, "onAliBLEDeviceFound " + bluetoothDeviceWrapper.getAddress());
        if (bluetoothDeviceWrapper instanceof FastProvisionDevice) {
            if (bluetoothDeviceWrapper.getScanRecord() == null) {
                a.a.a.a.b.m.a.b(this.f1387a.f1391a, "scan record is null");
                return;
            }
            this.f1387a.f1393c.onDataReceived(bluetoothDeviceWrapper.getBluetoothDevice(), new Data(((FastProvisionDevice) bluetoothDeviceWrapper).a()));
            return;
        }
        if (bluetoothDeviceWrapper == null) {
            a.a.a.a.b.m.a.c(this.f1387a.f1391a, "bluetoothDeviceWrapper is null");
            return;
        }
        a.a.a.a.b.m.a.c(this.f1387a.f1391a, "device is not FastProvisionDevice " + bluetoothDeviceWrapper.getClass().getSimpleName());
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStartScan() {
        a.a.a.a.b.m.a.c(this.f1387a.f1391a, "onStartScan: ");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStopScan() {
        a.a.a.a.b.m.a.c(this.f1387a.f1391a, "onStopScan");
    }
}
