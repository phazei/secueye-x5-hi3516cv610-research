package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.aliyun.alink.linksdk.tmp.TmpSdk;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceUuidFactory {
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static final String PREFS_FILE = "device_id.xml";
    protected static volatile String mDevicdId;

    public static void init(Context context) {
        if (mDevicdId == null) {
            synchronized (DeviceUuidFactory.class) {
                if (mDevicdId == null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, 0);
                    String string = sharedPreferences.getString(PREFS_DEVICE_ID, null);
                    if (string != null) {
                        mDevicdId = string;
                    } else {
                        String string2 = Settings.Secure.getString(context.getContentResolver(), "android_id");
                        try {
                            if (!"9774d56d682e549c".equals(string2)) {
                                mDevicdId = string2;
                            } else {
                                String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                                if (deviceId == null) {
                                    deviceId = new String(Base64.encode(TextHelper.getRandomString().getBytes(), 0));
                                }
                                mDevicdId = deviceId;
                            }
                            sharedPreferences.edit().putString(PREFS_DEVICE_ID, mDevicdId.toString()).commit();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static String getDeviceUuidStr() {
        init(TmpSdk.getContext());
        return mDevicdId;
    }
}
