package a.a.a.a.b.i.c;

import aisble.callback.DataReceivedCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import androidx.annotation.RequiresApi;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: TinyMeshAdvTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
@RequiresApi(api = 21)
public class g implements a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f1392b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public DataReceivedCallback f1393c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1391a = "InexpensiveMesh.AdvTransportLayer";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final ILeScanCallback f1394d = new d(this);

    @Override // a.a.a.a.b.i.c.a
    public void b() {
    }

    public final void c() {
        a.a.a.a.b.m.a.c(this.f1391a, "registerFastProvisionMeshScanStrategy");
        BLEScannerProxy.getInstance().registerLeScanStrategy(4096, new f(this));
    }

    @Override // a.a.a.a.b.i.c.a
    public void init(Context context) {
        this.f1392b = context;
        c();
        a.a.a.a.a.g.c().a(context);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(DataReceivedCallback dataReceivedCallback) {
        this.f1393c = dataReceivedCallback;
        BLEScannerProxy.getInstance().unlock();
        BLEScannerProxy.getInstance().startLeScan(this.f1392b, 60000, false, 4096, this.f1394d);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(BluetoothDevice bluetoothDevice, IConnectCallback iConnectCallback) {
        if (iConnectCallback != null) {
            iConnectCallback.onReady(bluetoothDevice);
        }
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(IActionListener<Boolean> iActionListener) {
        Utils.notifySuccess((IActionListener<boolean>) iActionListener, true);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(byte[] bArr, IActionListener<byte[]> iActionListener) {
        a.a.a.a.a.g.c().b(this.f1392b);
        a.a.a.a.a.g.c().b(bArr, new e(this, iActionListener));
    }

    @Override // a.a.a.a.b.i.c.a
    public void a() {
        a.a.a.a.a.g.c().d();
        BLEScannerProxy.getInstance().stopScan(this.f1394d);
    }
}
