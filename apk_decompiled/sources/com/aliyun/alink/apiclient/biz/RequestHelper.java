package com.aliyun.alink.apiclient.biz;

import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.constants.Constants;
import com.aliyun.alink.apiclient.constants.MethodType;
import com.aliyun.alink.apiclient.constants.Schema;
import com.aliyun.alink.apiclient.utils.StringUtils;
import com.http.utils.LogUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class RequestHelper {
    private static final String TAG = "RequestHelper";

    public static String getBaseUrl(CommonRequest commonRequest, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (commonRequest.getSchema() == Schema.HTTP) {
            stringBuffer.append("http://");
        } else {
            stringBuffer.append("https://");
        }
        if (StringUtils.isEmptyString(commonRequest.getDomain())) {
            stringBuffer.append(Constants.IOT_DOMAIN_DEFAULT);
        } else {
            stringBuffer.append(commonRequest.getDomain());
        }
        if (commonRequest.getMethod() == MethodType.GET) {
            LogUtils.error(TAG, "Get Not Support yet.");
        }
        if (str == null) {
            stringBuffer.append(commonRequest.getPath());
        } else {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public static Map<String, String> convert(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        HashMap map2 = new HashMap();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry != null && !StringUtils.isEmptyString(entry.getKey()) && entry.getValue() != null) {
                    if (entry.getValue() instanceof String) {
                        map2.put(entry.getKey(), (String) entry.getValue());
                    } else {
                        map2.put(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map2;
    }

    public static String getFormMapString(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (String str : map.keySet()) {
            if (i > 0) {
                stringBuffer.append("&");
            }
            stringBuffer.append(String.format("%s=%s", str, map.get(str)));
            i++;
        }
        return stringBuffer.toString();
    }
}
