package com.alibaba.sdk.android.tbrest.rest;

import android.content.Context;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.alibaba.sdk.android.tbrest.utils.MD5Utils;
import com.alibaba.sdk.android.tbrest.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class RestUrlWrapper {
    public static final String FIELD_APPKEY = "appkey";
    public static final String FIELD_APPVERSION = "app_version";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_PLATFORM = "platform";
    public static final String FIELD_SDK_VERSION = "sdk_version";
    public static final String FIELD_T = "t";
    public static final String FIELD_UTDID = "utdid";
    public static final String FIELD_V = "v";
    static boolean enableSecuritySDK = false;
    static Context mContext;

    public static void enableSecuritySDK() {
        enableSecuritySDK = true;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getSignedTransferUrl(String str, Map<String, Object> map, Map<String, Object> map2, Context context, String str2, String str3, String str4, String str5, String str6, String str7) throws Exception {
        String str8 = "";
        if (map2 != null && map2.size() > 0) {
            Set<String> setKeySet = map2.keySet();
            String[] strArr = new String[setKeySet.size()];
            setKeySet.toArray(strArr);
            for (String str9 : c.a().a(strArr, true)) {
                str8 = str8 + str9 + MD5Utils.getMd5Hex((byte[]) map2.get(str9));
            }
        }
        try {
            return wrapUrl(str, null, null, str8, context, str2, str3, str4, str5, str6, str7);
        } catch (Exception unused) {
            return wrapUrl(RestConstants.getTransferUrl(), null, null, str8, context, str2, str3, str4, str5, str6, str7);
        }
    }

    private static String wrapUrl(String str, String str2, String str3, String str4, Context context, String str5, String str6, String str7, String str8, String str9, String str10) throws Exception {
        String strValueOf = String.valueOf(System.currentTimeMillis());
        String str11 = "";
        String strB = "";
        if (enableSecuritySDK && mContext != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str5);
                sb.append(str6);
                sb.append(str7);
                sb.append(str8);
                sb.append(RestConstants.G_SDK_VERSION);
                sb.append(str10);
                sb.append(strValueOf);
                sb.append("3.0");
                sb.append("");
                if (str3 == null) {
                    str3 = "";
                }
                sb.append(str3);
                if (str4 == null) {
                    str4 = "";
                }
                sb.append(str4);
                strB = new g(mContext, str5).b(MD5Utils.getMd5Hex(sb.toString().getBytes()));
                if (StringUtils.isNotBlank(RestConstants.G_SDK_VERSION)) {
                    str11 = "1";
                }
            } catch (Exception e) {
                LogUtil.w("security sdk signed", e);
            }
        }
        String str12 = "";
        if (!StringUtils.isEmpty(str2)) {
            str12 = str2 + "&";
        }
        return String.format("%s?%sak=%s&av=%s&c=%s&v=%s&s=%s&d=%s&sv=%s&p=%s&t=%s&u=%s&is=%s", str, str12, getEncoded(str5), getEncoded(str7), getEncoded(str6), getEncoded("3.0"), getEncoded(strB), getEncoded(str10), RestConstants.G_SDK_VERSION, str8, strValueOf, "", str11);
    }

    private static String getEncoded(String str) {
        if (str == null) {
            return "";
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }
}
