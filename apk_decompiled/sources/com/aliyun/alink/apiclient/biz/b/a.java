package com.aliyun.alink.apiclient.biz.b;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/* JADX INFO: compiled from: PhoneUtils.java */
/* JADX INFO: loaded from: classes.dex */
public class a {
    public static String a() {
        String str = "64" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        String str2 = Build.SERIAL;
        if (TextUtils.isEmpty(str2)) {
            str2 = "serial";
        }
        return new UUID(str.hashCode(), str2.hashCode()).toString();
    }

    public static String a(Context context) {
        String strB = b(context);
        if (TextUtils.isEmpty(strB)) {
            ALog.d("ApiClient-PhoneUtils", "to get getPseudoUniqueID");
            strB = a();
        }
        if (TextUtils.isEmpty(strB)) {
            ALog.d("ApiClient-PhoneUtils", "to get default, should not be here.");
            strB = "default_" + Build.MODEL + Build.BRAND + Build.MANUFACTURER + Build.BOARD;
        }
        String strA = null;
        try {
            strA = b.a(strB, TmpConstant.VALUE_SHA256);
        } catch (UnsupportedEncodingException e) {
            ALog.w("ApiClient-PhoneUtils", "UnsupportedEncodingException " + e);
        } catch (NoSuchAlgorithmException e2) {
            ALog.w("ApiClient-PhoneUtils", "NoSuchAlgorithmException " + e2);
        } catch (Exception e3) {
            ALog.w("ApiClient-PhoneUtils", "exception " + e3);
        }
        if (TextUtils.isEmpty(strA)) {
            strA = strB;
        }
        ALog.d("ApiClient-PhoneUtils", "origin=" + strB + ", sha=" + strA);
        return strA;
    }

    public static String b() {
        String str = Build.VERSION.RELEASE;
        ALog.d("ApiClient-PhoneUtils", "getSystemVersion=" + str);
        return str;
    }

    public static String b(Context context) {
        String deviceId = null;
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (Build.VERSION.SDK_INT < 21) {
                deviceId = telephonyManager.getDeviceId();
            } else {
                deviceId = (String) telephonyManager.getClass().getMethod("getImei", new Class[0]).invoke(telephonyManager, new Object[0]);
            }
        } catch (SecurityException e) {
            ALog.w("ApiClient-PhoneUtils", "SecurityException " + e);
        } catch (Exception e2) {
            ALog.w("ApiClient-PhoneUtils", "Exception " + e2);
        }
        return deviceId;
    }
}
