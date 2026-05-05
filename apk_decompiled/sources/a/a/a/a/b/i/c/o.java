package a.a.a.a.b.i.c;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class o implements IActionListener<BluetoothDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ p f1407a;

    public o(p pVar) {
        this.f1407a = pVar;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(p.f1408a, "disconnect success");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.a(p.f1408a, "disconnect fail:" + i + ";msg:" + str);
    }
}
