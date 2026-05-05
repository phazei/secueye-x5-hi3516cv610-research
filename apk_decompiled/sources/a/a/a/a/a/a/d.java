package a.a.a.a.a.a;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;

/* JADX INFO: compiled from: GattTransmissionLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class d implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1169a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ GattTransmissionLayer f1170b;

    public d(GattTransmissionLayer gattTransmissionLayer, IActionListener iActionListener) {
        this.f1170b = gattTransmissionLayer;
        this.f1169a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        IActionListener iActionListener = this.f1169a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, String.format("Disconnect: %s failed", bluetoothDevice.getAddress()));
        }
    }
}
