package anet.channel.util;

import android.text.TextUtils;
import anet.channel.request.Request;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class HttpHelper {
    public static Map<String, List<String>> cloneMap(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        HashMap map2 = new HashMap(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            map2.put(entry.getKey(), new ArrayList(entry.getValue()));
        }
        return map2;
    }

    public static List<String> getHeaderFieldByKey(Map<String, List<String>> map, String str) {
        if (map == null || map.isEmpty() || TextUtils.isEmpty(str)) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (str.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static String getSingleHeaderFieldByKey(Map<String, List<String>> map, String str) {
        List<String> headerFieldByKey = getHeaderFieldByKey(map, str);
        if (headerFieldByKey == null || headerFieldByKey.isEmpty()) {
            return null;
        }
        return headerFieldByKey.get(0);
    }

    public static void removeHeaderFiledByKey(Map<String, List<String>> map, String str) {
        if (str == null) {
            return;
        }
        Iterator<String> it = map.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                break;
            } else if (str.equalsIgnoreCase(it.next())) {
                break;
            }
        }
        if (str != null) {
            map.remove(str);
        }
    }

    public static boolean checkRedirect(Request request, int i) {
        return request.isRedirectEnable() && i >= 300 && i < 400 && i != 304 && request.getRedirectTimes() < 10;
    }

    public static int parseContentLength(Map<String, List<String>> map) {
        try {
            return Integer.parseInt(getSingleHeaderFieldByKey(map, "Content-Length"));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static long parseServerRT(Map<String, List<String>> map) {
        try {
            List<String> list = map.get(HttpConstant.SERVER_RT);
            if (list == null || list.isEmpty()) {
                return 0L;
            }
            return Long.parseLong(list.get(0));
        } catch (NumberFormatException unused) {
            return 0L;
        }
    }

    public static int parseStatusCode(Map<String, List<String>> map) {
        try {
            List<String> list = map.get(":status");
            if (list != null && !list.isEmpty()) {
                return Integer.parseInt(list.get(0));
            }
        } catch (NumberFormatException unused) {
        }
        return 0;
    }

    public static String trySolveFileExtFromUrlPath(String str) {
        int iLastIndexOf;
        int iLastIndexOf2;
        if (str == null) {
            return null;
        }
        try {
            int length = str.length();
            if (length > 1 && (iLastIndexOf = str.lastIndexOf(47)) != -1 && iLastIndexOf != length - 1 && (iLastIndexOf2 = str.lastIndexOf(46)) != -1 && iLastIndexOf2 > iLastIndexOf) {
                return str.substring(iLastIndexOf2 + 1, length);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    @Deprecated
    public static String trySolveFileExtFromURL(URL url) {
        return trySolveFileExtFromUrlPath(url.getPath());
    }
}
