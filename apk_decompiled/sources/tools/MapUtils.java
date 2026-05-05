package tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class MapUtils {
    private static final double A = 6378245.0d;
    private static final double EE = 0.006693421622965943d;
    private static final double PI = 3.141592653589793d;

    public static double dddmmToDecimal(double d2) {
        int i = ((int) d2) / 100;
        return ((double) i) + ((d2 - ((double) (i * 100))) / 60.0d);
    }

    public static boolean isAvilible(Context context, String str) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        ArrayList arrayList = new ArrayList();
        if (installedPackages != null) {
            for (int i = 0; i < installedPackages.size(); i++) {
                arrayList.add(installedPackages.get(i).packageName);
            }
        }
        return arrayList.contains(str);
    }
}
