package com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.core.util.SecurityGuardDataStoreUtil;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class MobileTripleValueManager {
    private static final String TAG = "MobileTripleValueManager";
    private final String KEY_PRODUCTKEY = "LINKSDK_CHANNNEL_MOBILE_PRODUCTKEY";
    private final String KEY_DEVICENAME = "LINKSDK_CHANNNEL_MOBILE_DEVICENAME";
    private final String KEY_DEVICESECRET = "LINKSDK_CHANNNEL_MOBILE_DEVICESECRET";
    private MobileTripleValue mobileTripleValue = null;

    private static class InstanceHolder {
        private static final MobileTripleValueManager sInstance = new MobileTripleValueManager();

        private InstanceHolder() {
        }
    }

    public static MobileTripleValueManager getInstance() {
        return InstanceHolder.sInstance;
    }

    public boolean isTripleValueExist(Context context) {
        if (context == null) {
            ALog.e(TAG, "isTripleValueExist(), context is empty");
            return false;
        }
        MobileTripleValue mobileTripleValue = this.mobileTripleValue;
        return (mobileTripleValue != null && mobileTripleValue.checkValid()) || !TextUtils.isEmpty(SecurityGuardDataStoreUtil.getString(context, "LINKSDK_CHANNNEL_MOBILE_PRODUCTKEY"));
    }

    public MobileTripleValue getTripleValue(Context context) {
        MobileTripleValue mobileTripleValue = this.mobileTripleValue;
        if (mobileTripleValue != null && mobileTripleValue.checkValid()) {
            return this.mobileTripleValue;
        }
        if (context == null) {
            ALog.e(TAG, "getTripleValue(), context is empty");
            return null;
        }
        this.mobileTripleValue = new MobileTripleValue(SecurityGuardDataStoreUtil.getString(context, "LINKSDK_CHANNNEL_MOBILE_PRODUCTKEY"), SecurityGuardDataStoreUtil.getString(context, "LINKSDK_CHANNNEL_MOBILE_DEVICENAME"), SecurityGuardDataStoreUtil.getString(context, "LINKSDK_CHANNNEL_MOBILE_DEVICESECRET"));
        if (this.mobileTripleValue.checkValid()) {
            return this.mobileTripleValue;
        }
        return null;
    }

    public boolean saveTripleValue(Context context, MobileTripleValue mobileTripleValue) {
        if (context == null || mobileTripleValue == null || !mobileTripleValue.checkValid()) {
            ALog.d(TAG, "saveTripleValue(), params error");
            return false;
        }
        if (!SecurityGuardDataStoreUtil.putString(context, "LINKSDK_CHANNNEL_MOBILE_PRODUCTKEY", mobileTripleValue.productKey) || !SecurityGuardDataStoreUtil.putString(context, "LINKSDK_CHANNNEL_MOBILE_DEVICENAME", mobileTripleValue.deviceName) || !SecurityGuardDataStoreUtil.putString(context, "LINKSDK_CHANNNEL_MOBILE_DEVICESECRET", mobileTripleValue.deviceSecret)) {
            return false;
        }
        this.mobileTripleValue = mobileTripleValue;
        return true;
    }
}
