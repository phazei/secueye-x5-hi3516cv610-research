package a.a.a.a.b.i.c;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class m implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1403a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ p f1404b;

    public m(p pVar, IActionListener iActionListener) {
        this.f1404b = pVar;
        this.f1403a = iActionListener;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(BluetoothDevice bluetoothDevice, Data data) {
        if (data == null || data.getValue() == null) {
            return;
        }
        Utils.notifySuccess((IActionListener<byte[]>) this.f1403a, data.getValue());
    }
}
