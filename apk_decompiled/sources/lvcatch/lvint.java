package lvcatch;

import android.os.Build;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes4.dex */
public class lvint {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private static String f7975lvdo;

    public static String lvdo() {
        String property = System.getProperty("http.agent");
        if (!TextUtils.isEmpty(property)) {
            property = "(" + System.getProperty("os.name") + "/Android " + Build.VERSION.RELEASE + "/" + Build.MODEL + "/" + Build.ID + ")";
        }
        return property.replaceAll("[^\\p{ASCII}]", "?");
    }

    public static String lvfor() {
        return "2.0.0";
    }

    public static String lvif() {
        if (f7975lvdo == null) {
            f7975lvdo = "aliyun-log-sdk-android/" + lvfor() + "/" + lvdo();
        }
        return f7975lvdo;
    }
}
