package com.alibaba.ailabs.iot.bluetoothlesdk;

import aisscanner.ScanCallback;
import aisscanner.ScanFilter;
import aisscanner.ScanResult;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.AuthInfoListener;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.scanner.DirectionalLeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy;
import com.alibaba.ailabs.iot.aisbase.spec.AISManufacturerADData;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.bluetoothlesdk.adv.GenieBLEAdvDevice;
import com.alibaba.ailabs.iot.bluetoothlesdk.datasource.RequestManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.DefaultIoTDeviceManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.FeiyanDeviceManager;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class GenieBLEDeviceManager implements ILeScanStrategy {
    private static final int ALI_IOT_MANUFACTURE_ID = 424;
    private static final String MESH_PROVISIONING_UUID = "00001827-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "GenieBLEDeviceManager";
    private static Map<String, GenieBLEDevice> mCachedBLEDevices = new HashMap();
    private static GenieBLEDeviceManager mInstance;
    private GenieBLEDeviceManagerCallback mGenieBLEDeviceManagerCallback;
    private ILeScanCallback mGetRemoteAnyoneLeScanCallback;
    private boolean mGetRemoteAnyoneDeviceSuccess = false;
    private boolean mGetRemoteSpecifiedPIDDeviceSuccess = false;
    private ILeScanCallback mGetRemoteSpecifiedPIDLeScanCallback = null;
    private BLEScannerProxy mBLEScanProxy = BLEScannerProxy.getInstance();

    private GenieBLEDeviceManager() {
        this.mBLEScanProxy.registerLeScanStrategy(GenieBLEDevice.GENIE_BLE, this);
    }

    public static GenieBLEDeviceManager getInstance() {
        if (mInstance == null) {
            synchronized (GenieBLEDeviceManager.class) {
                if (mInstance == null) {
                    mInstance = new GenieBLEDeviceManager();
                }
            }
        }
        return mInstance;
    }

    public void initNetwork(AuthInfoListener authInfoListener) {
        RequestManager.getInstance().init(authInfoListener, new DefaultIoTDeviceManager());
    }

    public void initFeiyanNetwork() {
        RequestManager.getInstance().init(new AuthInfoListener() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager.1
            @Override // com.alibaba.ailabs.iot.aisbase.AuthInfoListener
            public String getAuthInfo() {
                return null;
            }
        }, new FeiyanDeviceManager());
    }

    public static GenieBLEDevice getRemoteGenieBLEDevice(String str) {
        LogUtils.d(TAG, "get remote genie ble device: " + str);
        if (!BluetoothAdapter.checkBluetoothAddress(str)) {
            throw new IllegalArgumentException(str + " is not a valid Bluetooth address");
        }
        if (mCachedBLEDevices.containsKey(str.toUpperCase())) {
            LogUtils.d(TAG, "device(" + str + ") in the cache");
            return mCachedBLEDevices.get(str.toUpperCase());
        }
        return new GenieBLEDevice(str);
    }

    public void getRemoteGenieBLEDeviceWithScan(Context context, String[] strArr, int i, IActionListener<GenieBLEDevice[]> iActionListener) {
        if (Build.VERSION.SDK_INT >= 26) {
            LogUtils.d(TAG, "Get scanned BLE devices[ " + String.join(",", strArr) + "]");
        }
        BLEScannerProxy.getInstance().startDirectionalLeScan(context, i, strArr, this, new DirectionalLeScanCallback(context, strArr, iActionListener, GenieBLEDevice.class));
    }

    public void getRemoteAnyoneGenieBLEDeviceWithScan(Context context, int i, final IActionListener<GenieBLEDevice> iActionListener) {
        LogUtils.d(TAG, "Get remote anyone genie BLE device");
        this.mGetRemoteAnyoneDeviceSuccess = false;
        BLEScannerProxy bLEScannerProxy = this.mBLEScanProxy;
        int i2 = GenieBLEDevice.GENIE_BLE;
        ILeScanCallback iLeScanCallback = new ILeScanCallback() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager.2
            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStartScan() {
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
                if (!GenieBLEDeviceManager.this.mGetRemoteAnyoneDeviceSuccess && (bluetoothDeviceWrapper instanceof GenieBLEDevice)) {
                    GenieBLEDeviceManager.this.mGetRemoteAnyoneDeviceSuccess = true;
                    GenieBLEDeviceManager.this.mBLEScanProxy.stopScan(this);
                    IActionListener iActionListener2 = iActionListener;
                    if (iActionListener2 != null) {
                        iActionListener2.onSuccess((GenieBLEDevice) bluetoothDeviceWrapper);
                    }
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStopScan() {
                IActionListener iActionListener2;
                GenieBLEDeviceManager.this.mGetRemoteAnyoneLeScanCallback = null;
                if (GenieBLEDeviceManager.this.mGetRemoteAnyoneDeviceSuccess || (iActionListener2 = iActionListener) == null) {
                    return;
                }
                iActionListener2.onFailure(-5, "Scanning device failed");
            }
        };
        this.mGetRemoteAnyoneLeScanCallback = iLeScanCallback;
        bLEScannerProxy.startLeScan(context, i, true, i2, iLeScanCallback);
    }

    public void stopGetRemoteAnyoneGenieBLEDeviceWithScan() {
        ILeScanCallback iLeScanCallback = this.mGetRemoteAnyoneLeScanCallback;
        if (iLeScanCallback != null) {
            this.mBLEScanProxy.stopScan(iLeScanCallback);
        }
    }

    public void getRemoteSpecifiedPIDGenieBLEDeviceWithScan(Context context, final String str, int i, final IActionListener<GenieBLEDevice> iActionListener) {
        LogUtils.d(TAG, String.format("Get remote specified PID(%s) genie BLE device", str));
        this.mGetRemoteSpecifiedPIDDeviceSuccess = false;
        BLEScannerProxy bLEScannerProxy = this.mBLEScanProxy;
        int i2 = GenieBLEDevice.GENIE_BLE;
        ILeScanCallback iLeScanCallback = new ILeScanCallback() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager.3
            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStartScan() {
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
                if (!GenieBLEDeviceManager.this.mGetRemoteSpecifiedPIDDeviceSuccess && (bluetoothDeviceWrapper instanceof GenieBLEDevice)) {
                    if (TextUtils.isEmpty(str) || str.equals(bluetoothDeviceWrapper.getAisManufactureDataADV().getPidStr())) {
                        GenieBLEDeviceManager.this.mGetRemoteSpecifiedPIDDeviceSuccess = true;
                        GenieBLEDeviceManager.this.mBLEScanProxy.stopScan(this);
                        IActionListener iActionListener2 = iActionListener;
                        if (iActionListener2 != null) {
                            iActionListener2.onSuccess((GenieBLEDevice) bluetoothDeviceWrapper);
                        }
                    }
                }
            }

            @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
            public void onStopScan() {
                IActionListener iActionListener2;
                GenieBLEDeviceManager.this.mGetRemoteSpecifiedPIDLeScanCallback = null;
                if (GenieBLEDeviceManager.this.mGetRemoteSpecifiedPIDDeviceSuccess || (iActionListener2 = iActionListener) == null) {
                    return;
                }
                iActionListener2.onFailure(-5, "Scanning device failed");
            }
        };
        this.mGetRemoteSpecifiedPIDLeScanCallback = iLeScanCallback;
        bLEScannerProxy.startLeScan(context, i, true, i2, iLeScanCallback);
    }

    public void stopGetRemoteSpecifiedPIDGenieBLEDeviceWithScan() {
        ILeScanCallback iLeScanCallback = this.mGetRemoteSpecifiedPIDLeScanCallback;
        if (iLeScanCallback != null) {
            this.mBLEScanProxy.stopScan(iLeScanCallback);
        }
    }

    public static void cacheBLEDevice(GenieBLEDevice genieBLEDevice) {
        LogUtils.d(TAG, "cache gma bluetooth device: " + genieBLEDevice.getAddress());
        if (genieBLEDevice != null) {
            mCachedBLEDevices.put(genieBLEDevice.getAddress().toUpperCase(), genieBLEDevice);
        }
    }

    public static void recycleGmaBluetoothDevice(GenieBLEDevice genieBLEDevice) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Recycle gma bluetooth device: ");
        sb.append(genieBLEDevice);
        LogUtils.d(str, sb.toString() == null ? TmpConstant.GROUP_ROLE_UNKNOWN : genieBLEDevice.getAddress());
        if (genieBLEDevice != null) {
            mCachedBLEDevices.remove(genieBLEDevice.getAddress().toUpperCase());
        }
    }

    public void setGenieBLEDeviceManagerCallback(GenieBLEDeviceManagerCallback genieBLEDeviceManagerCallback) {
        this.mGenieBLEDeviceManagerCallback = genieBLEDeviceManagerCallback;
    }

    public void startLeScan(Context context, int i, boolean z) {
        ScanCallback scanCallbackStartLeScan = this.mBLEScanProxy.startLeScan(context, i, z, GenieBLEDevice.GENIE_BLE, this.mGenieBLEDeviceManagerCallback);
        LogUtils.d(TAG, "Start le scan result " + scanCallbackStartLeScan);
    }

    public void stopScan(Context context) {
        GenieBLEDeviceManagerCallback genieBLEDeviceManagerCallback;
        if (!this.mBLEScanProxy.stopScan(this.mGenieBLEDeviceManagerCallback) || (genieBLEDeviceManagerCallback = this.mGenieBLEDeviceManagerCallback) == null) {
            return;
        }
        genieBLEDeviceManagerCallback.onStopScan();
    }

    public void connectToMesh() {
        TgMeshManager.getInstance().connect(new ArrayList());
        LogUtils.d(TAG, "Connected to Mesh.");
    }

    public void disconnectFromMesh() {
        TgMeshManager.getInstance().disconnect();
        LogUtils.d(TAG, "Disconnected from Mesh.");
    }

    public static GenieBLEDevice findBLEDeviceInCacheViaMacAddress(String str) {
        return mCachedBLEDevices.get(str);
    }

    public void startLeScan(Context context, int i, boolean z, GenieBLEDeviceManagerCallback genieBLEDeviceManagerCallback) {
        this.mBLEScanProxy.startLeScan(context, i, z, GenieBLEDevice.GENIE_BLE, genieBLEDeviceManagerCallback);
    }

    public void stopLeScan(Context context, GenieBLEDeviceManagerCallback genieBLEDeviceManagerCallback) {
        this.mBLEScanProxy.stopScan(genieBLEDeviceManagerCallback);
    }

    public boolean checkIfInScanning() {
        return this.mBLEScanProxy.checkIfInScanning();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public List<ScanFilter> getCustomScanFilters() {
        new ScanFilter.Builder().setManufacturerData(424, new byte[]{-88, 1, -91, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new byte[]{-1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).build();
        ScanFilter scanFilterBuild = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(Constants.AIS_PRIMARY_SERVICE)).build();
        ArrayList arrayList = new ArrayList();
        arrayList.add(scanFilterBuild);
        return arrayList;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceWrapper createFromScanResult(ScanResult scanResult) {
        AISManufacturerADData fromBytes;
        GenieBLEDevice genieBLEDevice;
        if (scanResult == null || scanResult.getScanRecord() == null) {
            return null;
        }
        byte[] manufacturerSpecificData = scanResult.getScanRecord().getManufacturerSpecificData(424);
        if ((manufacturerSpecificData != null || (manufacturerSpecificData = a.a(scanResult.getScanRecord().getServiceData(ParcelUuid.fromString("00001827-0000-1000-8000-00805F9B34FB")))) != null) && (fromBytes = AISManufacturerADData.parseFromBytes(manufacturerSpecificData)) != null) {
            LogUtils.d(TAG, "Check subtype: " + com.alibaba.ailabs.iot.aisbase.Utils.byte2String(fromBytes.getBluetoothSubtype(), true));
            byte bluetoothSubtype = fromBytes.getBluetoothSubtype();
            if (bluetoothSubtype == 9) {
                genieBLEDevice = new GenieBLEAdvDevice(scanResult.getDevice());
            } else if (bluetoothSubtype == 11 || bluetoothSubtype == 8) {
                genieBLEDevice = new GenieBLEDevice(scanResult.getDevice());
            } else if (bluetoothSubtype == 12) {
                genieBLEDevice = new GenieBLEDevice(scanResult.getDevice());
                genieBLEDevice.setSubtype(BluetoothDeviceSubtype.COMBO);
            } else {
                genieBLEDevice = (bluetoothSubtype == 0 || bluetoothSubtype == 1 || bluetoothSubtype == 2 || bluetoothSubtype == 3 || bluetoothSubtype == 4) ? new GenieBLEDevice(scanResult.getDevice()) : null;
            }
            if (genieBLEDevice != null) {
                genieBLEDevice.setAisManufactureDataADV(fromBytes);
                genieBLEDevice.setScanResult(scanResult);
                genieBLEDevice.setFromLeScan(true);
                genieBLEDevice.setMeshOtaFlag(bluetoothSubtype == 8);
                return genieBLEDevice;
            }
        }
        return null;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.ILeScanStrategy
    public BluetoothDeviceSubtype getBluetoothDeviceSubtype() {
        return BluetoothDeviceSubtype.BLE;
    }
}
