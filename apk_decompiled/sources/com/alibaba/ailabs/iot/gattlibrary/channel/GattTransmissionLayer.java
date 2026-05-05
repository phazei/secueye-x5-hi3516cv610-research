package com.alibaba.ailabs.iot.gattlibrary.channel;

import a.a.a.a.a.a.a;
import a.a.a.a.a.a.b;
import a.a.a.a.a.a.c;
import a.a.a.a.a.a.d;
import a.a.a.a.a.a.e;
import aisble.BleManager;
import aisble.BleManagerCallbacks;
import aisble.callback.FailCallback;
import aisble.callback.SuccessCallback;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICastEventListener;
import com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedLayerException;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedPluginTypeException;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.gattlibrary.callback.IBLEConnectionStateListener;
import com.alibaba.ailabs.iot.gattlibrary.plugin.BluetoothGattPlugin;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class GattTransmissionLayer extends BleManager implements BleManagerCallbacks, ITransmissionLayer {
    public static final int MTU_SIZE_MAX = 255;
    public static final int MTU_SIZE_MIN = 23;
    public static final String TAG = "GattTransmissionLayer";
    public List<ICastEventListener> mCastEventRegistrants;
    public IBLEConnectionStateListener mConnectionStateListener;
    public Map<String, CommandResponseDispatcher> mDispatchersMap;
    public final BleManager.BleManagerGattCallback mGattCallback;
    public Map<UUID, BluetoothGattCharacteristic> mGattCharacteristicMap;
    public List<BluetoothGattPlugin> mInstalledPlugins;
    public volatile byte mMessageID;
    public boolean mSupported;
    public ITransmissionLayerCallback mTransmissionLayerCallback;

    public GattTransmissionLayer(@NonNull Context context) {
        super(context);
        this.mSupported = true;
        this.mGattCharacteristicMap = new HashMap();
        this.mMessageID = (byte) 0;
        this.mDispatchersMap = new HashMap();
        this.mCastEventRegistrants = new ArrayList();
        this.mGattCallback = new a(this);
        setGattCallbacks(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectionStateChanged(BluetoothDevice bluetoothDevice, int i) {
        IBLEConnectionStateListener iBLEConnectionStateListener = this.mConnectionStateListener;
        if (iBLEConnectionStateListener != null) {
            iBLEConnectionStateListener.onBLEConnectionStateChanged(bluetoothDevice, i);
        }
        Iterator<BluetoothGattPlugin> it = this.mInstalledPlugins.iterator();
        while (it.hasNext()) {
            it.next().onBluetoothConnectionStateChanged(i);
        }
        ITransmissionLayerCallback iTransmissionLayerCallback = this.mTransmissionLayerCallback;
        if (iTransmissionLayerCallback != null) {
            iTransmissionLayerCallback.onConnectionStateUpdate(bluetoothDevice, i);
        }
    }

    public void connectDevice(BluetoothDevice bluetoothDevice, IActionListener<BluetoothDevice> iActionListener) {
        LogUtils.d(TAG, "Start connect bluetooth device: " + bluetoothDevice.getAddress());
        connect(bluetoothDevice).done((SuccessCallback) new c(this, iActionListener)).fail((FailCallback) new b(this, iActionListener)).retry(3, 500).enqueue();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void disconnectDevice(IActionListener<BluetoothDevice> iActionListener) {
        LogUtils.d(TAG, "Start disconnect from bluetooth device");
        int connectState = getConnectState();
        if (connectState == 2) {
            disconnect().done((SuccessCallback) new e(this, iActionListener)).fail((FailCallback) new d(this, iActionListener)).enqueue();
        } else {
            if (connectState != 0 || iActionListener == null) {
                return;
            }
            iActionListener.onSuccess(getBluetoothDevice());
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void dynamicInstallPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException {
        if (!(iPlugin instanceof BluetoothGattPlugin)) {
            throw new UnsupportedPluginTypeException();
        }
        if (!this.mServicesDiscovered || this.mDeviceNotSupported) {
            installPlugin((BluetoothGattPlugin) iPlugin);
            return;
        }
        try {
            if (((BluetoothGattPlugin) iPlugin).isRequiredServiceSupported(this.mBluetoothGatt)) {
                iPlugin.init(this);
                if (this.mReady) {
                    iPlugin.start();
                } else {
                    installPlugin((BluetoothGattPlugin) iPlugin);
                }
            }
        } catch (UnsupportedLayerException e) {
            e.printStackTrace();
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void forwardCommand(byte b2, byte[] bArr) {
        ITransmissionLayerCallback iTransmissionLayerCallback = this.mTransmissionLayerCallback;
        if (iTransmissionLayerCallback != null) {
            iTransmissionLayerCallback.onReceivedCommand(b2, bArr);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void forwardInnerCastEvent(String str) {
        Iterator<ICastEventListener> it = this.mCastEventRegistrants.iterator();
        while (it.hasNext()) {
            it.next().onCast(str);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void forwardStream(byte[] bArr) {
        ITransmissionLayerCallback iTransmissionLayerCallback = this.mTransmissionLayerCallback;
        if (iTransmissionLayerCallback != null) {
            iTransmissionLayerCallback.onReceivedStream(bArr);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public synchronized byte generateMessageID() {
        byte b2;
        this.mMessageID = this.mMessageID == 16 ? (byte) 0 : this.mMessageID;
        b2 = this.mMessageID;
        this.mMessageID = (byte) (b2 + 1);
        return b2;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public CommandResponseDispatcher getCommandResponseDispatcher(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.mDispatchersMap.containsKey(str)) {
            return this.mDispatchersMap.get(str);
        }
        CommandResponseDispatcher commandResponseDispatcher = new CommandResponseDispatcher();
        this.mDispatchersMap.put(str, commandResponseDispatcher);
        return commandResponseDispatcher;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public LayerState getConnectionState() {
        return LayerState.parserFromIntValue(getConnectState());
    }

    @Override // aisble.BleManager
    @NonNull
    public BleManager.BleManagerGattCallback getGattCallback() {
        return this.mGattCallback;
    }

    public BluetoothGattCharacteristic getGattCharacteristic(UUID uuid) {
        return this.mGattCharacteristicMap.get(uuid);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public TransmissionLayer getLayer() {
        return TransmissionLayer.BLE;
    }

    @Override // aisble.BleManager
    public int getServiceDiscoveryDelay(boolean z) {
        return 200;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public ITransmissionLayerCallback getTransmissionLayerCallback() {
        return this.mTransmissionLayerCallback;
    }

    public void installPlugin(BluetoothGattPlugin bluetoothGattPlugin) {
        LogUtils.d(TAG, String.format("Install plugin: %s on layer: %s", bluetoothGattPlugin, this));
        if (this.mInstalledPlugins == null) {
            this.mInstalledPlugins = new ArrayList();
        }
        this.mInstalledPlugins.add(bluetoothGattPlugin);
    }

    @Override // aisble.BleManagerCallbacks
    public void onBatteryValueReceived(@NonNull BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBonded(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingFailed(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingRequired(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void onDestroy() {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnected(@NonNull BluetoothDevice bluetoothDevice) {
        LogUtils.d(TAG, String.format("device: %s is connected", bluetoothDevice.getName()));
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnecting(@NonNull BluetoothDevice bluetoothDevice) {
        LogUtils.d(TAG, String.format("device: %s is connecting", bluetoothDevice.getName()));
        notifyConnectionStateChanged(bluetoothDevice, 1);
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnected(@NonNull BluetoothDevice bluetoothDevice) {
        LogUtils.d(TAG, String.format("device: %s is disconnected", bluetoothDevice.getName()));
        notifyConnectionStateChanged(bluetoothDevice, 0);
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnecting(@NonNull BluetoothDevice bluetoothDevice) {
        notifyConnectionStateChanged(bluetoothDevice, 3);
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceNotSupported(@NonNull BluetoothDevice bluetoothDevice) {
    }

    public void onDeviceReady(@NonNull BluetoothDevice bluetoothDevice) {
        LogUtils.d(TAG, "onDeviceReady");
        Iterator<BluetoothGattPlugin> it = this.mInstalledPlugins.iterator();
        while (it.hasNext()) {
            it.next().start();
        }
    }

    @Override // aisble.BleManagerCallbacks
    @SuppressLint({"DefaultLocale"})
    public void onError(@NonNull BluetoothDevice bluetoothDevice, @NonNull String str, int i) {
        LogUtils.e(TAG, String.format("%s: %s, errorCode: %d", str, bluetoothDevice.getAddress(), Integer.valueOf(i)));
    }

    @Override // aisble.BleManagerCallbacks
    public void onLinkLossOccurred(@NonNull BluetoothDevice bluetoothDevice) {
        LogUtils.d(TAG, String.format("device: %s is loss link", bluetoothDevice.getName()));
    }

    @Override // aisble.BleManagerCallbacks
    public void onServicesDiscovered(@NonNull BluetoothDevice bluetoothDevice, boolean z) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void registerCastEventListener(ICastEventListener iCastEventListener) {
        this.mCastEventRegistrants.add(iCastEventListener);
    }

    public void setOnBLEConnectionStateChangedListener(IBLEConnectionStateListener iBLEConnectionStateListener) {
        this.mConnectionStateListener = iBLEConnectionStateListener;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void setTransmissionLayerCallback(ITransmissionLayerCallback iTransmissionLayerCallback) {
        this.mTransmissionLayerCallback = iTransmissionLayerCallback;
    }

    @Override // aisble.BleManager
    public boolean shouldClearCacheWhenDisconnected() {
        return !this.mSupported;
    }

    @Override // aisble.BleManagerCallbacks
    public boolean shouldEnableBatteryLevelNotifications(@NonNull BluetoothDevice bluetoothDevice) {
        return false;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void uninstallPlugin(IPlugin iPlugin) {
        this.mInstalledPlugins.remove(iPlugin);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void unregisterCastEventListener(ICastEventListener iCastEventListener) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer
    public void installPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException {
        if (iPlugin instanceof BluetoothGattPlugin) {
            installPlugin((BluetoothGattPlugin) iPlugin);
            return;
        }
        throw new UnsupportedPluginTypeException();
    }
}
