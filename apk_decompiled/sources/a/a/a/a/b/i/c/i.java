package a.a.a.a.b.i.c;

import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class i implements SuccessCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1397a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ j f1398b;

    public i(j jVar, IActionListener iActionListener) {
        this.f1398b = jVar;
        this.f1397a = iActionListener;
    }

    @Override // aisble.callback.SuccessCallback
    public void onRequestCompleted(@NonNull BluetoothDevice bluetoothDevice) {
        IActionListener iActionListener = this.f1397a;
        if (iActionListener != null) {
            iActionListener.onSuccess(bluetoothDevice);
        }
    }
}
