package com.aliyun.iot.push.utils;

import android.content.Context;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class SecurityGuardUtils {
    public static void checkSecurityPicture(Context context, String str) {
        if (context.getResources().getIdentifier("yw_1222_" + str, CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, context.getPackageName()) == 0) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "can not find yw_1222_%s.jpg", str));
        }
    }

    public static String getAppKey(Context context, String str) {
        IStaticDataStoreComponent staticDataStoreComp;
        if (Utils.hasSecurityGuardDep()) {
            try {
                SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
                return (securityGuardManager == null || (staticDataStoreComp = securityGuardManager.getStaticDataStoreComp()) == null) ? "" : staticDataStoreComp.getAppKeyByIndex(0, str);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return new SecurityImpl().getAppKey();
    }
}
