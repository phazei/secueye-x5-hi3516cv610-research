package com.aliyun.alink.business.devicecenter.utils;

import android.text.TextUtils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.channel.coap.response.DevicePayload;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceInfoUtils {

    public interface IApiCallback {
        void onFail(int i, String str);

        void onSuccess(Object obj);
    }

    public static boolean a(String str) {
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
            try {
                return Integer.parseInt(str) > 0;
            } catch (Exception e) {
                ALog.w("DeviceInfoUtils", "isRemainTimeValid remainTime=" + str + ",e=" + e);
            }
        }
        return false;
    }

    public static String getMeshDeviceUniqueIDByDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.mac)) {
            return "";
        }
        return "MAC_" + deviceInfo.mac.replace(":", "").toLowerCase();
    }

    public static String getMeshDeviceUniqueIDByMac(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return "MAC_" + str.replace(":", "").toLowerCase();
    }

    public static boolean isDevicePayloadValid(DevicePayload devicePayload) {
        if (devicePayload == null || TextUtils.isEmpty(devicePayload.productKey) || TextUtils.isEmpty(devicePayload.deviceName)) {
            return false;
        }
        return (!TextUtils.isEmpty(devicePayload.token) && a(devicePayload.remainTime)) || TextUtils.isEmpty(devicePayload.token);
    }

    public static boolean isSupportFastProvisioningV2(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str);
        byte b2 = bArrHexString2Bytes[13];
        byte b3 = bArrHexString2Bytes[15];
        return (b2 >> 1) > 1 && (b3 >> 6) == 0 && (b3 & 16) != 0;
    }

    public static void pidReturnToPk(String str, final IApiCallback iApiCallback) {
        ProvisionRepositoryV2.pidReturnToPk(str, new ApiCallBack<Object>() { // from class: com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str2) {
                IApiCallback iApiCallback2 = iApiCallback;
                if (iApiCallback2 != null) {
                    iApiCallback2.onFail(-1, "recover pk is failed");
                }
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(Object obj) {
                IApiCallback iApiCallback2 = iApiCallback;
                if (iApiCallback2 == null) {
                    return;
                }
                iApiCallback2.onSuccess(obj);
            }
        });
    }
}
