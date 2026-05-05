package a.a.a.a.a.a.c;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.aliyun.alink.linksdk.securesigner.crypto.KeystoreSecureStorage;
import com.aliyun.alink.linksdk.securesigner.util.Utils;

/* JADX INFO: compiled from: SecurityGuardDataStoreUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class b {
    public static boolean a(Context context, String str, String str2) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        a.a.a.a.a.a.a.a.a("SecurityGuardDataStoreUtil", "putString, key = " + str + ", value=" + str2);
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                if (!Utils.hasSecurityGuardDep()) {
                    KeystoreSecureStorage.getInstance(context).put(str, str2);
                    return true;
                }
                SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
                if (securityGuardManager != null && (dynamicDataStoreComp = securityGuardManager.getDynamicDataStoreComp()) != null) {
                    return dynamicDataStoreComp.putStringDDpEx(str, str2, 0);
                }
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("SecurityGuardDataStoreUtil", "putString(),error, " + e.toString());
            }
        }
        return false;
    }

    public static String a(Context context, String str) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        a.a.a.a.a.a.a.a.a("SecurityGuardDataStoreUtil", "getString, key = " + str);
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                if (Utils.hasSecurityGuardDep()) {
                    SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
                    if (securityGuardManager != null && (dynamicDataStoreComp = securityGuardManager.getDynamicDataStoreComp()) != null) {
                        return dynamicDataStoreComp.getStringDDpEx(str, 0);
                    }
                } else {
                    return KeystoreSecureStorage.getInstance(context).get(str);
                }
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("SecurityGuardDataStoreUtil", "getString(),error, " + e.toString());
            }
        }
        return null;
    }
}
