package com.aliyun.iot.aep.sdk.connectchannel.log;

import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public class ALog {
    public static final byte LEVEL_DEBUG = 1;
    public static final byte LEVEL_ERROR = 4;
    public static final byte LEVEL_INFO = 2;
    public static final byte LEVEL_WARNING = 3;
    private static byte level = 3;
    private static IALogCloud sALogCloud;

    public static void setLevel(byte b2) {
        level = b2;
    }

    public static byte getLevel() {
        return level;
    }

    public static void setALogCloud(IALogCloud iALogCloud) {
        sALogCloud = iALogCloud;
        sALogCloud.setLevel(level);
    }

    public static void configCloudLog(String str, String str2, String str3, String str4) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.configCloudLog(str, str2, str3, str4);
        }
    }

    public static void d(String str, String str2, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.d(str, str2, z);
        } else {
            log((byte) 1, str, str2, z);
        }
    }

    public static void i(String str, String str2, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.i(str, str2, z);
        } else {
            log((byte) 2, str, str2, z);
        }
    }

    public static void w(String str, String str2, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.w(str, str2, z);
        } else {
            log((byte) 3, str, str2, z);
        }
    }

    public static void e(String str, String str2, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2, z);
        } else {
            log((byte) 4, str, str2, z);
        }
    }

    public static void e(String str, String str2, String str3, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2, str3, z);
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb.append(str2);
        sb.append(" ERROR: ");
        sb.append(str3);
        log((byte) 4, str, sb.toString(), z);
    }

    public static void e(String str, String str2, Exception exc, boolean z) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2, exc, z);
            return;
        }
        if (exc != null) {
            StringBuilder sb = new StringBuilder();
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(" EXCEPTION: ");
            sb.append(exc.getMessage());
            log((byte) 4, str, sb.toString(), z);
            exc.printStackTrace();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        sb2.append(" EXCEPTION: unknown");
        log((byte) 4, str, sb2.toString(), z);
    }

    public static void d(String str, String str2) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.d(str, str2);
        } else {
            log((byte) 1, str, str2);
        }
    }

    public static void i(String str, String str2) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.i(str, str2);
        } else {
            log((byte) 2, str, str2);
        }
    }

    public static void w(String str, String str2) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.w(str, str2);
        } else {
            log((byte) 3, str, str2);
        }
    }

    public static void e(String str, String str2) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2);
        } else {
            log((byte) 4, str, str2);
        }
    }

    public static void e(String str, String str2, String str3) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2, str3);
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb.append(str2);
        sb.append(" ERROR: ");
        sb.append(str3);
        log((byte) 4, str, sb.toString());
    }

    public static void e(String str, String str2, Exception exc) {
        IALogCloud iALogCloud = sALogCloud;
        if (iALogCloud != null) {
            iALogCloud.e(str, str2, exc);
            return;
        }
        if (exc != null) {
            StringBuilder sb = new StringBuilder();
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(" EXCEPTION: ");
            sb.append(exc.getMessage());
            log((byte) 4, str, sb.toString());
            exc.printStackTrace();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        sb2.append(" EXCEPTION: unknown");
        log((byte) 4, str, sb2.toString());
    }

    private static void log(byte b2, String str, String str2, boolean z) {
        if (level > b2) {
        }
        switch (b2) {
            case 1:
                Log.d(str, str2);
                break;
            case 2:
                Log.i(str, str2);
                break;
            case 3:
                Log.w(str, str2);
                break;
            case 4:
                Log.e(str, str2);
                break;
        }
    }

    private static void log(byte b2, String str, String str2) {
        if (level > b2) {
        }
        switch (b2) {
            case 1:
                Log.d(str, str2);
                break;
            case 2:
                Log.i(str, str2);
                break;
            case 3:
                Log.w(str, str2);
                break;
            case 4:
                Log.e(str, str2);
                break;
        }
    }
}
