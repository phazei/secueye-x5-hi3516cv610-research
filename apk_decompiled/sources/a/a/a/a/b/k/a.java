package a.a.a.a.b.k;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.tg.utils.ConvertUtils;

/* JADX INFO: compiled from: TinyMeshMessageAdvSender.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements DataReceivedCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ d f1467a;

    public a(d dVar) {
        this.f1467a = dVar;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        if (this.f1467a.i != null && this.f1467a.i.e()) {
            a.a.a.a.b.m.a.b(d.f1472a, "Exist provision activity for tinyMesh, discard");
            return;
        }
        a.a.a.a.b.m.a.c(d.f1472a, "onAliBLEDeviceFound " + bluetoothDevice.getAddress());
        if (data != null) {
            byte[] value = data.getValue();
            if (value != null && value.length >= 3) {
                a.a.a.a.b.m.a.c(d.f1472a, ConvertUtils.bytes2HexString(value));
                if (value[0] == 7) {
                    this.f1467a.a(value);
                    return;
                }
                return;
            }
            if (value == null) {
                a.a.a.a.b.m.a.b(d.f1472a, "payload is null");
                return;
            }
            a.a.a.a.b.m.a.b(d.f1472a, "payload length illegal " + value.length);
        }
    }
}
