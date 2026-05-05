package com.taobao.accs.utl;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.huawei.hms.framework.common.ContainerUtils;
import com.ta.utdid2.device.UTDevice;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public final class UTMini {
    public static final int EVENTID_AGOO = 19999;
    public static final String PAGE_AGOO = "Page_Push";
    private static final String TAG = "UTMini";
    private static UTMini instance = new UTMini();

    public final void commitEvent(int i, String str, Object obj) {
    }

    public final void commitEvent(int i, String str, Object obj, Object obj2) {
    }

    public final void commitEvent(int i, String str, Object obj, Object obj2, Object obj3) {
    }

    public final void commitEvent(int i, String str, Object obj, Object obj2, Object obj3, Map<String, String> map) {
    }

    public final void commitEvent(int i, String str, Object obj, Object obj2, Object obj3, String... strArr) {
    }

    public final void onCaughException(Throwable th) {
    }

    public final void start(Application application, String str, String str2, String str3) {
    }

    public final void stop(Context context) {
    }

    public static UTMini getInstance() {
        return instance;
    }

    public final String getUtdId(Context context) {
        try {
            return UTDevice.getUtdid(context);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Integer) {
            return "" + ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return "" + ((Long) obj).longValue();
        }
        if (obj instanceof Double) {
            return "" + ((Double) obj).doubleValue();
        }
        if (obj instanceof Float) {
            return "" + ((Float) obj).floatValue();
        }
        if (obj instanceof Short) {
            return "" + ((int) ((Short) obj).shortValue());
        }
        if (obj instanceof Byte) {
            return "" + ((int) ((Byte) obj).byteValue());
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).toString();
        }
        if (obj instanceof Character) {
            return ((Character) obj).toString();
        }
        return obj.toString();
    }

    private String _convertStringAToKVSString(String... strArr) {
        if (strArr != null && strArr.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (strArr != null && strArr.length > 0) {
            boolean z = false;
            for (int i = 0; i < strArr.length; i++) {
                if (!TextUtils.isEmpty(strArr[i])) {
                    if (z) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(strArr[i]);
                    z = true;
                }
            }
        }
        return stringBuffer.toString();
    }

    private static String[] mapToArray(Map<String, String> map) {
        int i = 0;
        if (map == null || map.isEmpty()) {
            return new String[0];
        }
        String[] strArr = new String[map.size()];
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null) {
                strArr[i] = "";
            } else {
                strArr[i] = key + ContainerUtils.KEY_VALUE_DELIMITER + value;
            }
            i++;
        }
        return strArr;
    }

    public static String getCommitInfo(int i, String str, String str2, String str3, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("eventId=");
        sb.append(i);
        sb.append(";arg1=");
        sb.append(str);
        sb.append(";arg2=");
        sb.append(str2);
        sb.append(";arg3=");
        sb.append(str3);
        if (map != null) {
            sb.append(";");
            sb.append("args=");
            sb.append(map.toString());
        }
        return sb.toString();
    }

    public static String getCommitInfo(int i, String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        sb.append("eventId=");
        sb.append(i);
        sb.append(";arg1=");
        sb.append(str);
        sb.append(";arg2=");
        sb.append(str2);
        sb.append(";arg3=");
        sb.append(str3);
        if (str4 != null) {
            sb.append(";");
            sb.append("args=");
            sb.append(str4);
        }
        return sb.toString();
    }
}
