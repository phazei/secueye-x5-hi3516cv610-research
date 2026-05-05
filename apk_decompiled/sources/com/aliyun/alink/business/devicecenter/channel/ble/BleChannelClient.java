package com.aliyun.alink.business.devicecenter.channel.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class BleChannelClient implements IBleInterface {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3423a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3424b;
    public IBleInterface bleStrategy;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AtomicBoolean f3425c = new AtomicBoolean(false);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BroadcastReceiver f3426d = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.channel.ble.BleChannelClient.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            byte b2;
            if (intent == null || intent.getAction() == null) {
                return;
            }
            try {
                String action = intent.getAction();
                b2 = -1;
                if (action.hashCode() == -1530327060 && action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                    b2 = 0;
                }
            } catch (Exception e) {
                ALog.w("BleChannelClient", "onReceive ble exception:" + e);
            }
            if (b2 != 0) {
                return;
            }
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0);
            BleChannelClient.this.f3424b = intExtra;
            switch (intExtra) {
                case 10:
                    ALog.d("BleChannelClient", "BluetoothAdapter.STATE_OFF");
                    break;
                case 11:
                    ALog.d("BleChannelClient", "BluetoothAdapter.STATE_TURNING_ON");
                    break;
                case 12:
                    ALog.d("BleChannelClient", "BluetoothAdapter.STATE_ON");
                    break;
                case 13:
                    ALog.d("BleChannelClient", "BluetoothAdapter.STATE_TURNING_OFF");
                    break;
            }
        }
    };

    public BleChannelClient(Context context) {
        this.bleStrategy = null;
        this.f3423a = null;
        this.f3424b = 10;
        if (context == null) {
            return;
        }
        this.f3423a = context.getApplicationContext();
        if (DCEnvHelper.isTgEnv()) {
            this.bleStrategy = new TgBleChannelImpl();
        } else {
            this.bleStrategy = new ILopBleChannelImpl();
        }
        this.f3424b = PermissionCheckerUtils.isBleAvailable(context) ? 12 : 10;
    }

    public final void b(Context context) {
        if (context == null) {
            return;
        }
        context.unregisterReceiver(this.f3426d);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean channelEncrypt(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        return this.bleStrategy.channelEncrypt(iBleChannelDevice);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void connect(String str, IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        this.bleStrategy.connect(str, iBleConnectionCallback);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void deinit() {
        if (this.f3425c.compareAndSet(true, false)) {
            b(this.f3423a);
        }
        this.bleStrategy.deinit();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void disconnect(String str, IBleInterface.IBleConnectionCallback iBleConnectionCallback) {
        this.bleStrategy.disconnect(str, iBleConnectionCallback);
    }

    public int getBluetoothState() {
        return this.f3424b;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void getDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleDeviceInfoCallback iBleDeviceInfoCallback) {
        this.bleStrategy.getDeviceName(iBleChannelDevice, iBleDeviceInfoCallback);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public String getType() {
        return this.bleStrategy.getType();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void init(Context context) {
        if (context == null) {
            return;
        }
        this.bleStrategy.init(context);
        a(context);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean isSupport() {
        return this.bleStrategy.isSupport();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean needGetDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        return this.bleStrategy.needGetDeviceName(iBleChannelDevice);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void registerOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        this.bleStrategy.registerOnReceivedListener(iBleChannelDevice, iBleReceiverCallback);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void sendMessage(IBleInterface.IBleChannelDevice iBleChannelDevice, int i, byte[] bArr, IBleInterface.IBleActionCallback iBleActionCallback) {
        this.bleStrategy.sendMessage(iBleChannelDevice, i, bArr, iBleActionCallback);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void setConfigParams(DCConfigParams dCConfigParams) {
        this.bleStrategy.setConfigParams(dCConfigParams);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public boolean startScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        return this.bleStrategy.startScan(iBleScanCallback);
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void stopScan(IBleInterface.IBleScanCallback iBleScanCallback) {
        this.bleStrategy.stopScan(iBleScanCallback);
        deinit();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface
    public void unregisterOnReceivedListener(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.IBleReceiverCallback iBleReceiverCallback) {
        this.bleStrategy.unregisterOnReceivedListener(iBleChannelDevice, iBleReceiverCallback);
    }

    public final void a(Context context) {
        if (context == null) {
            return;
        }
        this.f3425c.set(true);
        ALog.d("BleChannelClient", "registerBleStatusReceiver() called with: context = [" + context + "]");
        context.registerReceiver(this.f3426d, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
    }
}
