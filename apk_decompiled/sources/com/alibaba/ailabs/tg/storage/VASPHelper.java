package com.alibaba.ailabs.tg.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.ailabs.tg.UtilsConfig;

/* JADX INFO: loaded from: classes.dex */
@SuppressLint({"ApplySharedPref"})
public class VASPHelper {
    private static final String VASP_NAME = "video_assistant_sp";
    private static VASPHelper mInstance;
    private SharedPreferences mSharedPreferences;

    private VASPHelper(Context context) {
        this.mSharedPreferences = context.getApplicationContext().getSharedPreferences(VASP_NAME, 0);
    }

    public static VASPHelper getInstance() {
        VASPHelper vASPHelper = mInstance;
        if (vASPHelper != null) {
            return vASPHelper;
        }
        synchronized (VASPHelper.class) {
            if (mInstance == null) {
                mInstance = new VASPHelper(UtilsConfig.getInstance().getAppContext());
            }
        }
        return mInstance;
    }

    public void remove(String str) {
        this.mSharedPreferences.edit().remove(str).commit();
    }

    public void clear() {
        this.mSharedPreferences.edit().clear().commit();
    }

    public boolean isHas(String str) {
        return this.mSharedPreferences.contains(str);
    }

    public void put(String str, String str2) {
        this.mSharedPreferences.edit().putString(str, str2).commit();
    }

    public void put(String str, long j) {
        this.mSharedPreferences.edit().putLong(str, j).commit();
    }

    public void put(String str, int i) {
        this.mSharedPreferences.edit().putInt(str, i).commit();
    }

    public void put(String str, boolean z) {
        this.mSharedPreferences.edit().putBoolean(str, z).commit();
    }

    public String get(String str, String str2) {
        return this.mSharedPreferences.getString(str, str2);
    }

    public long get(String str, long j) {
        return this.mSharedPreferences.getLong(str, j);
    }

    public int get(String str, int i) {
        return this.mSharedPreferences.getInt(str, i);
    }

    public boolean get(String str, boolean z) {
        return this.mSharedPreferences.getBoolean(str, z);
    }
}
