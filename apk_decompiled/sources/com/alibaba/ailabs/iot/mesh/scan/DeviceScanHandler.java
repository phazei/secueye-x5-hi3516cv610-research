package com.alibaba.ailabs.iot.mesh.scan;

import a.a.a.a.b.m.a;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.os.ParcelUuid;
import com.alibaba.ailabs.iot.mesh.ScanStatusCallback;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: loaded from: classes.dex */
public class DeviceScanHandler implements ScanHandler {
    public static final String TAG = "tg_mesh_sdk_" + DeviceScanHandler.class.getSimpleName();
    public int mProductId;
    public ScanStatusCallback mScanStatusCallback;

    public DeviceScanHandler(ScanStatusCallback scanStatusCallback) {
        this.mScanStatusCallback = scanStatusCallback;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DeviceScanHandler) && ((DeviceScanHandler) obj).mScanStatusCallback == this.mScanStatusCallback;
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void exit() {
        a.a(TAG, "exit");
    }

    public int getProductId() {
        return this.mProductId;
    }

    public int hashCode() {
        ScanStatusCallback scanStatusCallback = this.mScanStatusCallback;
        return scanStatusCallback != null ? scanStatusCallback.hashCode() : super.hashCode();
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void onScanFailed(int i, String str) {
        ScanStatusCallback scanStatusCallback = this.mScanStatusCallback;
        if (scanStatusCallback != null) {
            scanStatusCallback.onStatus(i, str);
        }
        a.b(TAG, "onScanFailed, errorCode: " + i + ", errorMsg: " + str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void onScanResult(ScanResult scanResult, Scanner scanner) {
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord != null) {
            UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)));
            if (unprovisionedMeshNodeData.isValid()) {
                this.mProductId = unprovisionedMeshNodeData.getProductId();
                if (scanner != null) {
                    scanner.deviceDiscovered(scanResult);
                    ScanStatusCallback scanStatusCallback = this.mScanStatusCallback;
                    if (scanStatusCallback != null) {
                        scanStatusCallback.onScannResult(scanner.getDevices(), false);
                    }
                    a.a(TAG, "Scaned devices size: " + scanner.getDevices().size());
                }
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void onScanStop() {
        ScanStatusCallback scanStatusCallback = this.mScanStatusCallback;
        if (scanStatusCallback != null) {
            scanStatusCallback.onStatus(6, "scan stop");
        }
        a.a(TAG, "scan stop");
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void setScanStatusCallback(ScanStatusCallback scanStatusCallback) {
        this.mScanStatusCallback = scanStatusCallback;
    }
}
