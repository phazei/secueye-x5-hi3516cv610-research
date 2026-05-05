package com.aliyun.alink.business.devicecenter.discover;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CloudEnrolleeDeviceHelper {
    public static boolean a(CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel) {
        ALog.d("CloudEnrolleeDeviceHelp", "isInCurrentRouter model=" + cloudEnrolleeDeviceModel);
        if (cloudEnrolleeDeviceModel == null) {
            ALog.d("CloudEnrolleeDeviceHelp", "isInCurrentRouter model=null");
            return false;
        }
        if (TextUtils.isEmpty(cloudEnrolleeDeviceModel.bssid)) {
            ALog.d("CloudEnrolleeDeviceHelp", "isInCurrentRouter model.bssid=null");
            return false;
        }
        String currentApBssid = WiFiUtils.getCurrentApBssid();
        String str = cloudEnrolleeDeviceModel.bssid;
        if (!TextUtils.isEmpty(currentApBssid) && (str.toUpperCase().contains(currentApBssid.toUpperCase()) || str.equalsIgnoreCase(currentApBssid))) {
            ALog.d("CloudEnrolleeDeviceHelp", "isInCurrentRouter device find, same bssid=" + str);
            return true;
        }
        ALog.i("CloudEnrolleeDeviceHelp", "isInCurrentRouter device find by other router, currentBssid=" + currentApBssid + ",cloudBssid=" + str);
        return false;
    }

    public static List<DeviceInfo> convertCloudEnrolleeDevice(List<CloudEnrolleeDeviceModel> list, boolean z) {
        if (list == null || list.size() < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = list.get(i);
            if (cloudEnrolleeDeviceModel == null) {
                ALog.d("CloudEnrolleeDeviceHelp", "convertCloudEnrolleeDevice model=null.");
            } else if (z || a(cloudEnrolleeDeviceModel)) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.deviceName = cloudEnrolleeDeviceModel.enrolleeDeviceName;
                deviceInfo.productKey = cloudEnrolleeDeviceModel.enrolleeProductKey;
                deviceInfo.regDeviceName = cloudEnrolleeDeviceModel.regDeviceName;
                deviceInfo.regProductKey = cloudEnrolleeDeviceModel.regProductKey;
                if ("1".equals(cloudEnrolleeDeviceModel.type)) {
                    deviceInfo.addDeviceFrom = AlinkConstants.ADD_DEVICE_FROM_ROUTER;
                } else {
                    deviceInfo.addDeviceFrom = AlinkConstants.ADD_DEVICE_FROM_ZERO;
                }
                arrayList.add(deviceInfo);
            }
        }
        return arrayList;
    }

    public static List<CloudEnrolleeDeviceModel> getFilteredEnrolleeDevices(List<CloudEnrolleeDeviceModel> list, boolean z) {
        if (list == null || list.size() < 1) {
            return null;
        }
        List<CloudEnrolleeDeviceModel> listSynchronizedList = Collections.synchronizedList(new ArrayList());
        for (int i = 0; i < list.size(); i++) {
            CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = list.get(i);
            if (cloudEnrolleeDeviceModel == null) {
                ALog.d("CloudEnrolleeDeviceHelp", "getFilteredEnrolleeDevices model=null.");
            } else if (z || a(cloudEnrolleeDeviceModel)) {
                listSynchronizedList.add(cloudEnrolleeDeviceModel);
            }
        }
        return listSynchronizedList;
    }
}
