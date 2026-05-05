package tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import bean.DeviceInfoModel;
import config.AppConfig;
import event.EventType;
import event.MyEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes4.dex */
public class BleClient {
    public static final String NOTIFY_UUID = "00002a90-0000-1000-8000-00805f9b34fb";
    public static final String READ_DEDSCRIPTION_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private static final String TAG = "BleClient";
    public static final String WRITE_SERVICE_UUID = "0000181c-0000-1000-8000-00805f9b34fb";
    public static final String WRITE_UUID = "00002a8a-0000-1000-8000-00805f9b34fb";
    public static BleClient bleClient;
    private UUID UUID_NOTIFY_CHARA;
    private UUID UUID_WRITE_CHARA;
    private UUID UUID_WRITE_SERVICES;
    private BluetoothAdapter bluetoothAdapter;
    private BleConnectListener connectListener;
    private BluetoothGattService mBtGattService;
    private BluetoothGattCharacteristic mNotifyBtGattCharacteristic;
    private BleReceiveListener receiveListener;
    private BleScanListener scanListener;
    private BluetoothGattCharacteristic writeCharacteristic;
    private int TIME = 50;
    private int MTU_SIZE = 220;
    private List<BluetoothGatt> DEVICE_LIST = new ArrayList();
    private ScanCallback scanCallback = new ScanCallback() { // from class: tools.BleClient.1
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            if (scanResult == null || scanResult.getDevice() == null || scanResult.getDevice().getName() == null || scanResult.getDevice().getAddress() == null || BleClient.this.scanListener == null) {
                return;
            }
            BleClient.this.scanListener.onScanning(scanResult);
        }
    };

    public interface BleConnectListener {
        void onFailConnect();

        void onStartConnect();

        void onSuccessConnect();
    }

    public interface BleReceiveListener {
        void onDeviceSendData(String str, String str2, String str3);
    }

    public interface BleScanListener {
        void onScanning(ScanResult scanResult);
    }

    public static BleClient getInstence() {
        if (bleClient == null) {
            bleClient = new BleClient();
        }
        return bleClient;
    }

    @SuppressLint({"MissingPermission"})
    public boolean isConnected() {
        BluetoothAdapter bluetoothAdapter;
        return (this.DEVICE_LIST == null || (bluetoothAdapter = this.bluetoothAdapter) == null || !bluetoothAdapter.isEnabled() || this.DEVICE_LIST.size() == 0) ? false : true;
    }

    @SuppressLint({"MissingPermission"})
    public void initBLE(Activity activity2) {
        this.bluetoothAdapter = ((BluetoothManager) activity2.getSystemService("bluetooth")).getAdapter();
        if (this.bluetoothAdapter.isEnabled()) {
            return;
        }
        this.bluetoothAdapter.enable();
    }

    public List<BluetoothGatt> getAllDevices() {
        return this.DEVICE_LIST;
    }

    @SuppressLint({"MissingPermission"})
    public void scanBLE(Activity activity2) {
        if (this.bluetoothAdapter == null) {
            this.bluetoothAdapter = ((BluetoothManager) activity2.getSystemService("bluetooth")).getAdapter();
        }
        BluetoothLeScanner bluetoothLeScanner = this.bluetoothAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.startScan(this.scanCallback);
        }
    }

    public void getData() {
        new Thread(new Runnable() { // from class: tools.BleClient.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    byte[] bArr = new byte[1024];
                    Log.e("消息内容2", "" + new String(bArr, 0, BleClient.this.bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("serverSocket", UUID.fromString(BleClient.NOTIFY_UUID)).accept().getInputStream().read(bArr)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = 26)
    public void connectBLE(Activity activity2, String str) {
        if (this.bluetoothAdapter == null) {
            Log.e(TAG, "蓝牙适配器为空");
        }
        BluetoothDevice remoteDevice = this.bluetoothAdapter.getRemoteDevice(str);
        if (remoteDevice == null) {
            Log.e(TAG, "找不到该蓝牙设备");
            return;
        }
        BleConnectListener bleConnectListener = this.connectListener;
        if (bleConnectListener != null) {
            bleConnectListener.onStartConnect();
        }
        remoteDevice.connectGatt(activity2, false, new BluetoothGattCallback() { // from class: tools.BleClient.3
            @Override // android.bluetooth.BluetoothGattCallback
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
                super.onServicesDiscovered(bluetoothGatt, i);
                if (Build.VERSION.SDK_INT >= 21) {
                    bluetoothGatt.requestMtu(512);
                }
                if (bluetoothGatt.getServices() == null || bluetoothGatt.getServices().size() == 0) {
                    Log.e(BleClient.TAG, "service == null");
                    return;
                }
                Iterator<BluetoothGattService> it = bluetoothGatt.getServices().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BluetoothGattService next = it.next();
                    Log.e(BleClient.TAG, "service UUID:" + next.getUuid());
                    if (next.getUuid().equals(UUID.fromString(BleClient.WRITE_SERVICE_UUID))) {
                        BleClient.this.getServiceAndCharacteristic(bluetoothGatt, BleClient.WRITE_SERVICE_UUID, BleClient.WRITE_UUID);
                        BleClient.this.MTU_SIZE = 220;
                        AppConfig.DEVICES_MODEL = AppConfig.MODEL_1;
                        EventBus.getDefault().post(new MyEvent(EventType.DEVICE_CHAGE, null));
                        break;
                    }
                    Log.e(BleClient.TAG, "连接到未知蓝牙");
                }
                for (BluetoothGattService bluetoothGattService : bluetoothGatt.getServices()) {
                    for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                        int properties = bluetoothGattCharacteristic.getProperties();
                        Log.e(BleClient.TAG, "charaProp=" + properties);
                        if (((properties & 8) > 0 || (properties & 4) > 0) && UUID.fromString(BleClient.WRITE_UUID).equals(bluetoothGattCharacteristic.getUuid())) {
                            BleClient.this.UUID_WRITE_CHARA = bluetoothGattCharacteristic.getUuid();
                            Log.e(BleClient.TAG, "获取写入特征UUID=" + bluetoothGattCharacteristic.getUuid());
                            BleClient.this.UUID_WRITE_SERVICES = bluetoothGattService.getUuid();
                            Log.e(BleClient.TAG, "获取写入服务UUID=" + bluetoothGattService.getUuid());
                        }
                        if ((properties & 16) > 0 && UUID.fromString(BleClient.NOTIFY_UUID).equals(bluetoothGattCharacteristic.getUuid())) {
                            BleClient.this.UUID_NOTIFY_CHARA = bluetoothGattCharacteristic.getUuid();
                            Log.e(BleClient.TAG, "监听特征UUID=" + bluetoothGattCharacteristic.getUuid());
                        }
                    }
                }
                BleClient bleClient2 = BleClient.this;
                bleClient2.mBtGattService = bluetoothGatt.getService(bleClient2.UUID_WRITE_SERVICES);
                BleClient bleClient3 = BleClient.this;
                bleClient3.writeCharacteristic = bleClient3.mBtGattService.getCharacteristic(BleClient.this.UUID_WRITE_CHARA);
                BleClient bleClient4 = BleClient.this;
                bleClient4.mNotifyBtGattCharacteristic = bleClient4.mBtGattService.getCharacteristic(BleClient.this.UUID_NOTIFY_CHARA);
                bluetoothGatt.setCharacteristicNotification(BleClient.this.mNotifyBtGattCharacteristic, true);
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
                super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
                if (i == 0) {
                    Log.e(BleClient.TAG, "onDescriptorWrite: 开启监听成功，可以向设备写入命令了");
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
                super.onMtuChanged(bluetoothGatt, i, i2);
                if (i2 == 0) {
                    Log.e(BleClient.TAG, "MTU change success = " + i);
                    return;
                }
                Log.e(BleClient.TAG, "MTU change fail!");
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
                super.onConnectionStateChange(bluetoothGatt, i, i2);
                if (i2 == 2) {
                    bluetoothGatt.requestConnectionPriority(1);
                    bluetoothGatt.discoverServices();
                    BleClient.this.DEVICE_LIST.add(bluetoothGatt);
                    EventBus.getDefault().post(new MyEvent(EventType.CONNECTED, bluetoothGatt.getDevice().getAddress()));
                    if (BleClient.this.connectListener != null) {
                        BleClient.this.connectListener.onSuccessConnect();
                        return;
                    }
                    return;
                }
                if (i2 == 0) {
                    if (bluetoothGatt != null) {
                        bluetoothGatt.close();
                    }
                    for (int i3 = 0; i3 < BleClient.this.DEVICE_LIST.size(); i3++) {
                        if (((BluetoothGatt) BleClient.this.DEVICE_LIST.get(i3)).getDevice().getAddress().equals(bluetoothGatt.getDevice().getAddress())) {
                            DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
                            deviceInfoModel.deviceName = bluetoothGatt.getDevice().getName();
                            deviceInfoModel.deviceMAC = bluetoothGatt.getDevice().getAddress();
                            EventBus.getDefault().post(new MyEvent(EventType.DISCONNECT, deviceInfoModel));
                            BleClient.this.DEVICE_LIST.remove(i3);
                        }
                    }
                    if (BleClient.this.connectListener != null) {
                        BleClient.this.connectListener.onFailConnect();
                    }
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                BleClient.this.prepareBroadcastDataNotify(bluetoothGatt, bluetoothGattCharacteristic);
                try {
                    String str2 = new String(bluetoothGattCharacteristic.getValue(), "UTF-8");
                    if (BleClient.this.receiveListener != null) {
                        BleClient.this.receiveListener.onDeviceSendData(bluetoothGatt.getDevice().getName(), bluetoothGatt.getDevice().getAddress(), str2);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, 2, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public void prepareBroadcastDataNotify(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor descriptor;
        if ((bluetoothGattCharacteristic.getProperties() | 16) <= 0 || (descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(READ_DEDSCRIPTION_UUID))) == null) {
            return;
        }
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getServiceAndCharacteristic(BluetoothGatt bluetoothGatt, String str, String str2) {
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(str));
        if (service == null) {
            Log.e(TAG, "蓝牙服务为空");
            return;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(str2));
        if (characteristic == null) {
            Log.e(TAG, "蓝牙写BluetoothGattCharacteristic为空");
        } else {
            this.writeCharacteristic = characteristic;
        }
    }

    public static String byte2HexStr(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            sb.append(hexString);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public void cancleBleScan() {
        BluetoothLeScanner bluetoothLeScanner;
        BluetoothAdapter bluetoothAdapter = this.bluetoothAdapter;
        if (bluetoothAdapter == null || (bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner()) == null) {
            return;
        }
        bluetoothLeScanner.stopScan(this.scanCallback);
    }

    public void disconnect(BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt == null || bluetoothGatt.getDevice() == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }

    public void disconnectAddress(String str) {
        for (int i = 0; i < this.DEVICE_LIST.size(); i++) {
            if (str.equals(this.DEVICE_LIST.get(i).getDevice().getAddress())) {
                disconnect(this.DEVICE_LIST.get(i));
            }
        }
    }

    public void disconnectAll() {
        Iterator<BluetoothGatt> it = this.DEVICE_LIST.iterator();
        while (it.hasNext()) {
            disconnect(it.next());
        }
    }

    public void sendOrder2(byte[] bArr) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.writeCharacteristic;
        if (bluetoothGattCharacteristic != null) {
            bluetoothGattCharacteristic.setValue(bArr);
            for (BluetoothGatt bluetoothGatt : this.DEVICE_LIST) {
                String str = WRITE_SERVICE_UUID;
                String str2 = WRITE_UUID;
                if (AppConfig.DEVICES_MODEL == AppConfig.MODEL_1) {
                    str = WRITE_SERVICE_UUID;
                    str2 = WRITE_UUID;
                }
                if (bluetoothGatt != null && bluetoothGatt.getService(UUID.fromString(str)) != null && bluetoothGatt.getService(UUID.fromString(str)).getCharacteristic(UUID.fromString(str2)) != null) {
                    BluetoothGattCharacteristic characteristic = bluetoothGatt.getService(UUID.fromString(str)).getCharacteristic(UUID.fromString(str2));
                    characteristic.setValue(bArr);
                    bluetoothGatt.writeCharacteristic(characteristic);
                    Log.e(TAG, new String(bArr, StandardCharsets.UTF_8));
                }
            }
        }
    }

    public void sendOrder5(byte[] bArr) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.writeCharacteristic;
        if (bluetoothGattCharacteristic != null) {
            bluetoothGattCharacteristic.setValue(bArr);
            for (BluetoothGatt bluetoothGatt : this.DEVICE_LIST) {
                String str = WRITE_SERVICE_UUID;
                String str2 = WRITE_UUID;
                if (AppConfig.DEVICES_MODEL == AppConfig.MODEL_1) {
                    str = WRITE_SERVICE_UUID;
                    str2 = WRITE_UUID;
                }
                if (bluetoothGatt != null && bluetoothGatt.getService(UUID.fromString(str)) != null && bluetoothGatt.getService(UUID.fromString(str)).getCharacteristic(UUID.fromString(str2)) != null) {
                    BluetoothGattCharacteristic characteristic = bluetoothGatt.getService(UUID.fromString(str)).getCharacteristic(UUID.fromString(str2));
                    characteristic.setValue(bArr);
                    bluetoothGatt.writeCharacteristic(characteristic);
                }
            }
            Log.e("XXXX", byte2HexStr(bArr));
        }
    }

    public void sendOrder3(Context context, byte[] bArr) {
        int length;
        int iCeil = (int) Math.ceil(bArr.length / this.MTU_SIZE);
        for (int i = 0; i < iCeil; i++) {
            if (i < iCeil - 1) {
                length = this.MTU_SIZE;
            } else {
                length = bArr.length % this.MTU_SIZE;
            }
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, this.MTU_SIZE * i, bArr2, 0, length);
            Log.e("sendBleComd", byte2HexStr(bArr2));
            sendOrder2(bArr2);
            try {
                Thread.sleep(this.TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendOrder3(byte[] bArr) {
        int length;
        int iCeil = (int) Math.ceil(bArr.length / this.MTU_SIZE);
        for (int i = 0; i < iCeil; i++) {
            if (i < iCeil - 1) {
                length = this.MTU_SIZE;
            } else {
                length = bArr.length % this.MTU_SIZE;
            }
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, this.MTU_SIZE * i, bArr2, 0, length);
            Log.e("sendBleComd", byte2HexStr(bArr2));
            sendOrder2(bArr2);
            try {
                Thread.sleep(this.TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendOrder(final Activity activity2, final byte[] bArr, final byte[] bArr2) {
        if (this.writeCharacteristic == null || bArr == null) {
            return;
        }
        new Thread(new Runnable() { // from class: tools.BleClient.4
            @Override // java.lang.Runnable
            public void run() {
                int length = bArr.length / BleClient.this.MTU_SIZE;
                int length2 = bArr.length % BleClient.this.MTU_SIZE;
                for (int i = 0; i < length; i++) {
                    final byte[] bArr3 = new byte[BleClient.this.MTU_SIZE];
                    System.arraycopy(bArr, BleClient.this.MTU_SIZE * i, bArr3, 0, BleClient.this.MTU_SIZE);
                    activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleClient.this.sendOrder2(bArr3);
                        }
                    });
                    try {
                        Thread.sleep(BleClient.this.TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final byte[] bArr4 = new byte[length2];
                System.arraycopy(bArr, length * BleClient.this.MTU_SIZE, bArr4, 0, length2);
                activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BleClient.this.sendOrder2(bArr4);
                    }
                });
                byte[] bArr5 = bArr2;
                if (bArr5 == null || bArr5.length <= 0) {
                    return;
                }
                try {
                    Thread.sleep(BleClient.this.TIME);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.4.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BleClient.this.sendOrder2(bArr2);
                    }
                });
            }
        }).start();
    }

    public void sendOrder4(Activity activity2, byte[] bArr, final byte[] bArr2) {
        if (this.writeCharacteristic == null || bArr == null) {
            return;
        }
        int length = bArr.length;
        int i = this.MTU_SIZE;
        int i2 = length / i;
        int length2 = bArr.length % i;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = this.MTU_SIZE;
            final byte[] bArr3 = new byte[i4];
            System.arraycopy(bArr, i3 * i4, bArr3, 0, i4);
            activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.5
                @Override // java.lang.Runnable
                public void run() {
                    BleClient.this.sendOrder2(bArr3);
                }
            });
            try {
                Thread.sleep(this.TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final byte[] bArr4 = new byte[length2];
        System.arraycopy(bArr, i2 * this.MTU_SIZE, bArr4, 0, length2);
        activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.6
            @Override // java.lang.Runnable
            public void run() {
                BleClient.this.sendOrder2(bArr4);
            }
        });
        if (bArr2 == null || bArr2.length <= 0) {
            return;
        }
        try {
            Thread.sleep(this.TIME);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        activity2.runOnUiThread(new Runnable() { // from class: tools.BleClient.7
            @Override // java.lang.Runnable
            public void run() {
                BleClient.this.sendOrder2(bArr2);
            }
        });
    }

    public void setBleScanListener(BleScanListener bleScanListener) {
        this.scanListener = bleScanListener;
    }

    public void setBleReceiveListener(BleReceiveListener bleReceiveListener) {
        this.receiveListener = bleReceiveListener;
    }

    public void setBleConnectListener(BleConnectListener bleConnectListener) {
        this.connectListener = bleConnectListener;
    }
}
