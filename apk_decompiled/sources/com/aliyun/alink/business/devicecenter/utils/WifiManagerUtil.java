package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.aliyun.alink.business.devicecenter.base.WiFiFreqType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class WifiManagerUtil {
    public static int NO_PASSWORD_WIFI = 0;
    public static int WEP_CIPHER_WIFI = 1;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;
    public static int WPA_CIPHER_WIFI = 2;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3791a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public WifiManager.WifiLock f3792b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public WifiManager.MulticastLock f3793c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public WifiInfo f3794d;
    public ConnectivityManager e;
    public List<WifiConfiguration> f = new LinkedList();
    public WifiManager wifiManager;

    public enum NetworkType {
        WLAN,
        ETHERNET
    }

    public WifiManagerUtil(Context context) {
        this.f3791a = context;
        this.wifiManager = (WifiManager) context.getSystemService("wifi");
        this.f3794d = this.wifiManager.getConnectionInfo();
        this.f3792b = this.wifiManager.createWifiLock("Test");
        this.f3793c = this.wifiManager.createMulticastLock("Alink");
        this.e = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public static InetAddress getBroadcast(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        ALog.d("WifiManagerUtil", "getBroadcast(),inetAddr = " + inetAddress);
        try {
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(inetAddress);
            if (byInetAddress == null) {
                return null;
            }
            InetAddress broadcast = null;
            for (InterfaceAddress interfaceAddress : byInetAddress.getInterfaceAddresses()) {
                if (interfaceAddress.getAddress() instanceof Inet4Address) {
                    broadcast = interfaceAddress.getBroadcast();
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("iAddr=");
            sb.append(broadcast);
            ALog.d("WifiManagerUtil", sb.toString());
            return broadcast;
        } catch (SocketException e) {
            e.printStackTrace();
            ALog.w("WifiManagerUtil", "getBroadcast" + e.getMessage());
            return null;
        } catch (Exception e2) {
            ALog.w("WifiManagerUtil", "getBroadcast" + e2.getMessage());
            return null;
        }
    }

    public static InetAddress getIpAddress(NetworkType networkType) {
        ALog.d("WifiManagerUtil", "getIpAddress()");
        InetAddress inetAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterfaceNextElement = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterfaceNextElement.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                        if (networkInterfaceNextElement.getDisplayName().contains("wlan0") && networkType == NetworkType.WLAN) {
                            return inetAddressNextElement;
                        }
                        if (networkInterfaceNextElement.getDisplayName().contains("eth0") && networkType == NetworkType.ETHERNET) {
                            return inetAddressNextElement;
                        }
                        if (networkInterfaceNextElement.getDisplayName().contains("wlan0") || networkInterfaceNextElement.getDisplayName().contains("eth0") || networkInterfaceNextElement.getDisplayName().contains("ap0")) {
                            inetAddress = inetAddressNextElement;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            ALog.d("WifiManagerUtil", e.toString());
        }
        return inetAddress;
    }

    public static String getWifiIP(Context context) {
        ALog.d("WifiManagerUtil", "getWifiIP ");
        if (context == null) {
            return null;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (!wifiManager.isWifiEnabled()) {
                return null;
            }
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            StringBuilder sb = new StringBuilder();
            sb.append(ipAddress & 255);
            sb.append(".");
            sb.append((ipAddress >> 8) & 255);
            sb.append(".");
            sb.append((ipAddress >> 16) & 255);
            sb.append(".");
            sb.append((ipAddress >> 24) & 255);
            return sb.toString();
        } catch (Exception e) {
            ALog.w("WifiManagerUtil", "getWifiIP exception=" + e);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x004f A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String a() {
        /*
            r5 = this;
            android.net.ConnectivityManager r0 = r5.e     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.String r1 = "mContext"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r1)     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            r1 = 1
            r0.setAccessible(r1)     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            android.net.ConnectivityManager r1 = r5.e     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            android.content.Context r0 = (android.content.Context) r0     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.Class r1 = r0.getClass()     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.String r2 = "getOpPackageName"
            r3 = 0
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.reflect.Method r1 = r1.getMethod(r2, r4)     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.Object r0 = r1.invoke(r0, r2)     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Exception -> L2e java.lang.reflect.InvocationTargetException -> L33 java.lang.NoSuchMethodException -> L38 java.lang.IllegalAccessException -> L3d java.lang.NoSuchFieldException -> L42
            return r0
        L2e:
            r0 = move-exception
            r0.printStackTrace()
            goto L46
        L33:
            r0 = move-exception
            r0.printStackTrace()
            goto L46
        L38:
            r0 = move-exception
            r0.printStackTrace()
            goto L46
        L3d:
            r0 = move-exception
            r0.printStackTrace()
            goto L46
        L42:
            r0 = move-exception
            r0.printStackTrace()
        L46:
            android.content.Context r0 = r5.f3791a
            if (r0 == 0) goto L4f
            java.lang.String r0 = r0.getPackageName()
            return r0
        L4f:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil.a():java.lang.String");
    }

    public void acquireMulticastLock() {
        try {
            this.f3793c.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acquireWifiLock() {
        this.f3792b.acquire();
    }

    public int closeWiFiAP() {
        if (this.wifiManager == null) {
            ALog.d("WifiManagerUtil", "closeWiFiAP(), wifi manager == null");
            return 0;
        }
        ALog.d("WifiManagerUtil", "Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 25) {
            if (!isWifiApEnabled()) {
                return 0;
            }
            try {
                Method method = this.wifiManager.getClass().getMethod("getWifiApConfiguration", new Class[0]);
                method.setAccessible(true);
                this.wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE).invoke(this.wifiManager, (WifiConfiguration) method.invoke(this.wifiManager, new Object[0]), false);
                return 1;
            } catch (Exception e) {
                ALog.d("WifiManagerUtil", "closeWiFiAP(), error,e= " + e.toString());
                return -1;
            }
        }
        try {
            Field declaredField = this.e.getClass().getDeclaredField("mService");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this.e);
            Class<?> cls = Class.forName(obj.getClass().getName());
            if (Build.VERSION.SDK_INT >= 27) {
                cls.getMethod("stopTethering", Integer.TYPE, String.class).invoke(obj, 0, a());
            } else {
                cls.getMethod("stopTethering", Integer.TYPE).invoke(obj, 0);
            }
            return 1;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            ALog.e("WifiManagerUtil", "closeWiFiAP failed " + e2);
            return -1;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            ALog.e("WifiManagerUtil", "closeWiFiAP failed " + e3);
            return -1;
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
            ALog.e("WifiManagerUtil", "closeWiFiAP failed " + e4);
            return -1;
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
            return -1;
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
            ALog.e("WifiManagerUtil", "closeWiFiAP failed " + e6);
            return -1;
        } catch (Exception e7) {
            e7.printStackTrace();
            ALog.e("WifiManagerUtil", "closeWiFiAP failed " + e7);
            return -1;
        }
    }

    public WifiConfiguration createWifiConfiguration(String str, String str2, int i, boolean z) {
        ALog.d("WifiManagerUtil", "createWifiConfiguration(),SSID = " + str + "## passwd = " + str2 + "## Type = " + i + ",isHotSpot=" + z);
        WifiConfiguration wifiConfigurationIsOpenWifiExist = i == NO_PASSWORD_WIFI ? isOpenWifiExist(str) : isWifiExist(str);
        if (wifiConfigurationIsOpenWifiExist != null) {
            removeWifi(wifiConfigurationIsOpenWifiExist.networkId);
        }
        if (wifiConfigurationIsOpenWifiExist == null) {
            wifiConfigurationIsOpenWifiExist = new WifiConfiguration();
        }
        wifiConfigurationIsOpenWifiExist.allowedAuthAlgorithms.clear();
        wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.clear();
        wifiConfigurationIsOpenWifiExist.allowedKeyManagement.clear();
        wifiConfigurationIsOpenWifiExist.allowedPairwiseCiphers.clear();
        wifiConfigurationIsOpenWifiExist.allowedProtocols.clear();
        wifiConfigurationIsOpenWifiExist.SSID = str;
        if (i == NO_PASSWORD_WIFI) {
            wifiConfigurationIsOpenWifiExist.allowedKeyManagement.set(0);
        } else if (i == WEP_CIPHER_WIFI) {
            wifiConfigurationIsOpenWifiExist.wepKeys[0] = str2;
            wifiConfigurationIsOpenWifiExist.allowedAuthAlgorithms.set(1);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(3);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(2);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(0);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(1);
            wifiConfigurationIsOpenWifiExist.allowedKeyManagement.set(0);
            wifiConfigurationIsOpenWifiExist.wepTxKeyIndex = 0;
        } else if (i == WPA_CIPHER_WIFI) {
            wifiConfigurationIsOpenWifiExist.preSharedKey = str2;
            wifiConfigurationIsOpenWifiExist.hiddenSSID = false;
            wifiConfigurationIsOpenWifiExist.allowedAuthAlgorithms.set(0);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(2);
            wifiConfigurationIsOpenWifiExist.allowedKeyManagement.set(1);
            wifiConfigurationIsOpenWifiExist.allowedPairwiseCiphers.set(1);
            wifiConfigurationIsOpenWifiExist.allowedGroupCiphers.set(3);
            wifiConfigurationIsOpenWifiExist.allowedPairwiseCiphers.set(2);
            wifiConfigurationIsOpenWifiExist.status = 2;
        }
        return wifiConfigurationIsOpenWifiExist;
    }

    public void enableWifiBySsid(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("enableWifiBySsid ssid=");
            sb.append(str);
            ALog.d("WifiManagerUtil", sb.toString());
            if (this.f == null) {
                return;
            }
            for (int i = 0; i < this.f.size(); i++) {
                WifiConfiguration wifiConfiguration = this.f.get(i);
                if (wifiConfiguration != null && wifiConfiguration.SSID != null) {
                    String str2 = wifiConfiguration.SSID;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("\"");
                    sb2.append(str);
                    sb2.append("\"");
                    if (str2.equals(sb2.toString())) {
                        this.wifiManager.disconnect();
                        this.wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                        this.wifiManager.reconnect();
                    }
                }
            }
        } catch (Exception e) {
            ALog.w("WifiManagerUtil", "enableWifiBySsid e=" + e);
        }
    }

    public int getWifiApState() {
        try {
            return ((Integer) this.wifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.wifiManager, new Object[0])).intValue();
        } catch (Exception e) {
            ALog.d("WifiManagerUtil", "getWifiApState(), error = " + e.toString());
            return 14;
        }
    }

    public int getWifiRssid() {
        WifiInfo wifiInfo = this.f3794d;
        if (wifiInfo == null) {
            return -127;
        }
        return wifiInfo.getRssi();
    }

    public String getWifiType() {
        WifiInfo wifiInfo = this.f3794d;
        if (wifiInfo == null) {
            return WiFiFreqType.WIFI_UNKNOWN.value();
        }
        int frequency = 0;
        if (Build.VERSION.SDK_INT < 21) {
            String ssid = wifiInfo.getSSID();
            if (ssid != null && ssid.length() > 2) {
                String strSubstring = ssid.substring(1, ssid.length() - 1);
                Iterator<ScanResult> it = this.wifiManager.getScanResults().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ScanResult next = it.next();
                    if (next.SSID.equals(strSubstring)) {
                        frequency = next.frequency;
                        break;
                    }
                }
            }
        } else {
            frequency = wifiInfo.getFrequency();
        }
        return is5GHz(frequency) ? WiFiFreqType.WIFI_5G.value() : is24GHz(frequency) ? WiFiFreqType.WIFI_2_4G.value() : WiFiFreqType.WIFI_UNKNOWN.value();
    }

    public boolean is24GHz(int i) {
        return i > 2400 && i < 2500;
    }

    public boolean is5GHz(int i) {
        return i > 4900 && i < 5900;
    }

    public boolean isHTC() {
        try {
            String str = Build.MANUFACTURER;
            StringBuilder sb = new StringBuilder();
            sb.append("isHTC(), manu=");
            sb.append(str);
            ALog.d("WifiManagerUtil", sb.toString());
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return str.trim().toLowerCase().contains("htc");
        } catch (Exception e) {
            ALog.d("WifiManagerUtil", "isHTC(),error+" + e);
            return false;
        }
    }

    public WifiConfiguration isOpenWifiExist(String str) {
        ALog.d("WifiManagerUtil", "isOpenWifiExist,ssid=" + str);
        updateConfigedWifi();
        for (int i = 0; i < this.f.size(); i++) {
            WifiConfiguration wifiConfiguration = this.f.get(i);
            if (wifiConfiguration != null && !TextUtils.isEmpty(wifiConfiguration.SSID)) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"") && wifiConfiguration.allowedKeyManagement.get(0)) {
                    ALog.d("WifiManagerUtil", "isOpenWifiExist(),found config");
                    return this.f.get(i);
                }
            }
        }
        return null;
    }

    public boolean isWifiApEnabled() {
        int wifiApState = getWifiApState();
        ALog.d("WifiManagerUtil", "wifiApState=" + wifiApState);
        return wifiApState == 13;
    }

    public Boolean isWifiAvaiable() {
        ALog.d("WifiManagerUtil", "isWifiAvaiable");
        WifiManager wifiManager = this.wifiManager;
        if (wifiManager == null) {
            ALog.d("WifiManagerUtil", "isWifiAvaiable,wifiManager is null");
            return false;
        }
        boolean zIsWifiEnabled = wifiManager.isWifiEnabled();
        ALog.d("WifiManagerUtil", "isWifiAvaiable,enable = " + zIsWifiEnabled);
        return Boolean.valueOf(zIsWifiEnabled);
    }

    public WifiConfiguration isWifiExist(String str) {
        ALog.d("WifiManagerUtil", "isWifiExist");
        updateConfigedWifi();
        for (int i = 0; i < this.f.size(); i++) {
            WifiConfiguration wifiConfiguration = this.f.get(i);
            if (wifiConfiguration != null && !TextUtils.isEmpty(wifiConfiguration.SSID)) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                    ALog.d("WifiManagerUtil", "isWifiExist(),found config");
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }

    public void openWifi() {
        if (isWifiAvaiable().booleanValue()) {
            return;
        }
        this.wifiManager.setWifiEnabled(true);
    }

    public void releaseMulticastLock() {
        try {
            if (this.f3793c.isHeld()) {
                this.f3793c.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseWifiLock() {
        if (this.f3792b.isHeld()) {
            this.f3792b.acquire();
        }
    }

    public void removeWifi(int i) {
        this.wifiManager.removeNetwork(i);
    }

    public boolean setWifiApConfigurationForHTC(WifiConfiguration wifiConfiguration) {
        ALog.d("WifiManagerUtil", "setWifiApConfigurationForHTC, call, apConfig = " + wifiConfiguration.toString());
        try {
            Field declaredField = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(wifiConfiguration);
            declaredField.setAccessible(false);
            if (obj != null) {
                Field declaredField2 = obj.getClass().getDeclaredField("SSID");
                declaredField2.setAccessible(true);
                declaredField2.set(obj, wifiConfiguration.SSID);
                declaredField2.setAccessible(false);
                Field declaredField3 = obj.getClass().getDeclaredField(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_BSSID);
                declaredField3.setAccessible(true);
                declaredField3.set(obj, wifiConfiguration.BSSID);
                declaredField3.setAccessible(false);
                Field declaredField4 = obj.getClass().getDeclaredField("dhcpEnable");
                declaredField4.setAccessible(true);
                declaredField4.setInt(obj, 1);
                declaredField4.setAccessible(false);
                Field declaredField5 = obj.getClass().getDeclaredField("key");
                declaredField5.setAccessible(true);
                declaredField5.set(obj, wifiConfiguration.preSharedKey);
                declaredField5.setAccessible(false);
            }
            return true;
        } catch (Exception e) {
            ALog.d("WifiManagerUtil", "setWifiApConfigurationForHTC,error, e = " + e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfiguration, boolean z) {
        if (this.wifiManager == null) {
            ALog.d("WifiManagerUtil", "setWifiApEnabled(), wifiManager is null");
            return false;
        }
        if (isHTC()) {
            boolean wifiApConfigurationForHTC = setWifiApConfigurationForHTC(wifiConfiguration);
            ALog.d("WifiManagerUtil", "setWifiApEnabled(), success = " + wifiApConfigurationForHTC + ".");
            if (!wifiApConfigurationForHTC) {
                return false;
            }
        }
        if (z) {
            try {
                boolean wifiEnabled = this.wifiManager.setWifiEnabled(false);
                StringBuilder sb = new StringBuilder();
                sb.append("setWifiApEnabled(), success = ");
                sb.append(wifiEnabled);
                sb.append(".");
                ALog.d("WifiManagerUtil", sb.toString());
                if (!wifiEnabled) {
                    return false;
                }
            } catch (Exception e) {
                ALog.d("WifiManagerUtil", " setWifiApEnabled(), e = " + e);
                e.printStackTrace();
                return false;
            }
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f3791a.getSystemService("connectivity");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Build.VERSION.SDK_INT=");
        sb2.append(Build.VERSION.SDK_INT);
        ALog.d("WifiManagerUtil", sb2.toString());
        if (Build.VERSION.SDK_INT < 25) {
            return ((Boolean) this.wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE).invoke(this.wifiManager, wifiConfiguration, Boolean.valueOf(z))).booleanValue();
        }
        try {
            try {
                try {
                    Field declaredField = connectivityManager.getClass().getDeclaredField("mService");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get(connectivityManager);
                    Class<?> cls = Class.forName(obj.getClass().getName());
                    if (Build.VERSION.SDK_INT >= 27) {
                        cls.getMethod("startTethering", Integer.TYPE, ResultReceiver.class, Boolean.TYPE, String.class).invoke(obj, 0, new ResultReceiver(new Handler()) { // from class: com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil.1
                            @Override // android.os.ResultReceiver
                            public void onReceiveResult(int i, Bundle bundle) {
                                super.onReceiveResult(i, bundle);
                                ALog.d("WifiManagerUtil", "onReceiveResult resultData=" + bundle);
                            }
                        }, true, a());
                    } else {
                        cls.getMethod("startTethering", Integer.TYPE, ResultReceiver.class, Boolean.TYPE).invoke(obj, 0, new ResultReceiver(new Handler()) { // from class: com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil.2
                            @Override // android.os.ResultReceiver
                            public void onReceiveResult(int i, Bundle bundle) {
                                super.onReceiveResult(i, bundle);
                                ALog.d("WifiManagerUtil", "onReceiveResult resultData=" + bundle);
                            }
                        }, true);
                    }
                    return true;
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                    return false;
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                    return false;
                }
            } catch (NoSuchFieldException e4) {
                e4.printStackTrace();
                return false;
            } catch (Exception e5) {
                e5.printStackTrace();
                return false;
            }
        } catch (NoSuchMethodException e6) {
            e6.printStackTrace();
            return false;
        } catch (InvocationTargetException e7) {
            e7.printStackTrace();
            return false;
        }
    }

    public void updateConfigedWifi() {
        ALog.d("WifiManagerUtil", "updateConfigedWifi()");
        try {
            if (this.wifiManager.getConfiguredNetworks() != null) {
                this.f.clear();
                this.f.addAll(this.wifiManager.getConfiguredNetworks());
            }
        } catch (Exception e) {
            ALog.w("WifiManagerUtil", "updateConfigedWifi(),error," + e);
            e.printStackTrace();
        }
    }
}
