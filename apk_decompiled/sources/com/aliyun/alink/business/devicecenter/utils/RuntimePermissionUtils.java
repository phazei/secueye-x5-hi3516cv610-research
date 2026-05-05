package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RuntimePermissionUtils {

    public static final class Permission {
        public static final String[] LOCATION = {com.hjq.permissions.Permission.ACCESS_FINE_LOCATION, com.hjq.permissions.Permission.ACCESS_COARSE_LOCATION};
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String... strArr) {
        return hasPermission(context, (List<String>) Arrays.asList(strArr));
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> list) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : list) {
            if (ContextCompat.checkSelfPermission(context, str) == -1) {
                return false;
            }
            String strPermissionToOp = AppOpsManagerCompat.permissionToOp(str);
            if (!TextUtils.isEmpty(strPermissionToOp) && AppOpsManagerCompat.noteProxyOp(context, strPermissionToOp, context.getPackageName()) != 0) {
                return false;
            }
        }
        return true;
    }
}
