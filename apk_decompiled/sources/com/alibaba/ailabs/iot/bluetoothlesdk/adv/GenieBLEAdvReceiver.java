package com.alibaba.ailabs.iot.bluetoothlesdk.adv;

import aisscanner.ScanCallback;
import aisscanner.ScanSettings;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.bluetoothlesdk.Utils;
import com.alibaba.ailabs.iot.bluetoothlesdk.d;
import com.alibaba.ailabs.iot.bluetoothlesdk.datasource.RequestManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.bean.DeviceStatus;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import datasource.NetworkCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class GenieBLEAdvReceiver implements ILeScanCallback {
    private static final int MAX_COUNT_IN_TIME_WINDOW = 3;
    private static final int SIZE_RECENTLY_UPLOADED_ARRAY = 2;
    private static final int SIZE_TIME_WINDOW = 5000;
    public static final String TAG = "GenieBLEAdvReceiver";
    private static GenieBLEAdvReceiver mInstance;
    private BLEScannerProxy mBLEScannerProxy = null;
    private ScanCallback mScanCallback = null;
    private List<String> mWhitelist = new ArrayList();
    private boolean mStartFlag = false;
    private byte[][] mRecentlyUploadedDataArray = new byte[2][];
    private int mRecentlyUploadedDataArrayOffset = 0;
    private Map<String, LinkedList<byte[]>> mTimeSeriesUploadedData = new HashMap();
    private Queue<a> mToReportQueue = new LinkedList();
    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    private class a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private byte[] f2745b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private BluetoothDeviceWrapper f2746c;

        public a(byte[] bArr, BluetoothDeviceWrapper bluetoothDeviceWrapper) {
            this.f2745b = bArr;
            this.f2746c = bluetoothDeviceWrapper;
        }

        public byte[] a() {
            return this.f2745b;
        }

        public BluetoothDeviceWrapper b() {
            return this.f2746c;
        }
    }

    private GenieBLEAdvReceiver() {
    }

    public static GenieBLEAdvReceiver getInstance() {
        if (mInstance == null) {
            synchronized (GenieBLEAdvReceiver.class) {
                if (mInstance == null) {
                    mInstance = new GenieBLEAdvReceiver();
                }
            }
        }
        return mInstance;
    }

    public void startListen(Context context) {
        if (this.mStartFlag) {
            LogUtils.w(TAG, "Already listening");
            return;
        }
        LogUtils.d(TAG, "Start listen...");
        if (this.mBLEScannerProxy == null) {
            this.mBLEScannerProxy = BLEScannerProxy.getInstance();
        }
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(0).setUseHardwareFilteringIfSupported(true).build();
        this.mStartFlag = true;
        this.mScanCallback = this.mBLEScannerProxy.startLeScan(context, 0, false, GenieBLEAdvDevice.GENIE_BLE, this, scanSettingsBuild);
    }

    public void stopListen() {
        BLEScannerProxy bLEScannerProxy;
        this.mStartFlag = false;
        if (this.mScanCallback == null || (bLEScannerProxy = this.mBLEScannerProxy) == null) {
            return;
        }
        bLEScannerProxy.stopScan(this);
    }

    public void addWhitelist(String str) {
        if (!BluetoothAdapter.checkBluetoothAddress(str)) {
            LogUtils.w(TAG, "Illegal device address");
        } else {
            this.mWhitelist.add(str);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStartScan() {
        LogUtils.d(TAG, "Scanning begins");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onAliBLEDeviceFound(final BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        final byte[] manufacturerSpecificData;
        if (bluetoothDeviceWrapper instanceof GenieBLEAdvDevice) {
            if (this.mWhitelist.contains(bluetoothDeviceWrapper.getAddress()) && (manufacturerSpecificData = bluetoothDeviceWrapper.getScanRecord().getManufacturerSpecificData(424)) != null) {
                this.mMainThreadHandler.post(new Runnable() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.adv.GenieBLEAdvReceiver.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LogUtils.d(GenieBLEAdvReceiver.TAG, "Append new record");
                        GenieBLEAdvReceiver.this.mToReportQueue.add(GenieBLEAdvReceiver.this.new a(manufacturerSpecificData, bluetoothDeviceWrapper));
                        if (GenieBLEAdvReceiver.this.mToReportQueue.size() == 1) {
                            GenieBLEAdvReceiver.this.next();
                        }
                    }
                });
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStopScan() {
        LogUtils.d(TAG, "End of scan");
        this.mStartFlag = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void next() {
        LogUtils.d(TAG, "Prepare report next data");
        if (this.mToReportQueue.isEmpty()) {
            return;
        }
        a aVarPeek = this.mToReportQueue.peek();
        reportDeviceStatus(aVarPeek.b(), aVarPeek.a(), null);
    }

    public void reportDeviceStatus(final BluetoothDeviceWrapper bluetoothDeviceWrapper, final byte[] bArr, IActionListener<Boolean> iActionListener) {
        final d dVar = new d(iActionListener, bluetoothDeviceWrapper, AgooConstants.MESSAGE_REPORT);
        if (bArr == null || bArr.length < 12) {
            LogUtils.w(TAG, "Illegal Payload: " + ConvertUtils.bytes2HexString(bArr));
            dVar.onFailure(-302, "Illegal Payload: " + ConvertUtils.bytes2HexString(bArr));
            this.mToReportQueue.remove();
            next();
            return;
        }
        if (checkDataInRecentlyUploadedRecord(bArr)) {
            LogUtils.w(TAG, "Conflict with recent history upload data collection, discard");
            this.mToReportQueue.remove();
            next();
            return;
        }
        final long jCurrentTimeMillis = System.currentTimeMillis();
        if (checkDataInTimeWindowCache(bluetoothDeviceWrapper.getAddress(), bArr, jCurrentTimeMillis)) {
            LogUtils.w(TAG, "Conflict with recent time window upload data collection, discard");
            this.mToReportQueue.remove();
            next();
            return;
        }
        String strBytes2HexString = ConvertUtils.bytes2HexString(Arrays.copyOfRange(bArr, 12, bArr.length));
        LogUtils.d(TAG, "Report status(opcode:, parameter:" + strBytes2HexString);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("opcode", (Object) "");
        jSONObject.put("parameters", (Object) strBytes2HexString);
        String jSONString = jSONObject.toJSONString();
        DeviceStatus deviceStatus = new DeviceStatus();
        String userId = RequestManager.getInstance().getUserId();
        deviceStatus.setDevId(bluetoothDeviceWrapper.getAddress());
        deviceStatus.setUserId(userId);
        deviceStatus.setStatus(jSONString);
        deviceStatus.setUuid("");
        RequestManager.getInstance().reportDevicesStatus("", Collections.singletonList(deviceStatus), new NetworkCallback<String>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.adv.GenieBLEAdvReceiver.2
            @Override // datasource.NetworkCallback
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(String str) {
                LogUtils.d(GenieBLEAdvReceiver.TAG, "reportDevicesStatus request success");
                dVar.onSuccess(true);
                GenieBLEAdvReceiver.this.appendToRecentlyUploadedDataArray(bArr);
                GenieBLEAdvReceiver.this.appendToTimeWindowCache(bluetoothDeviceWrapper.getAddress(), bArr, jCurrentTimeMillis);
                GenieBLEAdvReceiver.this.mToReportQueue.remove();
                GenieBLEAdvReceiver.this.next();
            }

            @Override // datasource.NetworkCallback
            public void onFailure(String str, String str2) {
                LogUtils.e(GenieBLEAdvReceiver.TAG, "reportDevicesStatus request failed, errorMessage: " + str2);
                dVar.onFailure(-300, str2);
                GenieBLEAdvReceiver.this.appendToTimeWindowCache(bluetoothDeviceWrapper.getAddress(), bArr, jCurrentTimeMillis);
                GenieBLEAdvReceiver.this.mToReportQueue.remove();
                GenieBLEAdvReceiver.this.next();
            }
        });
    }

    private boolean checkDataInRecentlyUploadedRecord(byte[] bArr) {
        for (int i = 0; i < 2; i++) {
            if (Arrays.equals(this.mRecentlyUploadedDataArray[i], bArr)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendToRecentlyUploadedDataArray(byte[] bArr) {
        byte[][] bArr2 = this.mRecentlyUploadedDataArray;
        int i = this.mRecentlyUploadedDataArrayOffset;
        bArr2[i] = bArr;
        int i2 = i + 1;
        this.mRecentlyUploadedDataArrayOffset = i2;
        this.mRecentlyUploadedDataArrayOffset = i2 % 2;
    }

    private boolean checkDataInTimeWindowCache(String str, byte[] bArr, long j) {
        LinkedList<byte[]> linkedList = this.mTimeSeriesUploadedData.get(str);
        if (linkedList == null) {
            return false;
        }
        int i = 0;
        for (byte[] bArr2 : linkedList) {
            if (bArr2 != null && bArr2.length > 8) {
                long jBytesToLong = Utils.bytesToLong(Arrays.copyOfRange(bArr2, 0, 8));
                byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr2, 8, bArr2.length);
                long j2 = j - jBytesToLong;
                if (j2 < 5000 && Arrays.equals(bArr, bArrCopyOfRange)) {
                    return true;
                }
                if (j2 < 5000) {
                    i++;
                }
            }
        }
        return i >= 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendToTimeWindowCache(String str, byte[] bArr, long j) {
        if (TextUtils.isEmpty(str) || bArr == null) {
            return;
        }
        LinkedList<byte[]> linkedList = this.mTimeSeriesUploadedData.get(str);
        if (linkedList == null) {
            LinkedList<byte[]> linkedList2 = new LinkedList<>();
            linkedList2.addFirst(bArr);
            this.mTimeSeriesUploadedData.put(str, linkedList2);
            return;
        }
        LinkedList linkedList3 = new LinkedList();
        for (byte[] bArr2 : linkedList) {
            if (bArr2 == null || bArr2.length < Utils.SIZE_LONG) {
                linkedList3.add(bArr2);
            }
            if (j - Utils.bytesToLong(Arrays.copyOfRange(bArr2, 0, Utils.SIZE_LONG)) > 5000) {
                linkedList3.add(bArr2);
            }
        }
        linkedList.removeAll(linkedList3);
        if (linkedList.size() > 3) {
            linkedList.removeAll(linkedList.subList(0, linkedList.size() - 3));
        }
        byte[] bArr3 = new byte[Utils.SIZE_LONG + bArr.length];
        System.arraycopy(Utils.longToBytes(j), 0, bArr3, 0, Utils.SIZE_LONG);
        System.arraycopy(bArr, 0, bArr3, Utils.SIZE_LONG, bArr.length);
        linkedList.add(bArr3);
        this.mTimeSeriesUploadedData.put(str, linkedList);
    }
}
