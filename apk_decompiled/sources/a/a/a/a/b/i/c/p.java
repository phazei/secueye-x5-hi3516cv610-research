package a.a.a.a.b.i.c;

import aisble.callback.DataReceivedCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedPluginTypeException;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.tg.utils.ConvertUtils;

/* JADX INFO: compiled from: TinyMeshGattTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class p implements a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f1408a = "InexpensiveMesh.GattTransportLayer";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public ITransmissionLayer f1409b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public BluetoothDevice f1410c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f1411d;
    public c e;
    public IConnectCallback f = null;

    @Override // a.a.a.a.b.i.c.a
    public void init(Context context) {
        this.f1411d = context;
        if (this.f1409b == null) {
            synchronized (this) {
                this.f1409b = new j(this, this.f1411d);
                this.e = new c();
                try {
                    this.f1409b.installPlugin(this.e);
                } catch (UnsupportedPluginTypeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // a.a.a.a.b.i.c.a
    public void b() {
        c cVar;
        ITransmissionLayer iTransmissionLayer = this.f1409b;
        if (iTransmissionLayer == null || (cVar = this.e) == null) {
            return;
        }
        iTransmissionLayer.uninstallPlugin(cVar);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(DataReceivedCallback dataReceivedCallback) {
        this.e.a(dataReceivedCallback);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(BluetoothDevice bluetoothDevice, IConnectCallback iConnectCallback) {
        a.a.a.a.b.m.a.c(f1408a, "Connect...");
        this.f1410c = bluetoothDevice;
        this.f = iConnectCallback;
        this.f1409b.connectDevice(this.f1410c, new k(this));
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(IActionListener<Boolean> iActionListener) {
        this.f1409b.disconnectDevice(new l(this, iActionListener));
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(byte[] bArr, IActionListener<byte[]> iActionListener) {
        if (a(this.e, iActionListener)) {
            a.a.a.a.b.m.a.a(f1408a, "sendRawDataWithCallback data:" + ConvertUtils.bytes2HexString(bArr));
            int length = bArr == null ? 2 : bArr.length + 2;
            byte[] bArr2 = new byte[length];
            bArr2[0] = 1;
            bArr2[1] = -88;
            if (length > 2) {
                System.arraycopy(bArr, 0, bArr2, 2, bArr.length);
            }
            this.e.sendRawDataWithCallback(bArr2, new m(this, iActionListener), new n(this, iActionListener));
        }
    }

    @Override // a.a.a.a.b.i.c.a
    public void a() {
        ITransmissionLayer iTransmissionLayer = this.f1409b;
        if (iTransmissionLayer != null) {
            if (iTransmissionLayer.getConnectionState() == LayerState.CONNECTED) {
                this.f1409b.disconnectDevice(new o(this));
            } else {
                ((GattTransmissionLayer) this.f1409b).close();
            }
        }
    }

    public boolean a(IPlugin iPlugin, IActionListener iActionListener) {
        ITransmissionLayer iTransmissionLayer = this.f1409b;
        if (iTransmissionLayer == null || iTransmissionLayer.getConnectionState() != LayerState.CONNECTED) {
            if (iActionListener != null) {
                iActionListener.onFailure(-1, "Not connected");
            }
            return false;
        }
        if (iPlugin != null) {
            return true;
        }
        if (iActionListener != null) {
            iActionListener.onFailure(-202, "Not supported");
        }
        return false;
    }
}
