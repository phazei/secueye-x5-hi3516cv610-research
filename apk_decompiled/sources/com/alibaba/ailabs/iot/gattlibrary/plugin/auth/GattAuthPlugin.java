package com.alibaba.ailabs.iot.gattlibrary.plugin.auth;

import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.IAuthPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase;

/* JADX INFO: loaded from: classes.dex */
public class GattAuthPlugin extends AISBluetoothGattPluginBase implements IAuthPlugin {
    public IActionListener<byte[]> mAuthListener;
    public BluetoothGattCharacteristic mCommandResponseCharacteristic;
    public AuthPluginBusinessProxy mProxy;
    public BluetoothGattCharacteristic mSendCommandCharacteristic;
    public byte[] mProductID = null;
    public String mAddress = null;
    public boolean mBLEDevice = false;

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.PluginBase
    public String[] getChannelUUIDs() {
        return new String[]{Constants.AIS_COMMAND_RES.toString()};
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.PluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) {
        super.init(iTransmissionLayer);
        this.mGattTransmissionLayer.setNotificationCallback(this.mCommandResponseCharacteristic).with(iTransmissionLayer.getCommandResponseDispatcher(Constants.AIS_COMMAND_RES.toString()));
        this.mGattTransmissionLayer.enableIndications(this.mCommandResponseCharacteristic).enqueue();
        this.mGattTransmissionLayer.enableNotifications(this.mCommandResponseCharacteristic).enqueue();
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(Constants.AIS_PRIMARY_SERVICE);
        if (service == null) {
            return false;
        }
        this.mSendCommandCharacteristic = service.getCharacteristic(Constants.AIS_COMMAND_OUT);
        this.mCommandResponseCharacteristic = service.getCharacteristic(Constants.AIS_COMMAND_RES);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mSendCommandCharacteristic;
        boolean z = bluetoothGattCharacteristic != null && (bluetoothGattCharacteristic.getProperties() & 8) > 0;
        return (this.mSendCommandCharacteristic == null || this.mCommandResponseCharacteristic == null || !z) ? false : true;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public AISCommand sendCommandWithCallback(byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        return sendCommandWithCallback(this.mSendCommandCharacteristic, 2, b2, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void sendRawDataWithCallback(byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        sendRawDataWithCallback(this.mSendCommandCharacteristic, 2, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.auth.IAuthPlugin
    public void setAuthParams(byte[] bArr, String str, IActionListener<byte[]> iActionListener) {
        this.mProductID = bArr;
        this.mAddress = str;
        if (iActionListener == null) {
            iActionListener = new IActionListener<byte[]>() { // from class: com.alibaba.ailabs.iot.gattlibrary.plugin.auth.GattAuthPlugin.1
                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onFailure(int i, String str2) {
                }

                @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                public void onSuccess(byte[] bArr2) {
                }
            };
        }
        this.mAuthListener = iActionListener;
    }

    public void setIsBLEDevice(boolean z) {
        this.mBLEDevice = z;
        AuthPluginBusinessProxy authPluginBusinessProxy = this.mProxy;
        if (authPluginBusinessProxy != null) {
            authPluginBusinessProxy.setIsBLEDevice(z);
        }
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void start() {
        this.mProxy = new AuthPluginBusinessProxy(this.mGattTransmissionLayer.getCommandResponseDispatcher(Constants.AIS_COMMAND_RES.toString()), this.mGattTransmissionLayer);
        this.mProxy.setIsBLEDevice(this.mBLEDevice);
        this.mProxy.startAuth(this, this.mProductID, this.mAddress, this.mAuthListener);
    }
}
