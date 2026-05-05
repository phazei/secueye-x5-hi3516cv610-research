package com.alibaba.sdk.android.tbrest.rest;

import android.content.Context;
import android.os.Build;
import com.alibaba.sdk.android.tbrest.SendService;
import com.alibaba.sdk.android.tbrest.utils.DeviceUtils;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.alibaba.sdk.android.tbrest.utils.StringUtils;
import com.huawei.hms.framework.common.ContainerUtils;
import com.xiaomi.mipush.sdk.Constants;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: RestReqDataBuilder.java */
/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static long f3193c = System.currentTimeMillis();

    private static String a(String str) {
        if (StringUtils.isBlank(str)) {
            return Constants.ACCEPT_TIME_SEPARATOR_SERVER;
        }
        if ("".equals(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] != '\n' && charArray[i] != '\r' && charArray[i] != '\t' && charArray[i] != '|') {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }

    public static String a(SendService sendService, String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str3;
        String str4 = null;
        if (i == 0) {
            return null;
        }
        try {
            String utdid = DeviceUtils.getUtdid(sendService.context);
            if (utdid == null) {
                LogUtil.e("get utdid failure, so build report failure, now return");
                return null;
            }
            String[] networkType = DeviceUtils.getNetworkType(sendService.context);
            String str5 = networkType[0];
            if (networkType.length > 1 && str5 != null && !DeviceUtils.NETWORK_CLASS_WIFI.equals(str5)) {
                str4 = networkType[1];
            }
            String str6 = "" + (j > 0 ? j : System.currentTimeMillis());
            String strA = a(str2);
            String strA2 = a(String.valueOf(i));
            String strA3 = a(StringUtils.convertObjectToString(obj));
            String strA4 = a(StringUtils.convertObjectToString(obj2));
            String strA5 = a(StringUtils.convertObjectToString(obj3));
            String strA6 = a(StringUtils.convertMapToString(map));
            String strA7 = a(DeviceUtils.getImei(sendService.context));
            String strA8 = a(DeviceUtils.getImsi(sendService.context));
            String strA9 = a(Build.BRAND);
            a(DeviceUtils.getCpuName());
            a(strA7);
            String strA10 = a(Build.MODEL);
            String strA11 = a(DeviceUtils.getResolution(sendService.context));
            String strA12 = a(DeviceUtils.getCarrier(sendService.context));
            String strA13 = a(str5);
            String strA14 = a(str4);
            String strA15 = a(str);
            String strA16 = a(sendService.appVersion);
            String strA17 = a(sendService.channel);
            String strA18 = a(sendService.userNick);
            String strA19 = a(sendService.userNick);
            a(DeviceUtils.getCountry());
            String strA20 = a(DeviceUtils.getLanguage());
            String str7 = sendService.appId;
            String strA21 = a(Build.VERSION.RELEASE);
            String str8 = "" + f3193c;
            String strA22 = a(utdid);
            String strA23 = a(sendService.country);
            StringUtils.isBlank("");
            if (str7 != null) {
                str3 = strA15;
                String str9 = str7.contains("aliyunos") ? "y" : "a";
                HashMap map2 = new HashMap();
                map2.put(a.IMEI.toString(), strA7);
                map2.put(a.IMSI.toString(), strA8);
                map2.put(a.BRAND.toString(), strA9);
                map2.put(a.DEVICE_MODEL.toString(), strA10);
                map2.put(a.RESOLUTION.toString(), strA11);
                map2.put(a.CARRIER.toString(), strA12);
                map2.put(a.ACCESS.toString(), strA13);
                map2.put(a.ACCESS_SUBTYPE.toString(), strA14);
                map2.put(a.CHANNEL.toString(), strA17);
                map2.put(a.APPKEY.toString(), str3);
                map2.put(a.APPVERSION.toString(), strA16);
                map2.put(a.LL_USERNICK.toString(), strA18);
                map2.put(a.USERNICK.toString(), strA19);
                map2.put(a.LL_USERID.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                map2.put(a.USERID.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                map2.put(a.LANGUAGE.toString(), strA20);
                map2.put(a.OS.toString(), str9);
                map2.put(a.OSVERSION.toString(), strA21);
                map2.put(a.SDKVERSION.toString(), "1.0");
                map2.put(a.START_SESSION_TIMESTAMP.toString(), "" + f3193c);
                map2.put(a.UTDID.toString(), strA22);
                map2.put(a.SDKTYPE.toString(), "mini");
                map2.put(a.RESERVE2.toString(), strA22);
                map2.put(a.RESERVE3.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                map2.put(a.RESERVE4.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                map2.put(a.RESERVE5.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                map2.put(a.RESERVES.toString(), strA23);
                map2.put(a.RECORD_TIMESTAMP.toString(), str6);
                map2.put(a.PAGE.toString(), strA);
                map2.put(a.EVENTID.toString(), strA2);
                map2.put(a.ARG1.toString(), strA3);
                map2.put(a.ARG2.toString(), strA4);
                map2.put(a.ARG3.toString(), strA5);
                map2.put(a.ARGS.toString(), strA6);
                return a(map2);
            }
            str3 = strA15;
            HashMap map22 = new HashMap();
            map22.put(a.IMEI.toString(), strA7);
            map22.put(a.IMSI.toString(), strA8);
            map22.put(a.BRAND.toString(), strA9);
            map22.put(a.DEVICE_MODEL.toString(), strA10);
            map22.put(a.RESOLUTION.toString(), strA11);
            map22.put(a.CARRIER.toString(), strA12);
            map22.put(a.ACCESS.toString(), strA13);
            map22.put(a.ACCESS_SUBTYPE.toString(), strA14);
            map22.put(a.CHANNEL.toString(), strA17);
            map22.put(a.APPKEY.toString(), str3);
            map22.put(a.APPVERSION.toString(), strA16);
            map22.put(a.LL_USERNICK.toString(), strA18);
            map22.put(a.USERNICK.toString(), strA19);
            map22.put(a.LL_USERID.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            map22.put(a.USERID.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            map22.put(a.LANGUAGE.toString(), strA20);
            map22.put(a.OS.toString(), str9);
            map22.put(a.OSVERSION.toString(), strA21);
            map22.put(a.SDKVERSION.toString(), "1.0");
            map22.put(a.START_SESSION_TIMESTAMP.toString(), "" + f3193c);
            map22.put(a.UTDID.toString(), strA22);
            map22.put(a.SDKTYPE.toString(), "mini");
            map22.put(a.RESERVE2.toString(), strA22);
            map22.put(a.RESERVE3.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            map22.put(a.RESERVE4.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            map22.put(a.RESERVE5.toString(), Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            map22.put(a.RESERVES.toString(), strA23);
            map22.put(a.RECORD_TIMESTAMP.toString(), str6);
            map22.put(a.PAGE.toString(), strA);
            map22.put(a.EVENTID.toString(), strA2);
            map22.put(a.ARG1.toString(), strA3);
            map22.put(a.ARG2.toString(), strA4);
            map22.put(a.ARG3.toString(), strA5);
            map22.put(a.ARGS.toString(), strA6);
            return a(map22);
        } catch (Exception e) {
            LogUtil.e("UTRestAPI buildTracePostReqDataObj catch!", e);
            return "";
        }
    }

    public static String a(Map<String, String> map) {
        boolean z;
        a aVar;
        StringBuffer stringBuffer = new StringBuffer();
        a[] aVarArrValues = a.values();
        int length = aVarArrValues.length;
        int i = 0;
        while (true) {
            String strConvertObjectToString = null;
            if (i >= length || (aVar = aVarArrValues[i]) == a.ARGS) {
                break;
            }
            if (map.containsKey(aVar.toString())) {
                strConvertObjectToString = StringUtils.convertObjectToString(map.get(aVar.toString()));
                map.remove(aVar.toString());
            }
            stringBuffer.append(a(strConvertObjectToString));
            stringBuffer.append("||");
            i++;
        }
        if (map.containsKey(a.ARGS.toString())) {
            stringBuffer.append(a(StringUtils.convertObjectToString(map.get(a.ARGS.toString()))));
            map.remove(a.ARGS.toString());
            z = false;
        } else {
            z = true;
        }
        for (String str : map.keySet()) {
            String strConvertObjectToString2 = map.containsKey(str) ? StringUtils.convertObjectToString(map.get(str)) : null;
            if (z) {
                if ("StackTrace".equals(str)) {
                    stringBuffer.append("StackTrace=====>");
                    stringBuffer.append(strConvertObjectToString2);
                } else {
                    stringBuffer.append(a(str));
                    stringBuffer.append(ContainerUtils.KEY_VALUE_DELIMITER);
                    stringBuffer.append(strConvertObjectToString2);
                }
                z = false;
            } else if ("StackTrace".equals(str)) {
                stringBuffer.append(",");
                stringBuffer.append("StackTrace=====>");
                stringBuffer.append(strConvertObjectToString2);
            } else {
                stringBuffer.append(",");
                stringBuffer.append(a(str));
                stringBuffer.append(ContainerUtils.KEY_VALUE_DELIMITER);
                stringBuffer.append(strConvertObjectToString2);
            }
        }
        String string = stringBuffer.toString();
        if (StringUtils.isEmpty(string) || !string.endsWith("||")) {
            return string;
        }
        return string + Constants.ACCEPT_TIME_SEPARATOR_SERVER;
    }

    public static d a(SendService sendService, String str, String str2, Context context, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str4;
        if (i == 0) {
            return null;
        }
        try {
            String utdid = DeviceUtils.getUtdid(sendService.context);
            if (utdid == null) {
                LogUtil.e("get utdid failure, so build report failure, now return");
                return null;
            }
            String[] networkType = DeviceUtils.getNetworkType(sendService.context);
            String str5 = networkType[0];
            String str6 = (networkType.length <= 1 || str5 == null || DeviceUtils.NETWORK_CLASS_WIFI.equals(str5)) ? null : networkType[1];
            long jCurrentTimeMillis = j > 0 ? j : System.currentTimeMillis();
            String str7 = "" + jCurrentTimeMillis;
            String str8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(jCurrentTimeMillis));
            String strA = a(str3);
            String strA2 = a(String.valueOf(i));
            String strA3 = a(StringUtils.convertObjectToString(obj));
            String strA4 = a(StringUtils.convertObjectToString(obj2));
            String strA5 = a(StringUtils.convertObjectToString(obj3));
            String strA6 = a(StringUtils.convertMapToString(map));
            String strA7 = a(DeviceUtils.getImei(sendService.context));
            String strA8 = a(DeviceUtils.getImsi(sendService.context));
            String strA9 = a(Build.BRAND);
            String strA10 = a(DeviceUtils.getCpuName());
            String strA11 = a(strA7);
            String strA12 = a(Build.MODEL);
            String strA13 = a(DeviceUtils.getResolution(sendService.context));
            String strA14 = a(DeviceUtils.getCarrier(sendService.context));
            String strA15 = a(str5);
            String strA16 = a(str6);
            String strA17 = a(str);
            String strA18 = a(sendService.appVersion);
            String strA19 = a(sendService.channel);
            String strA20 = a(sendService.userNick);
            String strA21 = a(sendService.userNick);
            String strA22 = a(DeviceUtils.getCountry());
            String strA23 = a(DeviceUtils.getLanguage());
            String str9 = sendService.appId;
            if (str9 != null) {
                str4 = strA23;
                String str10 = str9.contains("aliyunos") ? "aliyunos" : "Android";
                String strA24 = a(Build.VERSION.RELEASE);
                StringBuilder sb = new StringBuilder();
                String str11 = str10;
                sb.append("");
                sb.append(f3193c);
                String string = sb.toString();
                String strA25 = a(utdid);
                StringUtils.isBlank("");
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("5.0.1");
                stringBuffer.append("||");
                stringBuffer.append(strA7);
                stringBuffer.append("||");
                stringBuffer.append(strA8);
                stringBuffer.append("||");
                stringBuffer.append(strA9);
                stringBuffer.append("||");
                stringBuffer.append(strA10);
                stringBuffer.append("||");
                stringBuffer.append(strA11);
                stringBuffer.append("||");
                stringBuffer.append(strA12);
                stringBuffer.append("||");
                stringBuffer.append(strA13);
                stringBuffer.append("||");
                stringBuffer.append(strA14);
                stringBuffer.append("||");
                stringBuffer.append(strA15);
                stringBuffer.append("||");
                stringBuffer.append(strA16);
                stringBuffer.append("||");
                stringBuffer.append(strA19);
                stringBuffer.append("||");
                stringBuffer.append(strA17);
                stringBuffer.append("||");
                stringBuffer.append(strA18);
                stringBuffer.append("||");
                stringBuffer.append(strA20);
                stringBuffer.append("||");
                stringBuffer.append(strA21);
                stringBuffer.append("||");
                stringBuffer.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                stringBuffer.append("||");
                stringBuffer.append(strA22);
                stringBuffer.append("||");
                stringBuffer.append(str4);
                stringBuffer.append("||");
                stringBuffer.append(str11);
                stringBuffer.append("||");
                stringBuffer.append(strA24);
                stringBuffer.append("||");
                stringBuffer.append("mini");
                stringBuffer.append("||");
                stringBuffer.append("1.0");
                stringBuffer.append("||");
                stringBuffer.append(string);
                stringBuffer.append("||");
                stringBuffer.append(strA25);
                stringBuffer.append("||");
                stringBuffer.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                stringBuffer.append("||");
                stringBuffer.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                stringBuffer.append("||");
                stringBuffer.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                stringBuffer.append("||");
                stringBuffer.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                stringBuffer.append("||");
                stringBuffer.append(str8);
                stringBuffer.append("||");
                stringBuffer.append(str7);
                stringBuffer.append("||");
                stringBuffer.append(strA);
                stringBuffer.append("||");
                stringBuffer.append(strA2);
                stringBuffer.append("||");
                stringBuffer.append(strA3);
                stringBuffer.append("||");
                stringBuffer.append(strA4);
                stringBuffer.append("||");
                stringBuffer.append(strA5);
                stringBuffer.append("||");
                stringBuffer.append(strA6);
                String string2 = stringBuffer.toString();
                HashMap map2 = new HashMap();
                map2.put("stm_x", string2.getBytes());
                d dVar = new d();
                dVar.b(RestUrlWrapper.getSignedTransferUrl(str2, null, map2, context, strA17, strA19, strA18, str11, "", strA25));
                dVar.a(map2);
                return dVar;
            }
            str4 = strA23;
            String strA242 = a(Build.VERSION.RELEASE);
            StringBuilder sb2 = new StringBuilder();
            String str112 = str10;
            sb2.append("");
            sb2.append(f3193c);
            String string3 = sb2.toString();
            String strA252 = a(utdid);
            StringUtils.isBlank("");
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("5.0.1");
            stringBuffer2.append("||");
            stringBuffer2.append(strA7);
            stringBuffer2.append("||");
            stringBuffer2.append(strA8);
            stringBuffer2.append("||");
            stringBuffer2.append(strA9);
            stringBuffer2.append("||");
            stringBuffer2.append(strA10);
            stringBuffer2.append("||");
            stringBuffer2.append(strA11);
            stringBuffer2.append("||");
            stringBuffer2.append(strA12);
            stringBuffer2.append("||");
            stringBuffer2.append(strA13);
            stringBuffer2.append("||");
            stringBuffer2.append(strA14);
            stringBuffer2.append("||");
            stringBuffer2.append(strA15);
            stringBuffer2.append("||");
            stringBuffer2.append(strA16);
            stringBuffer2.append("||");
            stringBuffer2.append(strA19);
            stringBuffer2.append("||");
            stringBuffer2.append(strA17);
            stringBuffer2.append("||");
            stringBuffer2.append(strA18);
            stringBuffer2.append("||");
            stringBuffer2.append(strA20);
            stringBuffer2.append("||");
            stringBuffer2.append(strA21);
            stringBuffer2.append("||");
            stringBuffer2.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            stringBuffer2.append("||");
            stringBuffer2.append(strA22);
            stringBuffer2.append("||");
            stringBuffer2.append(str4);
            stringBuffer2.append("||");
            stringBuffer2.append(str112);
            stringBuffer2.append("||");
            stringBuffer2.append(strA242);
            stringBuffer2.append("||");
            stringBuffer2.append("mini");
            stringBuffer2.append("||");
            stringBuffer2.append("1.0");
            stringBuffer2.append("||");
            stringBuffer2.append(string3);
            stringBuffer2.append("||");
            stringBuffer2.append(strA252);
            stringBuffer2.append("||");
            stringBuffer2.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            stringBuffer2.append("||");
            stringBuffer2.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            stringBuffer2.append("||");
            stringBuffer2.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            stringBuffer2.append("||");
            stringBuffer2.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            stringBuffer2.append("||");
            stringBuffer2.append(str8);
            stringBuffer2.append("||");
            stringBuffer2.append(str7);
            stringBuffer2.append("||");
            stringBuffer2.append(strA);
            stringBuffer2.append("||");
            stringBuffer2.append(strA2);
            stringBuffer2.append("||");
            stringBuffer2.append(strA3);
            stringBuffer2.append("||");
            stringBuffer2.append(strA4);
            stringBuffer2.append("||");
            stringBuffer2.append(strA5);
            stringBuffer2.append("||");
            stringBuffer2.append(strA6);
            String string22 = stringBuffer2.toString();
            HashMap map22 = new HashMap();
            map22.put("stm_x", string22.getBytes());
            d dVar2 = new d();
            dVar2.b(RestUrlWrapper.getSignedTransferUrl(str2, null, map22, context, strA17, strA19, strA18, str112, "", strA252));
            dVar2.a(map22);
            return dVar2;
        } catch (Exception e) {
            LogUtil.e("UTRestAPI buildTracePostReqDataObj catch!", e);
            return null;
        }
    }
}
