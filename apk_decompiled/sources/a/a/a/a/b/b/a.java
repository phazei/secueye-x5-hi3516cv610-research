package a.a.a.a.b.b;

import aisble.BleManager;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.RequiresApi;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: BleMeshManager.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends BleManager<BleMeshManagerCallbacks>.BleManagerGattCallback {
    public final /* synthetic */ BleMeshManager this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(BleMeshManager bleMeshManager) {
        super();
        this.this$0 = bleMeshManager;
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void initialize() {
        super.initialize();
        if (this.this$0.mNeedRequestMtu) {
            this.this$0.requestMtu(517).enqueue();
        }
        if (this.this$0.isProvisioningComplete) {
            BleMeshManager bleMeshManager = this.this$0;
            if (bleMeshManager.mWriteReadType != BleMeshManager.WriteReadType.WRITE) {
                bleMeshManager.internalEnableNotification(bleMeshManager.mMeshProxyDataOutCharacteristic, this.this$0.mEnableNotificationRetryMaxCount);
                return;
            }
            return;
        }
        BleMeshManager bleMeshManager2 = this.this$0;
        bleMeshManager2.internalEnableNotification(bleMeshManager2.mMeshProvisioningDataOutCharacteristic, this.this$0.mEnableNotificationRetryMaxCount);
        if (this.this$0.mMeshProxyDataOutCharacteristic != null) {
            BleMeshManager bleMeshManager3 = this.this$0;
            bleMeshManager3.internalEnableNotification(bleMeshManager3.mMeshProxyDataOutCharacteristic, this.this$0.mEnableNotificationRetryMaxCount);
        }
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    @RequiresApi(api = 18)
    public boolean isRequiredServiceSupported(BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(BleMeshManager.MESH_PROVISIONING_UUID);
        BluetoothGattService service2 = bluetoothGatt.getService(BleMeshManager.MESH_PROXY_UUID);
        a.a.a.a.b.m.a.a(this.this$0.TAG, "isProvisioningComplete: " + this.this$0.isProvisioningComplete + " meshService: " + service);
        if (service != null && !this.this$0.isProvisioningComplete) {
            this.this$0.isProvisioningComplete = false;
            this.this$0.mMeshProvisioningDataInCharacteristic = service.getCharacteristic(BleMeshManager.MESH_PROVISIONING_DATA_IN);
            this.this$0.mMeshProvisioningDataOutCharacteristic = service.getCharacteristic(BleMeshManager.MESH_PROVISIONING_DATA_OUT);
            boolean z = this.this$0.mMeshProvisioningDataInCharacteristic != null && (this.this$0.mMeshProvisioningDataInCharacteristic.getProperties() & 4) > 0;
            if (service2 != null) {
                this.this$0.mMeshProxyDataInCharacteristic = service2.getCharacteristic(BleMeshManager.MESH_PROXY_DATA_IN);
                this.this$0.mMeshProxyDataOutCharacteristic = service2.getCharacteristic(BleMeshManager.MESH_PROXY_DATA_OUT);
                if (this.this$0.mMeshProxyDataOutCharacteristic != null) {
                    a.a.a.a.b.m.a.c(this.this$0.TAG, "pre register proxy service: true");
                    this.this$0.mIsPreRegisterProxyService = true;
                }
            }
            return (this.this$0.mMeshProvisioningDataInCharacteristic == null || this.this$0.mMeshProvisioningDataInCharacteristic == null || !z) ? false : true;
        }
        BluetoothGattService service3 = bluetoothGatt.getService(BleMeshManager.MESH_PROXY_UUID);
        a.a.a.a.b.m.a.a(this.this$0.TAG, "isProvisioningComplete: " + this.this$0.isProvisioningComplete + " meshService: " + service3);
        if (service3 == null || !this.this$0.isProvisioningComplete) {
            return false;
        }
        this.this$0.isProvisioningComplete = true;
        this.this$0.mMeshProxyDataInCharacteristic = service3.getCharacteristic(BleMeshManager.MESH_PROXY_DATA_IN);
        this.this$0.mMeshProxyDataOutCharacteristic = service3.getCharacteristic(BleMeshManager.MESH_PROXY_DATA_OUT);
        boolean z2 = this.this$0.mMeshProxyDataInCharacteristic != null && (this.this$0.mMeshProxyDataInCharacteristic.getProperties() & 4) > 0;
        return (this.this$0.mMeshProxyDataInCharacteristic == null || this.this$0.mMeshProxyDataOutCharacteristic == null || !z2) ? false : true;
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    @RequiresApi(api = 18)
    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (value == null) {
            return;
        }
        a.a.a.a.b.m.a.a(this.this$0.TAG, "Data written: " + MeshParserUtils.bytesToHex(value, true));
        ((BleMeshManagerCallbacks) this.this$0.mCallbacks).onDataSent(bluetoothGatt.getDevice(), this.this$0.mtuSize, value);
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void onDeviceDisconnected() {
        a.a.a.a.b.m.a.a(this.this$0.TAG, "onDeviceDisconnected called, isConnected: " + this.this$0.isConnected());
        this.this$0.mNeedRequestMtu = true;
        if (this.this$0.isConnected() || this.this$0.isProvisioningComplete) {
            return;
        }
        this.this$0.isProvisioningComplete = false;
        this.this$0.mMeshProvisioningDataInCharacteristic = null;
        this.this$0.mMeshProvisioningDataOutCharacteristic = null;
        this.this$0.mMeshProxyDataInCharacteristic = null;
        this.this$0.mMeshProxyDataOutCharacteristic = null;
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void onDeviceReady() {
        super.onDeviceReady();
    }

    @Override // aisble.BleManager.BleManagerGattCallback
    public void onMtuChanged(BluetoothGatt bluetoothGatt, int i) {
        super.onMtuChanged(bluetoothGatt, i);
        this.this$0.mtuSize = i - 3;
        this.this$0.mNeedRequestMtu = false;
    }
}
