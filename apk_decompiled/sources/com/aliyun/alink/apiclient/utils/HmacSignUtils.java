package com.aliyun.alink.apiclient.utils;

import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.http.utils.LogUtils;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class HmacSignUtils {
    private static final String TAG = "[ITC]HmacSignUtils";

    public static String getHmacSign(Map<String, String> map, String str) {
        if (map == null || StringUtils.isEmptyString(str)) {
            return null;
        }
        String toSignString = getToSignString(map);
        LogUtils.print(TAG, "toSignString=" + toSignString);
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes("utf-8"), MqttConfigure.SIGN_METHOD);
            Mac mac = Mac.getInstance(secretKeySpec.getAlgorithm());
            mac.init(secretKeySpec);
            return bytesToHexString(mac.doFinal(toSignString.getBytes("utf-8")));
        } catch (Exception e) {
            LogUtils.error(TAG, "hmacSign error, e" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static final String bytesToHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public static String getToSignString(Map<String, String> map) {
        try {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            Arrays.sort(strArr);
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                if (!"sign".equalsIgnoreCase(str)) {
                    sb.append(str);
                    sb.append(map.get(str));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
