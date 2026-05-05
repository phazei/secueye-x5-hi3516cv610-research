package com.alibaba.ailabs.iot.bluetoothlesdk.datasource;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.AuthInfoListener;
import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import com.alibaba.ailabs.iot.iotmtopdatasource.DefaultIoTDeviceManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.FeiyanDeviceManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.IoTDeviceManager;
import com.alibaba.ailabs.iot.iotmtopdatasource.bean.BleControlResponse;
import com.alibaba.ailabs.iot.iotmtopdatasource.bean.DeviceStatus;
import com.alibaba.ailabs.iot.iotmtopdatasource.implemention.data.IotDeleteDeviceRespData;
import com.alibaba.ailabs.iot.iotmtopdatasource.implemention.data.IotDeviceControlRespData;
import com.alibaba.ailabs.iot.iotmtopdatasource.implemention.data.IotReportDevicesStatusRespData;
import com.alibaba.ailabs.iot.iotmtopdatasource.implemention.data.IotReportOtaProgressRespData;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import datasource.NetworkCallback;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RequestManager {
    private static final String TAG = "RequestManager";
    private AuthInfoListener mAuthInfoListener;
    private IoTDeviceManager mIoTDeviceManager;

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final RequestManager f2752a = new RequestManager();
    }

    public static RequestManager getInstance() {
        return a.f2752a;
    }

    public void init(AuthInfoListener authInfoListener, IoTDeviceManager ioTDeviceManager) {
        DefaultIoTDeviceManager feiyanDeviceManager;
        this.mAuthInfoListener = authInfoListener;
        if (AppEnv.IS_GENIE_ENV) {
            feiyanDeviceManager = new DefaultIoTDeviceManager();
        } else {
            feiyanDeviceManager = new FeiyanDeviceManager();
        }
        this.mIoTDeviceManager = feiyanDeviceManager;
    }

    public void getInfoByAuthInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback) {
        LogUtils.d(TAG, String.format("getInfoByAuthInfo called, module(%s), func(%s), args(%s)", str, str2, str3));
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
        } else {
            AuthInfoListener authInfoListener = this.mAuthInfoListener;
            this.mIoTDeviceManager.getInfoByAuthInfo(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, networkCallback);
        }
    }

    public void deleteDevice(@NonNull String str, NetworkCallback<IotDeleteDeviceRespData.Extensions> networkCallback) {
        LogUtils.d(TAG, "deleteDevice called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
            return;
        }
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        String authInfo = authInfoListener != null ? authInfoListener.getAuthInfo() : "";
        try {
            JSONObject object = JSONObject.parseObject(authInfo);
            if (object != null) {
                this.mIoTDeviceManager.deleteDevice(authInfo, object.getString("utdId"), str, networkCallback);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deviceControl(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<IotDeviceControlRespData> networkCallback) {
        LogUtils.d(TAG, "deviceControl called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
            return;
        }
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        this.mIoTDeviceManager.deviceControl(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, networkCallback);
    }

    public void reportDevicesStatus(@NonNull String str, @NonNull List<DeviceStatus> list, NetworkCallback<String> networkCallback) {
        LogUtils.d(TAG, "reportDevicesStatus called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
        } else {
            AuthInfoListener authInfoListener = this.mAuthInfoListener;
            this.mIoTDeviceManager.reportDevicesStatus(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, list, networkCallback);
        }
    }

    public void reportDeviceOnlineStatus(@NonNull String str, @NonNull String str2, @NonNull boolean z, @NonNull String str3, @NonNull String str4, @NonNull String str5, NetworkCallback<IotReportDevicesStatusRespData.OnlineRespModel> networkCallback) {
        LogUtils.d(TAG, "reportDeviceOnlineStatus called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str2);
        jSONObject.put("isOnline", (Object) Boolean.valueOf(z));
        jSONObject.put("version", (Object) str3);
        jSONObject.put("searchSource", (Object) str4);
        jSONObject.put("subDevicePlatform", (Object) str5);
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        this.mIoTDeviceManager.reportOnlineStatus(authInfoListener != null ? authInfoListener.getAuthInfo() : "", jSONObject.toJSONString(), networkCallback);
    }

    public void reportOtaProgress(@NonNull String str, @NonNull String str2, @NonNull boolean z, @NonNull String str3, @NonNull String str4, NetworkCallback<IotReportOtaProgressRespData> networkCallback) {
        LogUtils.d(TAG, "reportOtaProgress called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("productKey", (Object) str);
        jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) str2);
        jSONObject.put("isOnline", (Object) Boolean.valueOf(z));
        jSONObject.put("version", (Object) str3);
        jSONObject.put(TmpConstant.SERVICE_DESC, (Object) "app");
        jSONObject.put(WifiProvisionUtConst.KEY_STEP, (Object) str4);
        LogUtils.d(TAG, "Report OTA Progress..." + jSONObject.toJSONString());
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        this.mIoTDeviceManager.reportOtaProgressNew(authInfoListener != null ? authInfoListener.getAuthInfo() : "", jSONObject.toJSONString(), networkCallback);
    }

    public void reportFeiyanOtaProgress(@NonNull JSONObject jSONObject, NetworkCallback<Object> networkCallback) {
        LogUtils.d(TAG, "reportOtaProgress called...");
        IoTDeviceManager ioTDeviceManager = this.mIoTDeviceManager;
        if (ioTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
        } else {
            ioTDeviceManager.reportFeiyanOtaProgressNew(jSONObject, networkCallback);
        }
    }

    public void reportBleControlResult(@NonNull String str, @NonNull String str2, BleControlResponse bleControlResponse, NetworkCallback<String> networkCallback) {
        LogUtils.d(TAG, "reportBleControlResult called...");
        if (this.mIoTDeviceManager == null) {
            LogUtils.e(TAG, "IoTDeviceManager is null");
            return;
        }
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        this.mIoTDeviceManager.bleControlResult(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, bleControlResponse, networkCallback);
    }

    public String getUserId() {
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        if (authInfoListener == null) {
            LogUtils.e(TAG, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("userId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUtdId() {
        AuthInfoListener authInfoListener = this.mAuthInfoListener;
        if (authInfoListener == null) {
            LogUtils.e(TAG, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("utdId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
