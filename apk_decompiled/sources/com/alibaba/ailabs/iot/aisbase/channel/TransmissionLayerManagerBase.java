package com.alibaba.ailabs.iot.aisbase.channel;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import com.alibaba.ailabs.iot.aisbase.A;
import com.alibaba.ailabs.iot.aisbase.B;
import com.alibaba.ailabs.iot.aisbase.C;
import com.alibaba.ailabs.iot.aisbase.C0466z;
import com.alibaba.ailabs.iot.aisbase.D;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ITransmissionLayerCallback;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedPluginTypeException;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class TransmissionLayerManagerBase {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2562a = "TransmissionLayerManagerBase";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public ITransmissionLayer f2563b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ITransmissionLayerCallback f2564c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f2565d;
    public BluetoothDevice e;
    public IActionListener<BluetoothDevice> h;
    public int f = -1;
    public int g = 0;
    public BroadcastReceiver i = new C0466z(this);

    public TransmissionLayerManagerBase(Context context, BluetoothDeviceWrapper bluetoothDeviceWrapper, TransmissionLayer transmissionLayer) {
        this.f2565d = context;
        this.e = bluetoothDeviceWrapper.getBluetoothDevice();
        this.f2563b = createTransmissionLayer(context, transmissionLayer);
        c();
    }

    public void bind(BluetoothDevice bluetoothDevice, IActionListener<BluetoothDevice> iActionListener) {
        if (iActionListener == null) {
            iActionListener = new A(this);
        }
        this.h = iActionListener;
        if (Build.VERSION.SDK_INT < 19) {
            this.h.onFailure(-205, "");
        } else {
            if (bluetoothDevice.getBondState() != 10 || bluetoothDevice.createBond()) {
                return;
            }
            this.h.onFailure(-204, "");
        }
    }

    public void connectToA2DP(BluetoothDevice bluetoothDevice) {
        b();
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(this.f2565d, new B(this, bluetoothDevice), 2);
    }

    public abstract ITransmissionLayer createTransmissionLayer(Context context, TransmissionLayer transmissionLayer);

    public void disconnectToA2DP(BluetoothDevice bluetoothDevice) {
        b();
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(this.f2565d, new C(this, bluetoothDevice), 2);
    }

    public void dynamicInstallPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException {
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        if (iTransmissionLayer != null) {
            iTransmissionLayer.installPlugin(iPlugin);
        }
    }

    public int getA2DPConnectionState() {
        return this.g;
    }

    public int getActiveMethodType(BluetoothA2dp bluetoothA2dp) {
        if (bluetoothA2dp == null) {
            return 0;
        }
        try {
            for (Method method : bluetoothA2dp.getClass().getMethods()) {
                if (method.getName().equalsIgnoreCase("getActiveDevice")) {
                    return 1;
                }
                if (method.getName().equalsIgnoreCase("semGetActiveStreamDevice")) {
                    return 3;
                }
                if (method.getName().equalsIgnoreCase("getActiveStreamDevice")) {
                    return 2;
                }
            }
        } catch (Exception unused) {
        }
        return 0;
    }

    public TransmissionLayer getLayerType() {
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        return iTransmissionLayer == null ? TransmissionLayer.NONE : iTransmissionLayer.getLayer();
    }

    public ITransmissionLayer getTransmissionLayer() {
        return this.f2563b;
    }

    public void installPlugin(IPlugin iPlugin) throws UnsupportedPluginTypeException {
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        if (iTransmissionLayer != null) {
            iTransmissionLayer.installPlugin(iPlugin);
        }
    }

    public void onDestroy() {
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        if (iTransmissionLayer != null) {
            iTransmissionLayer.onDestroy();
        }
    }

    public void setTransmissionLayerCallback(ITransmissionLayerCallback iTransmissionLayerCallback) {
        this.f2564c = iTransmissionLayerCallback;
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        if (iTransmissionLayer == null) {
            LogUtils.w(f2562a, "Transmission Layer is not created");
        } else {
            iTransmissionLayer.setTransmissionLayerCallback(iTransmissionLayerCallback);
        }
    }

    public void switchTransmissionLayer(TransmissionLayer transmissionLayer) {
        ITransmissionLayer iTransmissionLayer = this.f2563b;
        if (iTransmissionLayer == null || !iTransmissionLayer.getLayer().equals(transmissionLayer)) {
            ITransmissionLayer iTransmissionLayer2 = this.f2563b;
            if (iTransmissionLayer2 != null) {
                iTransmissionLayer2.onDestroy();
            }
            this.f2563b = createTransmissionLayer(this.f2565d, transmissionLayer);
        }
    }

    public void unregisterReceiver() {
        try {
            this.f2565d.unregisterReceiver(this.i);
        } catch (Exception e) {
            LogUtils.e(f2562a, "Unregister receiver error: " + e.toString());
            e.printStackTrace();
        }
    }

    public final void b() {
        if (this.f == -1) {
            BluetoothAdapter.getDefaultAdapter().getProfileProxy(this.f2565d, new D(this), 2);
        }
    }

    public final void c() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        try {
            this.f2565d.registerReceiver(this.i, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void b(int i) {
        IActionListener<BluetoothDevice> iActionListener;
        ITransmissionLayerCallback iTransmissionLayerCallback = this.f2564c;
        if (iTransmissionLayerCallback != null) {
            iTransmissionLayerCallback.onBindStateUpdate(this.e, i);
        }
        if (i == 12 && this.e.getAddress().equals(this.e.getAddress()) && (iActionListener = this.h) != null) {
            iActionListener.onSuccess(this.e);
        }
    }

    public final boolean a(BluetoothA2dp bluetoothA2dp, BluetoothDevice bluetoothDevice) {
        if (bluetoothA2dp == null) {
            return false;
        }
        try {
            if (this.f == 0) {
                return false;
            }
            String str = "setActiveDevice";
            if (this.f == 1) {
                str = "setActiveDevice";
            } else if (this.f == 2 || this.f == 3) {
                str = "selectstream";
            }
            return ((Boolean) bluetoothA2dp.getClass().getMethod(str, BluetoothDevice.class).invoke(bluetoothA2dp, bluetoothDevice)).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public final BluetoothDevice a(BluetoothA2dp bluetoothA2dp) {
        if (bluetoothA2dp == null) {
            return null;
        }
        try {
            if (this.f == 0) {
                return null;
            }
            String str = "getActiveDevice";
            if (this.f == 1) {
                str = "getActiveDevice";
            } else if (this.f == 2) {
                str = "getActiveStreamDevice";
            } else if (this.f == 3) {
                str = "semGetActiveStreamDevice";
            }
            BluetoothDevice bluetoothDevice = (BluetoothDevice) bluetoothA2dp.getClass().getMethod(str, new Class[0]).invoke(bluetoothA2dp, new Object[0]);
            if (bluetoothDevice == null) {
                return null;
            }
            return bluetoothDevice;
        } catch (Exception unused) {
            return null;
        }
    }

    public final void a(int i) {
        LogUtils.d(f2562a, "update A2DP connection state: " + i);
        ITransmissionLayerCallback iTransmissionLayerCallback = this.f2564c;
        if (iTransmissionLayerCallback != null && i != this.g) {
            iTransmissionLayerCallback.onA2DPConnectionStateUpdate(this.e, i);
        }
        this.g = i;
    }
}
