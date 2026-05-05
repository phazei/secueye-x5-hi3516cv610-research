package com.alibaba.ailabs.iot.mesh;

import a.a.a.a.b.d.a;
import aisscanner.ScanFilter;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.os.ParcelUuid;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class UnprovisionedMeshDeviceScanStrategy implements ILeScanStrategy {
    public static final int MESH_COMPANY_ID = 424;
    public static final String MESH_PROVISIONING_UUID = "00001827-0000-1000-8000-00805F9B34FB";
    public static final String TAG = "UnprovisionedMeshDeviceScanStrategy";
    public static UnprovisionedMeshDeviceScanStrategy mInstance = new UnprovisionedMeshDeviceScanStrategy();

    public static UnprovisionedMeshDeviceScanStrategy getInstance() {
        return mInstance;
    }

    public static String getMacFromSimpleMac(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int length = str.length();
        if (length != 12 || str.contains(":")) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i += 2) {
            stringBuffer.append(str.charAt(i));
            int i2 = i + 1;
            stringBuffer.append(str.charAt(i2));
            if (i2 != length - 1) {
                stringBuffer.append(":");
            }
        }
        return stringBuffer.toString();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceWrapper createFromScanResult(ScanResult scanResult) {
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord != null) {
            byte[] serviceData = scanRecord.getServiceData(ParcelUuid.fromString("00001827-0000-1000-8000-00805F9B34FB"));
            LogUtils.d(TAG, scanResult.getDevice().getAddress() + ": createFromScanResult " + ConvertUtils.bytes2HexString(serviceData));
            UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(serviceData);
            if (unprovisionedMeshNodeData.isValid() && !unprovisionedMeshNodeData.isQuietModel()) {
                if (!a.f1315a) {
                    String macFromSimpleMac = getMacFromSimpleMac(unprovisionedMeshNodeData.getDeviceMac());
                    if (!TextUtils.isEmpty(macFromSimpleMac) && !macFromSimpleMac.equalsIgnoreCase(scanResult.getDevice().getAddress()) && !AliMeshUUIDParserUtil.isComboMesh(serviceData)) {
                        a.a.a.a.b.m.a.b(TAG, "addressOfServiceUUID: " + macFromSimpleMac + ", but physical address: " + scanResult.getDevice().getAddress());
                        return null;
                    }
                }
                boolean zIsSupportFastProvisioningV2 = unprovisionedMeshNodeData.isSupportFastProvisioningV2();
                byte[] manufacturerSpecificData = scanRecord.getManufacturerSpecificData(424);
                String strBytesToHex = MeshParserUtils.bytesToHex(manufacturerSpecificData, 10, 4, false);
                String strBytesToHex2 = MeshParserUtils.bytesToHex(manufacturerSpecificData, 14, 8, false);
                LogUtils.w(TAG, "random:" + strBytesToHex + ", authDev:" + strBytesToHex2 + ", rssi:" + scanResult.getRssi());
                if (zIsSupportFastProvisioningV2 && (TextUtils.isEmpty(strBytesToHex) || TextUtils.isEmpty(strBytesToHex2))) {
                    a.a.a.a.b.m.a.d(TAG, String.format("unexpected scan situation occurs, device(%s) support v2 provisioning, but random or auth dev not got, ignore the results of this scan and wait for the next result", scanResult.getDevice().getAddress()));
                    return null;
                }
                UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = new UnprovisionedBluetoothMeshDevice(scanResult.getDevice(), unprovisionedMeshNodeData.getProductId());
                unprovisionedBluetoothMeshDevice.setScanResult(scanResult);
                unprovisionedBluetoothMeshDevice.setScanRecord(scanResult.getScanRecord());
                unprovisionedBluetoothMeshDevice.setUnprovisionedMeshNodeData(unprovisionedMeshNodeData);
                unprovisionedBluetoothMeshDevice.setRandom(strBytesToHex);
                unprovisionedBluetoothMeshDevice.setAuthDev(strBytesToHex2);
                unprovisionedBluetoothMeshDevice.setAuthFlag((TextUtils.isEmpty(strBytesToHex) || TextUtils.isEmpty(strBytesToHex2)) ? "0" : "1");
                LogUtils.i(TAG, String.format("Create from scan result for device(%s) success", scanResult.getDevice().getAddress()));
                return unprovisionedBluetoothMeshDevice;
            }
            a.a.a.a.b.m.a.d(TAG, "data invalid or is quiet model");
        }
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceSubtype getBluetoothDeviceSubtype() {
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public List<ScanFilter> getCustomScanFilters() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString("00001827-0000-1000-8000-00805F9B34FB")).build());
        return arrayList;
    }

    public void register() {
        BLEScannerProxy.getInstance().registerLeScanStrategy(UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH, this);
    }
}
