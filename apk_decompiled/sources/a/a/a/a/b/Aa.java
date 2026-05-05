package a.a.a.a.b;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: compiled from: TgScanManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Aa implements ILeScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1194a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1195b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ TgScanManager f1196c;

    public Aa(TgScanManager tgScanManager, String str, IActionListener iActionListener) {
        this.f1196c = tgScanManager;
        this.f1194a = str;
        this.f1195b = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        if (!this.f1196c.mGetRemoteSpecifiedPIDDeviceSuccess && (bluetoothDeviceWrapper instanceof UnprovisionedBluetoothMeshDevice)) {
            UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = (UnprovisionedBluetoothMeshDevice) bluetoothDeviceWrapper;
            int sigMeshProductID = unprovisionedBluetoothMeshDevice.getSigMeshProductID();
            if (unprovisionedBluetoothMeshDevice.isComboMeshNode()) {
                String strValueOf = String.valueOf(sigMeshProductID);
                if (TextUtils.isEmpty(this.f1194a) || TmpConstant.GROUP_ROLE_UNKNOWN.equals(this.f1194a) || this.f1194a.equals(strValueOf)) {
                    this.f1196c.mGetRemoteSpecifiedPIDDeviceSuccess = true;
                    IActionListener iActionListener = this.f1195b;
                    if (iActionListener != null) {
                        iActionListener.onSuccess(unprovisionedBluetoothMeshDevice);
                    }
                    BLEScannerProxy.getInstance().stopScan(this);
                }
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStartScan() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStopScan() {
        IActionListener iActionListener;
        a.a.a.a.b.m.a.a(TgScanManager.TAG, "Scan finished");
        if (this.f1196c.mGetRemoteSpecifiedPIDDeviceSuccess || (iActionListener = this.f1195b) == null) {
            return;
        }
        iActionListener.onFailure(-5, "Scanning device failed");
    }
}
