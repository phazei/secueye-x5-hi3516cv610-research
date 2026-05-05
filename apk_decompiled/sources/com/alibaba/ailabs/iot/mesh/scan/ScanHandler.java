package com.alibaba.ailabs.iot.mesh.scan;

import aisscanner.ScanResult;
import com.alibaba.ailabs.iot.mesh.ScanStatusCallback;

/* JADX INFO: loaded from: classes.dex */
public interface ScanHandler {
    void exit();

    void onScanFailed(int i, String str);

    void onScanResult(ScanResult scanResult, Scanner scanner);

    void onScanStop();

    void setScanStatusCallback(ScanStatusCallback scanStatusCallback);
}
