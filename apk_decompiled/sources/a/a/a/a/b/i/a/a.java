package a.a.a.a.b.i.a;

import android.content.Context;
import android.content.SharedPreferences;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;

/* JADX INFO: compiled from: ProvisionSPUtils.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static SharedPreferences f1368a;

    public static SharedPreferences a(Context context) {
        if (f1368a == null) {
            f1368a = context.getSharedPreferences(WifiProvisionUtConst.ARG_CONNECTION, 0);
        }
        return f1368a;
    }

    public static int a(Context context, String str, int i) {
        return a(context).getInt(str, i);
    }
}
