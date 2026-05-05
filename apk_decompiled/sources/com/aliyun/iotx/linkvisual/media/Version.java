package com.aliyun.iotx.linkvisual.media;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.taobao.accs.AccsState;

/* JADX INFO: loaded from: classes2.dex */
public class Version {
    public static final String SDK_VERSION = ">>1.2.21-ilop<<";
    public static boolean isTg = SDK_VERSION.toLowerCase().contains("tg");
    public static boolean isIlop = SDK_VERSION.toLowerCase().contains("ilop");
    public static boolean isCC = SDK_VERSION.toLowerCase().contains(AccsState.CONNECTION_CHANGE);

    public static void checkSupport() {
        if (!isIlop && !isTg) {
            throw new UnsupportedOperationException("This interface is not support under the  version: >>1.2.21-ilop<<");
        }
    }

    public static String packageVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printVersionInfo() {
        Log.i("LinkVisual", "\n< - >\nMedia SDK Version:\t" + SDK_VERSION + SdkConstant.CLOUDAPI_LF + "< - >");
    }
}
