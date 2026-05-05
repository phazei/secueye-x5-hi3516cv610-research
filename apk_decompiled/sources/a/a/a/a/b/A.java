package a.a.a.a.b;

import aisscanner.ScanCallback;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.os.ParcelUuid;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import java.util.List;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class A extends ScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1193a;

    public A(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1193a = deviceProvisioningWorker;
    }

    @Override // aisscanner.ScanCallback
    public void onBatchScanResults(List<ScanResult> list) {
    }

    @Override // aisscanner.ScanCallback
    public void onScanFailed(int i) {
        a.a.a.a.b.m.a.b(this.f1193a.f2789b, "onScanFailed: " + i);
    }

    @Override // aisscanner.ScanCallback
    public void onScanResult(int i, ScanResult scanResult) {
        byte[] serviceData;
        if (!this.f1193a.J) {
            String address = scanResult.getDevice() == null ? "" : scanResult.getDevice().getAddress();
            a.a.a.a.b.m.a.b(this.f1193a.f2789b, "mIsScanning:" + this.f1193a.J + ";macAddress" + address);
            return;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        String address2 = scanResult.getDevice() == null ? "" : scanResult.getDevice().getAddress();
        a.a.a.a.b.m.a.a(this.f1193a.f2789b, "scanCallback ====>" + address2);
        if (TextUtils.isEmpty(address2) || !this.f1193a.a(address2) || scanRecord == null || (serviceData = scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROXY_UUID))) == null) {
            return;
        }
        a.a.a.a.b.m.a.a(this.f1193a.f2789b, "serviceData exect ====>");
        if (this.f1193a.e.f(serviceData)) {
            if (this.f1193a.a(serviceData)) {
                this.f1193a.s();
                this.f1193a.c(scanRecord.getDeviceName());
                this.f1193a.b(new ExtendedBluetoothDevice(scanResult));
                return;
            }
            return;
        }
        if (this.f1193a.e.g(serviceData)) {
            if (this.f1193a.e.a(this.f1193a.e.b(MeshParserUtils.toByteArray(this.f1193a.m.h())), serviceData)) {
                this.f1193a.s();
                this.f1193a.c(scanRecord.getDeviceName());
                this.f1193a.b(new ExtendedBluetoothDevice(scanResult));
            }
        }
    }
}
