package tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.link.ui.component.LinkAlertDialog;
import com.hjq.permissions.Permission;
import com.seculink.app.BuildConfig;
import com.seculink.app.R;
import com.taobao.accs.common.Constants;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class LocationUtil {
    public static final String MANUFACTURER_HUAWEI = "HUAWEI";
    public static final String MANUFACTURER_LENOVO = "LENOVO";
    public static final String MANUFACTURER_LETV = "Letv";
    public static final String MANUFACTURER_LG = "LG";
    public static final String MANUFACTURER_MEIZU = "Meizu";
    public static final String MANUFACTURER_OPPO = "OPPO";
    public static final String MANUFACTURER_SAMSUNG = "samsung";
    public static final String MANUFACTURER_SONY = "Sony";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_XIAOMI = "Xiaomi";
    public static final String MANUFACTURER_YULONG = "YuLong";
    public static final String MANUFACTURER_ZTE = "ZTE";
    private static final String TAG = "LocationUtil";
    private static LocationListener locationListener = new LocationListener() { // from class: tools.LocationUtil.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };
    private static LocationManager locationManager;
    private static Context mContext;

    static Address getCountryCode(Context context) {
        Location locationInfo = getLocationInfo(context);
        if (locationInfo == null) {
            return null;
        }
        try {
            List<Address> fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(locationInfo.getLatitude(), locationInfo.getLongitude(), 1);
            if (fromLocation.size() > 0) {
                return fromLocation.get(0);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Location getLocationInfo(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) != 0 && ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) != 0) {
            return null;
        }
        try {
            Location lastKnownLocation = ((LocationManager) context.getSystemService("location")).getLastKnownLocation("network");
            if (lastKnownLocation != null) {
                return lastKnownLocation;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void requestLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) == 0 || ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0) {
            try {
                locationManager = (LocationManager) context.getSystemService("location");
                locationManager.requestLocationUpdates("network", 1L, 1.0f, locationListener);
            } catch (Exception unused) {
            }
        }
    }

    public static void cancelLocating() {
        LocationManager locationManager2 = locationManager;
        if (locationManager2 != null) {
            locationManager2.removeUpdates(locationListener);
            locationManager = null;
        }
    }

    public static boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), "location_mode") != 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return !TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed"));
    }

    public static void remindStartLocateService(final Activity activity2) {
        String string = AApplication.getInstance().getString(R.string.location_service);
        new LinkAlertDialog.Builder(activity2).setTitle(string).setMessage(AApplication.getInstance().getString(R.string.location_service_des)).setPositiveButton(AApplication.getInstance().getString(R.string.component_set_up), new LinkAlertDialog.OnClickListener() { // from class: tools.LocationUtil.2
            @Override // com.aliyun.iot.link.ui.component.LinkAlertDialog.OnClickListener
            public void onClick(LinkAlertDialog linkAlertDialog) {
                activity2.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 0);
                linkAlertDialog.dismiss();
            }
        }).setNegativeButton(AApplication.getInstance().getString(R.string.component_cancel), new LinkAlertDialog.OnClickListener() { // from class: tools.-$$Lambda$UxZbxecrBHd8g0nIxH7fOBIbhPE
            @Override // com.aliyun.iot.link.ui.component.LinkAlertDialog.OnClickListener
            public final void onClick(LinkAlertDialog linkAlertDialog) {
                linkAlertDialog.dismiss();
            }
        }).create().show();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void goToSetting(android.content.Context r2) {
        /*
            java.lang.String r0 = android.os.Build.MANUFACTURER
            int r1 = r0.hashCode()
            switch(r1) {
                case -1675632421: goto L46;
                case 2427: goto L3c;
                case 2364891: goto L32;
                case 2432928: goto L28;
                case 2582855: goto L1e;
                case 74224812: goto L14;
                case 2141820391: goto La;
                default: goto L9;
            }
        L9:
            goto L50
        La:
            java.lang.String r1 = "HUAWEI"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 0
            goto L51
        L14:
            java.lang.String r1 = "Meizu"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 1
            goto L51
        L1e:
            java.lang.String r1 = "Sony"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 3
            goto L51
        L28:
            java.lang.String r1 = "OPPO"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 4
            goto L51
        L32:
            java.lang.String r1 = "Letv"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 6
            goto L51
        L3c:
            java.lang.String r1 = "LG"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 5
            goto L51
        L46:
            java.lang.String r1 = "Xiaomi"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L50
            r0 = 2
            goto L51
        L50:
            r0 = -1
        L51:
            switch(r0) {
                case 0: goto L77;
                case 1: goto L73;
                case 2: goto L6f;
                case 3: goto L6b;
                case 4: goto L67;
                case 5: goto L63;
                case 6: goto L5f;
                default: goto L54;
            }
        L54:
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "android.settings.LOCATION_SOURCE_SETTINGS"
            r0.<init>(r1)
            r2.startActivity(r0)
            goto L7a
        L5f:
            Letv(r2)
            goto L7a
        L63:
            LG(r2)
            goto L7a
        L67:
            OPPO(r2)
            goto L7a
        L6b:
            Sony(r2)
            goto L7a
        L6f:
            Xiaomi(r2)
            goto L7a
        L73:
            Meizu(r2)
            goto L7a
        L77:
            Huawei(r2)
        L7a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.LocationUtil.goToSetting(android.content.Context):void");
    }

    public static void Huawei(Context context) {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        context.startActivity(intent);
    }

    public static void Meizu(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        context.startActivity(intent);
    }

    public static void Xiaomi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(intent);
    }

    public static void Sony(Context context) {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
        context.startActivity(intent);
    }

    public static void OPPO(Context context) {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity"));
        context.startActivity(intent);
    }

    public static void LG(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity"));
        context.startActivity(intent);
    }

    public static void Letv(Context context) {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps"));
        context.startActivity(intent);
    }

    public static void _360(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(268435456);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        intent.setComponent(new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"));
        context.startActivity(intent);
    }

    public static boolean isOpenLocationService(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) != 0 && ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) != 0) {
            return false;
        }
        LocationManager locationManager2 = (LocationManager) context.getSystemService("location");
        boolean zIsProviderEnabled = locationManager2.isProviderEnabled("gps");
        boolean zIsProviderEnabled2 = locationManager2.isProviderEnabled("network");
        if (zIsProviderEnabled) {
            locationManager2.requestLocationUpdates("gps", 0L, 0.0f, locationListener);
        } else if (zIsProviderEnabled2) {
            locationManager2.requestLocationUpdates("network", 0L, 0.0f, locationListener);
        }
        return zIsProviderEnabled || zIsProviderEnabled2;
    }
}
