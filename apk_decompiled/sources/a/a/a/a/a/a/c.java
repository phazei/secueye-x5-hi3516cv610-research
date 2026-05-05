package a.a.a.a.a.a;

import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;

/* JADX INFO: compiled from: GattTransmissionLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements SuccessCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1167a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ GattTransmissionLayer f1168b;

    public c(GattTransmissionLayer gattTransmissionLayer, IActionListener iActionListener) {
        this.f1168b = gattTransmissionLayer;
        this.f1167a = iActionListener;
    }

    @Override // aisble.callback.SuccessCallback
    public void onRequestCompleted(@NonNull BluetoothDevice bluetoothDevice) {
        IActionListener iActionListener = this.f1167a;
        if (iActionListener != null) {
            iActionListener.onSuccess(bluetoothDevice);
        }
    }
}
