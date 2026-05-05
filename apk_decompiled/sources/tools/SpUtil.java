package tools;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class SpUtil {
    public static final String DAY = "day";
    public static final String DAY_DATA = "day_data";
    public static final String HISTORY_DATA = "history_data";
    public static final String MONTH = "month";
    public static final String MONTH_DATA = "month_data";
    public static final String NAME = "name";
    private static SharedPreferences mSharedPreferences;

    public static void putValue(Context context, String str, float f) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        mSharedPreferences.edit().putFloat(str, f).apply();
    }

    public static void putValue(Context context, String str, boolean z) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        mSharedPreferences.edit().putBoolean(str, z).apply();
    }

    public static void putValue(Context context, String str, long j) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        mSharedPreferences.edit().putLong(str, j).apply();
    }

    public static void putValue(Context context, String str, String str2) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        mSharedPreferences.edit().putString(str, str2).apply();
    }

    public static void putValue(Context context, String str, int i) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        mSharedPreferences.edit().putInt(str, i).apply();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        return mSharedPreferences.getBoolean(str, z);
    }

    public static String getString(Context context, String str, String str2) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        return mSharedPreferences.getString(str, str2);
    }

    public static int getInt(Context context, String str, int i) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        return mSharedPreferences.getInt(str, i);
    }

    public static float getFloat(Context context, String str, float f) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        return mSharedPreferences.getFloat(str, f);
    }

    public static long getLong(Context context, String str, long j) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        return mSharedPreferences.getLong(str, j);
    }

    public static List<String> hasPrefix(Context context, String str) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("name", 0);
        }
        ArrayList arrayList = new ArrayList();
        try {
            for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
                if (entry.getKey().startsWith(str)) {
                    arrayList.add((String) entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
