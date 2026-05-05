package a.a.a.a.b.i;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.tg.utils.ConvertUtils;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class z implements DataReceivedCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ J f1449a;

    public z(J j) {
        this.f1449a = j;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        byte[] value;
        a.a.a.a.b.m.a.c(this.f1449a.f1354a, "onAliBLEDeviceFound " + bluetoothDevice.getAddress());
        if (data != null) {
            if (this.f1449a.i == null || !this.f1449a.i.isFastSupportGatt()) {
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
                    a.a.a.a.b.m.a.b(this.f1449a.f1354a, "payload is null");
                    return;
                }
                a.a.a.a.b.m.a.b(this.f1449a.f1354a, "payload length illegal " + value.length);
                return;
            }
            a.a.a.a.b.m.a.c(this.f1449a.f1354a, ConvertUtils.bytes2HexString(value));
            if (!this.f1449a.g()) {
                if (value[0] == 7) {
                    this.f1449a.a(value);
                    return;
                }
                return;
            }
            if (this.f1449a.i != null && value[1] == this.f1449a.i.getMac()[4] && value[2] == this.f1449a.i.getMac()[5]) {
                a.a.a.a.b.m.a.c(this.f1449a.f1354a, "find excepted data");
                this.f1449a.b(value);
                return;
            }
            if (this.f1449a.i != null && value[1] == this.f1449a.i.getMac()[0] && value[2] == this.f1449a.i.getMac()[1]) {
                a.a.a.a.b.m.a.c(this.f1449a.f1354a, "find ack data");
                this.f1449a.b(value);
            } else {
                if (value[0] == 7) {
                    this.f1449a.a(value);
                    return;
                }
                a.a.a.a.b.m.a.b(this.f1449a.f1354a, "failed_to_process_data " + ConvertUtils.bytes2HexString(value));
            }
        }
    }
}
