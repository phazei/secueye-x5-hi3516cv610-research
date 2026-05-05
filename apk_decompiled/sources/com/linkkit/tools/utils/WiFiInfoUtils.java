package com.linkkit.tools.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.linkkit.tools.a;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes3.dex */
public class WiFiInfoUtils {
    private static final String TAG = "WiFiInfoUtils";

    /* JADX WARN: Removed duplicated region for block: B:12:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getWifiSsid(android.content.Context r5) {
        /*
            android.net.wifi.WifiInfo r0 = getWifiInfo(r5)
            if (r0 != 0) goto L9
            java.lang.String r5 = ""
            return r5
        L9:
            java.lang.String r1 = ""
            java.lang.String r2 = new java.lang.String     // Catch: java.lang.Exception -> L39 java.io.UnsupportedEncodingException -> L3f
            java.lang.String r0 = r0.getSSID()     // Catch: java.lang.Exception -> L39 java.io.UnsupportedEncodingException -> L3f
            java.lang.String r3 = "\""
            java.lang.String r4 = ""
            java.lang.String r0 = r0.replace(r3, r4)     // Catch: java.lang.Exception -> L39 java.io.UnsupportedEncodingException -> L3f
            byte[] r0 = r0.getBytes()     // Catch: java.lang.Exception -> L39 java.io.UnsupportedEncodingException -> L3f
            java.lang.String r3 = "UTF-8"
            r2.<init>(r0, r3)     // Catch: java.lang.Exception -> L39 java.io.UnsupportedEncodingException -> L3f
            java.lang.String r0 = "<unknown ssid>"
            boolean r0 = r2.equals(r0)     // Catch: java.lang.Exception -> L35 java.io.UnsupportedEncodingException -> L37
            if (r0 != 0) goto L32
            java.lang.String r0 = "0x"
            boolean r0 = r2.equals(r0)     // Catch: java.lang.Exception -> L35 java.io.UnsupportedEncodingException -> L37
            if (r0 == 0) goto L44
        L32:
            java.lang.String r2 = ""
            goto L44
        L35:
            r0 = move-exception
            goto L3b
        L37:
            r0 = move-exception
            goto L41
        L39:
            r0 = move-exception
            r2 = r1
        L3b:
            r0.printStackTrace()
            goto L44
        L3f:
            r0 = move-exception
            r2 = r1
        L41:
            r0.printStackTrace()
        L44:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L85
            java.lang.String r0 = "WiFiInfoUtils"
            java.lang.String r1 = "getWifiSsid(),try CONNECTIVITY_SERVICE"
            com.linkkit.tools.a.a(r0, r1)
            java.lang.String r0 = "connectivity"
            java.lang.Object r5 = r5.getSystemService(r0)
            android.net.ConnectivityManager r5 = (android.net.ConnectivityManager) r5
            r0 = 0
            if (r5 == 0) goto L61
            r0 = 1
            android.net.NetworkInfo r0 = r5.getNetworkInfo(r0)
        L61:
            if (r0 == 0) goto L85
            java.lang.String r5 = r0.getExtraInfo()
            if (r5 == 0) goto L85
            java.lang.String r5 = new java.lang.String     // Catch: java.lang.Exception -> L81
            java.lang.String r0 = r0.getExtraInfo()     // Catch: java.lang.Exception -> L81
            java.lang.String r1 = "\""
            java.lang.String r3 = ""
            java.lang.String r0 = r0.replace(r1, r3)     // Catch: java.lang.Exception -> L81
            byte[] r0 = r0.getBytes()     // Catch: java.lang.Exception -> L81
            java.lang.String r1 = "UTF-8"
            r5.<init>(r0, r1)     // Catch: java.lang.Exception -> L81
            goto L86
        L81:
            r5 = move-exception
            r5.printStackTrace()
        L85:
            r5 = r2
        L86:
            java.lang.String r0 = "WiFiInfoUtils"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getWifiSsid(), result ssid = "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            com.linkkit.tools.a.a(r0, r1)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.linkkit.tools.utils.WiFiInfoUtils.getWifiSsid(android.content.Context):java.lang.String");
    }

    private static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        return wifiManager.getConnectionInfo();
    }

    public static String getWifiIP(Context context) {
        a.a(TAG, "getWifiIP context = " + context);
        if (context == null) {
            return null;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null || !wifiManager.isWifiEnabled()) {
                return null;
            }
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            return (ipAddress & 255) + "." + ((ipAddress >> 8) & 255) + "." + ((ipAddress >> 16) & 255) + "." + ((ipAddress >> 24) & 255);
        } catch (Exception e) {
            a.b(TAG, "getWifiIP exception=" + e);
            return null;
        }
    }

    public static String getGatewayIp(Context context) {
        DhcpInfo dhcpInfo = ((WifiManager) context.getSystemService("wifi")).getDhcpInfo();
        if (dhcpInfo == null) {
            a.b(TAG, "getGatewayIp failed, dhcp info is empty.");
            return null;
        }
        String strIntIp2String = StringUtils.intIp2String(dhcpInfo.gateway);
        a.a(TAG, "getGatewayIp ip = " + strIntIp2String);
        return strIntIp2String;
    }

    public static String getMac(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return getLocalMacAddressFromWifiInfo(context);
        }
        if (Build.VERSION.SDK_INT < 24 && Build.VERSION.SDK_INT >= 23) {
            return getMacAddress(context);
        }
        if (Build.VERSION.SDK_INT < 24) {
            return null;
        }
        if (!TextUtils.isEmpty(getMacAddress())) {
            return getMacAddress();
        }
        if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
            return getMachineHardwareAddress();
        }
        return getLocalMacAddressFromBusybox();
    }

    private static String getLocalMacAddressFromWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0039, code lost:
    
        r0 = r4.trim();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String getMacAddress(android.content.Context r4) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r0 >= r1) goto L11
            java.lang.String r4 = getMacAddress0(r4)
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L11
            return r4
        L11:
            java.lang.String r4 = ""
            java.lang.String r0 = ""
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch: java.lang.Exception -> L3b
            java.lang.String r2 = "cat /sys/class/net/wlan0/address"
            java.lang.Process r1 = r1.exec(r2)     // Catch: java.lang.Exception -> L3b
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Exception -> L3b
            java.io.InputStream r1 = r1.getInputStream()     // Catch: java.lang.Exception -> L3b
            r2.<init>(r1)     // Catch: java.lang.Exception -> L3b
            java.io.LineNumberReader r1 = new java.io.LineNumberReader     // Catch: java.lang.Exception -> L3b
            r1.<init>(r2)     // Catch: java.lang.Exception -> L3b
        L2d:
            if (r4 == 0) goto L56
            java.lang.String r4 = r1.readLine()     // Catch: java.lang.Exception -> L3b
            if (r4 == 0) goto L2d
            java.lang.String r4 = r4.trim()     // Catch: java.lang.Exception -> L3b
            r0 = r4
            goto L56
        L3b:
            r4 = move-exception
            java.lang.String r1 = "WiFiInfoUtils"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getMacAddress:"
            r2.append(r3)
            java.lang.String r4 = r4.toString()
            r2.append(r4)
            java.lang.String r4 = r2.toString()
            com.linkkit.tools.a.c(r1, r4)
        L56:
            if (r0 == 0) goto L60
            java.lang.String r4 = ""
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L90
        L60:
            java.lang.String r4 = "/sys/class/net/eth0/address"
            java.lang.String r4 = loadFileAsString(r4)     // Catch: java.lang.Exception -> L72
            java.lang.String r4 = r4.toUpperCase()     // Catch: java.lang.Exception -> L72
            r1 = 0
            r2 = 17
            java.lang.String r4 = r4.substring(r1, r2)     // Catch: java.lang.Exception -> L72
            return r4
        L72:
            r4 = move-exception
            r4.printStackTrace()
            java.lang.String r1 = "WiFiInfoUtils"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getMacAddress:"
            r2.append(r3)
            java.lang.String r4 = r4.toString()
            r2.append(r4)
            java.lang.String r4 = r2.toString()
            com.linkkit.tools.a.c(r1, r4)
        L90:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.linkkit.tools.utils.WiFiInfoUtils.getMacAddress(android.content.Context):java.lang.String");
    }

    private static String getMacAddress0(Context context) {
        if (!isAccessWifiStateAuthorized(context)) {
            return "";
        }
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            a.c(TAG, "getMacAddress0:" + e.toString());
            return "";
        }
    }

    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != 0) {
            return false;
        }
        a.a(TAG, "isAccessWifiStateAuthorized:access wifi state is enabled");
        return true;
    }

    private static String loadFileAsString(String str) throws Exception {
        FileReader fileReader = new FileReader(str);
        String strLoadReaderAsString = loadReaderAsString(fileReader);
        fileReader.close();
        return strLoadReaderAsString;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder sb = new StringBuilder();
        char[] cArr = new char[4096];
        int i = reader.read(cArr);
        while (i >= 0) {
            sb.append(cArr, 0, i);
            i = reader.read(cArr);
        }
        return sb.toString();
    }

    private static String getMacAddress() {
        try {
            byte[] hardwareAddress = NetworkInterface.getByInetAddress(getLocalInetAddress()).getHardwareAddress();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < hardwareAddress.length; i++) {
                if (i != 0) {
                    stringBuffer.append(':');
                }
                String hexString = Integer.toHexString(hardwareAddress[i] & 255);
                if (hexString.length() == 1) {
                    hexString = 0 + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception unused) {
            return null;
        }
    }

    private static InetAddress getLocalInetAddress() {
        InetAddress inetAddress;
        SocketException e;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            inetAddress = null;
            do {
                try {
                    if (!networkInterfaces.hasMoreElements()) {
                        break;
                    }
                    Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                    while (true) {
                        if (!inetAddresses.hasMoreElements()) {
                            break;
                        }
                        InetAddress inetAddressNextElement = inetAddresses.nextElement();
                        try {
                            if (!inetAddressNextElement.isLoopbackAddress() && inetAddressNextElement.getHostAddress().indexOf(":") == -1) {
                                inetAddress = inetAddressNextElement;
                                break;
                            }
                            inetAddress = null;
                        } catch (SocketException e2) {
                            e = e2;
                            inetAddress = inetAddressNextElement;
                            e.printStackTrace();
                        }
                    }
                } catch (SocketException e3) {
                    e = e3;
                }
            } while (inetAddress == null);
        } catch (SocketException e4) {
            inetAddress = null;
            e = e4;
        }
        return inetAddress;
    }

    private static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress()) {
                        return inetAddressNextElement.getHostAddress().toString();
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> networkInterfaces;
        String strBytesToString = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            networkInterfaces = null;
        }
        if (networkInterfaces == null) {
            return null;
        }
        while (networkInterfaces.hasMoreElements()) {
            try {
                strBytesToString = bytesToString(networkInterfaces.nextElement().getHardwareAddress());
            } catch (SocketException e2) {
                e2.printStackTrace();
            }
            if (strBytesToString != null) {
                break;
            }
        }
        return strBytesToString;
    }

    private static String bytesToString(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArr) {
            sb.append(String.format("%02X:", Byte.valueOf(b2)));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String getLocalMacAddressFromBusybox() {
        String strCallCmd = callCmd("busybox ifconfig", "HWaddr");
        return strCallCmd == null ? "网络异常" : (strCallCmd.length() <= 0 || !strCallCmd.contains("HWaddr")) ? strCallCmd : strCallCmd.substring(strCallCmd.indexOf("HWaddr") + 6, strCallCmd.length() - 1);
    }

    private static String callCmd(String str, String str2) {
        String str3 = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(str).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null || line.contains(str2)) {
                    return line;
                }
                str3 = str3 + line;
            }
        } catch (Exception e) {
            String str4 = str3;
            e.printStackTrace();
            return str4;
        }
    }
}
