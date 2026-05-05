package tools;

import com.xiaomi.mipush.sdk.Constants;
import java.util.UUID;

/* JADX INFO: loaded from: classes4.dex */
public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isIntegerNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("-?[0-9]+");
    }

    public static boolean isIntegerPositiveNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("[0-9]+");
    }

    public static boolean isIntegerNumberInRange(String str, int i, int i2) {
        int i3;
        return isIntegerNumber(str) && i < i2 && i <= (i3 = Integer.parseInt(str)) && i3 <= i2;
    }

    public static String getUniqueRandomString() {
        return UUID.randomUUID().toString().replaceAll(Constants.ACCEPT_TIME_SEPARATOR_SERVER, "");
    }
}
