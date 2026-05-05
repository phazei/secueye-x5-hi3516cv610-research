package a.a.a.a.b.i.c;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class h implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1395a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ j f1396b;

    public h(j jVar, IActionListener iActionListener) {
        this.f1396b = jVar;
        this.f1395a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        IActionListener iActionListener = this.f1395a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, "");
        }
    }
}
