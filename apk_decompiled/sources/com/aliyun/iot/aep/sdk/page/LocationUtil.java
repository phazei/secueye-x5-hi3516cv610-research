package com.aliyun.iot.aep.sdk.page;

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
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.R;
import com.aliyun.iot.link.ui.component.LinkAlertDialog;
import com.hjq.permissions.Permission;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class LocationUtil {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static LocationManager f4846a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static LocationListener f4847b = new LocationListener() { // from class: com.aliyun.iot.aep.sdk.page.LocationUtil.1
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

    static Address a(Context context) {
        Location locationB = b(context);
        if (locationB != null) {
            try {
                List<Address> fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(locationB.getLatitude(), locationB.getLongitude(), 1);
                if (fromLocation.size() > 0) {
                    return fromLocation.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("LocationUtil", "解析获取国家码失败");
        return null;
    }

    private static Location b(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) != 0 && ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) != 0) {
            Log.i("LocationUtil", "提示无法定位,请先打开位置权限");
        } else {
            try {
                Location lastKnownLocation = ((LocationManager) context.getSystemService("location")).getLastKnownLocation("network");
                if (lastKnownLocation != null) {
                    Log.i("LocationUtil", "获取定位信息成功");
                    return lastKnownLocation;
                }
            } catch (Exception e) {
                Log.i("LocationUtil", "获取定位信息出错" + e.getMessage());
            }
        }
        Log.i("LocationUtil", "获取定位信息失败");
        return null;
    }

    public static void requestLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) != 0 && ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) != 0) {
            Log.i("LocationUtil", "提示无法定位,请先打开位置权限");
            return;
        }
        try {
            f4846a = (LocationManager) context.getSystemService("location");
            f4846a.requestLocationUpdates("network", 1L, 1.0f, f4847b);
        } catch (Exception e) {
            Log.i("LocationUtil", "请求定位信息出错" + e.getMessage());
        }
    }

    public static void cancelLocating() {
        LocationManager locationManager = f4846a;
        if (locationManager != null) {
            locationManager.removeUpdates(f4847b);
            f4846a = null;
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

    public static void remindStartLocateService(final Context context) {
        new LinkAlertDialog.Builder(context).setTitle(AApplication.getInstance().getString(R.string.component_unopened_location_service)).setMessage("").setPositiveButton(AApplication.getInstance().getString(R.string.component_set_up), new LinkAlertDialog.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.LocationUtil.2
            @Override // com.aliyun.iot.link.ui.component.LinkAlertDialog.OnClickListener
            public void onClick(LinkAlertDialog linkAlertDialog) {
                context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                linkAlertDialog.dismiss();
            }
        }).setNegativeButton(AApplication.getInstance().getString(R.string.component_cancel), new LinkAlertDialog.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.-$$Lambda$UxZbxecrBHd8g0nIxH7fOBIbhPE
            @Override // com.aliyun.iot.link.ui.component.LinkAlertDialog.OnClickListener
            public final void onClick(LinkAlertDialog linkAlertDialog) {
                linkAlertDialog.dismiss();
            }
        }).create().show();
    }
}
