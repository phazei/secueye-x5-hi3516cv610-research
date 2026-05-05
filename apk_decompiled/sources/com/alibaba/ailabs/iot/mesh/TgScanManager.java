package com.alibaba.ailabs.iot.mesh;

import a.a.a.a.b.Aa;
import a.a.a.a.b.na;
import a.a.a.a.b.va;
import a.a.a.a.b.wa;
import a.a.a.a.b.xa;
import a.a.a.a.b.ya;
import a.a.a.a.b.za;
import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanCallback;
import aisscanner.ScanFilter;
import aisscanner.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.scan.DeviceScanHandler;
import com.alibaba.ailabs.iot.mesh.scan.NodeScanHandler;
import com.alibaba.ailabs.iot.mesh.scan.ScanHandler;
import com.alibaba.ailabs.iot.mesh.scan.Scanner;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import datasource.channel.MeshNetworkBizOverUnifiedConnectChannel;
import datasource.implemention.DefaultMeshConfig;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TgScanManager {
    public final BroadcastReceiver mBluetoothStateBroadcastReceiver;
    public Context mContext;
    public boolean mGetRemoteSpecifiedPIDDeviceSuccess;
    public ILeScanCallback mGetRemoteSpecifiedPIDLeScanCallback;
    public Handler mHandler;
    public final BroadcastReceiver mLocationProviderChangedReceiver;
    public final ScanCallback mScanCallback;
    public ScanHandler mScanHandler;
    public Scanner mScanner;
    public final Runnable mScannerTimeout;
    public static final String TAG = "tg_mesh_sdk_" + TgScanManager.class.getSimpleName();
    public static int SCAN_TIME_OUT = 8000;

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final TgScanManager f2801a = new TgScanManager(null);
    }

    public /* synthetic */ TgScanManager(va vaVar) {
        this();
    }

    public static TgScanManager getInstance() {
        return a.f2801a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMultiConfigId(@NonNull String str, int i) {
        String[] strArrSplit;
        if (!TextUtils.isEmpty(str) && !TmpConstant.GROUP_ROLE_UNKNOWN.equals(str) && str.contains(",") && (strArrSplit = str.split(",")) != null && strArrSplit.length > 0) {
            String strValueOf = String.valueOf(i);
            for (String str2 : strArrSplit) {
                if (strValueOf.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        a.a.a.a.b.m.a.a(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        a.a.a.a.b.m.a.b(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        a.a.a.a.b.m.a.d(TAG, str);
    }

    private void realStartScan(Context context, boolean z) {
        logd("realStartScan...");
        if (!Utils.isLocationPermissionsGranted(context)) {
            ScanHandler scanHandler = this.mScanHandler;
            if (scanHandler != null) {
                scanHandler.onScanFailed(-10, "location_permmition_not_granted");
            }
            logw("scan failed, location permmition not granted");
            return;
        }
        if (!this.mScanner.isBluetoothEnabled()) {
            ScanHandler scanHandler2 = this.mScanHandler;
            if (scanHandler2 != null) {
                scanHandler2.onScanFailed(-11, "bluetooth_not_enabled");
            }
            logw("scan failed, bluetooth not enabled");
            return;
        }
        if (this.mScanner.isEmpty() && Utils.isLocationRequired(context) && !Utils.isLocationEnabled(context)) {
            Utils.markLocationNotRequired(context);
            ScanHandler scanHandler3 = this.mScanHandler;
            if (scanHandler3 != null) {
                scanHandler3.onScanFailed(-9, "location_not_enabled");
            }
            logw("scan failed, location not enabled");
            return;
        }
        if (this.mScanner.isStartScanRequested() && this.mScanner.isScanning()) {
            logd("isScanning...");
            return;
        }
        this.mScanner.startScanning();
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(2).setReportDelay(0L).setUseHardwareFilteringIfSupported(true).build();
        ParcelUuid parcelUuid = z ? new ParcelUuid(BleMeshManager.MESH_PROXY_UUID) : new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScanFilter.Builder().setServiceUuid(parcelUuid).build());
        try {
            BluetoothLeScannerCompat.getScanner().startScan(arrayList, scanSettingsBuild, this.mScanCallback);
            this.mScanner.scanningStarted();
            this.mHandler.postDelayed(this.mScannerTimeout, SCAN_TIME_OUT);
        } catch (IllegalArgumentException e) {
            loge(e.toString());
        } catch (IllegalStateException unused) {
            ScanHandler scanHandler4 = this.mScanHandler;
            if (scanHandler4 != null) {
                scanHandler4.onScanFailed(-11, "BT le scanner not available or BT Adapter is not turned ON");
            }
            logw("BT le scanner not available or BT Adapter is not turned ON");
        }
    }

    private void realStopScan(Context context) {
        logd("realStopScan...");
        this.mHandler.removeCallbacks(this.mScannerTimeout);
        if (this.mScanner.isScanning()) {
            this.mScanner.stopScanning();
            if (Utils.isBleEnabled()) {
                BluetoothLeScannerCompat.getScanner().stopScan(this.mScanCallback);
            }
            this.mScanner.scanningStopped();
            ScanHandler scanHandler = this.mScanHandler;
            if (scanHandler != null) {
                scanHandler.onScanStop();
            }
            try {
                context.getApplicationContext().unregisterReceiver(this.mBluetoothStateBroadcastReceiver);
                if (Utils.isMarshmallowOrAbove()) {
                    context.getApplicationContext().unregisterReceiver(this.mLocationProviderChangedReceiver);
                }
                a.a.a.a.b.m.a.a(TAG, "unregisterBroadcastReceivers");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                loge(e.getMessage());
            }
        }
    }

    private void resetScanTimeoutTask() {
        if (this.mScanner.isScanning()) {
            a.a.a.a.b.m.a.a(TAG, "Reset scan timeout task");
            this.mHandler.removeCallbacks(this.mScannerTimeout);
            this.mHandler.postDelayed(this.mScannerTimeout, SCAN_TIME_OUT);
        }
    }

    public List<ExtendedBluetoothDevice> getDevices() {
        return this.mScanner.getDevices();
    }

    public int getProductId() {
        ScanHandler scanHandler = this.mScanHandler;
        if (scanHandler instanceof DeviceScanHandler) {
            return ((DeviceScanHandler) scanHandler).getProductId();
        }
        return 0;
    }

    public void getRemoteSpecifiedPIDUnprovisionedComboMeshDeviceWithScan(Context context, String str, int i, IActionListener<UnprovisionedBluetoothMeshDevice> iActionListener) {
        a.a.a.a.b.m.a.a(TAG, "getRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan, PID: " + str);
        this.mGetRemoteSpecifiedPIDDeviceSuccess = false;
        BLEScannerProxy bLEScannerProxy = BLEScannerProxy.getInstance();
        int i2 = UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH;
        Aa aa = new Aa(this, str, iActionListener);
        this.mGetRemoteSpecifiedPIDLeScanCallback = aa;
        bLEScannerProxy.startLeScan(context, i, true, i2, aa);
    }

    public void getRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan(Context context, String str, int i, IActionListener<UnprovisionedBluetoothMeshDevice> iActionListener) {
        a.a.a.a.b.m.a.a(TAG, "getRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan, PID: " + str);
        a.a.a.a.b.m.a.a(TAG, String.format("Get remote specified PID(%s) genie BLE device", str));
        this.mGetRemoteSpecifiedPIDDeviceSuccess = false;
        BLEScannerProxy bLEScannerProxy = BLEScannerProxy.getInstance();
        int i2 = UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH;
        za zaVar = new za(this, str, iActionListener);
        this.mGetRemoteSpecifiedPIDLeScanCallback = zaVar;
        bLEScannerProxy.startLeScan(context, i, true, i2, zaVar);
    }

    public TgScanManager init(AuthInfoListener authInfoListener) {
        logd("TgScanManager init...");
        if (authInfoListener == null) {
            logw("authInfoListener is null");
        }
        na.a().a(authInfoListener, a.a.a.a.b.d.a.f1315a ? new DefaultMeshConfig() : new MeshNetworkBizOverUnifiedConnectChannel());
        return this;
    }

    public void setScanStatusCallback(ScanStatusCallback scanStatusCallback) {
        ScanHandler scanHandler = this.mScanHandler;
        if (scanHandler != null) {
            scanHandler.setScanStatusCallback(scanStatusCallback);
        }
    }

    public TgScanManager setScanTimeOut(int i) {
        SCAN_TIME_OUT = i;
        return this;
    }

    public void startScan(Context context, ScanStatusCallback scanStatusCallback, boolean z) {
        logd("startScan...");
        if (context == null) {
            loge("context is null");
            return;
        }
        this.mContext = context.getApplicationContext();
        if (this.mScanner.isScanning()) {
            if (scanStatusCallback != null) {
                scanStatusCallback.onStatus(7, "on scanning");
            }
            logd("on scanning...");
            setScanStatusCallback(scanStatusCallback);
            resetScanTimeoutTask();
            return;
        }
        this.mScanner.setLocationEnabled(Utils.isLocationEnabled(this.mContext));
        if (Utils.isBleEnabled()) {
            this.mScanner.bluetoothEnabled();
        } else {
            this.mScanner.bluetoothDisabled();
        }
        this.mContext.registerReceiver(this.mBluetoothStateBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        if (Utils.isMarshmallowOrAbove()) {
            this.mContext.registerReceiver(this.mLocationProviderChangedReceiver, new IntentFilter("android.location.MODE_CHANGED"));
        }
        this.mScanHandler = z ? new NodeScanHandler(this.mContext, scanStatusCallback) : new DeviceScanHandler(scanStatusCallback);
        realStartScan(this.mContext, z);
    }

    public void startScanRemoteUnprovisionedSigMeshDevice(int i) {
        int i2 = UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH;
        int i3 = Build.VERSION.SDK_INT;
    }

    public void stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan() {
        a.a.a.a.b.m.a.a(TAG, "stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan");
        if (this.mGetRemoteSpecifiedPIDLeScanCallback != null) {
            BLEScannerProxy.getInstance().stopScan(this.mGetRemoteSpecifiedPIDLeScanCallback);
            this.mGetRemoteSpecifiedPIDLeScanCallback = null;
        }
    }

    public void stopScan(Context context) {
        logd("stopScan...");
        if (context == null) {
            loge("context is null");
            return;
        }
        realStopScan(context);
        ScanHandler scanHandler = this.mScanHandler;
        if (scanHandler != null) {
            scanHandler.exit();
            this.mScanHandler = null;
        }
    }

    public TgScanManager() {
        this.mScanCallback = new va(this);
        this.mLocationProviderChangedReceiver = new wa(this);
        this.mBluetoothStateBroadcastReceiver = new xa(this);
        this.mScannerTimeout = new ya(this);
        this.mGetRemoteSpecifiedPIDDeviceSuccess = false;
        this.mGetRemoteSpecifiedPIDLeScanCallback = null;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mScanner = new Scanner(Utils.isBleEnabled());
    }
}
