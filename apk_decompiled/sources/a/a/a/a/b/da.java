package a.a.a.a.b;

import aisscanner.ScanCallback;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.os.ParcelUuid;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import java.util.List;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class da extends ScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1318a;

    public da(MeshService meshService) {
        this.f1318a = meshService;
    }

    @Override // aisscanner.ScanCallback
    public void onBatchScanResults(List<ScanResult> list) {
    }

    @Override // aisscanner.ScanCallback
    public void onScanFailed(int i) {
        a.a.a.a.b.m.a.b(MeshService.TAG, "onScanFailed: " + i);
    }

    @Override // aisscanner.ScanCallback
    public void onScanResult(int i, ScanResult scanResult) {
        byte[] serviceData;
        if (!this.f1318a.mIsScanning) {
            String address = scanResult.getDevice() == null ? "" : scanResult.getDevice().getAddress();
            a.a.a.a.b.m.a.b(MeshService.TAG, "mIsScanning:" + this.f1318a.mIsScanning + ";macAddress" + address);
            return;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        String address2 = scanResult.getDevice() == null ? "" : scanResult.getDevice().getAddress();
        a.a.a.a.b.m.a.a(MeshService.TAG, "scanCallback ====>" + address2);
        if (TextUtils.isEmpty(address2) || !this.f1318a.checkMacAddressInWhiteList(address2) || scanRecord == null || (serviceData = scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROXY_UUID))) == null) {
            return;
        }
        a.a.a.a.b.m.a.a(MeshService.TAG, "serviceData exect ====>");
        if (this.f1318a.mMeshManagerApi.f(serviceData)) {
            if (this.f1318a.checkIfNodeIdentityMatches(serviceData)) {
                this.f1318a.stopScan();
                this.f1318a.sendBroadcastProvisionedNodeFound(scanRecord.getDeviceName());
                this.f1318a.onProvisionedDeviceFound(new ExtendedBluetoothDevice(scanResult));
                return;
            }
            return;
        }
        if (this.f1318a.mMeshManagerApi.g(serviceData)) {
            if (this.f1318a.mMeshManagerApi.a(this.f1318a.mMeshManagerApi.b(MeshParserUtils.toByteArray(this.f1318a.mProvisioningSettings.h())), serviceData)) {
                this.f1318a.stopScan();
                this.f1318a.sendBroadcastProvisionedNodeFound(scanRecord.getDeviceName());
                this.f1318a.onProvisionedDeviceFound(new ExtendedBluetoothDevice(scanResult));
            }
        }
    }
}
