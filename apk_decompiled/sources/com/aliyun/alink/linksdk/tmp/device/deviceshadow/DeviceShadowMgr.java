package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.connect.a;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.CmpNotifyManager;
import com.aliyun.alink.linksdk.tmp.data.deviceshadow.UpdateParam;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.component.AliasNotifyData;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.component.PropertyAlias;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended.GetDeviceExtendRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener;
import com.aliyun.alink.linksdk.tmp.device.request.other.GetDeviceNetTypesSupportedRequest;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CheckMeshMessage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.breeze.mix.ConnectionCallback;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceShadowMgr implements IConnectNotifyListener, INotifyHandler {
    private static final String DEVICESHADOW_CACHE_FILE = "deviceShadow";
    private static final String DEVICESHADOW_DETAILINFO_PRE_KEY = "device_detailInfo_";
    private static final String DEVICESHADOW_PROPERTY_PRE_KEY = "device_property_";
    private static final String DEVICESHADOW_STATUS_PRE_KEY = "device_status_";
    private static final String DEVICESHADOW_SUPPORTED_NETTYPE_PRE_KEY = "device_supported_nettype_";
    private static final String DEVICESHADOW_SUPPORT_DOWN_ALL_PROPS_PRE_KEY = "device_supportDownAllProps_";
    private static final String DEVICESHADOW_TSL_PRE_KEY = "device_tsl_";
    private static final int DEVICESHADOW_VERSION = 1;
    private static final String DEVICESHADOW_WIFISTATUS_PRE_KEY = "device_wifistatus_";
    public static final String TAG = "[Tmp]DeviceShadowMgr";
    private DiskLruHelper mDiskLruCacheHelper;
    private MemoryLruHelper mMemoryLruHelper;
    private PropertyAlias mPropertyAlias;

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onConnectStateChange(String str, ConnectState connectState) {
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public boolean shouldHandle(String str, String str2) {
        return true;
    }

    private DeviceShadowMgr() {
        this.mDiskLruCacheHelper = new DiskLruHelper(DEVICESHADOW_CACHE_FILE, 1);
        this.mMemoryLruHelper = new MemoryLruHelper();
        this.mPropertyAlias = new PropertyAlias(this.mDiskLruCacheHelper);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_PROPERTIES, this);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_STATUS, this);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_NOTIFY, this);
    }

    private static class InstanceHolder {
        private static DeviceShadowMgr mInstance = new DeviceShadowMgr();

        private InstanceHolder() {
        }
    }

    public static DeviceShadowMgr getInstance() {
        return InstanceHolder.mInstance;
    }

    public String getCachedProps(String str) {
        return this.mMemoryLruHelper.getString(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str));
    }

    public void getTsl(String str, IProcessListener iProcessListener) {
        if (iProcessListener == null) {
            ALog.e(TAG, "gettsl processListener empty");
            return;
        }
        String cacheKey = getCacheKey(DEVICESHADOW_TSL_PRE_KEY, str);
        String string = this.mDiskLruCacheHelper.getString(cacheKey);
        StringBuilder sb = new StringBuilder();
        sb.append("gettsl iotId: ");
        sb.append(str);
        sb.append(" processListener:");
        sb.append(iProcessListener);
        sb.append(" cacheKey:");
        sb.append(cacheKey);
        sb.append(" isCallbacked:");
        boolean z = false;
        int length = 0;
        sb.append(false);
        sb.append(" tsl:");
        sb.append(string);
        ALog.d(TAG, sb.toString());
        if (!TextUtils.isEmpty(string)) {
            ALog.d(TAG, "getTsl() *** called with: iotId = [" + str + "], processListener = [" + iProcessListener + "]");
            CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
            commonResponsePayload.setCode(200);
            try {
                commonResponsePayload.setData(JSON.parseObject(string));
                String jSONString = JSON.toJSONString(commonResponsePayload);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getTsl response payload:");
                if (!TextUtils.isEmpty(jSONString)) {
                    length = jSONString.length();
                }
                sb2.append(length);
                ALog.d(TAG, sb2.toString());
                iProcessListener.onSuccess(jSONString);
            } catch (Exception e) {
                ALog.e(TAG, "getTsl onSuccess error:" + e.toString());
            }
            z = true;
        }
        ALog.d(TAG, "getTsl() called with: iotId = [" + str + "], processListener = [" + iProcessListener + "]");
        updateTslByCloud(str, z, iProcessListener);
    }

    public String getTsl(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_TSL_PRE_KEY, str));
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        try {
            commonResponsePayload.setData(JSON.parseObject(string));
            String jSONString = JSON.toJSONString(commonResponsePayload);
            StringBuilder sb = new StringBuilder();
            sb.append("getTsl response payload:");
            sb.append(TextUtils.isEmpty(jSONString) ? 0 : jSONString.length());
            ALog.d(TAG, sb.toString());
            return jSONString;
        } catch (Exception e) {
            ALog.e(TAG, "getTsl onSuccess error:" + e.toString());
            return null;
        }
    }

    public void setTsl(String str, String str2) throws Throwable {
        ALog.d(TAG, "setTsl() called with: iotId = [" + str + "], tsl = [" + str2 + "]");
        this.mDiskLruCacheHelper.saveValue(getCacheKey(DEVICESHADOW_TSL_PRE_KEY, str), str2);
    }

    public void setSupportDownAllProps(String str, boolean z) throws Throwable {
        ALog.d(TAG, "setIsAllPropsDevice() called with: iotId = [" + str + "], supportDownAllProps = [" + z + "]");
        this.mDiskLruCacheHelper.saveValue(getCacheKey(DEVICESHADOW_SUPPORT_DOWN_ALL_PROPS_PRE_KEY, str), String.valueOf(z));
    }

    public boolean getSupportDownAllProps(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_SUPPORT_DOWN_ALL_PROPS_PRE_KEY, str));
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        ALog.d(TAG, "getAllPropsDevice isAllPropsDevice: " + string);
        return string.equals("true");
    }

    public void getDetailInfo(String str, IProcessListener iProcessListener) {
        if (iProcessListener == null) {
            ALog.e(TAG, "getDetailInfo processListener empty");
            return;
        }
        boolean z = false;
        String cacheKey = getCacheKey(DEVICESHADOW_DETAILINFO_PRE_KEY, str);
        String string = this.mDiskLruCacheHelper.getString(cacheKey);
        ALog.d(TAG, "getDeviceDetailInfo iotId: " + str + " processListener:" + iProcessListener + " cacheKey:" + cacheKey + " isCallbacked:false deviceDetailInfo:" + string);
        if (!TextUtils.isEmpty(string)) {
            z = true;
            CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
            commonResponsePayload.setCode(200);
            try {
                commonResponsePayload.setData(JSON.parseObject(string));
                iProcessListener.onSuccess(JSON.toJSONString(commonResponsePayload));
            } catch (Exception e) {
                ALog.e(TAG, "getDetailInfo onSuccess error:" + e.toString());
            }
        }
        updateDetailInfoByCloud(str, z, iProcessListener);
    }

    public String getDetailInfo(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_DETAILINFO_PRE_KEY, str));
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        try {
            commonResponsePayload.setData(JSON.parseObject(string));
            return JSON.toJSONString(commonResponsePayload);
        } catch (Exception e) {
            ALog.e(TAG, "getDetailInfo onSuccess error:" + e.toString());
            return null;
        }
    }

    public boolean isPropertyCached(String str) {
        boolean z = !TextUtils.isEmpty(this.mMemoryLruHelper.getString(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str)));
        ALog.d(TAG, "isPropertyCached iotId:" + str + " ret:" + z);
        return z;
    }

    public void getProps(String str, DeviceShadowFetcher deviceShadowFetcher, IProcessListener iProcessListener) {
        getProps(str, false, deviceShadowFetcher, iProcessListener);
    }

    public void getProps(final String str, final boolean z, final DeviceShadowFetcher deviceShadowFetcher, final IProcessListener iProcessListener) {
        final boolean z2;
        if (iProcessListener == null) {
            ALog.e(TAG, "getProps processListener empty");
            return;
        }
        String cacheKey = getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str);
        final String string = this.mMemoryLruHelper.getString(cacheKey);
        ALog.d(TAG, "getProps iotId: " + str + " processListener:" + iProcessListener + " cacheKey:" + cacheKey + " isCallbacked:false propertiesStr:" + string);
        if (TextUtils.isEmpty(string)) {
            z2 = false;
        } else {
            CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
            commonResponsePayload.setCode(200);
            try {
                commonResponsePayload.setData(JSON.parseObject(string));
                String jSONString = JSON.toJSONString(commonResponsePayload);
                iProcessListener.onSuccess(jSONString);
                Log.d(TAG, "getLocalProperty,local value=[ " + jSONString + " ]");
            } catch (Exception e) {
                ALog.e(TAG, "getProps onSuccess error:" + e.toString());
            }
            z2 = true;
        }
        if (CheckMeshMessage.containsMessage(str)) {
            ALog.d(TAG, "getProps: mesh device is in optimistic Update time ,no need get from cloud");
            return;
        }
        ALog.d(TAG, "getProps: isCallbacked = " + z2);
        if (deviceShadowFetcher != null) {
            deviceShadowFetcher.getProperties(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z3, Object obj) {
                    ALog.d(DeviceShadowMgr.TAG, "getProperties onComplete isSuccess: " + z3 + " data:" + obj);
                    if (obj == null || !z3) {
                        if (z2) {
                            return;
                        }
                        iProcessListener.onFail(new ErrorInfo(300, "getProperties error error"));
                        return;
                    }
                    try {
                        DeviceShadowMgr.this.updatePropertyCacheAndNotify(str, JSON.parseObject(String.valueOf(obj)).getJSONObject("data"), string, z || z2, new DeviceShadowNotifier(deviceShadowFetcher.getMultipleChannelDevice()));
                    } catch (Exception e2) {
                        ALog.e(DeviceShadowMgr.TAG, "getProperties onComplete updatePropertyCacheAndNotify error:" + e2.toString());
                    }
                    if (z2) {
                        return;
                    }
                    iProcessListener.onSuccess(obj);
                }
            });
        } else {
            if (z2) {
                return;
            }
            iProcessListener.onFail(new ErrorInfo(300, "getProperties device empty error"));
        }
    }

    public String getProps(String str) {
        String cacheKey = getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str);
        String string = this.mMemoryLruHelper.getString(cacheKey);
        ALog.d(TAG, "getProps iotId: " + str + " cacheKey:" + cacheKey + " propertiesStr:" + string);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        try {
            commonResponsePayload.setData(JSON.parseObject(string));
            return JSON.toJSONString(commonResponsePayload);
        } catch (Exception e) {
            ALog.e(TAG, "getProps onSuccess error:" + e.toString());
            return null;
        }
    }

    public void getStatus(final String str, final DeviceShadowFetcher deviceShadowFetcher, final IProcessListener iProcessListener) {
        final boolean z;
        if (iProcessListener == null) {
            ALog.e(TAG, "getStatus processListener empty");
            return;
        }
        String cacheKey = getCacheKey(DEVICESHADOW_STATUS_PRE_KEY, str);
        final String string = this.mMemoryLruHelper.getString(cacheKey);
        ALog.d(TAG, "getStatus iotId: " + str + " processListener:" + iProcessListener + " cacheKey:" + cacheKey + " isCallbacked:false status:" + string);
        if (TextUtils.isEmpty(string)) {
            z = false;
        } else {
            CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
            commonResponsePayload.setCode(200);
            try {
                commonResponsePayload.setData(JSON.parseObject(string));
                iProcessListener.onSuccess(JSON.toJSONString(commonResponsePayload));
            } catch (Exception e) {
                ALog.e(TAG, "getStatus onSuccess error:" + e.toString());
            }
            z = true;
        }
        if (deviceShadowFetcher != null) {
            deviceShadowFetcher.getStatus(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, Object obj) {
                    ALog.d(DeviceShadowMgr.TAG, "getStatus onComplete isSuccess: " + z2 + " data:" + obj);
                    if (obj == null || !z2) {
                        if (z) {
                            return;
                        }
                        iProcessListener.onFail(new ErrorInfo(300, "getStatus error error"));
                        return;
                    }
                    try {
                        DeviceShadowMgr.this.updateStatusCacheAndNotify(str, JSON.parseObject(String.valueOf(obj)).getJSONObject("data"), string, z, new DeviceShadowNotifier(deviceShadowFetcher.getMultipleChannelDevice()));
                    } catch (Exception e2) {
                        ALog.e(DeviceShadowMgr.TAG, "getStatus notify error:" + e2.toString());
                    }
                    if (z) {
                        return;
                    }
                    iProcessListener.onSuccess(obj);
                }
            });
        } else {
            if (z) {
                return;
            }
            iProcessListener.onFail(new ErrorInfo(300, "getStatus device empty error"));
        }
    }

    public String getStatus(String str) {
        String cacheKey = getCacheKey(DEVICESHADOW_STATUS_PRE_KEY, str);
        String string = this.mMemoryLruHelper.getString(cacheKey);
        ALog.d(TAG, "getStatus iotId: " + str + " cacheKey:" + cacheKey + " isCallbacked:false status:" + string);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        try {
            commonResponsePayload.setData(JSON.parseObject(string));
            return JSON.toJSONString(commonResponsePayload);
        } catch (Exception e) {
            ALog.e(TAG, "getStatus onSuccess error:" + e.toString());
            return null;
        }
    }

    public void updateDeviceWifiStatus(final String str, final boolean z, final IProcessListener iProcessListener) {
        GateWayRequest getDeviceExtendRequest = new GetDeviceExtendRequest(str, TmpConstant.DATA_KEY_DEVICE_WIFI_STATUS);
        getDeviceExtendRequest.sendRequest(getDeviceExtendRequest, new IGateWayRequestListener<GetDeviceExtendRequest, GetDeviceExtendRequest.DeviceExtendGetResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetDeviceExtendRequest getDeviceExtendRequest2, GetDeviceExtendRequest.DeviceExtendGetResponse deviceExtendGetResponse) throws Throwable {
                IProcessListener iProcessListener2;
                DeviceShadowMgr.this.mDiskLruCacheHelper.saveValue(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_WIFISTATUS_PRE_KEY, str), (String) deviceExtendGetResponse.data);
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onSuccess(JSON.toJSONString(deviceExtendGetResponse));
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetDeviceExtendRequest getDeviceExtendRequest2, AError aError) {
                IProcessListener iProcessListener2;
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onFail(new ErrorInfo(aError));
            }
        });
    }

    public void getDeviceWifiStatus(String str, IProcessListener iProcessListener) {
        getDeviceWifiStatus(str);
        updateDeviceWifiStatus(str, true, iProcessListener);
    }

    public void setDeviceWifiStatus(String str, TmpEnum.DeviceWifiStatus deviceWifiStatus) {
        if (deviceWifiStatus == null) {
            ALog.e(TAG, "setDeviceWifiStatus deviceWifiStatus empty iotId:" + str);
            return;
        }
        this.mDiskLruCacheHelper.saveValue(getCacheKey(DEVICESHADOW_WIFISTATUS_PRE_KEY, str), String.valueOf(deviceWifiStatus.getValue()));
    }

    public String getDeviceWifiStatus(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_WIFISTATUS_PRE_KEY, str));
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            commonResponsePayload.setData(string);
            String jSONString = JSON.toJSONString(commonResponsePayload);
            ALog.d(TAG, "getDeviceWifiStatus response payload:" + jSONString);
            return jSONString;
        } catch (Exception e) {
            ALog.e(TAG, "getDeviceWifiStatus onSuccess error:" + e.toString());
            return null;
        }
    }

    public void getDeviceSupportedNetTypesByIotId(String str, IProcessListener iProcessListener) {
        boolean z;
        String deviceSupportedNetTypesByIotId = getDeviceSupportedNetTypesByIotId(str);
        if (TextUtils.isEmpty(deviceSupportedNetTypesByIotId)) {
            z = false;
        } else {
            if (iProcessListener != null) {
                iProcessListener.onSuccess(deviceSupportedNetTypesByIotId);
            }
            z = true;
        }
        updateDeviceNetTypesSupportedByIotId(str, !z, iProcessListener);
    }

    public String getDeviceSupportedNetTypesByIotId(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_SUPPORTED_NETTYPE_PRE_KEY, str));
        GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse = new GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse();
        getDeviceNetTypesSupportedResponse.code = 200;
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            getDeviceNetTypesSupportedResponse.setData((List) JSON.parseObject(string, List.class));
            String jSONString = JSON.toJSONString(getDeviceNetTypesSupportedResponse);
            StringBuilder sb = new StringBuilder();
            sb.append("getDeviceSupportedNetTypesByIotId response payload:");
            sb.append(TextUtils.isEmpty(jSONString) ? "" : jSONString);
            ALog.d(TAG, sb.toString());
            return jSONString;
        } catch (Exception e) {
            ALog.e(TAG, "getDeviceSupportedNetTypesByIotId onSuccess error:" + e.toString());
            return null;
        }
    }

    public void updateDeviceNetTypesSupportedByIotId(final String str, final boolean z, final IProcessListener iProcessListener) {
        GateWayRequest getDeviceNetTypesSupportedRequest = new GetDeviceNetTypesSupportedRequest(null, str);
        getDeviceNetTypesSupportedRequest.sendRequest(getDeviceNetTypesSupportedRequest, new IGateWayRequestListener<GetDeviceNetTypesSupportedRequest, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.4
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetDeviceNetTypesSupportedRequest getDeviceNetTypesSupportedRequest2, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse) throws Throwable {
                IProcessListener iProcessListener2;
                DeviceShadowMgr.this.mDiskLruCacheHelper.saveValue(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_SUPPORTED_NETTYPE_PRE_KEY, str), JSON.toJSONString(getDeviceNetTypesSupportedResponse.data));
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onSuccess(JSON.toJSONString(getDeviceNetTypesSupportedResponse));
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetDeviceNetTypesSupportedRequest getDeviceNetTypesSupportedRequest2, AError aError) {
                IProcessListener iProcessListener2;
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onFail(new ErrorInfo(aError));
            }
        });
    }

    public void getDeviceSupportedNetTypesByPk(String str, IProcessListener iProcessListener) {
        boolean z;
        String deviceSupportedNetTypesByPk = getDeviceSupportedNetTypesByPk(str);
        if (TextUtils.isEmpty(deviceSupportedNetTypesByPk)) {
            z = false;
        } else {
            if (iProcessListener != null) {
                iProcessListener.onSuccess(deviceSupportedNetTypesByPk);
            }
            z = true;
        }
        updateDeviceNetTypesSupportedByPk(str, !z, iProcessListener);
    }

    public String getDeviceSupportedNetTypesByPk(String str) {
        String string = this.mDiskLruCacheHelper.getString(getCacheKey(DEVICESHADOW_SUPPORTED_NETTYPE_PRE_KEY, str));
        GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse = new GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse();
        getDeviceNetTypesSupportedResponse.code = 200;
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            getDeviceNetTypesSupportedResponse.setData((List) JSON.parseObject(string, List.class));
            String jSONString = JSON.toJSONString(getDeviceNetTypesSupportedResponse);
            StringBuilder sb = new StringBuilder();
            sb.append("getDeviceSupportedNetTypesByPk response payload:");
            sb.append(TextUtils.isEmpty(jSONString) ? "" : jSONString);
            ALog.d(TAG, sb.toString());
            return jSONString;
        } catch (Exception e) {
            ALog.e(TAG, "getDeviceWifiStatus onSuccess error:" + e.toString());
            return null;
        }
    }

    public void updateDeviceNetTypesSupportedByPk(final String str, final boolean z, final IProcessListener iProcessListener) {
        GateWayRequest getDeviceNetTypesSupportedRequest = new GetDeviceNetTypesSupportedRequest(str, null);
        getDeviceNetTypesSupportedRequest.sendRequest(getDeviceNetTypesSupportedRequest, new IGateWayRequestListener<GetDeviceNetTypesSupportedRequest, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.5
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetDeviceNetTypesSupportedRequest getDeviceNetTypesSupportedRequest2, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse) throws Throwable {
                IProcessListener iProcessListener2;
                DeviceShadowMgr.this.mDiskLruCacheHelper.saveValue(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_SUPPORTED_NETTYPE_PRE_KEY, str), JSON.toJSONString(getDeviceNetTypesSupportedResponse.data));
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onSuccess(JSON.toJSONString(getDeviceNetTypesSupportedResponse));
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetDeviceNetTypesSupportedRequest getDeviceNetTypesSupportedRequest2, AError aError) {
                IProcessListener iProcessListener2;
                if (!z || (iProcessListener2 = iProcessListener) == null) {
                    return;
                }
                iProcessListener2.onFail(new ErrorInfo(aError));
            }
        });
    }

    public void refreshDeviceShadow(String str, UpdateParam updateParam, IProcessListener iProcessListener) {
        boolean z;
        ALog.d(TAG, "refreshDeviceShadow iotId:" + str + " updateParam:" + updateParam + " processListener:" + iProcessListener);
        if (TextUtils.isEmpty(str) || updateParam == null || updateParam.updateType == null) {
            ALog.e(TAG, "updateCahce empty error");
            return;
        }
        DeviceShadowRefreshListener deviceShadowRefreshListener = new DeviceShadowRefreshListener(iProcessListener, updateParam.updateType.getValue());
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_PROPERTIES.getValue()) > 0) {
            updatePropertiesByCloud(str, deviceShadowRefreshListener);
            z = true;
        } else {
            z = false;
        }
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_STATUS.getValue()) > 0) {
            updateStatusByCloud(str, deviceShadowRefreshListener);
            z = true;
        }
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_DEVICE_DETAIL_INFO.getValue()) > 0) {
            updateDetailInfoByCloud(str, false, deviceShadowRefreshListener);
            z = true;
        }
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_TSL.getValue()) > 0) {
            updateTslByCloud(str, false, deviceShadowRefreshListener);
            z = true;
        }
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_DEVICE_WIFI_STATUS.getValue()) > 0) {
            updateDeviceWifiStatus(str, true, deviceShadowRefreshListener);
            z = true;
        }
        if (updateParam.updateType == TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_ALL || (updateParam.updateType.getValue() & TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_DEVICE_NET_TYPE.getValue()) > 0) {
            updateDeviceNetTypesSupportedByIotId(str, true, deviceShadowRefreshListener);
            z = true;
        }
        if (z) {
            return;
        }
        ALog.e(TAG, "updateCahce updateType error:" + updateParam.updateType);
        if (iProcessListener != null) {
            iProcessListener.onFail(new ErrorInfo(300, "type error"));
        }
    }

    public void deleteDeviceShadow(String str, IProcessListener iProcessListener) {
        ALog.d(TAG, "deleteDeviceShadow iotId:" + str + " processListener:" + iProcessListener);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "deleteDeviceShadow empty error");
            return;
        }
        this.mDiskLruCacheHelper.deleteValue(getCacheKey(DEVICESHADOW_TSL_PRE_KEY, str));
        this.mDiskLruCacheHelper.deleteValue(getCacheKey(DEVICESHADOW_DETAILINFO_PRE_KEY, str));
        this.mMemoryLruHelper.deleteValue(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str));
        this.mMemoryLruHelper.deleteValue(getCacheKey(DEVICESHADOW_STATUS_PRE_KEY, str));
        this.mDiskLruCacheHelper.deleteValue(getCacheKey(DEVICESHADOW_SUPPORT_DOWN_ALL_PROPS_PRE_KEY, str));
        TmpStorage.DeviceInfo deviceInfo = TmpStorage.getInstance().getDeviceInfo(str);
        if (deviceInfo != null) {
            TmpStorage.getInstance().saveAccessInfo(null, null, deviceInfo.getId());
        }
        if (iProcessListener != null) {
            iProcessListener.onSuccess(null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void updateStatusCacheAndNotify(java.lang.String r7, com.alibaba.fastjson.JSONObject r8, java.lang.String r9, boolean r10, com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowNotifier r11) {
        /*
            r6 = this;
            if (r8 == 0) goto L97
            boolean r0 = r8.isEmpty()
            if (r0 == 0) goto La
            goto L97
        La:
            r0 = 0
            r1 = 0
            java.lang.String r0 = java.lang.String.valueOf(r8)     // Catch: java.lang.Exception -> L1e
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Exception -> L1e
            if (r2 != 0) goto L1c
            boolean r2 = r9.equalsIgnoreCase(r0)     // Catch: java.lang.Exception -> L1e
            if (r2 != 0) goto L3a
        L1c:
            r1 = 1
            goto L3a
        L1e:
            r2 = move-exception
            java.lang.String r3 = "[Tmp]DeviceShadowMgr"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "updateStatusCacheAndNotify error:"
            r4.append(r5)
            java.lang.String r2 = r2.toString()
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            com.aliyun.alink.linksdk.tools.ALog.e(r3, r2)
        L3a:
            java.lang.String r2 = "[Tmp]DeviceShadowMgr"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "updateStatusCacheAndNotify iotId:"
            r3.append(r4)
            r3.append(r7)
            java.lang.String r4 = " newValue:"
            r3.append(r4)
            r3.append(r8)
            java.lang.String r4 = " oldValue:"
            r3.append(r4)
            r3.append(r9)
            java.lang.String r9 = " isCallbacked:"
            r3.append(r9)
            r3.append(r10)
            java.lang.String r9 = " notifier:"
            r3.append(r9)
            r3.append(r11)
            java.lang.String r9 = " isDifference:"
            r3.append(r9)
            r3.append(r1)
            java.lang.String r9 = r3.toString()
            com.aliyun.alink.linksdk.tools.ALog.d(r2, r9)
            com.aliyun.alink.linksdk.tmp.device.deviceshadow.MemoryLruHelper r9 = r6.mMemoryLruHelper
            r9.setRefreshProperty(r7, r1)
            if (r1 == 0) goto L96
            com.aliyun.alink.linksdk.tmp.device.deviceshadow.MemoryLruHelper r9 = r6.mMemoryLruHelper
            java.lang.String r1 = "device_status_"
            java.lang.String r7 = getCacheKey(r1, r7)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r9.saveValue(r7, r0)
            if (r10 == 0) goto L96
            if (r11 == 0) goto L96
            r11.notifyStatusChange(r8)
        L96:
            return
        L97:
            java.lang.String r7 = "[Tmp]DeviceShadowMgr"
            java.lang.String r8 = "updateStatusCacheAndNotify newData empty"
            com.aliyun.alink.linksdk.tools.ALog.e(r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.updateStatusCacheAndNotify(java.lang.String, com.alibaba.fastjson.JSONObject, java.lang.String, boolean, com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowNotifier):void");
    }

    protected void updatePropertyCacheAndNotify(String str, JSONObject jSONObject, String str2, boolean z, DeviceShadowNotifier deviceShadowNotifier) {
        if (jSONObject == null || jSONObject.isEmpty()) {
            ALog.e(TAG, "updatePropertyCacheAndNotify newData empty");
            return;
        }
        try {
            JSONObject jSONObject2 = str2 == null ? new JSONObject() : JSON.parseObject(str2);
            HashSet<String> hashSet = new HashSet(jSONObject.keySet());
            hashSet.addAll(jSONObject2.keySet());
            JSONObject jSONObject3 = new JSONObject();
            ALog.d(TAG, "updatePropertyCacheAndNotify propertyKeySet:" + hashSet + " iotId:" + str);
            boolean z2 = false;
            for (String str3 : hashSet) {
                JSONObject jSONObject4 = jSONObject.getJSONObject(str3);
                JSONObject jSONObject5 = jSONObject2.getJSONObject(str3);
                if (comparePropertyValue(jSONObject4, jSONObject5)) {
                    z2 = true;
                    jSONObject3.put(str3, (Object) jSONObject4);
                } else {
                    jSONObject3.put(str3, (Object) jSONObject5);
                }
            }
            ALog.d(TAG, "updatePropertyCacheAndNotify iotId:" + str + " newData:" + jSONObject + " oldValue:" + str2 + " saveData:" + jSONObject3 + " needNotify:" + z + " notifier:" + deviceShadowNotifier + " isDifference:" + z2);
            if (z2) {
                this.mMemoryLruHelper.saveValue(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str), jSONObject3.toString());
                if (!z || deviceShadowNotifier == null) {
                    return;
                }
                deviceShadowNotifier.notifyPropertyChange(jSONObject);
            }
        } catch (Exception e) {
            ALog.e(TAG, "updatePropertyCacheAndNotify error:" + e.toString());
        }
    }

    public void optimisticUpdateMeshDevice(String str, JSONObject jSONObject) {
        Log.d(TAG, "optimisticUpdateMeshDevice() called with: iotId = [" + str + "], newData = [" + jSONObject + "]");
        this.mMemoryLruHelper.saveValue(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, str), jSONObject.toString());
    }

    public boolean comparePropertyValue(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject == null) {
            ALog.w(TAG, "comparePropertyValue newPropertyValue empty false");
            return false;
        }
        if (jSONObject2 == null) {
            ALog.w(TAG, "comparePropertyValue oldPropertyValue empty true");
            return true;
        }
        Object obj = jSONObject.get("value");
        Long lValueOf = jSONObject.getLong("time");
        if (lValueOf == null) {
            lValueOf = Long.valueOf(System.currentTimeMillis());
        }
        Object obj2 = jSONObject2.get("value");
        Long l = jSONObject2.getLong("time");
        if (l == null) {
            l = 0L;
        }
        if (obj2 == null) {
            ALog.w(TAG, "comparePropertyValue oldValue empty true");
            return true;
        }
        if (obj == null) {
            ALog.w(TAG, "comparePropertyValue newValue empty false");
            return false;
        }
        boolean z = lValueOf.longValue() >= l.longValue();
        ALog.i(TAG, "comparePropertyValue newValue:" + obj + " oldValue:" + obj2 + " newTime:" + lValueOf + " oldTime: " + l + " isNeedUpdate:" + z);
        return z;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        String string;
        JSONObject object;
        JSONObject jSONObject;
        Object objRemove;
        ALog.d(TAG, "onNotify connectedId:" + str + " topic:" + str2 + " aMessage:" + aMessage);
        if (TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "onNotify error topic:" + str2 + " connectedId:" + str);
            return;
        }
        try {
            if (aMessage.data instanceof byte[]) {
                string = new String((byte[]) aMessage.data, "UTF-8");
            } else {
                string = aMessage.data.toString();
            }
            object = JSONObject.parseObject(string);
            jSONObject = object.getJSONObject("params");
        } catch (Exception e) {
            ALog.e(TAG, "onNotify e:" + e.toString());
        }
        if (str2.contains(TmpConstant.MQTT_TOPIC_PROPERTIES)) {
            String string2 = jSONObject.getString("iotId");
            updatePropertyCacheAndNotify(string2, jSONObject.getJSONObject("items"), this.mMemoryLruHelper.getString(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, string2)), false, null);
            return;
        }
        if (str2.contains(TmpConstant.MQTT_TOPIC_STATUS)) {
            String string3 = jSONObject.getString("iotId");
            JSONObject jSONObject2 = jSONObject.getJSONObject("status");
            if (jSONObject2 != null && (objRemove = jSONObject2.remove("value")) != null) {
                jSONObject2.put("status", objRemove);
            }
            updateStatusCacheAndNotify(string3, jSONObject2, this.mMemoryLruHelper.getString(getCacheKey(DEVICESHADOW_STATUS_PRE_KEY, string3)), false, null);
            return;
        }
        if (str2.contains(TmpConstant.MQTT_TOPIC_NOTIFY)) {
            Log.d(TAG, "onNotify() called with: connectedId = [" + str + "], topic = [" + str2 + "], aMessage = [" + aMessage + "]");
            String string4 = jSONObject.getString("identifier");
            JSONObject jSONObject3 = jSONObject.getJSONObject("value");
            String string5 = jSONObject3.getString("iotId");
            String string6 = jSONObject3.getString("operation");
            if (!TextUtils.isEmpty(string6) && string6.equalsIgnoreCase("Unbind")) {
                deleteDeviceShadow(string5, null);
                try {
                    String string7 = jSONObject3.getString("deviceName");
                    String string8 = jSONObject3.getString("productKey");
                    GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse = (GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSONObject.parseObject(getDeviceSupportedNetTypesByIotId(string5), GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class);
                    if (getDeviceNetTypesSupportedResponse == null || !TmpEnum.DeviceNetType.isWifiBtCombo(TmpEnum.DeviceNetType.formatDeviceNetType((List) getDeviceNetTypesSupportedResponse.data))) {
                        return;
                    }
                    String macByDn = TmpStorage.getInstance().getMacByDn(string7);
                    ALog.d(TAG, "combo dev unbinded productKey:" + string8 + " deviceName:" + string7 + " mac:" + macByDn + " breeze close by mac BREEZE:");
                    StringBuilder sb = new StringBuilder();
                    sb.append(a.f4238b);
                    sb.append(string8);
                    sb.append(string7);
                    ConnectSDK.getInstance().destoryConnect(sb.toString());
                    if (TextUtils.isEmpty(macByDn)) {
                        return;
                    }
                    MixBleDelegate.getInstance().close(macByDn, new ConnectionCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.6
                        public void onConnectionStateChange(MixBleDevice mixBleDevice, int i, int i2) {
                        }
                    });
                    return;
                } catch (Exception e2) {
                    ALog.w(TAG, "GetDeviceNetTypesSupportedResponse e:" + e2.toString());
                    return;
                }
            }
            if (!TextUtils.isEmpty(string4) && string4.equalsIgnoreCase("_LivingLink.propertyNameUpdate")) {
                try {
                    this.mPropertyAlias.changeTslWithAlias(string5, PropertyAlias.create(string5, JSON.parseArray(jSONObject3.getJSONArray("data").toString(), AliasNotifyData.class)));
                    return;
                } catch (Exception e3) {
                    ALog.e(TAG, "update error:" + e3.toString());
                    return;
                }
            }
            if (TextUtils.equals(string4, "awss.BindNotify")) {
                boolean z = true;
                if (jSONObject3.getIntValue("owned") != 1) {
                    z = false;
                }
                if (z) {
                    Log.d(TAG, "onNotify: 绑定成功通知");
                    Intent intent = new Intent("iLop.bind.cloud.mesh");
                    intent.putExtra("bindData", jSONObject3.toJSONString());
                    TmpSdk.getContext().sendBroadcast(intent);
                    String string9 = jSONObject3.getString("iotId");
                    if (TextUtils.isEmpty(string9)) {
                        return;
                    }
                    JSONObject jSONObject4 = new JSONObject();
                    object.put("iotId", (Object) string9);
                    MeshManager.getInstance().addProvisionDevice(string9);
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put("value", (Object) "1");
                    jSONObject4.put("powerstate", (Object) jSONObject5);
                    optimisticUpdateMeshDevice(string9, jSONObject4);
                    return;
                }
                return;
            }
            return;
            ALog.e(TAG, "onNotify e:" + e.toString());
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.event.INotifyHandler
    public void onMessage(d dVar, e eVar) {
        JSONObject jSONObject;
        ALog.d(TAG, "onMessage request:" + dVar + " response:" + eVar);
        try {
            JSONObject object = JSONObject.parseObject(eVar.e());
            String string = object.getString("method");
            if (TextUtils.isEmpty(string) || !string.contains(TmpConstant.METHOD_PROPERTY_POST) || (jSONObject = object.getJSONObject("params")) == null) {
                return;
            }
            updatePropertyCacheAndNotify(eVar.g(), jSONObject, this.mMemoryLruHelper.getString(getCacheKey(DEVICESHADOW_PROPERTY_PRE_KEY, eVar.g())), false, null);
        } catch (Exception e) {
            ALog.e(TAG, "onMessage error:" + e.toString());
        }
    }

    protected void updatePropertiesByCloud(final String str, final IProcessListener iProcessListener) {
        ALog.d(TAG, "updatePropertiesByCloud iotId:" + str + " processListener:" + iProcessListener);
        if (CheckMeshMessage.containsMessage(str)) {
            ALog.d(TAG, "getProps: mesh device is in optimistic Update time ,no need get from cloud");
        } else {
            CloudUtils.getProperties(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.7
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    ALog.d(DeviceShadowMgr.TAG, "updatePropertiesByCloud onResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        IProcessListener iProcessListener2 = iProcessListener;
                        if (iProcessListener2 != null) {
                            iProcessListener2.onFail(new ErrorInfo(300, "getProperties error error"));
                            return;
                        }
                        return;
                    }
                    try {
                        DeviceShadowMgr.this.updatePropertyCacheAndNotify(str, JSON.parseObject(String.valueOf(aResponse.data)).getJSONObject("data"), DeviceShadowMgr.this.mMemoryLruHelper.getString(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_PROPERTY_PRE_KEY, str)), false, null);
                        if (iProcessListener != null) {
                            iProcessListener.onSuccess(aResponse.data);
                        }
                    } catch (Exception e) {
                        ALog.e(DeviceShadowMgr.TAG, "getProperties onComplete updatePropertyCacheAndNotify error:" + e.toString());
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    ALog.d(DeviceShadowMgr.TAG, "updatePropertiesByCloud onFailure:" + aError);
                    IProcessListener iProcessListener2 = iProcessListener;
                    if (iProcessListener2 != null) {
                        iProcessListener2.onFail(new ErrorInfo(aError));
                    }
                }
            });
        }
    }

    protected void updateStatusByCloud(final String str, final IProcessListener iProcessListener) {
        ALog.d(TAG, "updateStatusByCloud iotId:" + str + " processListener:" + iProcessListener);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "updateStatusByCloud empty error");
        } else {
            CloudUtils.getStatus(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.8
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    ALog.d(DeviceShadowMgr.TAG, "updateStatusByCloud onComplete aRequest: " + aRequest + " aResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        IProcessListener iProcessListener2 = iProcessListener;
                        if (iProcessListener2 != null) {
                            iProcessListener2.onFail(new ErrorInfo(300, "getStatus response error"));
                            return;
                        }
                        return;
                    }
                    try {
                        DeviceShadowMgr.this.updateStatusCacheAndNotify(str, JSON.parseObject(String.valueOf(aResponse.data)).getJSONObject("data"), DeviceShadowMgr.this.mMemoryLruHelper.getString(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_STATUS_PRE_KEY, str)), false, null);
                    } catch (Exception e) {
                        ALog.e(DeviceShadowMgr.TAG, "getStatus onResponse error:" + e.toString());
                    }
                    IProcessListener iProcessListener3 = iProcessListener;
                    if (iProcessListener3 != null) {
                        iProcessListener3.onSuccess(aResponse.data);
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    ALog.d(DeviceShadowMgr.TAG, "updateStatusByCloud onFailure aError: " + aError);
                    IProcessListener iProcessListener2 = iProcessListener;
                    if (iProcessListener2 != null) {
                        iProcessListener2.onFail(new ErrorInfo(300, "getStatus error error"));
                    }
                }
            });
        }
    }

    protected void updateDetailInfoByCloud(final String str, final boolean z, final IProcessListener iProcessListener) {
        ALog.d(TAG, "updateDetailInfoByCloud iotId:" + str + " isCallbacked:" + z + " processListener:" + iProcessListener);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "updateDetailInfoByCloud empty error");
        } else {
            CloudUtils.queryProductInfo(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.9
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) throws Throwable {
                    IProcessListener iProcessListener2;
                    ALog.d(DeviceShadowMgr.TAG, "updateDetailInfoByCloud onResponse iotId:" + str + " onResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        if (z || (iProcessListener2 = iProcessListener) == null) {
                            return;
                        }
                        iProcessListener2.onFail(new ErrorInfo(300, "getDeviceDetailInfo aResponse error"));
                        return;
                    }
                    try {
                        CommonResponsePayload commonResponsePayload = (CommonResponsePayload) JSON.parseObject(String.valueOf(aResponse.data), CommonResponsePayload.class);
                        if (commonResponsePayload != null && commonResponsePayload.getData() != null) {
                            DeviceShadowMgr.this.mDiskLruCacheHelper.saveValue(DeviceShadowMgr.getCacheKey(DeviceShadowMgr.DEVICESHADOW_DETAILINFO_PRE_KEY, str), String.valueOf(commonResponsePayload.getData()));
                            if (z || iProcessListener == null) {
                                return;
                            }
                            iProcessListener.onSuccess(String.valueOf(aResponse.data));
                            return;
                        }
                        ALog.e(DeviceShadowMgr.TAG, "queryProductInfo payload error");
                        if (z || iProcessListener == null) {
                            return;
                        }
                        iProcessListener.onFail(new ErrorInfo(300, "getDeviceDetailInfo parseObject error"));
                    } catch (Exception e) {
                        ALog.e(DeviceShadowMgr.TAG, "parseObject error:" + e.toString());
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    IProcessListener iProcessListener2;
                    ALog.d(DeviceShadowMgr.TAG, "updateDetailInfoByCloud onFailure iotId:" + str + " aError:" + aError);
                    if (z || (iProcessListener2 = iProcessListener) == null) {
                        return;
                    }
                    iProcessListener2.onFail(new ErrorInfo(aError));
                }
            });
        }
    }

    protected void updateTslByCloud(final String str, final boolean z, final IProcessListener iProcessListener) {
        ALog.d(TAG, "updateTslByCloud iotId:" + str + " isCallbacked:" + z + " processListener:" + iProcessListener);
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "updateTslByCloud empty error");
        } else {
            CloudUtils.getTsl(str, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.10
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    IProcessListener iProcessListener2;
                    final GetTslResponsePayload getTslResponsePayload;
                    IProcessListener iProcessListener3;
                    ALog.d(DeviceShadowMgr.TAG, "updateTslByCloud ***** iotId:" + str + " onResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        if (z || (iProcessListener2 = iProcessListener) == null) {
                            return;
                        }
                        iProcessListener2.onFail(new ErrorInfo(300, "getTsl aResponse error"));
                        return;
                    }
                    try {
                        getTslResponsePayload = (GetTslResponsePayload) JSON.parseObject(String.valueOf(aResponse.data), GetTslResponsePayload.class);
                    } catch (Exception e) {
                        e = e;
                        getTslResponsePayload = null;
                    }
                    try {
                        Log.d(DeviceShadowMgr.TAG, "onResponse() called with: aResponse = [" + aResponse.getData() + "]");
                        StringBuilder sb = new StringBuilder();
                        sb.append("onResponse() called with: getCode = ");
                        sb.append(getTslResponsePayload.getCode());
                        Log.d(DeviceShadowMgr.TAG, sb.toString());
                    } catch (Exception e2) {
                        e = e2;
                        ALog.e(DeviceShadowMgr.TAG, "parseObject error:" + e.toString());
                    }
                    if (getTslResponsePayload != null && getTslResponsePayload.data != null) {
                        DeviceShadowMgr.this.mPropertyAlias.refreshPropertyAlias(str, getTslResponsePayload, new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.10.1
                            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                            public void onSuccess(Object obj) throws Throwable {
                                DeviceShadowMgr.this.setTsl(str, String.valueOf(getTslResponsePayload.data));
                                if (z || iProcessListener == null) {
                                    return;
                                }
                                iProcessListener.onSuccess(JSON.toJSONString(getTslResponsePayload));
                            }

                            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                            public void onFail(ErrorInfo errorInfo) throws Throwable {
                                DeviceShadowMgr.this.setTsl(str, String.valueOf(getTslResponsePayload.data));
                                if (z || iProcessListener == null) {
                                    return;
                                }
                                iProcessListener.onSuccess(JSON.toJSONString(getTslResponsePayload));
                            }
                        });
                    } else {
                        if (z || (iProcessListener3 = iProcessListener) == null) {
                            return;
                        }
                        iProcessListener3.onFail(new ErrorInfo(300, "payload data parse error"));
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    IProcessListener iProcessListener2;
                    ALog.d(DeviceShadowMgr.TAG, "updateTslByCloud onFailure iotId:" + str + " aError:" + aError);
                    if (z || (iProcessListener2 = iProcessListener) == null) {
                        return;
                    }
                    iProcessListener2.onFail(new ErrorInfo(aError));
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.lang.CharSequence, java.lang.String] */
    public void getDeviceExtend(String str, String str2, final IProcessListener iProcessListener) {
        final String cacheKey = getCacheKey(str2, str);
        ?? string = this.mDiskLruCacheHelper.getString(cacheKey);
        if (!TextUtils.isEmpty(string)) {
            GetDeviceExtendRequest.DeviceExtendGetResponse deviceExtendGetResponse = new GetDeviceExtendRequest.DeviceExtendGetResponse();
            deviceExtendGetResponse.data = string;
            deviceExtendGetResponse.code = 200;
            if (iProcessListener != null) {
                iProcessListener.onSuccess(JSON.toJSONString(deviceExtendGetResponse));
                return;
            }
            return;
        }
        GateWayRequest getDeviceExtendRequest = new GetDeviceExtendRequest(str, str2);
        getDeviceExtendRequest.sendRequest(getDeviceExtendRequest, new IGateWayRequestListener<GetDeviceExtendRequest, GetDeviceExtendRequest.DeviceExtendGetResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr.11
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetDeviceExtendRequest getDeviceExtendRequest2, GetDeviceExtendRequest.DeviceExtendGetResponse deviceExtendGetResponse2) throws Throwable {
                DeviceShadowMgr.this.mDiskLruCacheHelper.saveValue(cacheKey, (String) deviceExtendGetResponse2.data);
                IProcessListener iProcessListener2 = iProcessListener;
                if (iProcessListener2 != null) {
                    iProcessListener2.onSuccess(JSON.toJSONString(deviceExtendGetResponse2));
                }
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetDeviceExtendRequest getDeviceExtendRequest2, AError aError) {
                IProcessListener iProcessListener2 = iProcessListener;
                if (iProcessListener2 != null) {
                    iProcessListener2.onFail(new ErrorInfo(aError));
                }
            }
        });
    }

    public void setPropertyAlias(String str, IPanelCallback iPanelCallback) {
        String string;
        String string2;
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject object = JSON.parseObject(str);
            String string3 = null;
            if (object != null) {
                string3 = object.getString("iotId");
                string2 = object.getString("name");
                string = object.getString("alias");
            } else {
                string = null;
                string2 = null;
            }
            this.mPropertyAlias.setPropertyAlias(string3, string2, string);
            if (iPanelCallback != null) {
                jSONObject.put("code", (Object) 200);
                iPanelCallback.onComplete(true, jSONObject.toString());
            }
        } catch (Exception e) {
            ALog.e(TAG, "setPropertyAlias error:" + e.toString());
            jSONObject.put("code", (Object) 300);
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(true, jSONObject.toString());
            }
        }
    }

    public static String getCacheKey(String str, String str2) {
        return (str + str2).toLowerCase();
    }
}
