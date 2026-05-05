package com.alibaba.ailabs.iot.bluetoothlesdk;

import aisscanner.ScanFilter;
import aisscanner.ScanResult;
import android.os.ParcelUuid;
import android.util.SparseArray;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class GenieSpeakerScanStrategy implements ILeScanStrategy {
    private static final String AILABS_MANUFACTURER_DATA = "TM_GENIE";
    private static final String GENIE_SPEAKER_SERVICE_UUID = "00009e20-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "GenieSpeakerScanStrategy";
    private static final GenieSpeakerScanStrategy mInstance = new GenieSpeakerScanStrategy();

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceSubtype getBluetoothDeviceSubtype() {
        return null;
    }

    private GenieSpeakerScanStrategy() {
    }

    public static GenieSpeakerScanStrategy getInstance() {
        return mInstance;
    }

    public void register() {
        BLEScannerProxy.getInstance().registerLeScanStrategy(SmartSpeakerBLEDevice.GENIE_SMART_SPEAKER_BLE, mInstance);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public List<ScanFilter> getCustomScanFilters() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(GENIE_SPEAKER_SERVICE_UUID)).build());
        return arrayList;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceWrapper createFromScanResult(ScanResult scanResult) {
        SparseArray<byte[]> manufacturerSpecificData;
        byte[] bArrValueAt;
        if (scanResult == null || scanResult.getScanRecord() == null || (manufacturerSpecificData = scanResult.getScanRecord().getManufacturerSpecificData()) == null || manufacturerSpecificData.size() < 1 || (bArrValueAt = manufacturerSpecificData.valueAt(0)) == null || bArrValueAt.length < 14 || !new String(bArrValueAt).startsWith(AILABS_MANUFACTURER_DATA) || bArrValueAt.length != 18) {
            return null;
        }
        byte[] bArr = new byte[6];
        System.arraycopy(bArrValueAt, 8, bArr, 0, 6);
        String macAddress = getMacAddress(bArr);
        logd("MAC Address:" + getMacAddress(bArr));
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArrValueAt, 14, bArr2, 0, 4);
        String str = new String(bArr2);
        logd("Net Config ID:" + new String(bArr2));
        SmartSpeakerBLEDevice smartSpeakerBLEDevice = new SmartSpeakerBLEDevice(scanResult.getDevice(), str, macAddress);
        smartSpeakerBLEDevice.setScanResult(scanResult);
        smartSpeakerBLEDevice.setScanRecord(scanResult.getScanRecord());
        return smartSpeakerBLEDevice;
    }

    private void logd(String str) {
        LogUtils.d("device_connect", str);
    }

    private void loge(String str) {
        LogUtils.e("device_connect", str);
    }

    private String getMacAddress(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b2 : bArr) {
            sb.append(String.format("%02x", Integer.valueOf(b2 & 255)));
            sb.append(":");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
