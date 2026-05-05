package tools;

import android.util.Log;

/* JADX INFO: loaded from: classes4.dex */
public class LogUtil {
    private static boolean IS_DEBUG = true;
    private static final int MAX_LENGTH = 3072;
    private static LogUtil logUtil;

    public static LogUtil getInstance() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }
        return logUtil;
    }

    private synchronized String getTAG() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null) {
            return "";
        }
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!stackTraceElement.isNativeMethod() && !stackTraceElement.getClassName().equals(Thread.class.getName()) && !stackTraceElement.getClassName().equals(getClass().getName())) {
                sb.append("(");
                sb.append(stackTraceElement.getFileName());
                sb.append(":");
                sb.append(stackTraceElement.getLineNumber());
                sb.append(")");
                return sb.toString();
            }
        }
        return "";
    }

    public synchronized void e(String str, String str2) {
        if (IS_DEBUG) {
            for (String str3 : splitStr(str2)) {
                Log.e(getTAG(), str + "=========" + str3);
            }
        }
    }

    public synchronized void d(String str, String str2) {
        if (IS_DEBUG) {
            for (String str3 : splitStr(str2)) {
                Log.d(getTAG(), str + "=========" + str3);
            }
        }
    }

    public synchronized void w(String str, String str2) {
        if (IS_DEBUG) {
            for (String str3 : splitStr(str2)) {
                Log.w(getTAG(), str + "=========" + str3);
            }
        }
    }

    public synchronized void i(String str, String str2) {
        if (IS_DEBUG) {
            for (String str3 : splitStr(str2)) {
                Log.i(getTAG(), str + "=========" + str3);
            }
        }
    }

    public synchronized void json(String str, String str2) {
        if (IS_DEBUG) {
            String tag = getTAG();
            try {
                for (String str3 : splitStr(formatJson(str))) {
                    Log.d(getTAG(), str2 + "=========" + str3);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(tag, e.toString());
            }
        }
    }

    private String[] splitStr(String str) {
        int length = str.length();
        String[] strArr = new String[(length / 3072) + 1];
        int i = 0;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            int i3 = i + 3072;
            if (i3 < length) {
                strArr[i2] = str.substring(i, i3);
                i = i3;
            } else {
                strArr[i2] = str.substring(i, length);
                i = length;
            }
        }
        return strArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String formatJson(java.lang.String r10) {
        /*
            r9 = this;
            if (r10 == 0) goto L79
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r10)
            if (r0 == 0) goto Lc
            goto L79
        Lc:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            r2 = r1
            r3 = r2
            r4 = r3
        L15:
            int r5 = r10.length()
            if (r1 >= r5) goto L74
            char r5 = r10.charAt(r1)
            r6 = 34
            r7 = 92
            if (r5 == r6) goto L69
            r6 = 44
            r8 = 10
            if (r5 == r6) goto L5b
            r2 = 91
            if (r5 == r2) goto L4d
            r2 = 93
            if (r5 == r2) goto L3f
            r2 = 123(0x7b, float:1.72E-43)
            if (r5 == r2) goto L4d
            r2 = 125(0x7d, float:1.75E-43)
            if (r5 == r2) goto L3f
            r0.append(r5)
            goto L70
        L3f:
            if (r3 != 0) goto L49
            r0.append(r8)
            int r4 = r4 + (-1)
            r9.addIndentBlank(r0, r4)
        L49:
            r0.append(r5)
            goto L70
        L4d:
            r0.append(r5)
            if (r3 != 0) goto L70
            r0.append(r8)
            int r4 = r4 + 1
            r9.addIndentBlank(r0, r4)
            goto L70
        L5b:
            r0.append(r5)
            if (r2 == r7) goto L70
            if (r3 != 0) goto L70
            r0.append(r8)
            r9.addIndentBlank(r0, r4)
            goto L70
        L69:
            if (r2 == r7) goto L6d
            r3 = r3 ^ 1
        L6d:
            r0.append(r5)
        L70:
            int r1 = r1 + 1
            r2 = r5
            goto L15
        L74:
            java.lang.String r10 = r0.toString()
            return r10
        L79:
            java.lang.String r10 = ""
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.LogUtil.formatJson(java.lang.String):java.lang.String");
    }

    private void addIndentBlank(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append('\t');
        }
    }
}
