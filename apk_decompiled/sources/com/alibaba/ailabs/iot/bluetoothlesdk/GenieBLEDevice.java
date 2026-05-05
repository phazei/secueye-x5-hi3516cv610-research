package com.alibaba.ailabs.iot.bluetoothlesdk;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.MainThread;
import anetwork.channel.util.RequestConstant;
import com.alibaba.ailabs.iot.aisbase.AuthInfoListener;
import com.alibaba.ailabs.iot.aisbase.OTAUTLogDecorator;
import com.alibaba.ailabs.iot.aisbase.UTLogUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedPluginTypeException;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;
import com.alibaba.ailabs.iot.aisbase.spec.AISManufacturerADData;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.bluetoothlesdk.ControlMessage;
import com.alibaba.ailabs.iot.bluetoothlesdk.auxiliary.AuxiliaryProvisionManager;
import com.alibaba.ailabs.iot.bluetoothlesdk.datasource.RequestManager;
import com.alibaba.ailabs.iot.bluetoothlesdk.interfaces.OnNotifyListener;
import com.alibaba.ailabs.iot.bluetoothlesdk.plugin.IBLEInfrastructurePlugin;
import com.alibaba.ailabs.iot.gattlibrary.plugin.GattCommandPlugin;
import com.alibaba.ailabs.iot.gattlibrary.plugin.auth.GattAuthPlugin;
import com.alibaba.ailabs.iot.gattlibrary.plugin.ota.GattOTAPlugin;
import com.alibaba.ailabs.iot.iotmtopdatasource.FeiyanDeviceManager;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import datasource.NetworkCallback;
import datasource.implemention.data.DeviceVersionInfo;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class GenieBLEDevice extends BluetoothDeviceWrapper implements Parcelable, ITransmissionLayerCallback, OnNotifyListener {
    public static int GENIE_BLE = 2;
    private static final String TAG = "GenieBLEDevice";
    private IBLEInfrastructurePlugin mBLEInfrastructurePlugin;
    private Context mContext;
    private IGenieBLEDeviceCallback mGenieBLEDeviceCallback;
    private b mControlMessageQueue = new b();
    private boolean mHasBeenAuthenticatedSuccessfully = false;
    private IActionListener<BluetoothDevice> mConnectionListener = null;
    private boolean mMeshOtaFlag = false;
    private boolean mHasOtaActivity = false;
    private byte mSubVersion = 0;
    private final String AIS_VERSION_REGEX = "^([0-9]\\d|[0-9])(\\.([0-9]\\d|\\d)){1,3}$";
    private int bleConnectState = 0;

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback
    public void onA2DPConnectionStateUpdate(BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback
    public void onBindStateUpdate(BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback
    public void onReceivedStream(byte[] bArr) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public /* bridge */ /* synthetic */ BluetoothDeviceWrapper connect(Context context, IActionListener iActionListener) {
        return connect(context, (IActionListener<BluetoothDevice>) iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public /* bridge */ /* synthetic */ BluetoothDeviceWrapper connect(Context context, TransmissionLayer transmissionLayer, boolean z, IActionListener iActionListener) {
        return connect(context, transmissionLayer, z, (IActionListener<BluetoothDevice>) iActionListener);
    }

    public int getBleConnectState() {
        return this.bleConnectState;
    }

    public GenieBLEDevice(BluetoothDevice bluetoothDevice) {
        setBluetoothDevice(bluetoothDevice);
    }

    public GenieBLEDevice(String str) {
        setBluetoothDevice(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(str));
    }

    public void setGenieBLEDeviceCallback(IGenieBLEDeviceCallback iGenieBLEDeviceCallback) {
        this.mGenieBLEDeviceCallback = iGenieBLEDeviceCallback;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public GenieBLEDevice connect(Context context, IActionListener<BluetoothDevice> iActionListener) {
        LogUtils.d(TAG, "Connect...");
        if (this.mChannelManager == null) {
            synchronized (this) {
                initTransmissionManager(context, TransmissionLayer.BLE);
            }
        }
        this.mConnectionListener = getUTLogDecoratorForConnectionListener(iActionListener);
        this.mChannelManager.getTransmissionLayer().connectDevice(this.mBluetoothDevice, new IActionListener<BluetoothDevice>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.1
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(BluetoothDevice bluetoothDevice) {
                if (GenieBLEDevice.this.isIsSafetyMode()) {
                    return;
                }
                GenieBLEDevice.this.mConnectionListener.onSuccess(bluetoothDevice);
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
                GenieBLEDevice.this.mConnectionListener.onFailure(i, str);
            }
        });
        return this;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public GenieBLEDevice connect(Context context, TransmissionLayer transmissionLayer, boolean z, IActionListener<BluetoothDevice> iActionListener) {
        return connect(context, iActionListener);
    }

    public void bindDevice(Context context, IActionListener<Boolean> iActionListener) {
        LogUtils.d(TAG, "Bind...");
        this.mControlMessageQueue.b(new ControlMessage(ControlMessage.Type.BIND).callback(iActionListener));
        nextRequest(false, context.getApplicationContext());
    }

    public void unbindDevice(Context context, IActionListener<Boolean> iActionListener) {
        LogUtils.d(TAG, "Unbind...");
        this.mControlMessageQueue.b(new ControlMessage(ControlMessage.Type.UNBIND).callback(iActionListener));
        nextRequest(false, context.getApplicationContext());
    }

    public void sendMessage(Context context, JSONObject jSONObject, IActionListener<Boolean> iActionListener) {
        this.mControlMessageQueue.a(new ControlMessage(ControlMessage.Type.CONTROL, jSONObject).callback(iActionListener));
        nextRequest(false, context.getApplicationContext());
    }

    public void sendMessage(Context context, ControlMessage controlMessage) {
        this.mControlMessageQueue.a(controlMessage);
        nextRequest(false, context.getApplicationContext());
    }

    public void sendAuxiliaryProvisionMessage(Context context, byte[] bArr, IActionListener<BluetoothDevice> iActionListener) {
        this.mControlMessageQueue.a(new ControlMessage(ControlMessage.Type.CONTROL, (byte) 13, bArr).callback(iActionListener));
        nextRequest(false, context.getApplicationContext());
    }

    public void getFirmwareVersion(IActionListener<Integer> iActionListener) {
        if (makeSurePluginIsInitialized(this.mOtaPlugin, iActionListener)) {
            this.mOtaPlugin.sendGetFirmwareVersionCommand(iActionListener);
        }
    }

    public void getFirmwareVersionV1(IActionListener<String> iActionListener) {
        if (makeSurePluginIsInitialized(this.mOtaPlugin, iActionListener)) {
            this.mOtaPlugin.sendGetFirmwareVersionCommandV1(iActionListener);
        }
    }

    public void reportDeviceStatus(byte[] bArr, IActionListener<Boolean> iActionListener) {
        if (makeSurePluginIsInitialized(this.mBLEInfrastructurePlugin, iActionListener)) {
            this.mBLEInfrastructurePlugin.reportDeviceStatus(bArr, iActionListener);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public LayerState getConnectionState() {
        if (this.mChannelManager == null) {
            return LayerState.NONE;
        }
        if (this.mChannelManager.getTransmissionLayer().getConnectionState() == LayerState.CONNECTED && (!isIsSafetyMode() || this.mHasBeenAuthenticatedSuccessfully)) {
            return LayerState.AUTH_SUCCESSFUL;
        }
        return this.mChannelManager.getTransmissionLayer().getConnectionState();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public TransmissionLayerManagerBase createChannelManager(Context context, TransmissionLayer transmissionLayer) {
        synchronized (this) {
            this.mChannelManager = new c(context, this, transmissionLayer);
            this.mChannelManager.setTransmissionLayerCallback(this);
        }
        return this.mChannelManager;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public void installPlugins(TransmissionLayer transmissionLayer) {
        this.mBasicPlugin = new GattCommandPlugin();
        this.mBLEInfrastructurePlugin = this.mSubVersion == 18 ? new com.alibaba.ailabs.iot.bluetoothlesdk.plugin.b() : new com.alibaba.ailabs.iot.bluetoothlesdk.plugin.a();
        this.mInstalledPlugins.add(this.mBasicPlugin);
        this.mInstalledPlugins.add(this.mBLEInfrastructurePlugin);
        this.mBLEInfrastructurePlugin.setOnNotifyListener(this);
        try {
            this.mChannelManager.installPlugin(this.mBasicPlugin);
            this.mChannelManager.installPlugin(this.mBLEInfrastructurePlugin);
            Iterator<IPlugin> it = this.mInstalledPlugins.iterator();
            while (it.hasNext()) {
                it.next().setBluetoothDeviceWrapper(this);
            }
            installOptionalPlugins(false);
        } catch (UnsupportedPluginTypeException unused) {
            LogUtils.w(TAG, String.format("Install plugin(%s) for transmission layer failed", this.mBasicPlugin));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installOptionalPlugins(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "Dynamic " : "Static ");
        sb.append("loading optional plugin");
        LogUtils.d(str, sb.toString());
        if (isIsSafetyMode() && this.mAisManufactureDataADV != null) {
            LogUtils.d(TAG, "Support safety mode");
            GattAuthPlugin gattAuthPlugin = new GattAuthPlugin();
            gattAuthPlugin.setIsBLEDevice(true);
            gattAuthPlugin.setAuthParams(this.mAisManufactureDataADV.getPId(), getAdvertiseMac(), getAuthListener());
            try {
                if (z) {
                    this.mChannelManager.dynamicInstallPlugin(gattAuthPlugin);
                } else {
                    this.mChannelManager.installPlugin(gattAuthPlugin);
                }
            } catch (UnsupportedPluginTypeException e) {
                e.printStackTrace();
            }
            this.mInstalledPlugins.add(gattAuthPlugin);
            gattAuthPlugin.setBluetoothDeviceWrapper(this);
        }
        if (isSupportOTA()) {
            LogUtils.d(TAG, "Support OTA");
            this.mOtaPlugin = new GattOTAPlugin();
            try {
                if (z) {
                    this.mChannelManager.dynamicInstallPlugin(this.mOtaPlugin);
                } else {
                    this.mChannelManager.installPlugin(this.mOtaPlugin);
                }
            } catch (UnsupportedPluginTypeException e2) {
                e2.printStackTrace();
            }
            this.mInstalledPlugins.add(this.mOtaPlugin);
            this.mOtaPlugin.setBluetoothDeviceWrapper(this);
        }
    }

    private String getAdvertiseMac() {
        byte[] macAddress = this.mAisManufactureDataADV.getMacAddress();
        return String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x", Byte.valueOf(macAddress[0]), Byte.valueOf(macAddress[1]), Byte.valueOf(macAddress[2]), Byte.valueOf(macAddress[3]), Byte.valueOf(macAddress[4]), Byte.valueOf(macAddress[5]));
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public String getAddress() {
        return getAdvertiseMac().toUpperCase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public synchronized void nextRequest(boolean z, Context context) {
        LayerState connectionState = getConnectionState();
        if (connectionState != LayerState.AUTH_SUCCESSFUL) {
            if (!z) {
                LogUtils.d(TAG, "First connect");
                if (connectionState != LayerState.CONNECTED) {
                    connect(context, getConnectResultListener());
                }
            } else {
                while (this.mControlMessageQueue.b()) {
                    ControlMessage controlMessageA = this.mControlMessageQueue.a();
                    if (controlMessageA != null) {
                        if (makeSurePluginIsInitialized(this.mBLEInfrastructurePlugin, controlMessageA.mCallback)) {
                            LogUtils.d(TAG, "Force performing operations, next one is " + controlMessageA.type);
                            switch (controlMessageA.type) {
                                case BIND:
                                case UNBIND:
                                case CONTROL:
                                    if (controlMessageA.mCallback != null) {
                                        controlMessageA.mCallback.onFailure(-1, "Not connected");
                                    }
                                    break;
                            }
                        } else {
                            LogUtils.w(TAG, "BLE infrastructure plugin not initialized");
                        }
                    }
                }
            }
        } else if (connectionState == LayerState.AUTH_SUCCESSFUL) {
            while (this.mControlMessageQueue.b()) {
                ControlMessage controlMessageA2 = this.mControlMessageQueue.a();
                if (controlMessageA2 != null) {
                    if (makeSurePluginIsInitialized(this.mBLEInfrastructurePlugin, controlMessageA2.mCallback)) {
                        LogUtils.d(TAG, "Performing operations, next one is " + controlMessageA2.type);
                        switch (controlMessageA2.type) {
                            case BIND:
                                this.mBLEInfrastructurePlugin.bindDevice(controlMessageA2.mCallback);
                                break;
                            case UNBIND:
                                this.mBLEInfrastructurePlugin.unbindDevice(controlMessageA2.mCallback);
                                break;
                            case CONTROL:
                                if (controlMessageA2.getJsonParameters() != null) {
                                    this.mBLEInfrastructurePlugin.sendMessage(controlMessageA2.getJsonParameters(), true, controlMessageA2.mCallback);
                                } else {
                                    this.mBLEInfrastructurePlugin.sendMessage(controlMessageA2.getRequest(), controlMessageA2.getParameters(), true, controlMessageA2.mCallback);
                                }
                                break;
                        }
                    } else {
                        LogUtils.w(TAG, "BLE infrastructure plugin not initialized");
                    }
                }
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback
    public void onReceivedCommand(byte b2, byte[] bArr) {
        if (b2 == 1) {
            AuxiliaryProvisionManager.getInstance().notifyAuxiliaryDeviceStatusChange(getAddress(), bArr);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback
    public void onConnectionStateUpdate(BluetoothDevice bluetoothDevice, int i) {
        LogUtils.d(TAG, "Connection status changes to " + i);
        if (i == 0) {
            notifyConnectionState(LayerState.DISCONNECTED);
            Iterator<IPlugin> it = this.mInstalledPlugins.iterator();
            while (it.hasNext()) {
                it.next().enableAESEncryption(null);
            }
            return;
        }
        if (i != 2) {
            return;
        }
        notifyConnectionState(LayerState.CONNECTED);
        if (this.mAisManufactureDataADV == null) {
            if (makeSurePluginIsInitialized(this.mBasicPlugin, null)) {
                getManufacturerSpecificData();
                return;
            } else {
                LogUtils.w(TAG, "Basic plugin not initialized");
                return;
            }
        }
        if (isIsSafetyMode()) {
            return;
        }
        notifyConnectionState(LayerState.AUTH_SUCCESSFUL);
        nextRequest(false, null);
    }

    private void getManufacturerSpecificData() {
        LogUtils.d(TAG, "Dynamic get manufacture specific data");
        this.mBasicPlugin.getManufacturerSpecificData(new IActionListener<byte[]>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.6
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(byte[] bArr) {
                if (bArr == null) {
                    LogUtils.w(GenieBLEDevice.TAG, "Get empty manufacture specific data!");
                    return;
                }
                GenieBLEDevice.this.setAisManufactureDataADV(AISManufacturerADData.parseFromBytes(bArr));
                if (!GenieBLEDevice.this.isIsSafetyMode()) {
                    GenieBLEDevice.this.notifyConnectionState(LayerState.AUTH_SUCCESSFUL);
                }
                GenieBLEDevice.this.installOptionalPlugins(true);
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
                LogUtils.e(GenieBLEDevice.TAG, "Get manufacture specific data failed(code: " + i + ", message: " + str);
            }
        });
    }

    public void setMeshOtaFlag(boolean z) {
        this.mMeshOtaFlag = z;
    }

    public boolean isMeshOtaDevice() {
        return this.mMeshOtaFlag;
    }

    private IActionListener<BluetoothDevice> getConnectResultListener() {
        return new IActionListener<BluetoothDevice>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.7
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(BluetoothDevice bluetoothDevice) {
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
                LogUtils.e(GenieBLEDevice.TAG, "Connection failed(" + i + "," + str + ")");
                GenieBLEDevice.this.nextRequest(true, null);
            }
        };
    }

    private IActionListener<byte[]> getAuthListener() {
        return new IDetailActionListener<byte[]>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.8
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener
            public void onState(int i, String str, Object obj) {
                LogUtils.d(GenieBLEDevice.TAG, "connect onState() called with: i = [" + i + "], s = [" + str + "], o = [" + obj + "]");
                GenieBLEDevice.this.bleConnectState = i;
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(byte[] bArr) {
                byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 0, 16);
                Iterator it = GenieBLEDevice.this.mInstalledPlugins.iterator();
                while (it.hasNext()) {
                    ((IPlugin) it.next()).enableAESEncryption(bArrCopyOfRange);
                }
                GenieBLEDevice.this.notifyConnectionState(LayerState.AUTH_SUCCESSFUL);
                GenieBLEDevice.this.nextRequest(true, null);
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
                LogUtils.d(GenieBLEDevice.TAG, "Auth failed, errorCode: " + i + ", errorDesc: " + str);
                GenieBLEDevice.this.notifyConnectionState(LayerState.AUTH_FAILED);
                GenieBLEDevice.this.mChannelManager.getTransmissionLayer().disconnectDevice(new IActionListener<BluetoothDevice>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.8.1
                    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                    public void onSuccess(BluetoothDevice bluetoothDevice) {
                    }

                    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                    public void onFailure(int i2, String str2) {
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IActionListener<Boolean> getOnlineEventListener() {
        return new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.9
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(Boolean bool) {
                LogUtils.d(GenieBLEDevice.TAG, "Online event: " + bool);
                if (GenieBLEDevice.this.mGenieBLEDeviceCallback != null) {
                    GenieBLEDevice.this.mGenieBLEDeviceCallback.onlineStateChanged(bool.booleanValue());
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectionState(LayerState layerState) {
        LogUtils.d(TAG, "Connection state change to " + layerState);
        if (layerState == LayerState.AUTH_SUCCESSFUL) {
            GenieBLEDeviceManager.cacheBLEDevice(this);
            IActionListener<BluetoothDevice> iActionListener = this.mConnectionListener;
            if (iActionListener != null) {
                iActionListener.onSuccess(getBluetoothDevice());
            }
            if (this.mHasOtaActivity) {
                return;
            }
            if (makeSurePluginIsInitialized(this.mOtaPlugin, null) && makeSurePluginIsInitialized(this.mBLEInfrastructurePlugin, null)) {
                this.mOtaPlugin.sendGetFirmwareVersionCommand(new IActionListener<Object>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.10
                    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                    public void onSuccess(Object obj) {
                        if (obj instanceof Integer) {
                            GenieBLEDevice.this.mBLEInfrastructurePlugin.reportOnlineStatus(true, Utils.adapterToIotServerVersion(((Integer) obj).intValue()), GenieBLEDevice.this.getOnlineEventListener());
                        } else if (obj instanceof String) {
                            GenieBLEDevice.this.mBLEInfrastructurePlugin.reportOnlineStatus(true, (String) obj, GenieBLEDevice.this.getOnlineEventListener());
                        }
                    }

                    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
                    public void onFailure(int i, String str) {
                        GenieBLEDevice.this.mBLEInfrastructurePlugin.reportOnlineStatus(true, "", GenieBLEDevice.this.getOnlineEventListener());
                    }
                });
            }
        } else if (layerState == LayerState.AUTH_FAILED) {
            Utils.notifyFailed(this.mConnectionListener, FailCallback.REASON_AUTH_FAILED, "auth failed");
        }
        this.mHasBeenAuthenticatedSuccessfully = layerState == LayerState.AUTH_SUCCESSFUL;
        IGenieBLEDeviceCallback iGenieBLEDeviceCallback = this.mGenieBLEDeviceCallback;
        if (iGenieBLEDeviceCallback != null) {
            iGenieBLEDeviceCallback.onChannelStateChanged(layerState);
        }
    }

    public void startOTA(Context context, String str, final IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        LogUtils.d(TAG, "Start OTA, layer state: " + this.mChannelManager.getTransmissionLayer().getConnectionState());
        this.mHasOtaActivity = true;
        final IOTAPlugin.IOTAActionListener listenerWrapper = getListenerWrapper(iOTAActionListener);
        if (makeSurePluginIsInitialized(this.mOtaPlugin, new IActionListener() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.11
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onSuccess(Object obj) {
                LogUtils.d(GenieBLEDevice.TAG, "on Success of makeSurePluginIsInitialized");
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str2) {
                listenerWrapper.onFailed(i, str2);
            }
        })) {
            DeviceVersionInfo deviceVersionInfo = this.mBLEInfrastructurePlugin.getDeviceVersionInfo();
            if (deviceVersionInfo == null || deviceVersionInfo.getModel().getCanOta().equals(RequestConstant.FALSE)) {
                listenerWrapper.onFailed(-201, "");
                return;
            }
            LogUtils.d(TAG, "Start download from server, otaInfo " + deviceVersionInfo);
            startDownloadDeviceFirmware(context, deviceVersionInfo, str, new IOTAPlugin.IFirmwareDownloadListener() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.12
                @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
                public void onDownloadStart() {
                    LogUtils.d(GenieBLEDevice.TAG, "Download start");
                    IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = iFirmwareDownloadListener;
                    if (iFirmwareDownloadListener2 != null) {
                        iFirmwareDownloadListener2.onDownloadStart();
                    }
                }

                @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
                public void onProgress(int i, int i2) {
                    LogUtils.d(GenieBLEDevice.TAG, "Download progress: " + i + "/" + i2);
                    IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = iFirmwareDownloadListener;
                    if (iFirmwareDownloadListener2 != null) {
                        iFirmwareDownloadListener2.onProgress(i, i2);
                    }
                }

                @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
                public void onFailed(int i, String str2) {
                    LogUtils.d(GenieBLEDevice.TAG, "Download failed(code:" + i + ", desc:" + str2 + ")");
                    listenerWrapper.onFailed(i, str2);
                }

                @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
                public void onComplete(String str2) {
                    GenieBLEDevice genieBLEDevice = GenieBLEDevice.this;
                    genieBLEDevice.realStartOta(str2, new OTAUTLogDecorator(listenerWrapper, genieBLEDevice));
                }
            });
        }
    }

    public void startDownloadFeiyanDeviceFirmware(Context context, DeviceVersionInfo.DeviceInfoModel deviceInfoModel, String str, final IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = new IOTAPlugin.IFirmwareDownloadListener() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.13
            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
            public void onDownloadStart() {
                LogUtils.d(GenieBLEDevice.TAG, "Download start");
                IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener3 = iFirmwareDownloadListener;
                if (iFirmwareDownloadListener3 != null) {
                    iFirmwareDownloadListener3.onDownloadStart();
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
            public void onProgress(int i, int i2) {
                LogUtils.d(GenieBLEDevice.TAG, "Download progress: " + i + "/" + i2);
                IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener3 = iFirmwareDownloadListener;
                if (iFirmwareDownloadListener3 != null) {
                    iFirmwareDownloadListener3.onProgress(i, i2);
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
            public void onFailed(int i, String str2) {
                LogUtils.d(GenieBLEDevice.TAG, "Download failed(code:" + i + ", desc:" + str2 + ")");
                IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener3 = iFirmwareDownloadListener;
                if (iFirmwareDownloadListener3 != null) {
                    iFirmwareDownloadListener3.onFailed(i, str2);
                }
                UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(GenieBLEDevice.this), UTLogUtils.buildOtaBusInfo("error", 0, i, str2));
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
            public void onComplete(String str2) {
                LogUtils.d(GenieBLEDevice.TAG, "Download Completed (path:" + str2 + ")");
                IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener3 = iFirmwareDownloadListener;
                if (iFirmwareDownloadListener3 != null) {
                    iFirmwareDownloadListener3.onComplete(str2);
                }
            }
        };
        UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(this), UTLogUtils.buildOtaBusInfo("start", (this.mChannelManager == null || this.mChannelManager.getTransmissionLayer() == null) ? 0 : this.mChannelManager.getTransmissionLayer().getMtu(), 0, ""));
        new OTADownloadHelper().startDownloadIlopFirmware(context, deviceInfoModel, str, iFirmwareDownloadListener2);
    }

    public static int adapterToAisVersion(String str) {
        if (str == null) {
            return 0;
        }
        String[] strArrSplit = str.split("\\.");
        LogUtils.d(TAG, "versionItems length: " + strArrSplit.length);
        if (strArrSplit == null || strArrSplit.length < 3) {
            return 0;
        }
        int[] iArr = {Integer.valueOf(strArrSplit[0]).intValue(), Integer.valueOf(strArrSplit[1]).intValue(), Integer.valueOf(strArrSplit[2]).intValue()};
        return (iArr[0] << 16) | (iArr[1] << 8) | iArr[2];
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public void setAisManufactureDataADV(AISManufacturerADData aISManufacturerADData) {
        super.setAisManufactureDataADV(aISManufacturerADData);
        this.mSubVersion = (byte) (aISManufacturerADData.getExt()[0] & BinaryMemcacheOpcodes.GATQ);
    }

    private byte[] getFillVersion(String str) {
        if (!TextUtils.isEmpty(str) && str.matches("^([0-9]\\d|[0-9])(\\.([0-9]\\d|\\d)){1,3}$")) {
            int iAdapterToAisVersion = adapterToAisVersion(str);
            LogUtils.d(TAG, "Real start ota, versionInt: " + iAdapterToAisVersion);
            return com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(iAdapterToAisVersion);
        }
        byte[] bArr = new byte[31];
        Arrays.fill(bArr, (byte) 0);
        byte[] bytes = str.getBytes();
        System.arraycopy(bytes, 0, bArr, 0, bytes.length >= bArr.length ? bArr.length - 1 : bytes.length);
        return bArr;
    }

    public void realStartOta(String str, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        LogUtils.d(TAG, "Real start ota, firmware path: " + str);
        if (!makeSurePluginIsInitialized(this.mOtaPlugin, null)) {
            if (iOTAActionListener != null) {
                iOTAActionListener.onFailed(-202, "Not connected or not supported");
                return;
            }
            return;
        }
        DeviceVersionInfo deviceVersionInfo = this.mBLEInfrastructurePlugin.getDeviceVersionInfo();
        if (deviceVersionInfo == null || deviceVersionInfo.getModel() == null || deviceVersionInfo.getModel().getCanOta().equals(RequestConstant.FALSE)) {
            iOTAActionListener.onFailed(-201, "");
            return;
        }
        String version = deviceVersionInfo.getModel().getVersion();
        LogUtils.d(TAG, "Real start ota, version: " + version);
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            int iAvailable = fileInputStream.available();
            byte[] bArr = new byte[iAvailable];
            fileInputStream.read(bArr);
            fileInputStream.close();
            int iAdapterToAisVersion = adapterToAisVersion(version);
            LogUtils.d(TAG, "Real start ota, versionInt: " + iAdapterToAisVersion);
            this.mOtaPlugin.startOTA(bArr, com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(iAdapterToAisVersion), com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(iAvailable), (byte) 0, Arrays.copyOfRange(com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(com.alibaba.ailabs.iot.aisbase.Utils.genCrc16CCITT(bArr, 0, bArr.length)), 0, 2), (byte) 0, iOTAActionListener);
        } catch (IOException e) {
            LogUtils.e(TAG, e.toString());
            if (iOTAActionListener != null) {
                iOTAActionListener.onFailed(-200, "Failed to open firmware file");
            }
        }
    }

    public void realStartFeiyanOta(String str, DeviceVersionInfo.DeviceInfoModel deviceInfoModel, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        LogUtils.d(TAG, "Real start feiyan ota, firmware path: " + str);
        if (!makeSurePluginIsInitialized(this.mOtaPlugin, null)) {
            if (iOTAActionListener != null) {
                iOTAActionListener.onFailed(-202, "Not connected or not supported");
                return;
            }
            return;
        }
        if (deviceInfoModel == null || deviceInfoModel.getCanOta().equals(RequestConstant.FALSE)) {
            iOTAActionListener.onFailed(-201, "");
            return;
        }
        String version = deviceInfoModel.getVersion();
        LogUtils.d(TAG, "Real start Feiyan ota, version: " + version);
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            int iAvailable = fileInputStream.available();
            byte[] bArr = new byte[iAvailable];
            fileInputStream.read(bArr);
            fileInputStream.close();
            this.mOtaPlugin.startOTA(bArr, getFillVersion(version), com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(iAvailable), (byte) 0, Arrays.copyOfRange(com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByLittleEndian(com.alibaba.ailabs.iot.aisbase.Utils.genCrc16CCITT(bArr, 0, bArr.length)), 0, 2), (byte) 0, iOTAActionListener);
        } catch (IOException e) {
            LogUtils.e(TAG, e.toString());
            if (iOTAActionListener != null) {
                iOTAActionListener.onFailed(-200, "Failed to open firmware file");
            }
        }
    }

    private IOTAPlugin.IOTAActionListener getListenerWrapper(final IOTAPlugin.IOTAActionListener iOTAActionListener) {
        return new IOTAPlugin.IOTAActionListener() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.2
            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
            public void onStateChanged(IOTAPlugin.OTAState oTAState) {
                IOTAPlugin.IOTAActionListener iOTAActionListener2 = iOTAActionListener;
                if (iOTAActionListener2 != null) {
                    iOTAActionListener2.onStateChanged(oTAState);
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
            public void onProgress(int i, int i2) {
                IOTAPlugin.IOTAActionListener iOTAActionListener2 = iOTAActionListener;
                if (iOTAActionListener2 != null) {
                    iOTAActionListener2.onProgress(i, i2);
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
            public void onSuccess(String str) {
                LogUtils.d(GenieBLEDevice.TAG, "ota success, version: " + str);
                DeviceVersionInfo deviceVersionInfo = GenieBLEDevice.this.mBLEInfrastructurePlugin.getDeviceVersionInfo();
                String version = "";
                if (deviceVersionInfo != null && deviceVersionInfo.getModel() != null) {
                    version = deviceVersionInfo.getModel().getVersion();
                }
                if (version.equalsIgnoreCase(str)) {
                    GenieBLEDevice.this.reportOtaProgress(IotServerErrorCode.INCONSISTENT_VERSION, true, version);
                    IOTAPlugin.IOTAActionListener iOTAActionListener2 = iOTAActionListener;
                    if (iOTAActionListener2 != null) {
                        iOTAActionListener2.onFailed(IotServerErrorCode.INCONSISTENT_VERSION, "Inconsistent version");
                        return;
                    }
                    return;
                }
                IOTAPlugin.IOTAActionListener iOTAActionListener3 = iOTAActionListener;
                if (iOTAActionListener3 != null) {
                    iOTAActionListener3.onSuccess(version);
                }
                GenieBLEDevice.this.reportOtaProgress(100, true, version);
                GenieBLEDevice.this.mHasOtaActivity = false;
            }

            @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
            public void onFailed(int i, String str) {
                GenieBLEDevice.this.mHasOtaActivity = false;
                int i2 = IotServerErrorCode.UNKNOWN;
                switch (i) {
                    case -402:
                        i2 = IotServerErrorCode.MD5_NOT_MATCH;
                        break;
                    case -401:
                    case -400:
                        i2 = IotServerErrorCode.DOWNLOAD_ERROR;
                        break;
                    default:
                        switch (i) {
                            case -202:
                                i2 = IotServerErrorCode.NOT_SUPPORT_OTA;
                                break;
                            case -201:
                                i2 = IotServerErrorCode.NO_OTA_INFO;
                                break;
                            default:
                                switch (i) {
                                    case -1:
                                    case 0:
                                        i2 = IotServerErrorCode.NOT_CONNECTED;
                                        break;
                                    case 2:
                                        i2 = IotServerErrorCode.DOES_NOT_ALLOW_OTA;
                                        break;
                                    case 3:
                                        i2 = IotServerErrorCode.VERIFY_FIRMWARE_FAILED;
                                        break;
                                    case 4:
                                        i2 = IotServerErrorCode.EXIST_OTA;
                                        break;
                                    case 5:
                                        i2 = IotServerErrorCode.OTA_TIMEOUT;
                                        break;
                                    case 6:
                                        i2 = IotServerErrorCode.LOSS_LINK;
                                        break;
                                }
                                break;
                        }
                        break;
                }
                IOTAPlugin.IOTAActionListener iOTAActionListener2 = iOTAActionListener;
                if (iOTAActionListener2 != null) {
                    iOTAActionListener2.onFailed(i2, str);
                }
                DeviceVersionInfo deviceVersionInfo = GenieBLEDevice.this.mBLEInfrastructurePlugin.getDeviceVersionInfo();
                String version = "";
                if (deviceVersionInfo != null && deviceVersionInfo.getModel() != null) {
                    version = deviceVersionInfo.getModel().getVersion();
                }
                GenieBLEDevice.this.reportOtaProgress(i2, false, version);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportOtaProgress(int i, boolean z, String str) {
        RequestManager.getInstance().reportOtaProgress(getAisManufactureDataADV().getPidStr(), getAddress(), z, str, String.valueOf(i), null);
    }

    public void reportFeiyanOtaProgress(JSONObject jSONObject) {
        RequestManager.getInstance().reportFeiyanOtaProgress(jSONObject, new NetworkCallback<Object>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.3
            @Override // datasource.NetworkCallback
            public void onSuccess(Object obj) {
                LogUtils.i(GenieBLEDevice.TAG, "on Success report Progress: " + obj);
            }

            @Override // datasource.NetworkCallback
            public void onFailure(String str, String str2) {
                LogUtils.i(GenieBLEDevice.TAG, "on Failed report Progress, s: " + str + " , s1: " + str2);
            }
        });
    }

    public void initFeiyanNetwork(AuthInfoListener authInfoListener) {
        RequestManager.getInstance().init(authInfoListener, new FeiyanDeviceManager());
    }

    public void disconnectBLEDevice() {
        disconnect(new IActionListener<BluetoothDevice>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice.4
            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(BluetoothDevice bluetoothDevice) {
                LogUtils.d(GenieBLEDevice.TAG, "BLE device disconnection successful.");
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
            public void onFailure(int i, String str) {
                LogUtils.d(GenieBLEDevice.TAG, "BLE device disconnection failed, i:" + i + " s:" + str);
            }
        });
    }

    private IActionListener<BluetoothDevice> getUTLogDecoratorForConnectionListener(IActionListener<BluetoothDevice> iActionListener) {
        return new d(iActionListener, this, "connection");
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.interfaces.OnNotifyListener
    public void onNotify(byte b2, byte[] bArr) {
        LogUtils.d(TAG, "onNotify, command type: " + ConvertUtils.bytes2HexString(new byte[]{b2}));
        if (b2 == 1) {
            AuxiliaryProvisionManager.getInstance().notifyAuxiliaryDeviceStatusChange(getAddress(), bArr);
        } else if (b2 == 64) {
            AuxiliaryProvisionManager.getInstance().notifyAuxiliaryDeviceStatusChange(getAddress(), bArr);
        }
    }
}
