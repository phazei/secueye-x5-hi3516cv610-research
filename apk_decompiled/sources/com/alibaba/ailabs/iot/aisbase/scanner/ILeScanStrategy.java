package com.alibaba.ailabs.iot.aisbase.scanner;

import aisscanner.ScanFilter;
import aisscanner.ScanResult;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ILeScanStrategy {
    BluetoothDeviceWrapper createFromScanResult(ScanResult scanResult);

    BluetoothDeviceSubtype getBluetoothDeviceSubtype();

    List<ScanFilter> getCustomScanFilters();
}
