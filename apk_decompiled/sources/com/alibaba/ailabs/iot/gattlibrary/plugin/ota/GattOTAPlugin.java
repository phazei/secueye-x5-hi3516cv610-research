package com.alibaba.ailabs.iot.gattlibrary.plugin.ota;

import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: loaded from: classes.dex */
public class GattOTAPlugin extends AISBluetoothGattPluginBase implements IOTAPlugin {
    public static final String TAG = "GattOTAPlugin";
    public CommandResponseDispatcher mCommandResponseDispatcher;
    public BluetoothGattCharacteristic mOATResponseCharacteristic;
    public BluetoothGattCharacteristic mOATWriteCharacteristic;
    public OTAPluginProxy mProxy;

    private AISCommand sendCommand(byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        return sendCommandWithCallback(this.mOATWriteCharacteristic, 1, b2, bArr, dataSentCallback, failCallback);
    }

    private void sendOTARequestCommand(byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b3) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.sendOTARequestCommand(b2, bArr, bArr2, bArr3, b3);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void checkNewVersion(String str, String str2, String str3, IActionListener<DeviceVersionInfo> iActionListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.checkNewVersion(str, str2, str3, iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void enableReportProgress(boolean z) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.enableReportOtaProgress(z);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.PluginBase
    public String[] getChannelUUIDs() {
        return new String[]{Constants.AIS_OTA_RES.toString()};
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.PluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) {
        super.init(iTransmissionLayer);
        this.mCommandResponseDispatcher = this.mGattTransmissionLayer.getCommandResponseDispatcher(Constants.AIS_OTA_RES.toString());
        this.mGattTransmissionLayer.setNotificationCallback(this.mOATResponseCharacteristic).with(this.mCommandResponseDispatcher);
        this.mGattTransmissionLayer.enableNotifications(this.mOATResponseCharacteristic).enqueue();
        if (this.mProxy == null) {
            this.mProxy = new OTAPluginProxy(this.mCommandResponseDispatcher, this.mTransmissionLayer, this);
        }
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isCommandSupported(byte b2) {
        return super.isCommandSupported(b2);
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin
    public boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(Constants.AIS_PRIMARY_SERVICE);
        if (service == null) {
            return false;
        }
        this.mOATWriteCharacteristic = service.getCharacteristic(Constants.AIS_OTA_OUT);
        this.mOATResponseCharacteristic = service.getCharacteristic(Constants.AIS_OTA_RES);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mOATWriteCharacteristic;
        boolean z = bluetoothGattCharacteristic != null && (bluetoothGattCharacteristic.getProperties() & 4) > 0;
        return (this.mOATWriteCharacteristic == null || this.mOATResponseCharacteristic == null || !z) ? false : true;
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void onBluetoothConnectionStateChanged(int i) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy != null) {
            oTAPluginProxy.onBluetoothConnectionStateChanged(i);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public AISCommand sendCommandWithCallback(byte b2, byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        return sendCommandWithCallback(this.mOATWriteCharacteristic, 1, b2, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void sendGetFirmwareVersionCommand(IActionListener iActionListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.getFirmwareVersionCommand(iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void sendGetFirmwareVersionCommandV1(IActionListener iActionListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.getFirmwareVersionCommandV1(iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void sendOTAFinishAndRequestVerifyCommand() {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.sendOTAFinishAndRequestVerifyCommand();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void sendRawDataWithCallback(byte[] bArr, DataSentCallback dataSentCallback, FailCallback failCallback) {
        sendRawDataWithCallback(this.mOATWriteCharacteristic, 1, bArr, dataSentCallback, failCallback);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void setSendCompleteFrameWithNoAckFlag(boolean z) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.setSendCompleteFrameWithNoAckFlag(z);
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void start() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void startDownloadFirmware(Context context, DeviceVersionInfo deviceVersionInfo, String str, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.startDownloadFirmware(context, deviceVersionInfo, str, iFirmwareDownloadListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void startOTA(byte[] bArr, byte[] bArr2, byte[] bArr3, byte b2, byte[] bArr4, byte b3, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.startOTA(bArr, bArr2, bArr3, b2, bArr4, b3, iOTAActionListener);
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void stop() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void stopDownloadFirmware() {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.stopDownloadFirmware();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void stopOTA() {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.stopOTA();
    }

    @Override // com.alibaba.ailabs.iot.gattlibrary.plugin.AISBluetoothGattPluginBase, com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void uninstall() {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin
    public void startOTA(String str, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        OTAPluginProxy oTAPluginProxy = this.mProxy;
        if (oTAPluginProxy == null) {
            return;
        }
        oTAPluginProxy.startOTA(str, iOTAActionListener);
    }
}
