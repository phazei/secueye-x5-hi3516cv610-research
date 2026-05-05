package com.alibaba.ailabs.iot.mesh;

import aisscanner.ScanResult;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: loaded from: classes.dex */
public class UnprovisionedBluetoothMeshDevice extends BluetoothDeviceWrapper {
    public String authDev;
    public String authFlag;
    public String mPk;
    public int mSigMeshProductID;
    public String random;
    public static final String TAG = UnprovisionedMeshNode.class.getSimpleName();
    public static int GENIE_SIGMESH = 16;
    public ScanResult mMeshScanResult = null;
    public UnprovisionedMeshNodeData mUnprovisionedMeshNodeData = null;

    public UnprovisionedBluetoothMeshDevice(BluetoothDevice bluetoothDevice, int i) {
        this.mSigMeshProductID = i;
        setBluetoothDevice(bluetoothDevice);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public String getAddress() {
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.mUnprovisionedMeshNodeData;
        return unprovisionedMeshNodeData == null ? super.getAddress() : unprovisionedMeshNodeData.getDeviceMac();
    }

    public String getAuthDev() {
        return this.authDev;
    }

    public String getAuthFlag() {
        return this.authFlag;
    }

    public ScanResult getMeshScanResult() {
        return this.mMeshScanResult;
    }

    public String getPk() {
        return this.mPk;
    }

    public String getRandom() {
        return this.random;
    }

    public int getSigMeshProductID() {
        return this.mSigMeshProductID;
    }

    public byte[] getUUID() {
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.mUnprovisionedMeshNodeData;
        if (unprovisionedMeshNodeData == null) {
            return null;
        }
        return unprovisionedMeshNodeData.getDeviceUuid();
    }

    public UnprovisionedMeshNodeData getmUnprovisionedMeshNodeData() {
        return this.mUnprovisionedMeshNodeData;
    }

    public boolean isComboMeshNode() {
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.mUnprovisionedMeshNodeData;
        if (unprovisionedMeshNodeData == null) {
            return false;
        }
        return AliMeshUUIDParserUtil.isComboMesh(unprovisionedMeshNodeData.getDeviceUuid());
    }

    public boolean isSupportGatt() {
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.mUnprovisionedMeshNodeData;
        return unprovisionedMeshNodeData == null || !unprovisionedMeshNodeData.isFastProvisionMesh() || this.mUnprovisionedMeshNodeData.isFastSupportGatt();
    }

    public void setAuthDev(String str) {
        this.authDev = str;
    }

    public void setAuthFlag(String str) {
        this.authFlag = str;
    }

    public void setPk(String str) {
        this.mPk = str;
    }

    public void setRandom(String str) {
        this.random = str;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public void setScanResult(ScanResult scanResult) {
        super.setScanResult(scanResult);
        this.mMeshScanResult = scanResult;
    }

    public void setSigMeshProductID(int i) {
        this.mSigMeshProductID = i;
    }

    public void setUnprovisionedMeshNodeData(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.mUnprovisionedMeshNodeData = unprovisionedMeshNodeData;
    }
}
