package com.aliyun.alink.business.devicecenter.channel.ble;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.iot.breeze.BreezeScanRecord;
import com.aliyun.iot.breeze.api.Config;
import com.aliyun.iot.breeze.api.ConnectConfig;
import com.aliyun.iot.breeze.api.Factory;
import com.aliyun.iot.breeze.api.IBreeze;
import com.aliyun.iot.breeze.biz.BreezeHelper;
import com.aliyun.iot.breeze.mix.ConnectionCallback;
import com.aliyun.iot.breeze.mix.LeScanCallBack;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import com.aliyun.iot.breeze.mix.MixBleDescriptor;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import com.google.android.exoplayer2.DefaultLoadControl;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ILopBleChannelImpl implements IBleInterface {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f3428a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public MixBleDelegate f3429b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IBleInterface.IBleScanCallback f3430c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ConnectConfig f3431d = null;
    public ConcurrentHashMap<IBleInterface.IBleConnectionCallback, ConnectionCallback> e = new ConcurrentHashMap<>();
    public ConcurrentHashMap<MixBleDevice, MixBleDevice.OnMessageCallback> f = new ConcurrentHashMap<>();
    public LeScanCallBack g = new LeScanCallBack() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.1
        public void onLeScan(MixBleDescriptor mixBleDescriptor, int i, byte[] bArr) {
            if (mixBleDescriptor == null || mixBleDescriptor.getBreezeDeviceDescriptor() == null || mixBleDescriptor.getBreezeDeviceDescriptor().getBreezeScanRecord() == null) {
                ALog.d("ILopBleChannelImpl", "invalid ilop ble device.");
                return;
            }
            try {
                BreezeScanRecord breezeScanRecord = mixBleDescriptor.getBreezeDeviceDescriptor().getBreezeScanRecord();
                StringBuilder sb = new StringBuilder();
                sb.append("breezeScanRecord=");
                sb.append(breezeScanRecord);
                ALog.d("ILopBleChannelImpl", sb.toString());
                int subType = breezeScanRecord.getSubType();
                if (subType == 2 || subType == 3 || subType == 4 || subType == 12) {
                    String strValueOf = String.valueOf(breezeScanRecord.getModelId());
                    String macWithColon = breezeScanRecord.getMacWithColon();
                    boolean zIsBindFlagSet = breezeScanRecord.isBindFlagSet();
                    if (TextUtils.isEmpty(strValueOf)) {
                        ALog.d("ILopBleChannelImpl", "invalid ilop ble device, productId = null.");
                        return;
                    }
                    if (TextUtils.isEmpty(macWithColon)) {
                        ALog.d("ILopBleChannelImpl", "invalid ilop ble device, mac = null.");
                        return;
                    }
                    DeviceInfo deviceInfo = new DeviceInfo();
                    deviceInfo.productId = strValueOf;
                    deviceInfo.mac = macWithColon;
                    if (subType == 2) {
                        deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_2;
                    } else if (subType == 3) {
                        deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3;
                        try {
                            if (zIsBindFlagSet) {
                                ALog.i("ILopBleChannelImpl", "find a combo 3 device with bind flag.");
                                deviceInfo.setExtraDeviceInfo(AlinkConstants.EXTRA_DEVICE_INFO_KEY_RESET_FLAG, false);
                            } else {
                                ALog.i("ILopBleChannelImpl", "find a combo 3 device with no bind flag. need reset.");
                                deviceInfo.setExtraDeviceInfo(AlinkConstants.EXTRA_DEVICE_INFO_KEY_RESET_FLAG, true);
                            }
                        } catch (Exception unused) {
                            ALog.w("ILopBleChannelImpl", "to support combo offline reset, you need update breeze version to at least 1.4.1.");
                        }
                    } else if (ProvisionConfigCenter.getInstance().handleBleSubType4Device() && subType == 4) {
                        deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_4;
                    } else if (subType != 12) {
                        ALog.d("ILopBleChannelImpl", "ignore subType = 4 combo device.");
                        return;
                    } else {
                        deviceInfo.devType = AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_12;
                        deviceInfo.setExtraDeviceInfo(AlinkConstants.EXTRA_DEVICE_INFO_KEY_BLE_FMASK, Integer.valueOf(breezeScanRecord.getFmsk()));
                    }
                    if (ILopBleChannelImpl.this.f3430c != null) {
                        ILopBleChannelImpl.this.f3430c.onBLEDeviceFound(deviceInfo);
                    }
                }
            } catch (Exception e) {
                ALog.w("ILopBleChannelImpl", "onLeScan parse exception=" + e);
            }
        }
    };
    public ConnectionCallback h = null;

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean channelEncrypt(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        MixBleDevice mixBleDevice;
        if (iBleChannelDevice == null || !(iBleChannelDevice.getChannelDevice() instanceof MixBleDevice) || (mixBleDevice = (MixBleDevice) iBleChannelDevice.getChannelDevice()) == null || mixBleDevice.getDescriptor() == null || mixBleDevice.getDescriptor().getScanRecord() == null) {
            return true;
        }
        return mixBleDevice.getDescriptor().getBreezeScanRecord().supportEncrypt();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void connect(String str, final IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        if (iBleConnectionCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            iBleConnectionCallback.onChannelStateChanged(null, IBleInterface.BleChannelState.NONE);
            return;
        }
        ConnectionCallback connectionCallback = new ConnectionCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.2
            public void onConnectionStateChange(final MixBleDevice mixBleDevice, int i, int i2) {
                IBleInterface.IBleConnectionCallback iBleConnectionCallback2 = iBleConnectionCallback;
                if (iBleConnectionCallback2 != null) {
                    iBleConnectionCallback2.onChannelStateChanged(new IBleInterface.IBleChannelDevice() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.2.1
                        @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleChannelDevice
                        public Object getChannelDevice() {
                            return mixBleDevice;
                        }
                    }, ILopBleChannelImpl.this.a(i));
                }
            }
        };
        synchronized (this.e) {
            this.e.put(iBleConnectionCallback, connectionCallback);
            StringBuilder sb = new StringBuilder();
            sb.append("connectionCallbackHashMap connectionCallback=");
            sb.append(this.e.get(iBleConnectionCallback));
            ALog.d("ILopBleChannelImpl", sb.toString());
        }
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mBreeze.open connect: mac = [");
            sb2.append(str);
            sb2.append("], bleConnectionCallback = [");
            sb2.append(iBleConnectionCallback);
            sb2.append("]");
            ALog.d("ILopBleChannelImpl", sb2.toString());
            if (this.f3429b != null) {
                this.f3429b.open(false, str, connectionCallback, this.f3431d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void deinit() {
        ALog.d("ILopBleChannelImpl", " deinit ");
        ConcurrentHashMap<MixBleDevice, MixBleDevice.OnMessageCallback> concurrentHashMap = this.f;
        if (concurrentHashMap != null) {
            concurrentHashMap.clear();
        }
        ConcurrentHashMap<IBleInterface.IBleConnectionCallback, ConnectionCallback> concurrentHashMap2 = this.e;
        if (concurrentHashMap2 != null) {
            concurrentHashMap2.clear();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void disconnect(String str, IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        if (iBleConnectionCallback == null || TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (this.e) {
            this.h = this.e.get(iBleConnectionCallback);
            this.e.remove(iBleConnectionCallback);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("mBreeze.close disconnect: mac = [");
            sb.append(str);
            sb.append("], ");
            sb.append("bleConnectionCallback = [");
            sb.append(iBleConnectionCallback);
            sb.append("],");
            sb.append("connectionCallback = [");
            sb.append(this.h);
            sb.append("]");
            ALog.d("ILopBleChannelImpl", sb.toString());
            if (this.f3429b != null) {
                this.f3429b.close(str, this.h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void getDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice, final IBleInterface.IBleDeviceInfoCallback iBleDeviceInfoCallback) {
        if (iBleChannelDevice == null) {
            if (iBleDeviceInfoCallback != null) {
                iBleDeviceInfoCallback.onError(new DCError(String.valueOf(DCErrorCode.PF_SDK_ERROR), "channel device is null."));
            }
        } else {
            if (iBleChannelDevice.getChannelDevice() instanceof MixBleDevice) {
                BreezeHelper.getDeviceInfo((MixBleDevice) iBleChannelDevice.getChannelDevice(), new BreezeHelper.IDeviceInfoCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.5
                    public void onDeviceInfo(BreezeHelper.DeviceInfo deviceInfo) {
                        ALog.d("ILopBleChannelImpl", "onDeviceInfo() called from breeze, deviceInfo = [" + deviceInfo + "]");
                        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName)) {
                            if (iBleDeviceInfoCallback != null) {
                                iBleDeviceInfoCallback.onError(new DCError(String.valueOf(DCErrorCode.PF_DEVICE_FAIL), "breeze returned device info is null or invalid." + deviceInfo));
                                return;
                            }
                            return;
                        }
                        IBleInterface.BleDeviceInfo bleDeviceInfo = new IBleInterface.BleDeviceInfo();
                        bleDeviceInfo.productKey = deviceInfo.productKey;
                        bleDeviceInfo.deviceName = deviceInfo.deviceName;
                        bleDeviceInfo.sign = deviceInfo.sign;
                        bleDeviceInfo.version = deviceInfo.version;
                        bleDeviceInfo.random = deviceInfo.random;
                        IBleInterface.IBleDeviceInfoCallback iBleDeviceInfoCallback2 = iBleDeviceInfoCallback;
                        if (iBleDeviceInfoCallback2 != null) {
                            iBleDeviceInfoCallback2.onDeviceInfo(bleDeviceInfo);
                        }
                    }
                });
                return;
            }
            if (iBleDeviceInfoCallback != null) {
                iBleDeviceInfoCallback.onError(new DCError(String.valueOf(DCErrorCode.PF_SDK_ERROR), "channel device type error. " + iBleChannelDevice.getChannelDevice()));
            }
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public String getType() {
        return "ilop";
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void init(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is null.");
        }
        if (this.f3428a) {
            ALog.d("ILopBleChannelImpl", "BreezeClient has been inited.");
            return;
        }
        IBreeze iBreezeCreateBreeze = Factory.createBreeze(context);
        iBreezeCreateBreeze.configure(new Config.Builder().debug(true).log(true).logLevel(1).build());
        this.f3429b = MixBleDelegate.getInstance();
        this.f3429b.init(context, iBreezeCreateBreeze);
        this.f3428a = true;
        this.f3431d = new ConnectConfig();
        ConnectConfig connectConfig = this.f3431d;
        connectConfig.connectRetryCount = 20;
        connectConfig.connectTimeout = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean isSupport() {
        return DCEnvHelper.hasBreeze();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean needGetDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        if (iBleChannelDevice != null && (iBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
            MixBleDevice mixBleDevice = (MixBleDevice) iBleChannelDevice.getChannelDevice();
            if (mixBleDevice.getDescriptor() != null && mixBleDevice.getDescriptor().getBreezeScanRecord() != null) {
                return mixBleDevice.getDescriptor().getBreezeScanRecord().getSubType() != 12;
            }
            ALog.w("ILopBleChannelImpl", "device instance of MixBleDevice, but getDescriptor() or getDescriptor().getBreezeScanRecord() is null, " + mixBleDevice.getDescriptor());
        }
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void registerOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, final IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        if (iBleChannelDevice != null && (iBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
            MixBleDevice.OnMessageCallback onMessageCallback = new MixBleDevice.OnMessageCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.4
                public void onMessage(byte[] bArr) {
                    IBleInterface.IBleReceiverCallback iBleReceiverCallback2 = iBleReceiverCallback;
                    if (iBleReceiverCallback2 != null) {
                        iBleReceiverCallback2.onDataReceived(bArr);
                    }
                }
            };
            synchronized (this.f) {
                this.f.put((MixBleDevice) iBleChannelDevice.getChannelDevice(), onMessageCallback);
            }
            MixBleDevice mixBleDevice = (MixBleDevice) iBleChannelDevice.getChannelDevice();
            ALog.d("ILopBleChannelImpl", "registerOnReceivedListener() addOnMessageCallback: channelDevice = [" + iBleChannelDevice + "], receiverCallback = [" + iBleReceiverCallback + "]");
            mixBleDevice.addOnMessageCallback(onMessageCallback);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void sendMessage(IBleInterface.IBleChannelDevice iBleChannelDevice, int i, byte[] bArr, final IBleInterface.IBleActionCallback iBleActionCallback) {
        if (iBleChannelDevice != null && bArr != null && bArr.length >= 1 && (iBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
            ALog.d("ILopBleChannelImpl", "sendMessage() called with: channelDevice = [" + iBleChannelDevice + "], messageType = [" + i + "], data = [" + bArr + "], callback = [" + iBleActionCallback + "]");
            MixBleDevice mixBleDevice = (MixBleDevice) iBleChannelDevice.getChannelDevice();
            mixBleDevice.sendProvisionMessage(mixBleDevice.newMessage(i, bArr), new MixBleDevice.ResponseCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.ILopBleChannelImpl.3
                public void onResponse(int i2, byte[] bArr2) {
                    IBleInterface.IBleActionCallback iBleActionCallback2 = iBleActionCallback;
                    if (iBleActionCallback2 != null) {
                        iBleActionCallback2.onResponse(i2, bArr2);
                    }
                }
            });
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void setConfigParams(DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean startScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        if (iBleScanCallback == null) {
            ALog.w("ILopBleChannelImpl", "ilop ble scan start failed, bleScanCallback is null.");
            return false;
        }
        this.f3430c = iBleScanCallback;
        try {
            if (this.f3429b != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("mBreeze.startLeScan: bleScan = [");
                sb.append(iBleScanCallback);
                sb.append("]");
                ALog.d("ILopBleChannelImpl", sb.toString());
                boolean zStartLeScan = this.f3429b.startLeScan(this.g);
                if (this.f3430c != null) {
                    if (zStartLeScan) {
                        this.f3430c.onStartScan();
                    } else {
                        this.f3430c.onStartScan();
                    }
                }
                return zStartLeScan;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IBleInterface.IBleScanCallback iBleScanCallback2 = this.f3430c;
        if (iBleScanCallback2 != null) {
            iBleScanCallback2.onStopScan();
        }
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void stopScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        this.f3430c = null;
        try {
            if (this.f3429b != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("mBreeze.stopLeScan: bleScan = [");
                sb.append(iBleScanCallback);
                sb.append("]");
                ALog.d("ILopBleChannelImpl", sb.toString());
                this.f3429b.stopLeScan(this.g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void unregisterOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        MixBleDevice.OnMessageCallback onMessageCallback;
        if (iBleChannelDevice != null && (iBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
            MixBleDevice mixBleDevice = (MixBleDevice) iBleChannelDevice.getChannelDevice();
            synchronized (this.f) {
                onMessageCallback = this.f.get(mixBleDevice);
            }
            ALog.d("ILopBleChannelImpl", "unregisterOnReceivedListener() removeOnMessageCallback: channelDevice = [" + iBleChannelDevice + "], receiverCallback = [" + iBleReceiverCallback + "]");
            mixBleDevice.removeOnMessageCallback(onMessageCallback);
        }
    }

    public final IBleInterface.BleChannelState a(int i) {
        if (i == 0) {
            return IBleInterface.BleChannelState.DISCONNECTED;
        }
        if (i == 2) {
            return IBleInterface.BleChannelState.AUTH_SUCCESSFUL;
        }
        if (i == 3) {
            return IBleInterface.BleChannelState.DISCONNECTING;
        }
        if (i == 1) {
            return IBleInterface.BleChannelState.CONNECTING;
        }
        return IBleInterface.BleChannelState.NONE;
    }
}
