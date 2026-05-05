package com.taobao.agoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.taobao.accs.utl.ALog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public class LocalStorage {
    private static final String ALIAS_KEY_PREFIX = "alicloud-third-push-pat";
    private static final String ALIAS_LIST_KEY = "alicloud-third-push-alias-list";
    private static final String ALIAS_LIST_SEPARATOR = "#&#";
    public static final String TAG = "LocalStorage";

    public static void saveAliasToken(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str) || context == null) {
            ALog.d(TAG, "saveAliasToken input invalid", new Object[0]);
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        if (TextUtils.isEmpty(str2)) {
            editorEdit.remove(getAliasTokenKey(str));
            String string = defaultSharedPreferences.getString(ALIAS_LIST_KEY, "");
            String alisTag = getAlisTag(str);
            if (string.contains(alisTag)) {
                editorEdit.putString(ALIAS_LIST_KEY, string.replace(alisTag, ""));
            }
        } else {
            editorEdit.putString(getAliasTokenKey(str), str2);
            String string2 = defaultSharedPreferences.getString(ALIAS_LIST_KEY, "");
            String alisTag2 = getAlisTag(str);
            if (!string2.contains(alisTag2)) {
                editorEdit.putString(ALIAS_LIST_KEY, string2 + alisTag2);
            }
        }
        editorEdit.apply();
    }

    private static String getAlisTag(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return ALIAS_LIST_SEPARATOR + str + ALIAS_LIST_SEPARATOR;
    }

    public static String getAliasToken(Context context, String str) {
        if (TextUtils.isEmpty(str) || context == null) {
            ALog.d(TAG, "getAliasToken input invalid", new Object[0]);
            return null;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getString(getAliasTokenKey(str), null);
    }

    public static ArrayList<String> getAliasList(Context context) {
        String[] strArrSplit = PreferenceManager.getDefaultSharedPreferences(context).getString(ALIAS_LIST_KEY, "").split(ALIAS_LIST_SEPARATOR);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strArrSplit.length; i++) {
            if (strArrSplit[i] != null && !strArrSplit[i].isEmpty()) {
                arrayList.add(strArrSplit[i]);
            }
        }
        return arrayList;
    }

    private static String getAliasTokenKey(String str) {
        return "alicloud-third-push-pat-" + str;
    }
}
