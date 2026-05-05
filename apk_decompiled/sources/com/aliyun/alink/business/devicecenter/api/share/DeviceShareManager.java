package com.aliyun.alink.business.devicecenter.api.share;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceShareManager {
    public static final String SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL = "EMAIL";
    public static final String SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE = "MOBILE";

    public static void shareDevice(String str, List<String> list, String str2, String str3, IoTCallback ioTCallback) {
        ALog.i("DeviceShareRepository", "shareDevice");
        if (ioTCallback == null || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str) || (list != null && list.size() == 0)) {
            ALog.e("DeviceShareRepository", "callback is null or accountAttrType is null or accountAttr is null or iotIdList is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("DeviceShareRepository", "getDiscoveredMeshDevice apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        HashMap map = new HashMap();
        if (SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE.equals(str3)) {
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        map.put("iotIdList", list);
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, str3);
        IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion("1.0.3").setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
        authType.setParams(map);
        new IoTAPIClientFactory().getClient().send(authType.build(), ioTCallback);
    }

    public static void shareDeviceList(int i, int i2, IoTCallback ioTCallback) {
        ALog.i("DeviceShareRepository", "shareDeviceList");
        if (ioTCallback == null) {
            ALog.e("DeviceShareRepository", "callback is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("DeviceShareRepository", "getDiscoveredMeshDevice apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_PAGE_NO, Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i2));
        IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion("1.0.2").setPath(AlinkConstants.HTTP_PATH_SHARE_LIST).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
        authType.setParams(map);
        new IoTAPIClientFactory().getClient().send(authType.build(), ioTCallback);
    }

    public static void shareDeviceMessage(int i, List<String> list, IoTCallback ioTCallback) {
        ALog.i("DeviceShareRepository", "shareDeviceMessage");
        if (ioTCallback == null || (list != null && list.size() == 0)) {
            ALog.e("DeviceShareRepository", "callback is null or recordIdList is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("DeviceShareRepository", "getDiscoveredMeshDevice apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_RECORD_ID_LIST, list);
        map.put(AlinkConstants.KEY_AGREE, Integer.valueOf(i));
        IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion("1.0.2").setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE_MSG).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
        authType.setParams(map);
        new IoTAPIClientFactory().getClient().send(authType.build(), ioTCallback);
    }

    public static void shareUnbind(String str, List<String> list, IoTCallback ioTCallback) {
        ALog.i("DeviceShareRepository", "shareUnbind");
        if (ioTCallback == null || TextUtils.isEmpty(str) || (list != null && list.size() == 0)) {
            ALog.e("DeviceShareRepository", "callback is null or targetIdentityId is null or iotIdList is null");
        }
        if (!DCEnvHelper.hasApiClient()) {
            ALog.w("DeviceShareRepository", "getDiscoveredMeshDevice apiclient not exist, return.");
            ioTCallback.onFailure(null, new Exception("apiclient not exist"));
            return;
        }
        HashMap map = new HashMap();
        map.put("iotIdList", list);
        map.put(AlinkConstants.KEY_TARGET_IDENTITY_ID, str);
        IoTRequestBuilder authType = new IoTRequestBuilder().setApiVersion("1.0.3").setPath(AlinkConstants.HTTP_PATH_SHARE_UNBIND).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH);
        authType.setParams(map);
        new IoTAPIClientFactory().getClient().send(authType.build(), ioTCallback);
    }
}
