package a.a.a.a.b.i;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import datasource.bean.DeviceStatus;
import java.util.LinkedList;

/* JADX INFO: renamed from: a.a.a.a.b.i.p, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionV2Worker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0350p implements DataReceivedCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ u f1433a;

    public C0350p(u uVar) {
        this.f1433a = uVar;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        byte[] value;
        if (this.f1433a.k == null) {
            a.a.a.a.b.m.a.d(this.f1433a.f1439a, "received data in invalid state");
            return;
        }
        if (this.f1433a.f == null || !this.f1433a.f.isSupportFastProvisioningV2()) {
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
                a.a.a.a.b.m.a.b(this.f1433a.f1439a, "payload is null");
                return;
            }
            a.a.a.a.b.m.a.b(this.f1433a.f1439a, "payload length illegal " + value.length);
            return;
        }
        a.a.a.a.b.m.a.c(this.f1433a.f1439a, ConvertUtils.bytes2HexString(value));
        int i = t.f1438a[this.f1433a.k.b().ordinal()];
        if (i == 1) {
            if (this.f1433a.k.a(value)) {
                this.f1433a.a();
                this.f1433a.d();
                this.f1433a.f();
                return;
            }
            return;
        }
        if (i == 2 && this.f1433a.k.a(value)) {
            this.f1433a.g();
            LinkedList linkedList = null;
            String strC = ((a.a.a.a.b.i.b.a) this.f1433a.k).c();
            if (!TextUtils.isEmpty(strC)) {
                DeviceStatus deviceStatus = new DeviceStatus();
                deviceStatus.setUserId("");
                deviceStatus.setUuid("");
                deviceStatus.setUnicastAddress(this.f1433a.m.getPrimaryUnicastAddress().intValue());
                deviceStatus.setStatus(strC);
                LinkedList linkedList2 = new LinkedList();
                linkedList2.add(deviceStatus);
                linkedList = linkedList2;
            }
            if (this.f1433a.f1442d != null) {
                this.f1433a.f1442d.onProvisioningComplete(this.f1433a.h, linkedList);
            }
        }
    }
}
