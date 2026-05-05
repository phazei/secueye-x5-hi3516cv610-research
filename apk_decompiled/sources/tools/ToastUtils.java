package tools;

import android.content.Context;
import android.widget.Toast;

/* JADX INFO: loaded from: classes4.dex */
public class ToastUtils {
    private static String oldMsg;
    private static long oneTime;
    private static Toast toast;
    private static long twoTime;

    public static void showToast(Context context, String str) {
        Toast toast2 = toast;
        if (toast2 != null) {
            toast2.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, "", 0);
        toast.setGravity(17, 0, 0);
        toast.setText(str);
        toast.show();
    }
}
