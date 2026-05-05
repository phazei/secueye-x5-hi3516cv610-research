package com.alibaba.ailabs.iot.aisbase.channel;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICastEventListener;
import com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedPluginTypeException;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;

/* JADX INFO: loaded from: classes.dex */
public interface ITransmissionLayer {
    public static final int MSG_ID_MAX = 16;

    void connectDevice(BluetoothDevice bluetoothDevice, IActionListener<BluetoothDevice> iActionListener);

    void disconnectDevice(IActionListener<BluetoothDevice> iActionListener);

    void dynamicInstallPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException;

    void forwardCommand(byte b2, byte[] bArr);

    void forwardInnerCastEvent(String str);

    void forwardStream(byte[] bArr);

    byte generateMessageID();

    BluetoothDevice getBluetoothDevice();

    CommandResponseDispatcher getCommandResponseDispatcher(String str);

    LayerState getConnectionState();

    TransmissionLayer getLayer();

    int getMtu();

    ITransmissionLayerCallback getTransmissionLayerCallback();

    void installPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException;

    void onDestroy();

    void registerCastEventListener(ICastEventListener iCastEventListener);

    void setTransmissionLayerCallback(ITransmissionLayerCallback iTransmissionLayerCallback);

    void uninstallPlugin(IPlugin iPlugin);

    void unregisterCastEventListener(ICastEventListener iCastEventListener);
}
