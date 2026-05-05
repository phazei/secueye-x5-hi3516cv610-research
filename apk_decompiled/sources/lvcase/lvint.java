package lvcase;

import android.util.Log;

/* JADX INFO: loaded from: classes4.dex */
public class lvint {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private static boolean f7973lvdo = false;

    public static void lvdo() {
        f7973lvdo = true;
    }

    public static void lvdo(String str) {
        lvdo("linksdk_lv_sls", str);
    }

    public static void lvdo(String str, String str2) {
        lvdo(str, str2, true);
    }

    public static void lvdo(String str, String str2, boolean z) {
        if (f7973lvdo) {
            Log.d(str, str2);
            lvdo(str2, z);
        }
    }

    private static void lvdo(String str, boolean z) {
    }

    public static void lvif(String str) {
        lvif("linksdk_lv_sls", str);
    }

    public static void lvif(String str, String str2) {
        lvdo(str, str2, true);
    }

    public static void lvif(String str, boolean z) {
        lvdo("linksdk_lv_sls", str, z);
    }

    public static boolean lvif() {
        return f7973lvdo;
    }
}
