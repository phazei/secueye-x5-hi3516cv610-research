package a.a.a.a.a.a;

import aisble.BleManager;
import android.bluetooth.BluetoothGatt;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedLayerException;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;
import com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: GattTransmissionLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends BleManager.BleManagerGattCallback {
    public final /* synthetic */ GattTransmissionLayer this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(GattTransmissionLayer gattTransmissionLayer) {
        super();
        this.this$0 = gattTransmissionLayer;
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void initialize() {
        super.initialize();
        this.this$0.requestMtu(255).enqueue();
        for (BluetoothGattPlugin bluetoothGattPlugin : this.this$0.mInstalledPlugins) {
            try {
                LogUtils.d(GattTransmissionLayer.TAG, String.format("Init plugin %s on transmission layer: %s", bluetoothGattPlugin, this));
                bluetoothGattPlugin.init(this.this$0);
            } catch (UnsupportedLayerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
        this.this$0.mSupported = true;
        for (BluetoothGattPlugin bluetoothGattPlugin : this.this$0.mInstalledPlugins) {
            boolean zIsRequiredServiceSupported = bluetoothGattPlugin.isRequiredServiceSupported(bluetoothGatt);
            boolean z = false;
            if (!zIsRequiredServiceSupported) {
                LogUtils.d(GattTransmissionLayer.TAG, String.format("Plugin %s not support transmission layer: %s ", bluetoothGattPlugin, this));
            }
            GattTransmissionLayer gattTransmissionLayer = this.this$0;
            if (gattTransmissionLayer.mSupported && zIsRequiredServiceSupported) {
                z = true;
            }
            gattTransmissionLayer.mSupported = z;
        }
        if (this.this$0.mSupported) {
            GattTransmissionLayer gattTransmissionLayer2 = this.this$0;
            gattTransmissionLayer2.notifyConnectionStateChanged(gattTransmissionLayer2.getBluetoothDevice(), 2);
        }
        return this.this$0.mSupported;
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void onDeviceDisconnected() {
    }
}
