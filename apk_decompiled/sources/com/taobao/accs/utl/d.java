package com.taobao.accs.utl;

import android.content.SharedPreferences;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class d {
    public static final int MAX_FAIL_TIMES = 3;

    public static void a() {
        try {
            int iC = c();
            if (iC > 0) {
                SharedPreferences.Editor editorEdit = GlobalClientInfo.getContext().getSharedPreferences(Constants.SP_LOAD_SO_FILE_NAME, 0).edit();
                editorEdit.clear();
                editorEdit.apply();
                ALog.i("LoadSoFailUtil", "loadSoSuccess", "fail times", Integer.valueOf(iC));
            }
        } catch (Throwable th) {
            ALog.e("LoadSoFailUtil", "loadSoSuccess", th, new Object[0]);
        }
    }

    public static void b() {
        try {
            SharedPreferences sharedPreferences = GlobalClientInfo.getContext().getSharedPreferences(Constants.SP_LOAD_SO_FILE_NAME, 0);
            int i = sharedPreferences.getInt(Constants.SP_KEY_LOAD_SO_TIMES, 0) + 1;
            if (i > 0) {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.putInt(Constants.SP_KEY_LOAD_SO_TIMES, i);
                editorEdit.apply();
            }
            ALog.e("LoadSoFailUtil", "loadSoFail", "times", Integer.valueOf(i));
        } catch (Throwable th) {
            ALog.e("LoadSoFailUtil", "loadSoFail", th, new Object[0]);
        }
    }

    public static int c() {
        int i;
        try {
            i = GlobalClientInfo.getContext().getSharedPreferences(Constants.SP_LOAD_SO_FILE_NAME, 0).getInt(Constants.SP_KEY_LOAD_SO_TIMES, 0);
        } catch (Throwable th) {
            th = th;
            i = 0;
        }
        try {
            ALog.i("LoadSoFailUtil", "getSoFailTimes", "times", Integer.valueOf(i));
        } catch (Throwable th2) {
            th = th2;
            ALog.e("LoadSoFailUtil", "getSoFailTimes", th, new Object[0]);
        }
        return i;
    }
}
