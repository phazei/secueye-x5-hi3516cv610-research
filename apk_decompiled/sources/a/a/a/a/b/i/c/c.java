package a.a.a.a.b.i.c;

import aisble.callback.DataReceivedCallback;
import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedLayerException;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;
import com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import java.util.UUID;

/* JADX INFO: compiled from: ProvisionPlugin.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements BluetoothGattPlugin {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final UUID f1383a = UUID.fromString("00002ADB-0000-1000-8000-00805F9B34FB");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final UUID f1384b = UUID.fromString("00002ADC-0000-1000-8000-00805F9B34FB");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f1385c = "ProvisionPlugin";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public GattTransmissionLayer f1386d;
    public BluetoothDevice e;
    public BluetoothGattCharacteristic f;
    public BluetoothGattCharacteristic g;
    public DataReceivedCallback h;

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void enableAESEncryption(byte[] bArr) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public BluetoothDeviceWrapper getBluetoothDeviceWrapper() {
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) throws UnsupportedLayerException {
        if (!(iTransmissionLayer instanceof GattTransmissionLayer)) {
            throw new UnsupportedLayerException();
        }
        this.f1386d = (GattTransmissionLayer) iTransmissionLayer;
        this.e = this.f1386d.getBluetoothDevice();
        this.f1386d.setNotificationCallback(this.g).with(new b(this));
        this.f1386d.enableNotifications(this.g).enqueue();
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isCommandSupported(byte b2) {
        return false;
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isRequiredServiceSupported(BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(BleMeshManager.MESH_PROVISIONING_UUID);
        if (service == null) {
            return false;
        }
        this.f = service.getCharacteristic(f1383a);
        this.g = service.getCharacteristic(f1384b);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.f;
        boolean z = bluetoothGattCharacteristic != null && (bluetoothGattCharacteristic.getProperties() & 4) > 0;
        BluetoothGattCharacteristic bluetoothGattCharacteristic2 = this.f;
        return (bluetoothGattCharacteristic2 == null || bluetoothGattCharacteristic2 == null || !z) ? false : true;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onBluetoothConnectionStateChanged(int i) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onDeviceReady() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public AISCommand sendCommandWithCallback(byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void sendRawDataWithCallback(byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        GattTransmissionLayer gattTransmissionLayer = this.f1386d;
        if (gattTransmissionLayer == null || gattTransmissionLayer.getConnectionState() != LayerState.CONNECTED) {
            failCallback.onRequestFailed(this.e, -1);
        } else {
            this.f.setWriteType(1);
            this.f1386d.writeCharacteristic(this.f, bArr).with(dataSentCallback).fail(failCallback).enqueue();
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void setBluetoothDeviceWrapper(BluetoothDeviceWrapper bluetoothDeviceWrapper) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void start() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void stop() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void uninstall() {
    }

    public void a(DataReceivedCallback dataReceivedCallback) {
        this.h = dataReceivedCallback;
    }
}
