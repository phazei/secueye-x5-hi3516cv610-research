package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Context;
import anetwork.channel.util.RequestConstant;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.aliyun.alink.linksdk.securesigner.crypto.KeystoreSecureStorage;
import com.aliyun.alink.linksdk.securesigner.crypto.SecureStorageException;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class SecurityGuardProxy {
    protected static final String TAG = "[Tmp]SecurityGuardProxy";
    protected Context mContext;

    public SecurityGuardProxy(Context context) {
        this.mContext = context;
        if (Utils.hasSecurityGuardDep()) {
            try {
                SecurityGuardManager.getInitializer().initialize(context);
            } catch (Exception unused) {
                ALog.w(TAG, "SecurityGuardProxy Exception context:" + context);
            } catch (Throwable unused2) {
                ALog.w(TAG, "SecurityGuardProxy throwable context:" + context);
            }
        }
    }

    public void removeStringDDpEx(String str) {
        if (Utils.hasSecurityGuardDep()) {
            LogCat.d(TAG, "removeStringDDpEx key:" + str);
            try {
                SecurityGuardManager.getInstance(this.mContext).getDynamicDataStoreComp().removeStringDDpEx(str, 0);
                return;
            } catch (Exception unused) {
                LogCat.w(TAG, "removeStringDDpEx SecurityGuardManager Exception key:" + str);
                return;
            } catch (Throwable unused2) {
                ALog.w(TAG, "removeStringDDpEx SecurityGuardProxy Throwable key:" + str);
                return;
            }
        }
        try {
            KeystoreSecureStorage.getInstance(this.mContext).remove(str);
        } catch (SecureStorageException e) {
            e.printStackTrace();
        }
    }

    public boolean addStringDDpEx(String str, String str2) {
        if (Utils.hasSecurityGuardDep()) {
            try {
                boolean zPutStringDDpEx = SecurityGuardManager.getInstance(this.mContext).getDynamicDataStoreComp().putStringDDpEx(str, str2, 0);
                LogCat.d(TAG, "addStringDDpEx key:" + str + " ret:" + zPutStringDDpEx);
                return zPutStringDDpEx;
            } catch (Exception unused) {
                LogCat.w(TAG, "addStringDDpEx SecurityGuardManager Exception key:" + str);
                return false;
            } catch (Throwable unused2) {
                LogCat.w(TAG, "addStringDDpEx SecurityGuardManager Throwable key:" + str);
                return false;
            }
        }
        try {
            KeystoreSecureStorage.getInstance(this.mContext).put(str, str2);
            return true;
        } catch (SecureStorageException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getStringDDpEx(String str) {
        if (Utils.hasSecurityGuardDep()) {
            LogCat.d(TAG, "getStringDDpEx key:" + str);
            try {
                String stringDDpEx = SecurityGuardManager.getInstance(this.mContext).getDynamicDataStoreComp().getStringDDpEx(str, 0);
                StringBuilder sb = new StringBuilder();
                sb.append("getStringDDpEx key:");
                sb.append(str);
                sb.append(" ret:");
                sb.append(stringDDpEx == null ? RequestConstant.FALSE : "true");
                LogCat.d(TAG, sb.toString());
                return stringDDpEx;
            } catch (Exception unused) {
                LogCat.w(TAG, "getStringDDpEx SecurityGuardManager Exception key:" + str);
                return null;
            } catch (Throwable unused2) {
                LogCat.w(TAG, "getStringDDpEx SecurityGuardManager throwable key:" + str);
                return null;
            }
        }
        try {
            return KeystoreSecureStorage.getInstance(this.mContext).get(str);
        } catch (SecureStorageException e) {
            e.printStackTrace();
            return null;
        }
    }
}
