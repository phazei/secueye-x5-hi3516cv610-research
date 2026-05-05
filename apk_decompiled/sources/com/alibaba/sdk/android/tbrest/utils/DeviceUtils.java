package com.alibaba.sdk.android.tbrest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.ta.utdid2.device.UTDevice;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class DeviceUtils {
    private static final String NETWORK_CLASS_2_G = "2G";
    private static final String NETWORK_CLASS_3_G = "3G";
    private static final String NETWORK_CLASS_4_G = "4G";
    private static final String NETWORK_CLASS_5_G = "5G";
    private static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static String carrier;
    private static String cpuName;
    private static final String[] ARRAY_OF_STRING = {"Unknown", "Unknown"};
    private static String imsi = null;
    private static String imei = null;

    private static String getNetworkClass(int i) {
        if (i == 20) {
            return NETWORK_CLASS_5_G;
        }
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return NETWORK_CLASS_2_G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return NETWORK_CLASS_3_G;
            case 13:
                return NETWORK_CLASS_4_G;
            default:
                return "Unknown";
        }
    }

    public static String getCpuName() throws Throwable {
        FileReader fileReader;
        Throwable th;
        BufferedReader bufferedReader;
        String line;
        String str = cpuName;
        if (str != null) {
            return str;
        }
        try {
            try {
                fileReader = new FileReader("/proc/cpuinfo");
                try {
                    bufferedReader = new BufferedReader(fileReader);
                } catch (IOException unused) {
                    bufferedReader = null;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = null;
                }
            } catch (Exception unused2) {
            }
        } catch (IOException unused3) {
            bufferedReader = null;
            fileReader = null;
        } catch (Throwable th3) {
            fileReader = null;
            th = th3;
            bufferedReader = null;
        }
        do {
            try {
                line = bufferedReader.readLine();
                if (line == null) {
                    fileReader.close();
                }
            } catch (IOException unused4) {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                }
                return null;
            } catch (Throwable th4) {
                th = th4;
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception unused5) {
                        throw th;
                    }
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
            bufferedReader.close();
            return null;
        } while (!line.contains("Hardware"));
        cpuName = line.split(":")[1];
        String str2 = cpuName;
        try {
            fileReader.close();
            bufferedReader.close();
        } catch (Exception unused6) {
        }
        return str2;
    }

    public static String getCarrier(Context context) {
        try {
            if (carrier != null) {
                return carrier;
            }
            carrier = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
            return carrier;
        } catch (Exception unused) {
            return null;
        }
    }

    @SuppressLint({"WrongConstant"})
    public static String[] getNetworkType(Context context) {
        if (context == null) {
            return ARRAY_OF_STRING;
        }
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            return ARRAY_OF_STRING;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return ARRAY_OF_STRING;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return ARRAY_OF_STRING;
        }
        if (activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == 1) {
                ARRAY_OF_STRING[0] = NETWORK_CLASS_WIFI;
                return ARRAY_OF_STRING;
            }
            if (activeNetworkInfo.getType() == 0) {
                if (isNRConnected((TelephonyManager) context.getSystemService("phone"))) {
                    ARRAY_OF_STRING[0] = NETWORK_CLASS_5_G;
                } else {
                    ARRAY_OF_STRING[0] = getNetworkClass(activeNetworkInfo.getSubtype());
                }
                ARRAY_OF_STRING[1] = activeNetworkInfo.getSubtypeName();
                return ARRAY_OF_STRING;
            }
        }
        return ARRAY_OF_STRING;
    }

    private static boolean isNRConnected(TelephonyManager telephonyManager) {
        int i;
        try {
            Object objInvoke = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getServiceState", new Class[0]).invoke(telephonyManager, new Object[0]);
            for (Method method : Class.forName(objInvoke.getClass().getName()).getDeclaredMethods()) {
                i = (method.getName().equals("getNrStatus") || method.getName().equals("getNrState")) ? 0 : i + 1;
                method.setAccessible(true);
                return ((Integer) method.invoke(objInvoke, new Object[0])).intValue() == 3;
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static String getLanguage() {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Exception e) {
            LogUtil.e("get country error ", e);
            return null;
        }
    }

    public static String getCountry() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Exception e) {
            LogUtil.e("get country error ", e);
            return null;
        }
    }

    public static String getResolution(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            if (i > i2) {
                int i3 = i ^ i2;
                i2 ^= i3;
                i = i3 ^ i2;
            }
            return i2 + WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD + i;
        } catch (Exception e) {
            LogUtil.e("DeviceUtils getResolution: error", e);
            return "Unknown";
        }
    }

    public static String getUtdid(Context context) {
        try {
            return UTDevice.getUtdid(context);
        } catch (Exception e) {
            LogUtil.e("get utdid error ", e);
            return null;
        }
    }

    public static String getImsi(Context context) {
        String str = imsi;
        if (str != null) {
            return str;
        }
        StringUtils.isEmpty(str);
        imsi = getUniqueID();
        return imsi;
    }

    public static String getImei(Context context) {
        String str = imei;
        if (str != null) {
            return str;
        }
        imei = getUniqueID();
        return imei;
    }

    public static byte[] IntGetBytes(int i) {
        byte[] bArr = {(byte) ((i >> 8) % 256), (byte) (i % 256), (byte) (i % 256), (byte) (i % 256)};
        int i2 = i >> 8;
        int i3 = i2 >> 8;
        return bArr;
    }

    public static String getUniqueID() {
        try {
            int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            int iNanoTime = (int) System.nanoTime();
            int iNextInt = new Random().nextInt();
            int iNextInt2 = new Random().nextInt();
            byte[] bArrIntGetBytes = IntGetBytes(iCurrentTimeMillis);
            byte[] bArrIntGetBytes2 = IntGetBytes(iNanoTime);
            byte[] bArrIntGetBytes3 = IntGetBytes(iNextInt);
            byte[] bArrIntGetBytes4 = IntGetBytes(iNextInt2);
            byte[] bArr = new byte[16];
            System.arraycopy(bArrIntGetBytes, 0, bArr, 0, 4);
            System.arraycopy(bArrIntGetBytes2, 0, bArr, 4, 4);
            System.arraycopy(bArrIntGetBytes3, 0, bArr, 8, 4);
            System.arraycopy(bArrIntGetBytes4, 0, bArr, 12, 4);
            return Base64.encodeBase64String(bArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
