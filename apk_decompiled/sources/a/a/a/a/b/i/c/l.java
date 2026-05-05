package a.a.a.a.b.i.c;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class l implements IActionListener<BluetoothDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1401a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ p f1402b;

    public l(p pVar, IActionListener iActionListener) {
        this.f1402b = pVar;
        this.f1401a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(p.f1408a, "disconnect success");
        this.f1401a.onSuccess(true);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.a(p.f1408a, "disconnect fail:" + i + ";msg:" + str);
        this.f1401a.onFailure(i, str);
    }
}
