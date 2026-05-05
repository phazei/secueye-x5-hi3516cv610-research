package anet.channel.strategy.utils;

import android.text.TextUtils;
import anet.channel.util.ALog;
import com.google.android.exoplayer2.C;
import com.huawei.hms.framework.common.ContainerUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c {
    public static String d(String str) {
        return str == null ? "" : str;
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length < 7 || charArray.length > 15) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        for (char c2 : charArray) {
            if (c2 >= '0' && c2 <= '9') {
                i2 = ((i2 * 10) + c2) - 48;
                if (i2 > 255) {
                    return false;
                }
            } else {
                if (c2 != '.' || (i = i + 1) > 3) {
                    return false;
                }
                i2 = 0;
            }
        }
        return true;
    }

    public static boolean b(String str) {
        int i;
        boolean z;
        int i2;
        int i3;
        boolean z2;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            return false;
        }
        if (charArray[0] != ':') {
            i = 0;
            z = false;
            i2 = 0;
            i3 = 0;
            z2 = true;
        } else {
            if (charArray[1] != ':') {
                return false;
            }
            z = false;
            i3 = 0;
            i = 1;
            i2 = 1;
            z2 = true;
        }
        while (i < charArray.length) {
            char c2 = charArray[i];
            int iDigit = Character.digit(c2, 16);
            if (iDigit != -1) {
                i3 = (i3 << 4) + iDigit;
                if (i3 > 65535) {
                    return false;
                }
                z2 = false;
            } else {
                if (c2 != ':' || (i2 = i2 + 1) > 7) {
                    return false;
                }
                if (!z2) {
                    i3 = 0;
                    z2 = true;
                } else {
                    if (z) {
                        return false;
                    }
                    z = true;
                }
            }
            i++;
        }
        return z || i2 >= 7;
    }

    public static boolean c(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length <= 0 || charArray.length > 255) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 'A' && charArray[i] <= 'Z') || ((charArray[i] >= 'a' && charArray[i] <= 'z') || charArray[i] == '*')) {
                z = true;
            } else if ((charArray[i] < '0' || charArray[i] > '9') && charArray[i] != '.' && charArray[i] != '-') {
                return false;
            }
        }
        return z;
    }

    public static String a(long j) {
        StringBuilder sb = new StringBuilder(16);
        long j2 = C.NANOS_PER_SECOND;
        do {
            sb.append(j / j2);
            sb.append('.');
            j %= j2;
            j2 /= 1000;
        } while (j2 > 0);
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String a(Map<String, String> map, String str) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(64);
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    sb.append(URLEncoder.encode(entry.getKey(), str));
                    sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                    sb.append(URLEncoder.encode(d(entry.getValue()), str).replace(MqttTopic.SINGLE_LEVEL_WILDCARD, "%20"));
                    sb.append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (UnsupportedEncodingException e) {
            ALog.e("Request", "format params failed", null, e, new Object[0]);
        }
        return sb.toString();
    }
}
