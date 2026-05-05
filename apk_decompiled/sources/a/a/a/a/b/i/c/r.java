package a.a.a.a.b.i.c;

import a.a.a.a.b.C0327b;
import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;

/* JADX INFO: compiled from: TinyMeshGattTransportLayerV2.java */
/* JADX INFO: loaded from: classes.dex */
public class r implements a, BleMeshManagerCallbacks {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BleMeshManager f1414b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public DataReceivedCallback f1415c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BluetoothDevice f1416d;
    public String g;
    public Runnable i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1413a = "InexpensiveMesh.GattTransportLayer";
    public IConnectCallback e = null;
    public IActionListener f = null;
    public Handler h = new Handler(Looper.getMainLooper());

    public r(String str) {
        this.f1413a += str;
        this.g = str;
    }

    @Override // a.a.a.a.b.i.c.a
    public void b() {
    }

    public final void c() {
        Runnable runnable = this.i;
        if (runnable != null) {
            this.h.removeCallbacks(runnable);
            this.i = null;
        }
    }

    public BleMeshManager d() {
        return this.f1414b;
    }

    public final void e() {
        Handler handler = this.h;
        q qVar = new q(this);
        this.i = qVar;
        handler.postDelayed(qVar, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
    }

    @Override // a.a.a.a.b.i.c.a
    public void init(Context context) {
        this.f1414b = new BleMeshManager(context, this.g);
        this.f1414b.setProvisioningComplete(false);
        this.f1414b.setGattCallbacks(this);
    }

    @Override // aisble.BleManagerCallbacks
    public void onBatteryValueReceived(BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBonded(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingFailed(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingRequired(BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataReceived(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        a.a.a.a.b.m.a.a(this.f1413a, "onDataReceived, device: " + bluetoothDevice.getName() + ", mac: " + bluetoothDevice.getAddress() + ", mtu: " + i + ", pdu: " + bArr);
        DataReceivedCallback dataReceivedCallback = this.f1415c;
        if (dataReceivedCallback != null) {
            dataReceivedCallback.onDataReceived(bluetoothDevice, new Data(bArr));
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataSent(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnected(BluetoothDevice bluetoothDevice) {
        e();
        IConnectCallback iConnectCallback = this.e;
        if (iConnectCallback != null) {
            iConnectCallback.onConnected(bluetoothDevice);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnecting(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnected(BluetoothDevice bluetoothDevice) {
        IActionListener iActionListener = this.f;
        if (iActionListener != null) {
            iActionListener.onSuccess(true);
            this.f = null;
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnecting(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceNotSupported(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceReady(BluetoothDevice bluetoothDevice) {
        IConnectCallback iConnectCallback = this.e;
        if (iConnectCallback != null) {
            iConnectCallback.onReady(bluetoothDevice);
            this.e = null;
        }
        C0327b.b().a(this.f1414b);
    }

    @Override // aisble.BleManagerCallbacks
    public void onError(BluetoothDevice bluetoothDevice, String str, int i) {
        a.a.a.a.b.m.a.b(this.f1413a, "onError: " + str);
    }

    @Override // aisble.BleManagerCallbacks
    public void onLinkLossOccurred(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onServicesDiscovered(BluetoothDevice bluetoothDevice, boolean z) {
        c();
    }

    @Override // aisble.BleManagerCallbacks
    public boolean shouldEnableBatteryLevelNotifications(BluetoothDevice bluetoothDevice) {
        return false;
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(DataReceivedCallback dataReceivedCallback) {
        this.f1415c = dataReceivedCallback;
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(BluetoothDevice bluetoothDevice, IConnectCallback iConnectCallback) {
        a.a.a.a.b.m.a.c(this.f1413a, "Connect...");
        this.f1416d = bluetoothDevice;
        this.e = iConnectCallback;
        this.f1414b.connect(bluetoothDevice).retry(5, 300).useAutoConnect(true).enqueue();
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(IActionListener<Boolean> iActionListener) {
        this.f = iActionListener;
        this.f1414b.disconnect().enqueue();
    }

    @Override // a.a.a.a.b.i.c.a
    public void a(byte[] bArr, IActionListener<byte[]> iActionListener) {
        a.a.a.a.b.m.a.a(this.f1413a, "sendRawDataWithCallback data:" + ConvertUtils.bytes2HexString(bArr));
        int length = bArr == null ? 2 : bArr.length + 2;
        byte[] bArr2 = new byte[length];
        bArr2[0] = 1;
        bArr2[1] = -88;
        if (length > 2) {
            System.arraycopy(bArr, 0, bArr2, 2, bArr.length);
        }
        this.f1414b.sendPdu(bArr2);
        Utils.notifySuccess(iActionListener, bArr2);
    }

    @Override // a.a.a.a.b.i.c.a
    public void a() {
        if (this.f1414b.getConnectState() == 2) {
            this.f1414b.disconnect().enqueue();
        } else {
            this.f1414b.close();
        }
        C0327b.b().b(this.f1414b);
    }
}
