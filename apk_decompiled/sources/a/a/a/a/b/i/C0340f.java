package a.a.a.a.b.i;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import com.alibaba.ailabs.tg.utils.ConvertUtils;

/* JADX INFO: renamed from: a.a.a.a.b.i.f, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0340f implements DataReceivedCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1419a;

    public C0340f(FastProvisionManager fastProvisionManager) {
        this.f1419a = fastProvisionManager;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        byte[] value;
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "onAliBLEDeviceFound " + bluetoothDevice.getAddress());
        if (data != null) {
            if (this.f1419a.unprovisionedMeshNodeData == null || !this.f1419a.unprovisionedMeshNodeData.isFastSupportGatt()) {
                value = data.getValue();
            } else {
                byte[] value2 = data.getValue();
                value = new byte[value2 == null ? 0 : value2.length - 3];
                if (value2 != null) {
                    System.arraycopy(value2, 3, value, 0, value.length);
                }
            }
            if (value == null || value.length < 3) {
                if (value == null) {
                    a.a.a.a.b.m.a.b(FastProvisionManager.TAG, "payload is null");
                    return;
                }
                a.a.a.a.b.m.a.b(FastProvisionManager.TAG, "payload length illegal " + value.length);
                return;
            }
            a.a.a.a.b.m.a.c(FastProvisionManager.TAG, ConvertUtils.bytes2HexString(value));
            if (!this.f1419a.isProvisionStatus()) {
                if (value[0] == 7) {
                    this.f1419a.assembleControlResp(value);
                    return;
                }
                return;
            }
            if (this.f1419a.unprovisionedMeshNodeData != null && value[1] == this.f1419a.unprovisionedMeshNodeData.getMac()[4] && value[2] == this.f1419a.unprovisionedMeshNodeData.getMac()[5]) {
                a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "find excepted data");
                this.f1419a.dispatchProvisionData(value);
                return;
            }
            if (this.f1419a.unprovisionedMeshNodeData != null && value[1] == this.f1419a.unprovisionedMeshNodeData.getMac()[0] && value[2] == this.f1419a.unprovisionedMeshNodeData.getMac()[1]) {
                a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "find ack data");
                this.f1419a.dispatchProvisionData(value);
            } else {
                if (value[0] == 7) {
                    this.f1419a.assembleControlResp(value);
                    return;
                }
                a.a.a.a.b.m.a.b(FastProvisionManager.TAG, "failed_to_process_data " + ConvertUtils.bytes2HexString(value));
            }
        }
    }
}
