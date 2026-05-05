package a.a.a.a.b.i.c;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class n implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1405a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ p f1406b;

    public n(p pVar, IActionListener iActionListener) {
        this.f1406b = pVar;
        this.f1405a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
        Utils.notifyFailed(this.f1405a, i, "Write failed");
    }
}
