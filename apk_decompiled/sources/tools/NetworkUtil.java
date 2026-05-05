package tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import enums.NetworkStateEnum;

/* JADX INFO: loaded from: classes4.dex */
public class NetworkUtil {
    public static final NetworkStateEnum getCurrentNetworkState(Context context) {
        NetworkStateEnum networkStateEnum = NetworkStateEnum.NONE;
        if (Build.VERSION.SDK_INT < 21) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
            if (networkInfo.isConnected() && networkInfo2.isConnected()) {
                return NetworkStateEnum.WIFI;
            }
            if (!networkInfo.isConnected() || networkInfo2.isConnected()) {
                return (networkInfo.isConnected() || !networkInfo2.isConnected()) ? networkStateEnum : NetworkStateEnum.MOBILE;
            }
            return NetworkStateEnum.WIFI;
        }
        ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
        for (Network network : connectivityManager2.getAllNetworks()) {
            NetworkInfo networkInfo3 = connectivityManager2.getNetworkInfo(network);
            if (networkInfo3 != null) {
                if (networkInfo3.getType() == 0) {
                    networkStateEnum = NetworkStateEnum.MOBILE;
                }
                if (networkInfo3.getType() == 1) {
                    networkStateEnum = NetworkStateEnum.WIFI;
                }
            }
        }
        return networkStateEnum;
    }
}
