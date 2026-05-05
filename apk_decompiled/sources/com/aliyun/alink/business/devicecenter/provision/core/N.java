package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentGateMeshStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: compiled from: ConcurrentGateMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class N extends ApiCallBack<JSONArray> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ O f3673a;

    public N(O o) {
        this.f3673a = o;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(JSONArray jSONArray) {
        int i;
        boolean zBooleanValue;
        String string;
        String string2;
        String string3;
        String string4;
        String string5;
        String string6;
        String string7;
        String string8;
        int intFromString;
        String string9;
        String str;
        StringBuilder sb;
        if (this.f3673a.f3674a.provisionHasStopped.get()) {
            ALog.d(ConcurrentGateMeshStrategy.TAG, "provision has stopped, ignore check result.");
            return;
        }
        ArrayList<String> arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < jSONArray.size()) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                zBooleanValue = jSONObject.getBoolean("success").booleanValue();
                string = jSONObject.getString("state");
                string2 = jSONObject.getString("gatewayIotId");
                JSONObject jSONObject2 = jSONObject.getJSONObject(AlinkConstants.KEY_DEVICE_INFO);
                string3 = jSONObject2.getString("deviceId");
                string4 = jSONObject2.getString("deviceIotId");
                string5 = jSONObject2.getString(AlinkConstants.KEY_DEVICE_PRODUCT_KEY_NAME);
                string6 = jSONObject2.getString("deviceName");
                string7 = jSONObject.getString(AlinkConstants.KEY_PAGE_ROUTER_URL);
                string8 = jSONObject.getString("code");
                intFromString = StringUtils.getIntFromString(string8);
                string9 = jSONObject.getString(AlinkConstants.KEY_LOCALIZED_MSG);
                str = ConcurrentGateMeshStrategy.TAG;
                sb = new StringBuilder();
                i = i2;
            } catch (Exception e) {
                e = e;
                i = i2;
            }
            try {
                sb.append("query mesh device provisioning ");
                sb.append(string3);
                sb.append(" state=");
                sb.append(string);
                sb.append(", isSuccess=");
                sb.append(zBooleanValue);
                ALog.d(str, sb.toString());
                if ("FINISH".equals(string)) {
                    DCErrorCode subcode = null;
                    if (!zBooleanValue) {
                        Log.d(ConcurrentGateMeshStrategy.TAG, "onResponse: provisioning false");
                        DeviceInfo deviceInfo = this.f3673a.f3674a.mUnprovisionedGateMeshDevice.getDeviceInfo(string3);
                        arrayList.add(deviceInfo.mac);
                        if (!zBooleanValue) {
                            DCErrorCode dCErrorCode = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mesh/gateway/discovery/device/query provision failed state=");
                            sb2.append(string);
                            sb2.append(", msg=");
                            sb2.append(string9);
                            DCErrorCode msg = dCErrorCode.setMsg(sb2.toString());
                            if (intFromString == 0) {
                                intFromString = DCErrorCode.SUBCODE_SRE_GET_MESH_PROVISION_RESULT_BIZ_FAIL;
                            }
                            subcode = msg.setSubcode(intFromString);
                        }
                        this.f3673a.f3674a.provisionResultCallback(deviceInfo, subcode);
                    } else if (!TextUtils.isEmpty(string2) && !TextUtils.isEmpty(string3) && !TextUtils.isEmpty(string6)) {
                        DeviceInfo deviceInfo2 = this.f3673a.f3674a.mUnprovisionedGateMeshDevice.getDeviceInfo(string3);
                        if (!TextUtils.isEmpty(string3)) {
                            deviceInfo2.deviceId = string3;
                        }
                        if (!TextUtils.isEmpty(string2)) {
                            deviceInfo2.regIotId = string2;
                        }
                        if (!TextUtils.isEmpty(string5)) {
                            deviceInfo2.productKey = string5;
                        }
                        if (!TextUtils.isEmpty(string6)) {
                            deviceInfo2.deviceName = string6;
                        }
                        if (!TextUtils.isEmpty(string4)) {
                            deviceInfo2.iotId = string4;
                        }
                        deviceInfo2.bindResultInfo = new DeviceBindResultInfo();
                        deviceInfo2.bindResultInfo.productKey = string5;
                        deviceInfo2.bindResultInfo.deviceName = string6;
                        deviceInfo2.bindResultInfo.iotId = string4;
                        deviceInfo2.bindResultInfo.bindResult = 1;
                        deviceInfo2.bindResultInfo.pageRouterUrl = string7;
                        deviceInfo2.bindResultInfo.iotId = string4;
                        deviceInfo2.bindResultInfo.errorCode = string8;
                        deviceInfo2.bindResultInfo.localizedMsg = string9;
                        String str2 = ConcurrentGateMeshStrategy.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("query mesh device provisioning deviceInfo.iotId=");
                        sb3.append(deviceInfo2.iotId);
                        sb3.append("deviceInfo.deviceName=");
                        sb3.append(deviceInfo2.deviceName);
                        sb3.append(" state=");
                        sb3.append(string);
                        sb3.append(", isSuccess=");
                        sb3.append(true);
                        ALog.d(str2, sb3.toString());
                        arrayList.add(deviceInfo2.mac);
                        this.f3673a.f3674a.provisionResultCallback(deviceInfo2, null);
                    }
                }
            } catch (Exception e2) {
                e = e2;
                LogUtils.w(ConcurrentGateMeshStrategy.TAG, "query one device parse exception=" + e);
            }
            i2 = i + 1;
        }
        try {
            if (arrayList.size() > 0) {
                Iterator it = this.f3673a.f3674a.mTaskIds.iterator();
                while (it.hasNext()) {
                    for (String str3 : arrayList) {
                        if (((String) it.next()).contains(str3)) {
                            String str4 = ConcurrentGateMeshStrategy.TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("onResponse: 删除");
                            sb4.append(str3);
                            sb4.append("查询任务");
                            Log.d(str4, sb4.toString());
                            it.remove();
                        }
                    }
                }
            }
        } catch (Exception e3) {
            Log.d(ConcurrentGateMeshStrategy.TAG, "onResponse: delete task Exception=" + e3);
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
    }
}
