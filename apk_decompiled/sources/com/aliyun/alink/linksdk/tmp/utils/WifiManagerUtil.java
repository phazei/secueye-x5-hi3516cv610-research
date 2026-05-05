package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class WifiManagerUtil {
    public static int NO_PASSWORD_WIFI = 0;
    private static final String TAG = "WifiManagerUtil";
    public static int WEP_CIPHER_WIFI = 1;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_FAILED = 14;
    public static int WPA_CIPHER_WIFI = 2;
    private ConnectivityManager connectivityManager;
    private Context context;
    private WifiInfo currWifiInfo;
    private WifiManager.MulticastLock multicastLock;
    private WifiManager.WifiLock wifiLock;
    public WifiManager wifiManager;
    public final String ALINK_SOFT_AP_GATEWAY = "172.31.254.250";
    public final String ALINK_SOFT_AP_STATIC_IP = "172.31.254.153";
    public final String ALINK_SOFT_AP_DNS = "192.192.192.192";
    private List<ScanResult> scanResultList = new LinkedList();
    private List<WifiConfiguration> wifiConfigedList = new LinkedList();

    public enum NetworkType {
        WLAN,
        ETHERNET
    }

    public boolean is24GHz(int i) {
        return i > 2400 && i < 2500;
    }

    public boolean is5GHz(int i) {
        return i > 4900 && i < 5900;
    }

    public WifiManagerUtil(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService("wifi");
        this.currWifiInfo = this.wifiManager.getConnectionInfo();
        this.wifiLock = this.wifiManager.createWifiLock("Test");
        this.multicastLock = this.wifiManager.createMulticastLock("Alink");
        this.connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public WifiInfo getCurrWifiInfo() {
        return this.currWifiInfo;
    }

    public DhcpInfo getRouterDhcp() {
        return ((WifiManager) this.context.getSystemService("wifi")).getDhcpInfo();
    }

    public static void startScanWifiList(Context context) {
        ALog.d(TAG, "startScanWifiList()");
        ALog.d(TAG, "startScanWifiList()," + ((WifiManager) context.getSystemService("wifi")).startScan());
    }

    public void openWifi() {
        if (isWifiAvaiable().booleanValue()) {
            this.wifiManager.setWifiEnabled(true);
        }
    }

    public void updateWifi() {
        ALog.d(TAG, "updateWifi()");
        this.currWifiInfo = this.wifiManager.getConnectionInfo();
    }

    public void updateConfigedWifi() {
        ALog.d(TAG, "updateConfigedWifi()");
        try {
            if (this.wifiManager.getConfiguredNetworks() != null) {
                this.wifiConfigedList.clear();
                this.wifiConfigedList.addAll(this.wifiManager.getConfiguredNetworks());
            }
        } catch (Exception e) {
            ALog.d(TAG, "updateConfigedWifi(),error," + e);
            e.printStackTrace();
        }
    }

    public void enableWifiBySsid(String str) {
        try {
            ALog.w(TAG, "enableWifiBySsid ssid=" + str);
            if (this.wifiConfigedList == null) {
                return;
            }
            for (int i = 0; i < this.wifiConfigedList.size(); i++) {
                WifiConfiguration wifiConfiguration = this.wifiConfigedList.get(i);
                if (wifiConfiguration != null && wifiConfiguration.SSID != null) {
                    if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                        this.wifiManager.disconnect();
                        this.wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                        this.wifiManager.reconnect();
                    }
                }
            }
        } catch (Exception e) {
            ALog.w(TAG, "enableWifiBySsid e=" + e);
        }
    }

    public void closeWifi() {
        if (isWifiAvaiable().booleanValue()) {
            this.wifiManager.setWifiEnabled(false);
        }
    }

    public WifiConfiguration createWifiConfiguration(String str, String str2, int i, boolean z) {
        WifiConfiguration wifiConfigurationIsWifiExist;
        ALog.d(TAG, "createWifiConfiguration(),SSID = " + str + "## Password = " + str2 + "## Type = " + i + ",isHotSpot=" + z);
        if (i == NO_PASSWORD_WIFI) {
            wifiConfigurationIsWifiExist = isOpenWifiExist(str);
        } else {
            wifiConfigurationIsWifiExist = isWifiExist(str);
        }
        if (wifiConfigurationIsWifiExist != null) {
            removeWifi(wifiConfigurationIsWifiExist.networkId);
        }
        if (wifiConfigurationIsWifiExist == null) {
            wifiConfigurationIsWifiExist = new WifiConfiguration();
        }
        wifiConfigurationIsWifiExist.allowedAuthAlgorithms.clear();
        wifiConfigurationIsWifiExist.allowedGroupCiphers.clear();
        wifiConfigurationIsWifiExist.allowedKeyManagement.clear();
        wifiConfigurationIsWifiExist.allowedPairwiseCiphers.clear();
        wifiConfigurationIsWifiExist.allowedProtocols.clear();
        wifiConfigurationIsWifiExist.SSID = str;
        if (i == NO_PASSWORD_WIFI) {
            wifiConfigurationIsWifiExist.allowedKeyManagement.set(0);
        } else if (i == WEP_CIPHER_WIFI) {
            wifiConfigurationIsWifiExist.wepKeys[0] = str2;
            wifiConfigurationIsWifiExist.allowedAuthAlgorithms.set(1);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(3);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(2);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(0);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(1);
            wifiConfigurationIsWifiExist.allowedKeyManagement.set(0);
            wifiConfigurationIsWifiExist.wepTxKeyIndex = 0;
        } else if (i == WPA_CIPHER_WIFI) {
            wifiConfigurationIsWifiExist.preSharedKey = str2;
            wifiConfigurationIsWifiExist.hiddenSSID = false;
            wifiConfigurationIsWifiExist.allowedAuthAlgorithms.set(0);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(2);
            wifiConfigurationIsWifiExist.allowedKeyManagement.set(1);
            wifiConfigurationIsWifiExist.allowedPairwiseCiphers.set(1);
            wifiConfigurationIsWifiExist.allowedGroupCiphers.set(3);
            wifiConfigurationIsWifiExist.allowedPairwiseCiphers.set(2);
            wifiConfigurationIsWifiExist.status = 2;
        }
        try {
            setStaticIp(wifiConfigurationIsWifiExist);
        } catch (Exception e) {
            ALog.d(TAG, "createWifiConfiguration(), setStaticIP error, e=" + e);
        }
        return wifiConfigurationIsWifiExist;
    }

    private void setStaticIp(WifiConfiguration wifiConfiguration) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalArgumentException, UnknownHostException, InvocationTargetException {
        setEnumField(wifiConfiguration, "STATIC", "ipAssignment");
        Object field = getField(wifiConfiguration, "linkProperties");
        if (field == null) {
            return;
        }
        Object objNewInstance = Class.forName("android.net.LinkAddress").getConstructor(InetAddress.class, Integer.TYPE).newInstance(InetAddress.getByName("172.31.254.153"), 24);
        ArrayList arrayList = (ArrayList) getDeclaredField(field, "mLinkAddresses");
        arrayList.clear();
        arrayList.add(objNewInstance);
        if (Build.VERSION.SDK_INT >= 14) {
            Object objNewInstance2 = Class.forName("android.net.RouteInfo").getConstructor(InetAddress.class).newInstance(InetAddress.getByName("172.31.254.250"));
            ArrayList arrayList2 = (ArrayList) getDeclaredField(field, "mRoutes");
            arrayList2.clear();
            arrayList2.add(objNewInstance2);
        } else {
            ArrayList arrayList3 = (ArrayList) getDeclaredField(field, "mGateways");
            arrayList3.clear();
            arrayList3.add(InetAddress.getByName("172.31.254.250"));
        }
        ArrayList arrayList4 = (ArrayList) getDeclaredField(field, "mDnses");
        arrayList4.clear();
        arrayList4.add(InetAddress.getByName("192.192.192.192"));
    }

    private Object getField(Object obj, String str) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        return obj.getClass().getField(str).get(obj);
    }

    private static Object getDeclaredField(Object obj, String str) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    private static void setEnumField(Object obj, String str, String str2) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field field = obj.getClass().getField(str2);
        field.set(obj, Enum.valueOf(field.getType(), str));
    }

    public void addWifi(WifiConfiguration wifiConfiguration) {
        ALog.d(TAG, "addWifi()");
        if (wifiConfiguration == null) {
            ALog.d(TAG, "addwifi(),config is null");
            return;
        }
        int iAddNetwork = wifiConfiguration.networkId;
        if (iAddNetwork == -1) {
            ALog.d(TAG, "addWifi(), addNetwork..");
            iAddNetwork = this.wifiManager.addNetwork(wifiConfiguration);
        }
        ALog.d(TAG, "addWifi(),netId = " + iAddNetwork);
        if (iAddNetwork == -1) {
            return;
        }
        try {
            disconnectAllConfiguredWifi();
            ALog.d(TAG, "addWifi(),enable = " + this.wifiManager.enableNetwork(iAddNetwork, true) + " reconnect = " + this.wifiManager.reconnect());
        } catch (Exception e) {
            ALog.e(TAG, "addWifi(),error,", e);
        }
    }

    public void removeWifi(int i) {
        this.wifiManager.removeNetwork(i);
    }

    private void disconnectAllConfiguredWifi() {
        try {
            ALog.w(TAG, "disconnectAllConfiguredWifi");
            for (int i = 0; i < this.wifiConfigedList.size(); i++) {
                if (this.wifiConfigedList.get(i) != null) {
                    disconnectWifi(this.wifiConfigedList.get(i).networkId);
                }
            }
        } catch (Exception e) {
            ALog.w(TAG, "disconnectAllConfiguredWifi e=" + e);
        }
    }

    public void disconnectWifi(int i) {
        this.wifiManager.disableNetwork(i);
        this.wifiManager.disconnect();
    }

    public NetworkInfo getCurrentNetInfo() {
        ALog.d(TAG, "getCurrentNetInfo(),call");
        if (this.connectivityManager == null) {
            return null;
        }
        ALog.d(TAG, "getCurrentNetInfo(),connectivityManager != null");
        return this.connectivityManager.getNetworkInfo(1);
    }

    public Boolean isAPNetworkReady(String str) {
        ALog.d(TAG, "isAPNetworkReady(),ssid" + str);
        updateWifi();
        NetworkInfo currentNetInfo = getCurrentNetInfo();
        if (currentNetInfo == null) {
            ALog.d(TAG, "isAPNetworkReady(),false,info is empty");
            return false;
        }
        if (currentNetInfo.getType() != 1) {
            ALog.d(TAG, "isAPNetworkReady: false,is not wifi");
            return false;
        }
        ALog.d(TAG, "isAPNetworkReady: State = " + currentNetInfo.getState() + ", detailState=" + currentNetInfo.getDetailedState().toString());
        if (!currentNetInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
            ALog.d(TAG, "isAPNetworkReady(),false,state=" + currentNetInfo.getState());
            return false;
        }
        if (!this.currWifiInfo.getSSID().replace("\"", "").equals(str)) {
            ALog.d(TAG, "isAPNetworkReady(),false,cur ssid=" + this.currWifiInfo.getSSID());
            return false;
        }
        return isWifiAvaiable();
    }

    public Boolean isCurrWifiOk() {
        updateWifi();
        NetworkInfo currentNetInfo = getCurrentNetInfo();
        if (currentNetInfo != null && currentNetInfo.getType() == 1 && currentNetInfo.getState().equals(NetworkInfo.State.CONNECTED) && isWifiAvaiable().booleanValue()) {
            return true;
        }
        return false;
    }

    public WifiConfiguration isOpenWifiExist(String str) {
        ALog.d(TAG, "isOpenWifiExist,ssid=" + str);
        updateConfigedWifi();
        for (int i = 0; i < this.wifiConfigedList.size(); i++) {
            WifiConfiguration wifiConfiguration = this.wifiConfigedList.get(i);
            if (wifiConfiguration != null && !TextUtils.isEmpty(wifiConfiguration.SSID)) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"") && wifiConfiguration.allowedKeyManagement.get(0)) {
                    ALog.d(TAG, "isOpenWifiExist(),found config");
                    return this.wifiConfigedList.get(i);
                }
            }
        }
        return null;
    }

    public void logWifiConfig() {
        ALog.d(TAG, "logWifiConfig()");
        updateConfigedWifi();
        for (int i = 0; i < this.wifiConfigedList.size(); i++) {
            WifiConfiguration wifiConfiguration = this.wifiConfigedList.get(i);
            ALog.d(TAG, "logWifiConfig(),networkId=" + wifiConfiguration.networkId + ",ssid=" + wifiConfiguration.SSID + ",config=" + wifiConfiguration.toString());
        }
    }

    public WifiConfiguration isWifiExist(String str) {
        ALog.d(TAG, "isWifiExist");
        updateConfigedWifi();
        for (int i = 0; i < this.wifiConfigedList.size(); i++) {
            WifiConfiguration wifiConfiguration = this.wifiConfigedList.get(i);
            if (wifiConfiguration != null && !TextUtils.isEmpty(wifiConfiguration.SSID)) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                    ALog.d(TAG, "isWifiExist(),found config");
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }

    public List<ScanResult> startScanWifi() {
        ALog.d(TAG, "startScanWifi()");
        ALog.d(TAG, "startScanWifi()," + this.wifiManager.startScan());
        this.scanResultList = this.wifiManager.getScanResults();
        return this.scanResultList;
    }

    public List<WifiConfiguration> getWifiConfiged() {
        this.wifiConfigedList = this.wifiManager.getConfiguredNetworks();
        return this.wifiConfigedList;
    }

    public WifiConfiguration getCurWifiConfig(String str) {
        ALog.d(TAG, "getCurWifiConfig(),ssid=" + str);
        updateConfigedWifi();
        for (int i = 0; i < this.wifiConfigedList.size(); i++) {
            WifiConfiguration wifiConfiguration = this.wifiConfigedList.get(i);
            if (wifiConfiguration != null && !TextUtils.isEmpty(wifiConfiguration.SSID)) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"") && wifiConfiguration.status == 0) {
                    ALog.d(TAG, "getCurWifiConfig(),succ。  networkId=" + wifiConfiguration.networkId + ",ssid=" + wifiConfiguration.SSID + ",config=" + wifiConfiguration.toString());
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }

    public Boolean isWifiAvaiable() {
        ALog.d(TAG, "isWifiAvaiable");
        WifiManager wifiManager = this.wifiManager;
        if (wifiManager == null) {
            ALog.d(TAG, "isWifiAvaiable,wifiManager is null");
            return false;
        }
        boolean zIsWifiEnabled = wifiManager.isWifiEnabled();
        ALog.d(TAG, "isWifiAvaiable,enable = " + zIsWifiEnabled);
        return Boolean.valueOf(zIsWifiEnabled);
    }

    public void acquireWifiLock() {
        this.wifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.wifiLock.isHeld()) {
            this.wifiLock.acquire();
        }
    }

    public void acquireMulticastLock() {
        try {
            this.multicastLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseMulticastLock() {
        try {
            if (this.multicastLock.isHeld()) {
                this.multicastLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfiguration, boolean z) {
        if (this.wifiManager == null) {
            ALog.d(TAG, "setWifiApEnabled(), wifiManager is null");
            return false;
        }
        if (isHTC()) {
            ALog.d(TAG, "setWifiApEnabled(), isSucc = " + setWifiApConfigurationForHTC(wifiConfiguration));
        }
        if (z) {
            try {
                this.wifiManager.setWifiEnabled(false);
            } catch (Exception e) {
                ALog.d(TAG, " setWifiApEnabled(), e = " + e);
                e.printStackTrace();
                return false;
            }
        }
        return ((Boolean) this.wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE).invoke(this.wifiManager, wifiConfiguration, Boolean.valueOf(z))).booleanValue();
    }

    public int getWifiApState() {
        try {
            return ((Integer) this.wifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.wifiManager, new Object[0])).intValue();
        } catch (Exception e) {
            ALog.d(TAG, "getWifiApState(), error = " + e.toString());
            return 14;
        }
    }

    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    public void closeWiFiAP() {
        if (this.wifiManager == null) {
            ALog.d(TAG, "closeWiFiAP(), wifi manager == null");
            return;
        }
        if (isWifiApEnabled()) {
            try {
                Method method = this.wifiManager.getClass().getMethod("getWifiApConfiguration", new Class[0]);
                method.setAccessible(true);
                this.wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE).invoke(this.wifiManager, (WifiConfiguration) method.invoke(this.wifiManager, new Object[0]), false);
                this.wifiManager.setWifiEnabled(true);
            } catch (Exception e) {
                ALog.d(TAG, "closeWiFiAP(), error,e= " + e.toString());
            }
        }
    }

    public boolean isHTC() {
        try {
            String str = Build.MANUFACTURER;
            ALog.d(TAG, "isHTC(), manu=" + str);
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return str.trim().toLowerCase().contains("htc");
        } catch (Exception e) {
            ALog.d(TAG, "isHTC(),error+" + e);
            return false;
        }
    }

    public boolean setWifiApConfigurationForHTC(WifiConfiguration wifiConfiguration) {
        ALog.d(TAG, "setWifiApConfigurationForHTC, call, apConfig = " + wifiConfiguration.toString());
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
            ALog.d(TAG, "setWifiApConfigurationForHTC,error, e = " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static InetAddress getIpAddress(NetworkType networkType) {
        ALog.d(TAG, "getIpAddress()");
        InetAddress inetAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces != null) {
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
            }
        } catch (Exception e) {
            ALog.d(TAG, e.toString());
        }
        return inetAddress;
    }

    public static InetAddress getBroadcast(InetAddress inetAddress) {
        List<InterfaceAddress> interfaceAddresses;
        if (inetAddress == null) {
            return null;
        }
        ALog.d(TAG, "getBroadcast(),inetAddr = " + inetAddress);
        try {
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(inetAddress);
            if (byInetAddress != null && (interfaceAddresses = byInetAddress.getInterfaceAddresses()) != null && !interfaceAddresses.isEmpty()) {
                InetAddress broadcast = null;
                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    if (interfaceAddress.getAddress() instanceof Inet4Address) {
                        broadcast = interfaceAddress.getBroadcast();
                    }
                }
                ALog.d(TAG, "iAddr=" + broadcast);
                return broadcast;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ALog.d(TAG, "getBroadcast" + e.getMessage());
            return null;
        }
    }

    public String getWifiType() {
        int frequency;
        if (Build.VERSION.SDK_INT < 21) {
            String ssid = this.currWifiInfo.getSSID();
            if (ssid == null || ssid.length() <= 2) {
                frequency = 0;
            } else {
                String strSubstring = ssid.substring(1, ssid.length() - 1);
                for (ScanResult scanResult : this.wifiManager.getScanResults()) {
                    if (scanResult.SSID.equals(strSubstring)) {
                        frequency = scanResult.frequency;
                        break;
                    }
                }
                frequency = 0;
            }
        } else {
            frequency = this.currWifiInfo.getFrequency();
        }
        if (is5GHz(frequency)) {
            return WiFiFreqType.WIFI_5G.value();
        }
        return WiFiFreqType.WIFI_2_4G.value();
    }

    public int getWifiRssid() {
        return this.currWifiInfo.getRssi();
    }

    public enum WiFiFreqType {
        WIFI_5G("5GHZ"),
        WIFI_2_4G("2.4GHZ");

        private String name;

        WiFiFreqType(String str) {
            this.name = str;
        }

        public String value() {
            return this.name;
        }
    }
}
