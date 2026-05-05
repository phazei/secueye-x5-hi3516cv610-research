package com.alibaba.ailabs.iot.gattlibrary.plugin;

import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandSendListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.IBasicPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;

/* JADX INFO: loaded from: classes.dex */
public class GattCommandPlugin extends AISBluetoothGattPluginBase implements IBasicPlugin {
    public static final String TAG = "GattCommandPlugin";
    public ICommandActionListener mCommandListener;
    public BluetoothGattCharacteristic mCommandResponseCharacteristic;
    public BasicProxy mProxy;
    public BluetoothGattCharacteristic mReadCharacteristic;
    public BluetoothGattCharacteristic mSendCommandCharacteristic;

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.PluginBase
    public String[] getChannelUUIDs() {
        return new String[]{Constants.AIS_COMMAND_RES.toString()};
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.basic.IBasicPlugin
    public void getManufacturerSpecificData(IActionListener<byte[]> iActionListener) {
        this.mProxy.getManufacturerSpecificData(iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.PluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) {
        super.init(iTransmissionLayer);
        CommandResponseDispatcher commandResponseDispatcher = this.mGattTransmissionLayer.getCommandResponseDispatcher(Constants.AIS_COMMAND_RES.toString());
        this.mGattTransmissionLayer.setNotificationCallback(this.mCommandResponseCharacteristic).with(commandResponseDispatcher);
        this.mGattTransmissionLayer.enableNotifications(this.mCommandResponseCharacteristic).enqueue();
        this.mProxy = new BasicProxy(commandResponseDispatcher, this.mTransmissionLayer, this);
        ICommandActionListener iCommandActionListener = this.mCommandListener;
        if (iCommandActionListener != null) {
            this.mProxy.setCommandActionListener(iCommandActionListener);
        }
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(Constants.AIS_PRIMARY_SERVICE);
        if (service == null) {
            return false;
        }
        this.mReadCharacteristic = service.getCharacteristic(Constants.AIS_DATA_IN);
        this.mCommandResponseCharacteristic = service.getCharacteristic(Constants.AIS_COMMAND_RES);
        this.mSendCommandCharacteristic = service.getCharacteristic(Constants.AIS_COMMAND_OUT);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mSendCommandCharacteristic;
        boolean z = bluetoothGattCharacteristic != null && (bluetoothGattCharacteristic.getProperties() & 8) > 0;
        return (this.mCommandResponseCharacteristic == null || this.mSendCommandCharacteristic == null || this.mReadCharacteristic == null || !z) ? false : true;
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onBluetoothConnectionStateChanged(int i) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.basic.IBasicPlugin
    public void sendCommand(byte[] bArr, ICommandSendListener iCommandSendListener) {
        this.mProxy.sendCommand(bArr, iCommandSendListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public AISCommand sendCommandWithCallback(byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        return sendCommandWithCallback(this.mSendCommandCharacteristic, 2, b2, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void sendRawDataWithCallback(byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        sendRawDataWithCallback(this.mSendCommandCharacteristic, 2, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.basic.IBasicPlugin
    public void setCommandActionListener(ICommandActionListener iCommandActionListener) {
        BasicProxy basicProxy = this.mProxy;
        if (basicProxy == null) {
            this.mCommandListener = iCommandActionListener;
        } else {
            basicProxy.setCommandActionListener(iCommandActionListener);
        }
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void start() {
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void stop() {
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void uninstall() {
    }
}
