package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.breeze.BreezeDeviceDescriptor;
import com.aliyun.iot.breeze.BreezeScanRecord;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkEnvironmentUtils {
    public static Context a(Context context) {
        if (context == null) {
            context = AppUtils.getContext();
        }
        if (context == null) {
            return null;
        }
        return context.getApplicationContext();
    }

    public static HashMap<String, String> getBleInfo(Context context, IBleInterface.IBleChannelDevice iBleChannelDevice) {
        if (iBleChannelDevice == null || !(iBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        try {
            BreezeDeviceDescriptor descriptor = ((MixBleDevice) iBleChannelDevice.getChannelDevice()).getDescriptor();
            if (descriptor == null) {
                return map;
            }
            map.put("rssi", String.valueOf(descriptor.getRssi()));
            BreezeScanRecord breezeScanRecord = descriptor.getBreezeScanRecord();
            if (breezeScanRecord != null) {
                map.put(AlinkConstants.KEY_MAC, breezeScanRecord.getMac());
                map.put("bleV", BreezeScanRecord.bleVersion2Str(breezeScanRecord.bleVersion()));
                map.put("st", String.valueOf(breezeScanRecord.getSubType()));
                map.put("pv", String.valueOf(breezeScanRecord.getProtocolVersion()));
                map.put("mi", breezeScanRecord.getModelIdHexStr());
                map.put("fm", String.valueOf(breezeScanRecord.getFmsk()));
            }
        } catch (Exception unused) {
        }
        return map;
    }

    public static HashMap<String, String> getPhoneWiFiInfo(Context context) {
        WifiManager wifiManager;
        WifiInfo connectionInfo;
        Context contextA = a(context);
        if (contextA == null || (wifiManager = (WifiManager) contextA.getSystemService("wifi")) == null || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        try {
            String ssid = connectionInfo.getSSID();
            if (!TextUtils.isEmpty(ssid)) {
                if (!ssid.startsWith("\"") || ssid.length() <= 2) {
                    map.put("ssid", connectionInfo.getSSID());
                } else {
                    map.put("ssid", ssid.substring(1, ssid.length() - 1));
                }
            }
            map.put("bssid", connectionInfo.getBSSID());
            map.put("rssi", String.valueOf(connectionInfo.getRssi()));
            if (Build.VERSION.SDK_INT >= 21) {
                map.put("freq", String.valueOf(connectionInfo.getFrequency()));
                map.put("channel", getWiFiChannel(connectionInfo.getFrequency()));
            }
            map.put("speed", String.valueOf(connectionInfo.getLinkSpeed()));
            map.put(UtilityImpl.NET_TYPE_MOBILE, String.valueOf(NetworkTypeUtils.isMobileNetwork(context)));
            map.put("wifi", String.valueOf(NetworkTypeUtils.isWiFi(context)));
            HashMap<String, String> wiFiConfiguration = getWiFiConfiguration(context, ssid);
            if (wiFiConfiguration != null && !wiFiConfiguration.isEmpty()) {
                map.putAll(wiFiConfiguration);
            }
        } catch (Exception unused) {
        }
        return map;
    }

    public static String getWiFiChannel(int i) {
        int i2;
        switch (i) {
            case 2412:
                i2 = 1;
                break;
            case 2417:
                i2 = 2;
                break;
            case 2422:
                i2 = 3;
                break;
            case 2427:
                i2 = 4;
                break;
            case 2432:
                i2 = 5;
                break;
            case 2437:
                i2 = 6;
                break;
            case 2442:
                i2 = 7;
                break;
            case 2447:
                i2 = 8;
                break;
            case 2452:
                i2 = 9;
                break;
            case 2457:
                i2 = 10;
                break;
            case 2462:
                i2 = 11;
                break;
            case 2467:
                i2 = 12;
                break;
            case 2472:
                i2 = 13;
                break;
            case 2484:
                i2 = 14;
                break;
            case 5745:
                i2 = 149;
                break;
            case 5765:
                i2 = 153;
                break;
            case 5785:
                i2 = 157;
                break;
            case 5805:
                i2 = 161;
                break;
            case 5825:
                i2 = 165;
                break;
            default:
                i2 = -1;
                break;
        }
        return String.valueOf(i2);
    }

    public static HashMap<String, String> getWiFiConfiguration(Context context, String str) {
        WifiManager wifiManager;
        List<WifiConfiguration> configuredNetworks;
        Context contextA = a(context);
        if (contextA == null || TextUtils.isEmpty(str) || (wifiManager = (WifiManager) contextA.getSystemService("wifi")) == null || (configuredNetworks = wifiManager.getConfiguredNetworks()) == null || configuredNetworks.isEmpty()) {
            return null;
        }
        int size = configuredNetworks.size();
        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            WifiConfiguration wifiConfiguration = configuredNetworks.get(i);
            if (wifiConfiguration != null && str.equals(wifiConfiguration.SSID)) {
                map.put("encType", String.valueOf(wifiConfiguration.allowedKeyManagement));
                break;
            }
            i++;
        }
        return map;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01c6  */
    /*  JADX ERROR: JadxRuntimeException in pass: SimplifyVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r10v11 int, still in use, count: 1, list:
          (r10v11 int) from 0x0137: INVOKE (r9v2 java.lang.String) = (r9v1 java.lang.String), (r11v4 int), (r10v11 int) VIRTUAL call: java.lang.String.substring(int, int):java.lang.String A[Catch: Throwable -> 0x017d, IOException -> 0x0180, MD:(int, int):java.lang.String (c)] (LINE:23)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:91)
        	at jadx.core.utils.InsnRemover.unbindArgUsage(InsnRemover.java:174)
        	at jadx.core.utils.InsnRemover.unbindAllArgs(InsnRemover.java:106)
        	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:90)
        	at jadx.core.utils.InsnRemover.unbindArgUsage(InsnRemover.java:174)
        	at jadx.core.dex.instructions.args.InsnArg.wrapInstruction(InsnArg.java:141)
        	at jadx.core.dex.visitors.SimplifyVisitor.simplifyArgs(SimplifyVisitor.java:116)
        	at jadx.core.dex.visitors.SimplifyVisitor.simplifyInsn(SimplifyVisitor.java:132)
        	at jadx.core.dex.visitors.SimplifyVisitor.simplifyBlock(SimplifyVisitor.java:86)
        	at jadx.core.dex.visitors.SimplifyVisitor.visit(SimplifyVisitor.java:71)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.HashMap<java.lang.String, java.lang.String> ping(java.lang.String r13, boolean r14) {
        /*
            Method dump skipped, instruction units count: 500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.utils.NetworkEnvironmentUtils.ping(java.lang.String, boolean):java.util.HashMap");
    }

    public static boolean waitFor(Process process, long j, TimeUnit timeUnit) throws InterruptedException {
        long jNanoTime = System.nanoTime();
        long nanos = timeUnit.toNanos(j);
        do {
            try {
                process.exitValue();
                return true;
            } catch (IllegalThreadStateException unused) {
                if (nanos > 0) {
                    Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(nanos) + 1, 100L));
                }
                nanos = timeUnit.toNanos(j) - (System.nanoTime() - jNanoTime);
            }
        } while (nanos > 0);
        return false;
    }

    public static HashMap<String, String> ping() {
        if (DCEnvHelper.isTgEnv()) {
            return null;
        }
        String defaultHost = IoTAPIClientImpl.getInstance().getDefaultHost();
        if (TextUtils.isEmpty(defaultHost)) {
            return null;
        }
        if (defaultHost.startsWith("https://")) {
            defaultHost = defaultHost.replace("https://", "");
        }
        if (TextUtils.isEmpty(defaultHost)) {
            return null;
        }
        return ping(defaultHost, false);
    }
}
