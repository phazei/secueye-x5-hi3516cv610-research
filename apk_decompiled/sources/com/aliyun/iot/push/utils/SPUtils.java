package com.aliyun.iot.push.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes2.dex */
public class SPUtils {
    public static void clearSPFile(Context context, String str) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
            editorEdit.clear();
            editorEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
