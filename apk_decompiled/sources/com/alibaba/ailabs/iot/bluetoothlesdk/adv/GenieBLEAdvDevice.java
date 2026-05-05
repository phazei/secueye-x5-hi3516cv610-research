package com.alibaba.ailabs.iot.bluetoothlesdk.adv;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.ailabs.iot.aisbase.Utils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.spec.AISManufacturerADData;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice;
import com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager;
import com.alibaba.ailabs.iot.bluetoothlesdk.d;
import com.alibaba.ailabs.iot.bluetoothlesdk.datasource.RequestManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.bean.IotDevice;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import datasource.NetworkCallback;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class GenieBLEAdvDevice extends GenieBLEDevice {
    public static int GENIE_BLE_ADV = 32;
    private static final String TAG = "GenieBLEAdvDevice";
    private Handler mHandler;

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice, com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public /* bridge */ /* synthetic */ BluetoothDeviceWrapper connect(Context context, IActionListener iActionListener) {
        return connect(context, (IActionListener<BluetoothDevice>) iActionListener);
    }

    public GenieBLEAdvDevice(BluetoothDevice bluetoothDevice) {
        super(bluetoothDevice);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public GenieBLEAdvDevice(String str) {
        super(str);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice, com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public GenieBLEDevice connect(final Context context, IActionListener<BluetoothDevice> iActionListener) {
        LogUtils.d(TAG, "Connect...");
        final GenieBLEAdvReceiver genieBLEAdvReceiver = GenieBLEAdvReceiver.getInstance();
        genieBLEAdvReceiver.addWhitelist(getAddress());
        this.mHandler.postDelayed(new Runnable() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.adv.GenieBLEAdvDevice.1
            @Override // java.lang.Runnable
            public void run() {
                genieBLEAdvReceiver.startListen(context.getApplicationContext());
            }
        }, 1000L);
        if (iActionListener != null) {
            iActionListener.onSuccess(getBluetoothDevice());
        }
        GenieBLEDeviceManager.cacheBLEDevice(this);
        return this;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public void disconnect(IActionListener<BluetoothDevice> iActionListener) {
        LogUtils.d(TAG, "Disconnect...");
        GenieBLEAdvReceiver.getInstance().stopListen();
        if (iActionListener != null) {
            iActionListener.onSuccess(getBluetoothDevice());
        }
        GenieBLEDeviceManager.recycleGmaBluetoothDevice(this);
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice
    public void bindDevice(Context context, IActionListener<Boolean> iActionListener) {
        LogUtils.d(TAG, "BindDevice...");
        final d dVar = new d(iActionListener, this, DeviceCommonConstants.VALUE_BOX_BIND);
        IotDevice iotDevice = new IotDevice();
        iotDevice.setPlatform("BLENIADV");
        iotDevice.setSource("app");
        iotDevice.setDevId(getAddress());
        iotDevice.setProductKey(getAisManufactureDataADV().getPidStr());
        if (getScanRecord() == null || getScanRecord().getManufacturerSpecificData(424) == null) {
            LogUtils.e(TAG, "Empty manufacturer data");
            dVar.onFailure(-201, "Empty manufacturer data");
            return;
        }
        byte[] manufacturerSpecificData = getScanRecord().getManufacturerSpecificData(424);
        if (manufacturerSpecificData.length < 10) {
            LogUtils.e(TAG, "Invaild manufacture data");
            dVar.onFailure(-201, "Not enough data length");
            return;
        }
        iotDevice.setIdentifySign(ConvertUtils.bytes2HexString(Arrays.copyOfRange(manufacturerSpecificData, 12, manufacturerSpecificData.length)));
        iotDevice.setUserId(RequestManager.getInstance().getUserId());
        iotDevice.setUuid(RequestManager.getInstance().getUtdId());
        ArrayList arrayList = new ArrayList();
        arrayList.add(iotDevice);
        RequestManager.getInstance().getInfoByAuthInfo("iot", "bindBLEDevice", JSON.toJSONString(arrayList), new NetworkCallback<String>() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.adv.GenieBLEAdvDevice.2
            @Override // datasource.NetworkCallback
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(String str) {
                LogUtils.d(GenieBLEAdvDevice.TAG, "request bind BLE device success");
                dVar.onSuccess(true);
            }

            @Override // datasource.NetworkCallback
            public void onFailure(String str, String str2) {
                LogUtils.e(GenieBLEAdvDevice.TAG, "request bind BLE device failed, errorMessage: " + str2);
                dVar.onFailure(-300, str2);
            }
        });
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice
    public void unbindDevice(Context context, IActionListener<Boolean> iActionListener) {
        super.unbindDevice(context, iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDevice, com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper
    public String getAddress() {
        String macAddress = Utils.formatMacAddress(getAisManufactureDataADV().getMacAddress());
        LogUtils.d(TAG, "Get address: " + macAddress);
        return macAddress;
    }

    public static GenieBLEAdvDevice getGenieBLEAdvDevice(String str, String str2) {
        GenieBLEAdvDevice genieBLEAdvDevice = new GenieBLEAdvDevice(str2);
        byte[] bArr = new byte[14];
        bArr[0] = -88;
        bArr[1] = 1;
        bArr[2] = -107;
        bArr[3] = 0;
        byte[] bArrInt2ByteArrayByLittleEndian = Utils.int2ByteArrayByLittleEndian(Integer.parseInt(str));
        bArr[4] = bArrInt2ByteArrayByLittleEndian[0];
        bArr[5] = bArrInt2ByteArrayByLittleEndian[1];
        String[] strArrSplit = str2.split(":");
        Byte[] bArr2 = new Byte[6];
        for (int i = 0; i < 6; i++) {
            bArr2[i] = Byte.valueOf(Integer.valueOf(Integer.parseInt(strArrSplit[i], 16)).byteValue());
        }
        for (int i2 = 0; i2 < 6; i2++) {
            bArr[11 - i2] = bArr2[i2].byteValue();
        }
        genieBLEAdvDevice.setAisManufactureDataADV(AISManufacturerADData.parseFromBytes(bArr));
        return genieBLEAdvDevice;
    }
}
