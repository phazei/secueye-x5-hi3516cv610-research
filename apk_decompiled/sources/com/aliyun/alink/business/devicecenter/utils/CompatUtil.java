package com.aliyun.alink.business.devicecenter.utils;

import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class CompatUtil {
    public static boolean isAlinkPhoneApConfigStrategyFromOldHotspotFlow() {
        try {
            return ((Boolean) ReflectUtils.callStaticMethod("com.aliyun.alink.business.devicecenter.provision.other.phoneap.AlinkPhoneApConfigStrategy", "isFromOldHotspotFlow", new Object[0])).booleanValue();
        } catch (Exception e) {
            ALog.w("CompatUtil", "call isFromOldHotspotFlow exception: " + e);
            e.printStackTrace();
            return false;
        }
    }
}
