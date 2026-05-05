package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenResponse;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: loaded from: classes.dex */
public class DeviceBindResultInfo extends LKDeviceInfo {
    public static final String TAG = "DeviceBindResultInfo";
    public int bindResult = -1;
    public int insideResult = 0;
    public String iotId = null;
    public String pageRouterUrl = null;
    public String categoryKey = null;
    public String errorCode = null;
    public String subErrorCode = null;
    public String localizedMsg = null;

    public static DeviceBindResultInfo getFirstBindResultInfo(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str3)) {
            return null;
        }
        try {
            JSONArray array = JSONArray.parseArray(str3);
            if (array != null && array.size() >= 1) {
                DeviceBindResultInfo deviceBindResultInfo = null;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jSONObject = array.getJSONObject(i);
                    if (jSONObject == null) {
                        ALog.d(TAG, "getBindResultInfo firstBindInfo=null.");
                        return null;
                    }
                    DeviceBindResultInfo deviceBindResultInfo2 = new DeviceBindResultInfo();
                    deviceBindResultInfo2.productKey = jSONObject.getString("productKey");
                    deviceBindResultInfo2.deviceName = jSONObject.getString("deviceName");
                    deviceBindResultInfo2.iotId = jSONObject.getString("iotId");
                    deviceBindResultInfo2.pageRouterUrl = jSONObject.getString(AlinkConstants.KEY_PAGE_ROUTER_URL);
                    deviceBindResultInfo2.categoryKey = jSONObject.getString(AlinkConstants.KEY_CATEGORY_KEY);
                    deviceBindResultInfo2.bindResult = jSONObject.getIntValue(AlinkConstants.KEY_BIND_RESULT);
                    deviceBindResultInfo2.insideResult = jSONObject.getIntValue(AlinkConstants.KEY_INSIDE_RESULT);
                    if (deviceBindResultInfo2.bindResult == 2 || deviceBindResultInfo2.bindResult == 3) {
                        deviceBindResultInfo2.subErrorCode = jSONObject.getString("code");
                        deviceBindResultInfo2.errorCode = String.valueOf(DCErrorCode.PF_PROVISION_CLOUD_BIND_ERROR);
                    }
                    deviceBindResultInfo2.localizedMsg = jSONObject.getString(AlinkConstants.KEY_LOCALIZED_MSG);
                    if (TextUtils.isEmpty(str) || str.equals(deviceBindResultInfo2.productKey)) {
                        if (TextUtils.isEmpty(str2) || !str.equals(deviceBindResultInfo2.deviceName)) {
                            return deviceBindResultInfo2;
                        }
                        if (deviceBindResultInfo == null) {
                            deviceBindResultInfo = deviceBindResultInfo2;
                        }
                    } else if (deviceBindResultInfo == null) {
                        deviceBindResultInfo = deviceBindResultInfo2;
                    }
                }
                return deviceBindResultInfo;
            }
            ALog.d(TAG, "getBindResultInfo empty.");
            return null;
        } catch (Exception e) {
            ALog.w(TAG, "getFirstBindResultInfo parse exception=" + e);
            return null;
        }
    }

    public static DeviceBindResultInfo getTgBindResultInfo(CheckBindTokenResponse checkBindTokenResponse) {
        if (checkBindTokenResponse == null) {
            return null;
        }
        DeviceBindResultInfo deviceBindResultInfo = new DeviceBindResultInfo();
        deviceBindResultInfo.productKey = checkBindTokenResponse.getProductKey();
        deviceBindResultInfo.deviceName = checkBindTokenResponse.getDeviceName();
        deviceBindResultInfo.iotId = checkBindTokenResponse.getIotid();
        deviceBindResultInfo.bindResult = checkBindTokenResponse.getBindResult();
        deviceBindResultInfo.insideResult = checkBindTokenResponse.getInsideResult();
        deviceBindResultInfo.errorCode = String.valueOf(checkBindTokenResponse.getCode());
        deviceBindResultInfo.localizedMsg = checkBindTokenResponse.getLocalizedMsg();
        deviceBindResultInfo.categoryKey = checkBindTokenResponse.getCategoryKey();
        deviceBindResultInfo.pageRouterUrl = checkBindTokenResponse.getPageRouterUrl();
        return deviceBindResultInfo;
    }

    public String toString() {
        return "{productKey:" + this.productKey + ", deviceName:" + this.deviceName + ", bindResult:" + this.bindResult + ", iotId:" + this.iotId + ", pageRouterUrl:" + this.pageRouterUrl + ", categoryKey:" + this.categoryKey + ", errorCode:" + this.errorCode + ", subErrorCode:" + this.subErrorCode + ", localizedMsg:" + this.localizedMsg + "}";
    }
}
