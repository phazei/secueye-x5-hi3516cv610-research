package com.aliyun.alink.linksdk.tmp.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.resource.ResDescpt;
import com.aliyun.alink.linksdk.tmp.resource.e;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes2.dex */
public class TextHelper {
    static final String RANDOM_BYTE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    protected static final String TAG = "[Tmp]TextHelper";
    protected static Random sRandom = new Random();

    public static String combineStr(String str, String str2) {
        return str + str2;
    }

    public static String combineStr(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public static String queryProduct(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments == null || pathSegments.size() <= 1) {
            return null;
        }
        return pathSegments.get(1);
    }

    public static String queryDeviceName(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments == null || pathSegments.size() <= 2) {
            return null;
        }
        return pathSegments.get(2);
    }

    public static String queryEventIdentifier(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.equalsIgnoreCase(TmpConstant.METHOD_PROPERTY_POST)) {
            return "post";
        }
        String[] strArrSplit = str.split("\\.");
        if (strArrSplit == null || strArrSplit.length <= 1) {
            return null;
        }
        if ("post".equalsIgnoreCase(strArrSplit[strArrSplit.length - 1])) {
            return strArrSplit[strArrSplit.length - 2];
        }
        return strArrSplit[strArrSplit.length - 1];
    }

    public static String getRandomString() {
        return getRandomString(16);
    }

    public static String getRandomString(int i) {
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(RANDOM_BYTE.charAt(sRandom.nextInt(62)));
        }
        return sb.toString();
    }

    public static int getRandomInt() {
        return sRandom.nextInt();
    }

    public static String formatDownRawTopic(String str, String str2) {
        return "/sys/" + str + "/" + str2 + TmpConstant.URI_THING + TmpConstant.URI_MODEL + "/down_raw";
    }

    public static String formatPostReplyTopic(String str, String str2) {
        return "/sys/" + str + "/" + str2 + "/" + TmpConstant.EVENT_PROPERTY_URI_PRE + "/" + TmpConstant.EVENT_PROPERTY_URI_POST_REPLY;
    }

    public static String formatDownRawId(String str, String str2) {
        return str + str2 + "/" + TmpConstant.IDENTIFIER_RAW_DATA_DOWN;
    }

    public static String formatTopic(String str, String str2, String str3) {
        return "/dev/" + str + "/" + str2 + str3;
    }

    public static String formatReplaceTopic(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.replace(TmpConstant.URI_PRODUCT_PRODUCT_REPLACE, str2).replace(TmpConstant.URI_PRODUCT_DEVICE_REPLACE, str3);
    }

    public static String getTopicStr(DeviceModel deviceModel, String str) {
        ResDescpt.ResElementType resElementTypeA = e.a(deviceModel, str);
        ALog.d(TAG, "getTopicStr identifier:" + str + " type:" + resElementTypeA + " deviceModel:" + deviceModel);
        if (TextUtils.isEmpty(str) || resElementTypeA == null) {
            ALog.e(TAG, "getTopicStr null");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (resElementTypeA) {
            case PROPERTY:
                if (deviceModel != null) {
                    sb = new StringBuilder("/");
                    sb.append("sys");
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getProdKey());
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getName());
                    sb.append("/");
                    sb.append(TmpConstant.PROPERTY_URI_PRE);
                    sb.append("/");
                    sb.append(str);
                }
                break;
            case SERVICE:
                if (deviceModel != null) {
                    sb = new StringBuilder("/");
                    sb.append("sys");
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getProdKey());
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getName());
                    sb.append("/");
                    sb.append(TmpConstant.METHOD_URI_PRE);
                    sb.append("/");
                    sb.append(str);
                }
                break;
            case EVENT:
                if (deviceModel != null) {
                    sb = new StringBuilder("/");
                    sb.append("sys");
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getProdKey());
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getName());
                    if (str.equalsIgnoreCase("post")) {
                        sb.append("/");
                        sb.append(TmpConstant.EVENT_PROPERTY_URI_PRE);
                        sb.append("/");
                        sb.append(str);
                    } else {
                        sb.append("/");
                        sb.append(TmpConstant.EVENT_URI_PRE);
                        sb.append("/");
                        sb.append(str);
                        sb.append("/");
                        sb.append("post");
                    }
                }
                break;
            case DISCOVERY:
                sb = new StringBuilder(TmpConstant.PATH_DISCOVERY);
                break;
            case ALCS:
                if (deviceModel != null) {
                    sb = new StringBuilder("/");
                    sb.append("dev");
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getProdKey());
                    sb.append("/");
                    sb.append(deviceModel.getProfile().getName());
                    sb.append("/");
                    sb.append(TmpConstant.URI_PREFIX_ALCS_SERVICE);
                    sb.append("/");
                    sb.append(str);
                }
                break;
        }
        return sb.toString();
    }

    public static String byte2hex(byte[] bArr, int i) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (bArr.length < i) {
            i = bArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(String.format("%02X", Integer.valueOf(bArr[i2] & 255)));
        }
        return sb.toString();
    }

    public static String byte2hex(byte[] bArr) {
        return bArr == null ? "" : byte2hex(bArr, bArr.length);
    }

    public static String formatResponseData(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            try {
                return new String((byte[]) obj, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return String.valueOf(obj);
    }

    public static String foramtMethod(String str) {
        return TmpConstant.METHOD_SERVICE_PRE + str;
    }
}
