package com.aliyun.alink.business.devicecenter.log;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.log.HLoggerFactory;
import com.aliyun.alink.linksdk.tools.log.ILogger;

/* JADX INFO: loaded from: classes.dex */
public class ALog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    public static ILogger logger = new HLoggerFactory().getInstance(AlinkConstants.AWSS_PRE_TAG);

    public static void d(String str, String str2) {
        logger.d(str, getFilterString(str2));
    }

    public static void e(String str, String str2) {
        logger.e(str, getFilterString(str2));
    }

    public static String getFilterString(String str) {
        return str;
    }

    public static void i(String str, String str2) {
        logger.i(str, getFilterString(str2));
    }

    public static void llog(byte b2, String str, String str2) {
        com.aliyun.alink.linksdk.tools.ALog.llog(b2, AlinkConstants.AWSS_PRE_TAG + str, getFilterString(str2));
    }

    public static void llogForExternal(byte b2, String str, byte[] bArr) {
        com.aliyun.alink.linksdk.tools.ALog.logBA(b2, str, bArr);
    }

    public static void setLevel(byte b2) {
        com.aliyun.alink.linksdk.tools.ALog.setLevel(b2);
    }

    public static void w(String str, String str2) {
        logger.w(str, getFilterString(str2));
    }

    public static void e(String str, String str2, Exception exc) {
        if (exc == null) {
            logger.e(str, getFilterString(str2) + " EXCEPTION: unknown");
            return;
        }
        logger.e(str, getFilterString(str2) + " EXCEPTION: " + exc.getMessage());
        exc.printStackTrace();
    }
}
