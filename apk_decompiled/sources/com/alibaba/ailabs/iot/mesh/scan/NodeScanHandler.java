package com.alibaba.ailabs.iot.mesh.scan;

import a.a.a.a.b.m.a;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelUuid;
import b.C0378l;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.ScanStatusCallback;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import java.util.LinkedHashMap;
import java.util.Map;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: loaded from: classes.dex */
public class NodeScanHandler implements ScanHandler {
    public static final String TAG = "tg_mesh_sdk_" + NodeScanHandler.class.getSimpleName();
    public MeshService.b mBinder;
    public Context mContext;
    public C0378l mMeshManagerApi;
    public ScanStatusCallback mScanStatusCallback;
    public final LinkedHashMap<Integer, ProvisionedMeshNode> mProvisionedNodesMap = new LinkedHashMap<>();
    public ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.alibaba.ailabs.iot.mesh.scan.NodeScanHandler.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NodeScanHandler.this.mBinder = (MeshService.b) iBinder;
            if (NodeScanHandler.this.mBinder != null) {
                NodeScanHandler nodeScanHandler = NodeScanHandler.this;
                nodeScanHandler.mMeshManagerApi = nodeScanHandler.mBinder.e();
                NodeScanHandler.this.mProvisionedNodesMap.clear();
                NodeScanHandler.this.mProvisionedNodesMap.putAll(NodeScanHandler.this.mBinder.i());
                if (NodeScanHandler.this.mScanStatusCallback != null) {
                    NodeScanHandler.this.mScanStatusCallback.onStatus(1, "Connected to meshService");
                }
                a.a(NodeScanHandler.TAG, "Connected to meshService");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            NodeScanHandler.this.mBinder = null;
            if (NodeScanHandler.this.mScanStatusCallback != null) {
                NodeScanHandler.this.mScanStatusCallback.onStatus(-1, "Disconnected to meshService");
            }
            a.b(NodeScanHandler.TAG, "Disconnected to meshService");
        }
    };

    public NodeScanHandler(Context context, ScanStatusCallback scanStatusCallback) {
        if (context == null) {
            a.b(TAG, "Context is null...");
            return;
        }
        this.mContext = context;
        this.mScanStatusCallback = scanStatusCallback;
        context.bindService(new Intent(context, (Class<?>) MeshService.class), this.mServiceConnection, 0);
    }

    private boolean checkIfNodeIdentityMatches(byte[] bArr) {
        if (this.mBinder == null) {
            return false;
        }
        for (Map.Entry<Integer, ProvisionedMeshNode> entry : this.mProvisionedNodesMap.entrySet()) {
            C0378l c0378l = this.mMeshManagerApi;
            if (c0378l != null && c0378l.b(entry.getValue(), bArr)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        return (obj instanceof NodeScanHandler) && ((NodeScanHandler) obj).mScanStatusCallback == this.mScanStatusCallback;
    }

    @Override // com.alibaba.ailabs.iot.mesh.scan.ScanHandler
    public void exit() {
        this.mContext.unbindService(this.mServiceConnection);
        this.mScanStatusCallback = null;
        a.a(TAG, "exit");
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
        byte[] serviceData;
        C0378l c0378l;
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord == null || (serviceData = scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROXY_UUID))) == null || this.mBinder == null || (c0378l = this.mMeshManagerApi) == null) {
            return;
        }
        if (c0378l.g(serviceData)) {
            if (this.mMeshManagerApi.a(this.mBinder.h(), serviceData)) {
                scanner.deviceDiscovered(scanResult);
                ScanStatusCallback scanStatusCallback = this.mScanStatusCallback;
                if (scanStatusCallback != null) {
                    scanStatusCallback.onScannResult(scanner.getDevices(), true);
                }
                a.a(TAG, "Scaned devices size: " + scanner.getDevices().size());
                return;
            }
            return;
        }
        if (this.mMeshManagerApi.f(serviceData) && checkIfNodeIdentityMatches(serviceData)) {
            scanner.deviceDiscovered(scanResult);
            ScanStatusCallback scanStatusCallback2 = this.mScanStatusCallback;
            if (scanStatusCallback2 != null) {
                scanStatusCallback2.onScannResult(scanner.getDevices(), true);
            }
            a.a(TAG, "Scaned devices size: " + scanner.getDevices().size());
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
