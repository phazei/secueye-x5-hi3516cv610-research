package com.aliyun.alink.business.devicecenter.channel.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice;
import com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager;
import com.alibaba.ailabs.iot.bluetoothlesdk.IGenieBLEDeviceCallback;
import com.alibaba.ailabs.iot.bluetoothlesdk.auxiliary.AuxiliaryProvisionManager;
import com.alibaba.ailabs.iot.bluetoothlesdk.interfaces.AuxiliaryDeviceStatusListener;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.ailabs.iot.mesh.UnprovisionedMeshDeviceScanStrategy;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.EnrolleeTgBleDeviceCacheModel;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.ut.LinkUtHelper;
import com.aliyun.alink.business.devicecenter.ut.UtLinkInfo;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class TgBleChannelImpl implements IBleInterface {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3443a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3444b = 600000;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f3445c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public IBleInterface.IBleScanCallback f3446d = null;
    public ILeScanCallback e = null;
    public DCAlibabaConfigParams f = null;
    public ConcurrentHashMap<GenieBLEDevice, AuxiliaryDeviceStatusListener> g = new ConcurrentHashMap<>();

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean channelEncrypt(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void connect(String str, final IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        ALog.d("TgBleChannelImpl", "connect() called with: mac = [" + str + "], connectionCallback = [" + iBleConnectionCallback + "]");
        if (TextUtils.isEmpty(str)) {
            if (iBleConnectionCallback != null) {
                iBleConnectionCallback.onChannelStateChanged(null, IBleInterface.BleChannelState.NONE);
                return;
            }
            return;
        }
        try {
            final GenieBLEDevice genieBLEDeviceA = a(str);
            if (genieBLEDeviceA == null) {
                return;
            }
            IGenieBLEDeviceCallback iGenieBLEDeviceCallback = new IGenieBLEDeviceCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.2
                @Override // com.alibaba.ailabs.iot.aisbase.callback.IBluetoothDeviceWrapperCallback
                public void onChannelStateChanged(LayerState layerState) {
                    IBleInterface.IBleConnectionCallback iBleConnectionCallback2 = iBleConnectionCallback;
                    if (iBleConnectionCallback2 == null || layerState == null) {
                        return;
                    }
                    iBleConnectionCallback2.onChannelStateChanged(null, IBleInterface.BleChannelState.valueOf(layerState.name()));
                }

                @Override // com.alibaba.ailabs.iot.bluetoothlesdk.IGenieBLEDeviceCallback
                public void onlineStateChanged(boolean z) {
                }
            };
            StringBuilder sb = new StringBuilder();
            sb.append("setGenieBLEDeviceCallback = ");
            sb.append(iGenieBLEDeviceCallback);
            ALog.d("TgBleChannelImpl", sb.toString());
            genieBLEDeviceA.setGenieBLEDeviceCallback(iGenieBLEDeviceCallback);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("do connect, GenieBLEDevice=");
            sb2.append(genieBLEDeviceA);
            ALog.d("TgBleChannelImpl", sb2.toString());
            genieBLEDeviceA.connect(this.f3443a, new IActionListener<BluetoothDevice>() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.3
                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onFailure(int i, String str2) {
                    IBleInterface.IBleConnectionCallback iBleConnectionCallback2 = iBleConnectionCallback;
                    if (iBleConnectionCallback2 != null) {
                        iBleConnectionCallback2.onChannelStateChanged(null, IBleInterface.BleChannelState.DISCONNECTED);
                    }
                    if (TgBleChannelImpl.this.f != null) {
                        if (i == -5) {
                            LinkUtHelper.connectEvent(LinkUtHelper.CONNECT_TIMEOUT, new UtLinkInfo(TgBleChannelImpl.this.f.userId, TgBleChannelImpl.this.f.productKey, TgBleChannelImpl.this.f.linkType.getName()));
                            return;
                        }
                        UtLinkInfo utLinkInfo = new UtLinkInfo(TgBleChannelImpl.this.f.userId, TgBleChannelImpl.this.f.productKey, TgBleChannelImpl.this.f.linkType.getName());
                        utLinkInfo.setErrorCode(String.valueOf(i));
                        LinkUtHelper.connectEvent(LinkUtHelper.CONNECT_FAIL, utLinkInfo);
                    }
                }

                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onSuccess(BluetoothDevice bluetoothDevice) {
                    ALog.d("TgBleChannelImpl", "onSuccess() called with: bluetoothDevice = [" + bluetoothDevice + "]");
                    IBleInterface.IBleConnectionCallback iBleConnectionCallback2 = iBleConnectionCallback;
                    if (iBleConnectionCallback2 != null) {
                        iBleConnectionCallback2.onChannelStateChanged(new IBleInterface.IBleChannelDevice() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.3.1
                            @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleChannelDevice
                            public Object getChannelDevice() {
                                return genieBLEDeviceA;
                            }
                        }, IBleInterface.BleChannelState.CONNECTED);
                    }
                }
            });
        } catch (Exception e) {
            ALog.w("TgBleChannelImpl", "GenieBLEDevice connect exception " + e);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void deinit() {
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void disconnect(String str, IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        ALog.d("TgBleChannelImpl", "disconnect() called with: mac = [" + str + "], connectionCallback = [" + iBleConnectionCallback + "]");
        if (TextUtils.isEmpty(str)) {
            if (iBleConnectionCallback != null) {
                iBleConnectionCallback.onChannelStateChanged(null, IBleInterface.BleChannelState.NONE);
                return;
            }
            return;
        }
        try {
            GenieBLEDevice genieBLEDeviceA = a(str);
            if (genieBLEDeviceA == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("setGenieBLEDeviceCallback = ");
            sb.append((Object) null);
            ALog.d("TgBleChannelImpl", sb.toString());
            genieBLEDeviceA.setGenieBLEDeviceCallback(null);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("do disconnect, GenieBLEDevice=");
            sb2.append(genieBLEDeviceA);
            ALog.d("TgBleChannelImpl", sb2.toString());
            genieBLEDeviceA.disconnect(new IActionListener<BluetoothDevice>() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.4
                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onFailure(int i, String str2) {
                }

                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onSuccess(BluetoothDevice bluetoothDevice) {
                }
            });
        } catch (Exception e) {
            ALog.w("TgBleChannelImpl", "GenieBLEDevice disconnect exception " + e);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void getDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleDeviceInfoCallback iBleDeviceInfoCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public String getType() {
        return "genie";
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void init(Context context) {
        if (context == null) {
            return;
        }
        this.f3443a = context.getApplicationContext();
        if (AppEnv.IS_GENIE_ENV) {
            UnprovisionedMeshDeviceScanStrategy.getInstance().register();
            GenieBLEDeviceManager.getInstance();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean isSupport() {
        return DCEnvHelper.hasTGBleScanAbilityAB();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean needGetDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void registerOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, final IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        if (iBleChannelDevice == null) {
            return;
        }
        if (!(iBleChannelDevice.getChannelDevice() instanceof GenieBLEDevice)) {
            ALog.d("TgBleChannelImpl", "not GenieBLEDevice type.");
            return;
        }
        GenieBLEDevice genieBLEDevice = (GenieBLEDevice) iBleChannelDevice.getChannelDevice();
        AuxiliaryDeviceStatusListener auxiliaryDeviceStatusListener = new AuxiliaryDeviceStatusListener() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.5
            @Override // com.alibaba.ailabs.iot.bluetoothlesdk.interfaces.AuxiliaryDeviceStatusListener
            public void onDeviceStatusChange(byte[] bArr) {
                ALog.d("TgBleChannelImpl", "onReceiveWifiProvisionData() called with: bytes = [" + bArr + "]");
                IBleInterface.IBleReceiverCallback iBleReceiverCallback2 = iBleReceiverCallback;
                if (iBleReceiverCallback2 != null) {
                    iBleReceiverCallback2.onDataReceived(bArr);
                }
            }
        };
        synchronized (this.g) {
            this.g.put(genieBLEDevice, auxiliaryDeviceStatusListener);
        }
        String address = genieBLEDevice.getAddress();
        ALog.d("TgBleChannelImpl", "registerOnReceivedListener() called with: channelDeviceMac = [" + address + "], receiverCallback = [" + iBleReceiverCallback + "]");
        AuxiliaryProvisionManager.getInstance().registerAuxiliaryDeviceStatusListener(address, auxiliaryDeviceStatusListener);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void sendMessage(IBleInterface.IBleChannelDevice iBleChannelDevice, int i, byte[] bArr, final IBleInterface.IBleActionCallback iBleActionCallback) {
        ALog.d("TgBleChannelImpl", "sendMessage() called with: channelDevice = [" + iBleChannelDevice + "], messageType = [" + i + "], data = [" + bArr + "], callback = [" + iBleActionCallback + "]");
        if (iBleChannelDevice == null || bArr == null || bArr.length < 1) {
            return;
        }
        if (!(iBleChannelDevice.getChannelDevice() instanceof GenieBLEDevice)) {
            ALog.d("TgBleChannelImpl", "not GenieBLEDevice type.");
            return;
        }
        ALog.d("TgBleChannelImpl", "AuxiliaryProvisionManager do sendMessage.");
        AuxiliaryProvisionManager.getInstance().sendMessage(this.f3443a, (GenieBLEDevice) iBleChannelDevice.getChannelDevice(), bArr, new IActionListener<byte[]>() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.6
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i2, String str) {
                IBleInterface.IBleActionCallback iBleActionCallback2 = iBleActionCallback;
                if (iBleActionCallback2 != null) {
                    iBleActionCallback2.onResponse(i2, str == null ? null : str.getBytes());
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onSuccess(byte[] bArr2) {
                IBleInterface.IBleActionCallback iBleActionCallback2 = iBleActionCallback;
                if (iBleActionCallback2 != null) {
                    iBleActionCallback2.onResponse(0, bArr2);
                }
            }
        });
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void setConfigParams(DCConfigParams dCConfigParams) {
        if (dCConfigParams == null || !(dCConfigParams instanceof DCAlibabaConfigParams)) {
            return;
        }
        this.f = (DCAlibabaConfigParams) dCConfigParams;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean startScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        if (iBleScanCallback == null) {
            ALog.w("TgBleChannelImpl", "tg ble scan start failed, bleScanCallback is null.");
            return false;
        }
        this.f3446d = iBleScanCallback;
        this.e = new ILeScanCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.TgBleChannelImpl.1
            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
                ALog.d("TgBleChannelImpl", "onAliBLEDeviceFound with callback=" + bluetoothDeviceWrapper + ", hashCode=" + TgBleChannelImpl.this.hashCode());
                if (bluetoothDeviceWrapper == null || bluetoothDeviceSubtype == null || bluetoothDeviceWrapper.getAddress() == null || bluetoothDeviceWrapper.getAisManufactureDataADV() == null || bluetoothDeviceWrapper.getAisManufactureDataADV().getPId() == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("bluetoothDeviceSubtype: ");
                    sb.append(bluetoothDeviceSubtype);
                    sb.append(", ");
                    sb.append("bluetoothDeviceWrapper: ");
                    sb.append(bluetoothDeviceWrapper);
                    sb.append(", ");
                    if (bluetoothDeviceWrapper != null) {
                        sb.append("bluetoothDeviceWrapper.getAddress: ");
                        sb.append(bluetoothDeviceWrapper.getAddress());
                        sb.append(", ");
                        sb.append("bluetoothDeviceWrapper.getAisManufactureDataADV: ");
                        sb.append(bluetoothDeviceWrapper.getAisManufactureDataADV());
                        sb.append(", ");
                        if (bluetoothDeviceWrapper.getAisManufactureDataADV() != null) {
                            sb.append("bluetoothDeviceWrapper.getAisManufactureDataADV().getPId: ");
                            sb.append(Arrays.toString(bluetoothDeviceWrapper.getAisManufactureDataADV().getPId()));
                            sb.append(", ");
                        }
                    }
                    ALog.d("TgBleChannelImpl", "invalid tg ble device. " + sb.toString());
                    return;
                }
                try {
                    int bluetoothSubtype = bluetoothDeviceWrapper.getAisManufactureDataADV().getBluetoothSubtype() & 255;
                    if (bluetoothSubtype >= 8 && bluetoothSubtype <= 12) {
                        String lowerCase = bluetoothDeviceWrapper.getAddress().toLowerCase();
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.productId = bluetoothDeviceWrapper.getAisManufactureDataADV().getPidStr();
                        deviceInfo.mac = lowerCase;
                        if (bluetoothSubtype == 12) {
                            deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_12;
                        } else if (bluetoothSubtype == 11) {
                            deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_11;
                        } else if (bluetoothSubtype == 8) {
                            deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_8;
                        } else if (bluetoothSubtype == 9) {
                            deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_9;
                        } else {
                            deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_10;
                        }
                        deviceInfo.setExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE, bluetoothDeviceWrapper);
                        CacheCenter.getInstance().updateCache(CacheType.BLE_DISCOVERED_DEVICE, new EnrolleeTgBleDeviceCacheModel(bluetoothDeviceWrapper, bluetoothDeviceSubtype));
                        if (TgBleChannelImpl.this.f3446d != null) {
                            TgBleChannelImpl.this.f3446d.onBLEDeviceFound(deviceInfo);
                            return;
                        }
                        return;
                    }
                    ALog.d("TgBleChannelImpl", "unsupport tg subType device.");
                } catch (Exception e) {
                    ALog.w("TgBleChannelImpl", "onAliBLEDeviceFound parse exception=" + e);
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStartScan() {
                ALog.d("TgBleChannelImpl", "onStartScan ");
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStopScan() {
            }
        };
        ALog.d("TgBleChannelImpl", "BLEScannerProxy.getInstance().startLeScan: bleTgScanCallback = [" + this.e + "], scanTimeout=" + this.f3444b);
        BLEScannerProxy.getInstance().startLeScan(this.f3443a, this.f3444b, this.f3445c, UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH | GenieBLEDevice.GENIE_BLE, this.e);
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void stopScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        ALog.d("TgBleChannelImpl", "stopScan() called with: bleScan = [" + iBleScanCallback + "]");
        BLEScannerProxy.getInstance().stopScan(this.e);
        this.f3446d = null;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void unregisterOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        AuxiliaryDeviceStatusListener auxiliaryDeviceStatusListener;
        if (iBleChannelDevice == null) {
            return;
        }
        if (!(iBleChannelDevice.getChannelDevice() instanceof GenieBLEDevice)) {
            ALog.d("TgBleChannelImpl", "not GenieBLEDevice type.");
            return;
        }
        GenieBLEDevice genieBLEDevice = (GenieBLEDevice) iBleChannelDevice.getChannelDevice();
        synchronized (this.g) {
            auxiliaryDeviceStatusListener = this.g.get(genieBLEDevice);
        }
        String address = genieBLEDevice.getAddress();
        ALog.d("TgBleChannelImpl", "unregisterOnReceivedListener() called with: channelDeviceMac = [" + address + "], receiverCallback = [" + iBleReceiverCallback + "]");
        AuxiliaryProvisionManager.getInstance().unregisterAuxiliaryDeviceStatusListener(address, auxiliaryDeviceStatusListener);
    }

    public final GenieBLEDevice a(String str) {
        List cachedModel = CacheCenter.getInstance().getCachedModel(CacheType.BLE_DISCOVERED_DEVICE, str);
        if (cachedModel == null || cachedModel.isEmpty() || !(cachedModel.get(0) instanceof EnrolleeTgBleDeviceCacheModel)) {
            return null;
        }
        EnrolleeTgBleDeviceCacheModel enrolleeTgBleDeviceCacheModel = (EnrolleeTgBleDeviceCacheModel) cachedModel.get(0);
        if (!(enrolleeTgBleDeviceCacheModel.getBluetoothDeviceWrapper() instanceof GenieBLEDevice)) {
            return null;
        }
        GenieBLEDevice genieBLEDevice = (GenieBLEDevice) enrolleeTgBleDeviceCacheModel.getBluetoothDeviceWrapper();
        ALog.d("TgBleChannelImpl", "get cached genie ble device " + genieBLEDevice);
        return genieBLEDevice;
    }
}
