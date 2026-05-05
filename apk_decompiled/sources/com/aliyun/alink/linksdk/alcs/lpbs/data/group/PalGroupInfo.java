package com.aliyun.alink.linksdk.alcs.lpbs.data.group;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public class PalGroupInfo {
    public PalDeviceInfo[] deviceArray;
    public String groupId;

    public ICADeviceInfo[] getIcaGroupInfo() {
        PalDeviceInfo[] palDeviceInfoArr = this.deviceArray;
        if (palDeviceInfoArr == null || palDeviceInfoArr.length <= 0) {
            return null;
        }
        ICADeviceInfo[] iCADeviceInfoArr = new ICADeviceInfo[palDeviceInfoArr.length];
        int i = 0;
        while (true) {
            PalDeviceInfo[] palDeviceInfoArr2 = this.deviceArray;
            if (i >= palDeviceInfoArr2.length) {
                return iCADeviceInfoArr;
            }
            iCADeviceInfoArr[i] = new ICADeviceInfo(palDeviceInfoArr2[i].productModel, this.deviceArray[i].deviceId, this.deviceArray[i].ip);
            i++;
        }
    }
}
