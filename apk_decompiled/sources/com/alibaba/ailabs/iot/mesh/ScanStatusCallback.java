package com.alibaba.ailabs.iot.mesh;

import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ScanStatusCallback extends StatusCallback {
    void onScannResult(List<ExtendedBluetoothDevice> list, boolean z);
}
