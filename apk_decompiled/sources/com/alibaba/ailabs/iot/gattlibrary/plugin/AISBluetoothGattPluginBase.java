package com.alibaba.ailabs.iot.gattlibrary.plugin;

import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedLayerException;
import com.alibaba.ailabs.iot.aisbase.plugin.PluginBase;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class AISBluetoothGattPluginBase extends PluginBase implements BluetoothGattPlugin {
    public static final String TAG = "AISBluetoothGattPluginBase";
    public BluetoothDevice mBluetoothDevice;
    public GattTransmissionLayer mGattTransmissionLayer;

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.PluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) {
        super.init(iTransmissionLayer);
        if (!(iTransmissionLayer instanceof GattTransmissionLayer)) {
            throw new UnsupportedLayerException();
        }
        this.mGattTransmissionLayer = (GattTransmissionLayer) iTransmissionLayer;
        this.mBluetoothDevice = this.mGattTransmissionLayer.getBluetoothDevice();
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isCommandSupported(byte b2) {
        return false;
    }

    public boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
        return true;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onBluetoothConnectionStateChanged(int i) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onDeviceReady() {
    }

    public AISCommand sendCommand(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte b2, byte[] bArr) {
        LogUtils.v(TAG, "Sending data : " + ConvertUtils.bytes2HexString(bArr));
        List<AISCommand> listSplitDataToCommands = splitDataToCommands(b2, bArr, true);
        Iterator<AISCommand> it = listSplitDataToCommands.iterator();
        while (it.hasNext()) {
            byte[] bytes = it.next().getBytes();
            LogUtils.v(TAG, "Sending data: " + ConvertUtils.bytes2HexString(bytes));
            bluetoothGattCharacteristic.setWriteType(2);
            this.mGattTransmissionLayer.writeCharacteristic(bluetoothGattCharacteristic, bytes).enqueue();
        }
        return listSplitDataToCommands.get(0);
    }

    public AISCommand sendCommandWithCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i, byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        GattTransmissionLayer gattTransmissionLayer = this.mGattTransmissionLayer;
        if (gattTransmissionLayer == null || gattTransmissionLayer.getConnectionState() != LayerState.CONNECTED) {
            failCallback.onRequestFailed(this.mBluetoothDevice, -1);
            return null;
        }
        List<AISCommand> listSplitDataToCommands = splitDataToCommands(b2, bArr, true);
        Iterator<AISCommand> it = listSplitDataToCommands.iterator();
        while (it.hasNext()) {
            byte[] bytes = it.next().getBytes();
            LogUtils.v(TAG, "Sending command data with callback: " + ConvertUtils.bytes2HexString(bytes));
            bluetoothGattCharacteristic.setWriteType(i);
            this.mGattTransmissionLayer.writeCharacteristic(bluetoothGattCharacteristic, bytes).with(dataSentCallback).fail(failCallback).enqueue();
        }
        return listSplitDataToCommands.get(0);
    }

    public void sendRawData(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        LogUtils.v(TAG, "Send raw data: " + ConvertUtils.bytes2HexString(bArr));
        bluetoothGattCharacteristic.setWriteType(2);
        this.mGattTransmissionLayer.writeCharacteristic(bluetoothGattCharacteristic, bArr).enqueue();
    }

    public void sendRawDataWithCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        GattTransmissionLayer gattTransmissionLayer = this.mGattTransmissionLayer;
        if (gattTransmissionLayer == null || gattTransmissionLayer.getConnectionState() != LayerState.CONNECTED) {
            failCallback.onRequestFailed(this.mBluetoothDevice, -1);
            return;
        }
        LogUtils.v(TAG, "Send raw data with callback: " + ConvertUtils.bytes2HexString(bArr));
        bluetoothGattCharacteristic.setWriteType(i);
        this.mGattTransmissionLayer.writeCharacteristic(bluetoothGattCharacteristic, bArr).with(dataSentCallback).fail(failCallback).enqueue();
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
}
