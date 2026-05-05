package com.aliyun.alink.business.devicecenter.base;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigParams;
import com.aliyun.alink.business.devicecenter.config.IDataCallback;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ReflectUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.linksdk.logextra.upload.Log2Cloud;
import com.aliyun.alink.linksdk.logextra.upload.OSSManager;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AlinkHelper {
    public static WifiInfo a(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        return wifiManager.getConnectionInfo();
    }

    public static String getHalfMacFromMac(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (!str.contains(":")) {
            return null;
        }
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length != 6) {
            return null;
        }
        return (strArrSplit[3] + strArrSplit[4] + strArrSplit[5]).toUpperCase();
    }

    public static String getMacFromAp(String str) {
        if (!isValidSoftAp(str, true)) {
            return null;
        }
        String[] strArrSplit = str.split(OpenAccountUIConstants.UNDER_LINE);
        if (strArrSplit.length == 3) {
            return strArrSplit[2];
        }
        if (strArrSplit.length == 2 && str.startsWith(AlinkConstants.MOCK_AP_SSID_PREFIX) && strArrSplit[1].length() >= 4) {
            return a(strArrSplit[1].substring(0, 4));
        }
        return null;
    }

    public static String getMacFromSimpleMac(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int length = str.length();
        if (length != 12 || str.contains(":")) {
            ALog.w("AlinkHelper", "invalid simple mac");
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i += 2) {
            stringBuffer.append(str.charAt(i));
            int i2 = i + 1;
            stringBuffer.append(str.charAt(i2));
            if (i2 != length - 1) {
                stringBuffer.append(":");
            }
        }
        return stringBuffer.toString();
    }

    public static String getPkFromAp(String str) {
        if (!isValidSoftAp(str, true)) {
            return null;
        }
        String[] strArrSplit = str.split(OpenAccountUIConstants.UNDER_LINE);
        if (strArrSplit.length == 3) {
            return strArrSplit[1];
        }
        if (strArrSplit.length == 2 && str.startsWith(AlinkConstants.MOCK_AP_SSID_PREFIX)) {
            return strArrSplit[0].substring(3);
        }
        return null;
    }

    public static String getUppercaseMacFromSimpleMac(String str) {
        String macFromSimpleMac = getMacFromSimpleMac(str);
        if (TextUtils.isEmpty(macFromSimpleMac)) {
            return null;
        }
        return macFromSimpleMac.toUpperCase();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getWifiSsid(android.content.Context r5) {
        /*
            android.net.wifi.WifiInfo r0 = a(r5)
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
            java.lang.String r0 = "AlinkHelper"
            java.lang.String r1 = "getWifiSsid(),try CONNECTIVITY_SERVICE"
            com.aliyun.alink.business.devicecenter.log.ALog.d(r0, r1)
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
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "getWifiSsid(), result ssid = "
            r0.append(r1)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "AlinkHelper"
            com.aliyun.alink.business.devicecenter.log.ALog.d(r1, r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.base.AlinkHelper.getWifiSsid(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isBatch(com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams r3) {
        /*
            r0 = 0
            if (r3 == 0) goto L45
            com.aliyun.alink.business.devicecenter.api.add.LinkType r1 = r3.linkType     // Catch: java.lang.Exception -> L2d
            if (r1 == 0) goto L45
            com.aliyun.alink.business.devicecenter.api.add.LinkType r1 = com.aliyun.alink.business.devicecenter.api.add.LinkType.ALI_BROADCAST_IN_BATCHES     // Catch: java.lang.Exception -> L2d
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> L2d
            com.aliyun.alink.business.devicecenter.api.add.LinkType r2 = r3.linkType     // Catch: java.lang.Exception -> L2d
            java.lang.String r2 = r2.getName()     // Catch: java.lang.Exception -> L2d
            boolean r1 = r1.equals(r2)     // Catch: java.lang.Exception -> L2d
            if (r1 != 0) goto L2b
            com.aliyun.alink.business.devicecenter.api.add.LinkType r1 = com.aliyun.alink.business.devicecenter.api.add.LinkType.ALI_ZERO_IN_BATCHES     // Catch: java.lang.Exception -> L2d
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> L2d
            com.aliyun.alink.business.devicecenter.api.add.LinkType r3 = r3.linkType     // Catch: java.lang.Exception -> L2d
            java.lang.String r3 = r3.getName()     // Catch: java.lang.Exception -> L2d
            boolean r3 = r1.equals(r3)     // Catch: java.lang.Exception -> L2d
            if (r3 == 0) goto L45
        L2b:
            r0 = 1
            goto L45
        L2d:
            r3 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "unfkg believable e="
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            java.lang.String r1 = "AlinkHelper"
            com.aliyun.alink.business.devicecenter.log.ALog.d(r1, r3)
        L45:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r1 = "isBatch="
            r3.append(r1)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            java.lang.String r1 = "AlinkHelper"
            com.aliyun.alink.business.devicecenter.log.ALog.d(r1, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.base.AlinkHelper.isBatch(com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams):boolean");
    }

    public static boolean isBatchBroadcast(DCAlibabaConfigParams dCAlibabaConfigParams) {
        boolean z = false;
        if (dCAlibabaConfigParams != null) {
            try {
                if (dCAlibabaConfigParams.linkType != null) {
                    if (LinkType.ALI_BROADCAST_IN_BATCHES.getName().equals(dCAlibabaConfigParams.linkType.getName())) {
                        z = true;
                    }
                }
            } catch (Exception e) {
                ALog.d("AlinkHelper", "unfkg believable e=" + e);
            }
        }
        ALog.d("AlinkHelper", "isBatchBroadcast=" + z);
        return z;
    }

    public static boolean isBatchZero(DCAlibabaConfigParams dCAlibabaConfigParams) {
        boolean z = false;
        if (dCAlibabaConfigParams != null) {
            try {
                if (dCAlibabaConfigParams.linkType != null) {
                    if (LinkType.ALI_ZERO_IN_BATCHES.getName().equals(dCAlibabaConfigParams.linkType.getName())) {
                        z = true;
                    }
                }
            } catch (Exception e) {
                ALog.d("AlinkHelper", "unfkg believable e=" + e);
            }
        }
        ALog.d("AlinkHelper", "isBatchZero=" + z);
        return z;
    }

    public static boolean isValidSoftAp(String str, boolean z) {
        List<String> list;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ProvisionConfigParams provisionConfigParams = ProvisionConfigCenter.getInstance().getProvisionConfigParams();
        if (provisionConfigParams == null || (list = provisionConfigParams.deviceApPrefixList) == null || list.isEmpty()) {
            return str.startsWith(AlinkConstants.SOFT_AP_SSID_PREFIX) || (z && str.startsWith(AlinkConstants.MOCK_AP_SSID_PREFIX));
        }
        int size = provisionConfigParams.deviceApPrefixList.size();
        for (int i = 0; i < size; i++) {
            String str2 = provisionConfigParams.deviceApPrefixList.get(i);
            if (!TextUtils.isEmpty(str2) && str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] sixBitsToEightBits(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        long j = 0;
        for (int i = 0; i < bArr.length; i++) {
            j |= (long) (((bArr[i] - 32) & 63) << (i * 6));
        }
        int length = (bArr.length * 6) / 8;
        byte[] bArr2 = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            bArr2[i2] = (byte) (((4294967295L & j) >> (i2 * 8)) & 255);
        }
        return bArr2;
    }

    public static void uploadData2Oss(byte[] bArr, final IDataCallback<String> iDataCallback) {
        ALog.d("AlinkHelper", "uploadData2Oss() called with: data = [" + bArr + "], dataCallback = [" + iDataCallback + "]");
        if (bArr == null || bArr.length < 1) {
            if (iDataCallback != null) {
                iDataCallback.onState("ignored", "data empty, upload ignored.");
                return;
            }
            return;
        }
        try {
            if (!DCEnvHelper.getHasLogExtraSDK()) {
                ALog.w("AlinkHelper", "uploadData2Oss ignored, no log upload sdk exists.");
                if (iDataCallback != null) {
                    iDataCallback.onState("ignored", "current env not need, upload ignored.");
                    return;
                }
                return;
            }
            Object identifyId = ReflectUtils.getIdentifyId();
            String string = null;
            if (identifyId != null && !TextUtils.isEmpty(identifyId.toString())) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(identifyId));
                sb.append("/fb_dev_");
                sb.append(System.currentTimeMillis());
                sb.append(".log");
                string = sb.toString();
            }
            if (iDataCallback != null) {
                iDataCallback.onState("devOssKey", string);
            }
            Log2Cloud.getInstance().uploadOriginalByteLog(bArr, string, new OSSManager.OSSUploadCallback() { // from class: com.aliyun.alink.business.devicecenter.base.AlinkHelper.1
                public void onUploadFailed(int i, String str) {
                    ALog.d("AlinkHelper", "uploadData2Oss onUploadFailed() called with: i = [" + i + "], s = [" + str + "]");
                    IDataCallback iDataCallback2 = iDataCallback;
                    if (iDataCallback2 != null) {
                        iDataCallback2.onResult(false, i + str);
                    }
                }

                public void onUploadSuccess(String str, String str2) {
                    ALog.d("AlinkHelper", "uploadData2Oss onUploadSuccess() called with: ossObjectName = [" + str + "], s1 = [" + str2 + "]");
                    IDataCallback iDataCallback2 = iDataCallback;
                    if (iDataCallback2 != null) {
                        iDataCallback2.onResult(true, str);
                    }
                }
            });
        } catch (Exception e) {
            ALog.w("AlinkHelper", "uploadData2Oss exception= " + e);
        }
    }

    public static String a(String str) {
        if (str == null || str.length() != 4) {
            return null;
        }
        String strBytesToHexString = StringUtils.bytesToHexString(sixBitsToEightBits(str.getBytes()));
        ALog.d("AlinkHelper", "getMacFromConvertedMac conv=" + str + ", pri=" + strBytesToHexString);
        return strBytesToHexString;
    }
}
