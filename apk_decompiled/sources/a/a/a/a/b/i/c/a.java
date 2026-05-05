package a.a.a.a.b.i.c;

import aisble.callback.DataReceivedCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;

/* JADX INFO: compiled from: ITinyMeshTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public interface a {
    void a();

    void a(DataReceivedCallback dataReceivedCallback);

    void a(BluetoothDevice bluetoothDevice, IConnectCallback iConnectCallback);

    void a(IActionListener<Boolean> iActionListener);

    void a(byte[] bArr, IActionListener<byte[]> iActionListener);

    void b();

    void init(Context context);
}
